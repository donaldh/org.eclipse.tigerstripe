/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

public class ArtifactRenameRequest extends BaseArtifactElementRequest implements
		IArtifactRenameRequest {

	private String newName;

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewName() {
		return newName;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		if (!super.canExecute(mgrSession)) {
			return false;
		}
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());

			String target = newName;
			if (art != null) {
				String artPack = art.getPackage();
				if (artPack != null && artPack.length() != 0) {
					target = artPack + "." + newName;
				}
				IAbstractArtifact newArt = mgrSession
						.getArtifactByFullyQualifiedName(target);
				return newArt == null;
			}

			return false;
		} catch (TigerstripeException t) {
			return false;
		}
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		super.execute(mgrSession);

		IAbstractArtifact origArt = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());

		AbstractArtifact aArt = (AbstractArtifact) origArt;
		String oldFQN = aArt.getFullyQualifiedName();

		// This is using the fact that this is a file. Will need to be removed
		// to attach to EMF (nokia)
		IPath path = new Path(aArt.getArtifactManager().getTSProject()
				.getProjectLabel()
				+ "/" + aArt.getArtifactPath());
		IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

		// Bug 939: should be using a rename rather than doing a remove/add
		// manually
		// bug 299124: the artifact may be coming from a parent project, so 
		// we need to get the right session mgr for that very artifact, not
		// the one attached here which is the leaf.
		IArtifactManagerSession session = aArt.getProject().getArtifactManagerSession();
		session
				.renameArtifact(origArt, origArt.getPackage() + "." + newName);
		origArt.doSave(new NullProgressMonitor());

		if (f != null) {
			try {
				f.delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				BasePlugin.log(e);
			}
		}
		// Update references
		updateReferences(mgrSession, origArt, oldFQN);
	}

	/**
	 * Tries to find all references to renamed artifact and make sure they are
	 * updated accordingly.
	 * 
	 * @param mgrSession
	 */
	protected void updateReferences(IArtifactManagerSession mgrSession,
			IAbstractArtifact referencedArtifact, String oldFQN) {
		IQueryAllArtifacts query = (IQueryAllArtifacts) mgrSession
				.makeQuery(IQueryAllArtifacts.class.getName());
		String newName = referencedArtifact.getFullyQualifiedName();
		try {
			Collection<IAbstractArtifact> artifacts = mgrSession
					.queryArtifact(query);
			for (IAbstractArtifact artifact : artifacts) {
				if (artifact.getFullyQualifiedName().equals(newName)){
					// This is the one we have just updated, so skip it
					continue;
				}
				
				boolean needSave = false;

				// The extended artifact case is a bit tricky. Because we
				// renamed the artifact object
				// directly, the reference is going to be valid at this stage.
				// But the underlying POJO
				// is not. So, if the extended artifact matches here, we need to
				// force a save on the POJO
				// and match on the new name as well
				if (artifact.getExtendedArtifact() != null
						&& (artifact.getExtendedArtifact()
								.getFullyQualifiedName().equals(newName) || artifact
								.getExtendedArtifact().getFullyQualifiedName()
								.equals(oldFQN))) {
					artifact.setExtendedArtifact(referencedArtifact);
					needSave = true;
				}

				// take care of implemented artifacts same way
				Collection<IAbstractArtifact> implemented = artifact
						.getImplementedArtifacts();
				boolean changed = false;
				for (IAbstractArtifact impl : implemented) {
					IAbstractArtifact implArt = impl;
					if (implArt.getFullyQualifiedName().equals(oldFQN)
							|| implArt.getFullyQualifiedName().equals(newName)) {
						impl = referencedArtifact;
						changed = true;
					}
				}
				if (changed) {
					artifact.setImplementedArtifacts(implemented);
					needSave = true;
				}

				// take care of containing artifacts same way
				// This is looking UPWARDS
				IModelComponent containing = artifact
						.getContainingModelComponent();
				boolean containingChanged = false;

				AbstractArtifact aArtifact = (AbstractArtifact) artifact;

				if (containing instanceof IAbstractArtifact) {
					AbstractArtifact containingArt = (AbstractArtifact) containing;
					if (containingArt.getFullyQualifiedName().equals(oldFQN)
							|| containingArt.getFullyQualifiedName().equals(
									newName)) {
						aArtifact
								.setContainingModelComponent(referencedArtifact);
						needSave = false;
					}
				}

				// take care of contained artifacts same way
				// This is looking DOWNWARDS
				Collection<IModelComponent> contains = artifact
						.getContainedModelComponents();
				boolean containsChanged = false;
				for (IModelComponent cont : contains) {

					if (cont instanceof AbstractArtifact) {
						AbstractArtifact containedArt = (AbstractArtifact) cont;
						if (containedArt.getFullyQualifiedName().equals(oldFQN)
								|| containedArt.getFullyQualifiedName().equals(
										newName)) {
							aArtifact.removeContainedModelComponent(cont);
							aArtifact
									.addContainedModelComponent(referencedArtifact);
							needSave = false;
						}
					}
				}

				for (IField field : artifact.getFields()) {
					if (field.getType() != null
							&& field.getType().getFullyQualifiedName().equals(
									oldFQN)) {
						((Type) field.getType())
								.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
						needSave = true;
					}
				}

				for (IMethod method : artifact.getMethods()) {
					if (method.getReturnType() != null
							&& method.getReturnType().getFullyQualifiedName()
									.equals(oldFQN)) {
						((Type) method.getReturnType())
								.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
						needSave = true;
					}

					for (IArgument arg : method.getArguments()) {
						if (arg.getType() != null
								&& arg.getType().getFullyQualifiedName()
										.equals(oldFQN)) {
							((Type) arg.getType())
									.setFullyQualifiedName(referencedArtifact
											.getFullyQualifiedName());
							needSave = true;
						}
					}

					if (referencedArtifact instanceof IExceptionArtifact) {
						for (IException exc : method.getExceptions()) {
							if (exc.getFullyQualifiedName().equals(oldFQN)) {
								exc.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
								needSave = true;
							}
						}
					}
				}

				if (artifact instanceof ISessionArtifact) {
					ISessionArtifact session = (ISessionArtifact) artifact;

					if (referencedArtifact instanceof IQueryArtifact) {
						for (INamedQuery q : session.getNamedQueries()) {
							if (q.getFullyQualifiedName().equals(oldFQN)) {
								q.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
								needSave = true;
							}
						}
					} else if (referencedArtifact instanceof IManagedEntityArtifact) {
						for (IManagedEntityDetails detail : session
								.getManagedEntityDetails()) {
							if (detail.getFullyQualifiedName().equals(oldFQN)) {
								detail.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
								needSave = true;
							}
						}
					} else if (referencedArtifact instanceof IUpdateProcedureArtifact) {
						for (IExposedUpdateProcedure proc : session
								.getExposedUpdateProcedures()) {
							if (proc.getFullyQualifiedName().equals(oldFQN)) {
								proc.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
								needSave = true;
							}
						}
					} else if (referencedArtifact instanceof IEventArtifact) {
						for (IEmittedEvent event : session.getEmittedEvents()) {
							if (event.getFullyQualifiedName().equals(oldFQN)) {
								event.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
								needSave = true;
							}
						}
					}
				} else if (artifact instanceof IQueryArtifact) {
					IQueryArtifact qArt = (IQueryArtifact) artifact;
					if (qArt.getReturnedType() != null
							&& qArt.getReturnedType().getFullyQualifiedName()
									.equals(oldFQN)) {
						((Type) qArt.getReturnedType())
								.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
						needSave = true;
					}
				} else if (artifact instanceof IAssociationArtifact) {
					IAssociationArtifact assoc = (IAssociationArtifact) artifact;
					if (assoc.getAEnd() != null
							&& assoc.getAEnd().getType() != null
							&& assoc.getAEnd().getType()
									.getFullyQualifiedName().equals(oldFQN)) {
						((Type) assoc.getAEnd().getType())
								.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
						needSave = true;
					}
					if (assoc.getZEnd() != null
							&& assoc.getZEnd().getType() != null
							&& assoc.getZEnd().getType()
									.getFullyQualifiedName().equals(oldFQN)) {
						((Type) assoc.getZEnd().getType())
								.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
						needSave = true;
					}
				} else if (artifact instanceof IDependencyArtifact) {
					IDependencyArtifact dep = (IDependencyArtifact) artifact;
					if (dep.getAEndType() != null
							&& dep.getAEndType().getFullyQualifiedName()
									.equals(oldFQN)) {
						((Type) dep.getAEndType())
								.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
						needSave = true;
					}
					if (dep.getZEndType() != null
							&& dep.getZEndType().getFullyQualifiedName()
									.equals(oldFQN)) {
						((Type) dep.getZEndType())
								.setFullyQualifiedName(referencedArtifact
										.getFullyQualifiedName());
						needSave = true;
					}
				}

				if (needSave)
					artifact.doSave(new NullProgressMonitor());
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e); // FIXME
		}
	}

	@Override
	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.SET);

		try {
			AbstractArtifact comp = (AbstractArtifact) getMgrSession()
					.getArtifactByFullyQualifiedName(
							Util.packageOf(getArtifactFQN()) + "." + newName);
			delta
					.setAffectedModelComponentURI((URI) comp
							.getAdapter(URI.class));

			delta.setFeature("name");
			URI newUri = (URI) comp.getAdapter(URI.class);
			IPath path = new Path(comp.getProject().getName());
			path = path.append(getArtifactFQN());
			URI oldUri = URI.createHierarchicalURI(
					TigerstripeURIAdapterFactory.SCHEME_TS, null, null, path
							.segments(), null, null);
			delta.setNewValue(newUri);
			delta.setOldValue(oldUri);

			delta.setSource(this);
			delta.setProject(comp.getProject());

		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return ModelChangeDelta.UNKNOWNDELTA;
		}

		return delta;
	}

}
