/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
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
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIConverterImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IPackage;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.modelManager.ModelManager;
import org.eclipse.tigerstripe.workbench.internal.modelManager.ModelRepository;

public class PojoModelRepository extends ModelRepository {

	public final static String POJO_EXTENSION = "java";
	public final static URI BASE_POJO_URI = URI.createURI("pojo:///");

	private Map<String, Resource> resourceMap = new HashMap<String, Resource>();
	private URIConverter converter = new URIConverterImpl();

	public PojoModelRepository(URI repository, ModelManager manager) {
		super(repository, manager);
		converter.getURIMap().put(BASE_POJO_URI, repository);

		getEditingDomain().addResourceSetListener(
				new PojoModelRepositoryListener(this));
	}

	public URI normalize(URI uri) {
		return converter.normalize(uri);
	}

	private final class AllPojosVisitor implements IResourceVisitor {

		private List<IResource> allMembers = new ArrayList<IResource>();

		public boolean visit(IResource resource) throws CoreException {
			if (resource instanceof IFolder)
				return true;
			else {
				if (POJO_EXTENSION.equals(resource.getFileExtension()))
					allMembers.add(resource);
				return false;
			}
		}
	}

	@Override
	protected void loadResourceSet() throws TigerstripeException {
		String platformString = getRepositoryURI().toPlatformString(true);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IResource repositoryRes = root.findMember(platformString);

		AllPojosVisitor visitor = new AllPojosVisitor();
		try {
			repositoryRes.accept(visitor);
		} catch (CoreException e) {
			throw new TigerstripeException("While loading resourceSet: "
					+ e.getMessage(), e);
		}

		for (IResource pojo : visitor.allMembers) {
			Resource res = getResourceSet().getResource(
					URI.createPlatformResourceURI(pojo.getFullPath()
							.toOSString(), true), true);
			try {
				res.load(null);
				IAbstractArtifact art = (IAbstractArtifact) res.getContents()
						.get(0);
				resourceMap.put(art.getFullyQualifiedName(), res);
			} catch (IOException e) {
				throw new TigerstripeException("while loading pojo: "
						+ e.getMessage(), e);
			}
		}
	}

	@Override
	public IAbstractArtifact store(final IAbstractArtifact artifact,
			boolean force) throws TigerstripeException {

		assert (artifact != null);

		TransactionalEditingDomain editingDomain = getEditingDomain();

		String fqn = artifact.getFullyQualifiedName();
		final URI uri = PojoUtils.getURIforFQN(fqn, this);

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
						targetResource = getResourceSet().getResource(uri,
								false);
						if (targetResource == null) {
							targetResource = getResourceSet().createResource(
									uri);
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
							getResourceSet().getResources().remove(
									targetResource);
							IResource[] res = ResourcesPlugin.getWorkspace()
									.getRoot().findFilesForLocation(
											new Path(oldUri.toFileString()));
							IFile file = (IFile) res[0];
							try {
								file.delete(true, null);
							} catch (CoreException e) {
								BasePlugin.log(e);
							}
							targetResource = getResourceSet().createResource(
									uri);
							targetResource.getContents().add(artifact);
						}
					}

					targetResource.save(null);

				} catch (IOException e) {
					e.printStackTrace();
					BasePlugin.log(e); // TODO gracefully handle this
					IStatus status = new Status(IStatus.ERROR,
							BasePlugin.PLUGIN_ID, e.getMessage(), e);
					result.add(status);
				}

			}

			@Override
			public Collection getResult() {
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
			throw new TigerstripeException("Error during store: "
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

	public Collection<IAbstractArtifact> getAllArtifacts() {
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
		for (Resource resource : getResourceSet().getResources()) {
			for (EObject o : resource.getContents()) {
				result.add((IAbstractArtifact) o);
			}
		}
		return result;
	}

	public IAbstractArtifact getArtifactByFullyQualifiedName(
			String fullyQualifiedName) {

		if (fullyQualifiedName == null || fullyQualifiedName.length() == 0)
			return null;

		for (IAbstractArtifact artifact : getAllArtifacts()) {
			if (fullyQualifiedName.equals(artifact.getFullyQualifiedName()))
				return artifact;
		}
		return null;
	}

	public void refresh(IPackage rootPackage) {
		// TODO: implement me properly!
		try {
			getCorrespondingResource(rootPackage).refreshLocal(
					IResource.DEPTH_INFINITE, null);
			loadResourceSet();
			System.out.println("" + resourceMap);
		} catch (TigerstripeException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private IResource getCorrespondingResource(IPackage package_) {
		if (package_ == null) {
			String platformString = getRepositoryURI().toPlatformString(true);
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			IResource repositoryRes = root.findMember(platformString);
			return repositoryRes;
		}

		throw new UnsupportedOperationException();
	}
}
