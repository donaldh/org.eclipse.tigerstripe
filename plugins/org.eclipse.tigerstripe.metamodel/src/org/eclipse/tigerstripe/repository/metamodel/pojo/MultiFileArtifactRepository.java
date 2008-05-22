/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.metamodel.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.repository.internal.Activator;
import org.eclipse.tigerstripe.repository.manager.BaseModelRepository;
import org.eclipse.tigerstripe.repository.manager.KeyService;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;
import org.eclipse.tigerstripe.repository.metamodel.pojo.internal.AllOutOfSyncPojosVisitor;
import org.eclipse.tigerstripe.repository.metamodel.pojo.internal.RepositoryRefresherJob;
import org.eclipse.tigerstripe.repository.metamodel.pojo.utils.Util;

public class MultiFileArtifactRepository extends BaseModelRepository {

	public final static String POJO_EXTENSION = "java";

	public enum RepositoryState {
		UNKNOWN, // Unknown state, should never get here
		NOT_LOADED, // repository just created nothing loaded from disk yet
		LOADING, // repository is loading now
		NEEDREFRESH, // a RepositoryDelta has been posted but not applied yet
		REFRESHING, // a repository delta is been applied.
		UP2DATE; // the repository can be accessed
	};

	private RepositoryState state = RepositoryState.UNKNOWN;

	// private MultiFileRepositoryChangeTracker tracker = null;

	private RepositoryRefresherJob pendingRefresher = null;

	public MultiFileArtifactRepository(URI uri) {
		super(uri);

		setURIResourceMap(new HashMap<URI, Resource>());

		try {
			getEditingDomain().addResourceSetListener(
					new TigerstripeLocalPojoRepositoryListener(this));
		} catch (ModelCoreException e) {
			Activator.log(e);
		}

		state = RepositoryState.NOT_LOADED;

		// create the tracker but don't start it until the repository is loaded
		// tracker = new MultiFileRepositoryChangeTracker(this);
	}

	public String[] getArtifactFileExtensions() {
		return new String[] { POJO_EXTENSION };
	}

	public boolean isArtifact(IResource resource) {
		for (String ext : getArtifactFileExtensions()) {
			if (ext.equals(resource.getFileExtension())) {
				return true;
			}
		}
		return false;
	}

	protected void blockUntilUp2date() {
		RepositoryRefresherJob job = pendingRefresher;
		if (job != null) {
			try {
				job.join();
			} catch (InterruptedException e) {
				// FIXME not sure how to handle that!
				Activator.log(e);
			}
		}
	}

	/**
	 * Returns the normalized URI for the given artifact within the local
	 * repository.
	 * 
	 * Note that this method doesn't validate/check whether the artifact is
	 * indeed in this repository.
	 * 
	 * @param artifact
	 * @return
	 */
	public URI normalizedURI(IAbstractArtifact artifact)
			throws ModelCoreException {
		String fqn = (String) KeyService.INSTANCE.getKey(artifact);

		List<String> segments = new ArrayList<String>();

		String pojoPath = Util.packageOf(fqn);
		String pojoName = Util.nameOf(fqn) + "."
				+ MultiFileArtifactRepository.POJO_EXTENSION;
		for (StringTokenizer tokenizer = new StringTokenizer(pojoPath, "."); tokenizer
				.hasMoreElements();) {
			segments.add(tokenizer.nextToken());
		}
		segments.add(pojoName);
		URI resourceURI = getURI().appendSegments(
				segments.toArray(new String[segments.size()]));
		return resourceURI;
	}

	public EObject getEObjectByKey(Object key) throws ModelCoreException {
		if (state == RepositoryState.NOT_LOADED) {
			load(null);
		}
		return getArtifactByFullyQualifiedName((String) key);
	}

	public EObject store(EObject obj, boolean forceReplace)
			throws ModelCoreException {
		if (obj instanceof IAbstractArtifact)
			return store((IAbstractArtifact) obj, forceReplace);
		throw new ModelCoreException(
				"Can't store non-IAbstractArtifact EObject: "
						+ obj.eClass().getName());
	}

	protected void load(Map<Object, Object> loadOptions)
			throws ModelCoreException {
		state = RepositoryState.LOADING;

		refresh();

		// tracker.startListening();
		state = RepositoryState.UP2DATE;
	}

	public IAbstractArtifact store(final IAbstractArtifact artifact,
			boolean force) throws ModelCoreException {

		assert (artifact != null);

		if (state == RepositoryState.NOT_LOADED)
			load(null);

		blockUntilUp2date();

		TransactionalEditingDomain editingDomain = getEditingDomain();

		final URI uri = normalizedURI(artifact);

		// If no artifact existed for this URI, the resource would have been
		// created but would be empty.

		Command cmd = new AbstractCommand() {

			@Override
			public boolean canExecute() {
				return true;
			}

			private Collection<IStatus> result = new ArrayList<IStatus>();

			public void execute() {

				try {
					Resource targetResource = artifact.eResource();
					if (targetResource == null) {
						// Trying to store a working copy
						targetResource = getResource(uri, false);
						if (targetResource == null) {
							targetResource = createResource(uri);
							getURIResourceMap().put(uri, targetResource); // FIXME
							// surprised
							// this
							// is
							// necessary?
							targetResource.getContents().add(artifact);
						} else {
							if (!targetResource.isLoaded())
								targetResource.load(null);
							IAbstractArtifact original = (IAbstractArtifact) targetResource
									.getContents().get(0);
							applyChanges(original, artifact);
						}
					} else {
						URI oldUri = targetResource.getURI();
						if (oldUri == uri) {
							// The artifact hasn't been renamed, no problem
							IAbstractArtifact original = (IAbstractArtifact) targetResource
									.getContents().get(0);
							applyChanges(original, artifact);
						} else {
							// the artifact has been renamed. We need to remove
							// the old resource
							// and create a new one.
							targetResource.unload();
							getResources().remove(targetResource);
							IResource[] res = ResourcesPlugin.getWorkspace()
									.getRoot().findFilesForLocation(
											new Path(oldUri.toFileString()));
							IFile file = (IFile) res[0];
							try {
								file.delete(true, null);
							} catch (CoreException e) {
								Activator.log(e);
							}
							targetResource = createResource(uri);
							targetResource.getContents().add(artifact);
						}
					}

					targetResource.save(null);

				} catch (IOException e) {
					e.printStackTrace();
					Activator.log(e); // TODO gracefully handle this
					IStatus status = new Status(IStatus.ERROR,
							Activator.PLUGIN_ID, e.getMessage(), e);
					result.add(status);
				}

			}

			@Override
			public Collection<IStatus> getResult() {
				return result;
			}

			public void redo() {
				// nothing here
			}

		};

		editingDomain.getCommandStack().execute(cmd);

		if (!cmd.getResult().isEmpty()) {
			Exception ee = (Exception) ((IStatus) cmd.getResult().iterator()
					.next()).getException();
			throw new ModelCoreException("Error during store: "
					+ ee.getMessage(), ee);
		}
		return artifact;
	}

	private void applyChanges(IAbstractArtifact original,
			IAbstractArtifact workingCopy) {
		EClass e = original.eClass();
		for (EStructuralFeature o : e.getEAllStructuralFeatures()) {
			if (o instanceof EAttribute) {
				original.eSet(o, workingCopy.eGet(o));
			} else if (o instanceof EReference) {
				// FIXME: todo
			}
		}

	}

	public Collection<IAbstractArtifact> getAllArtifacts()
			throws ModelCoreException {
		if (state == RepositoryState.NOT_LOADED)
			load(null);

		blockUntilUp2date();

		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
		for (Resource resource : getResources()) {
			for (EObject o : resource.getContents()) {
				result.add((IAbstractArtifact) o);
			}
		}
		return result;
	}

	public IAbstractArtifact getArtifactByFullyQualifiedName(
			String fullyQualifiedName) throws ModelCoreException {

		if (state == RepositoryState.NOT_LOADED)
			load(null);

		blockUntilUp2date();

		if (fullyQualifiedName == null || fullyQualifiedName.length() == 0)
			return null;

		for (IAbstractArtifact artifact : getAllArtifacts()) {
			if (fullyQualifiedName.equals(artifact.getFullyQualifiedName()))
				return artifact;
		}
		return null;
	}

	public void refresh() throws ModelCoreException {
		AllOutOfSyncPojosVisitor visitor = new AllOutOfSyncPojosVisitor(this);
		IResource topRepoRes = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(getURI().toPlatformString(true));

		try {
			topRepoRes.accept(visitor);
			RepositoryRefresherJob job = new RepositoryRefresherJob(visitor);
			job.schedule();
			job.join();
		} catch (CoreException e) {
			throw new ModelCoreException("While refreshing: " + e.getMessage(),
					e);
		} catch (InterruptedException e) {
			throw new ModelCoreException("While refreshing: " + e.getMessage(),
					e);
		}

	}

	public IResource getIResource(IAbstractArtifact artifact)
			throws ModelCoreException {
		URI artifactURI = normalizedURI(artifact);
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				artifactURI.toPlatformString(true));

		return res;
	}

}
