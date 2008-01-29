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

import java.io.File;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.INamedQuery;
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
		IAbstractArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		String target = newName;
		if (art != null) {
			String artPack = art.getPackage();
			if (artPack != null && artPack.length() != 0) {
				target = artPack + "." + newName;
			}
			IAbstractArtifact newArt = mgrSession
					.getIArtifactByFullyQualifiedName(target);
			return newArt == null;
		}

		return false;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {

		IAbstractArtifact origArt = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());

		AbstractArtifact aArt = (AbstractArtifact) origArt;
		String oldFQN = aArt.getFullyQualifiedName();

		// This is using the fact that this is a file. Will need to be removed
		// to attach to EMF (nokia)
		String artPath = aArt.getArtifactPath();
		String projectDir = aArt.getArtifactManager().getTSProject()
				.getBaseDir().toString();
		String fullArtPath = projectDir + File.separator + artPath;
		File f = new File(fullArtPath);

		// Bug 939: should be using a rename rather than doing a remove/add
		// manually
		mgrSession
				.renameArtifact(origArt, origArt.getPackage() + "." + newName);
		origArt.doSave(new TigerstripeNullProgressMonitor());

		if (!f.delete()) {
			// TigerstripeRuntime.logInfoMessage("Error deleting: " +
			// f.toURI());
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
	private void updateReferences(IArtifactManagerSession mgrSession,
			IAbstractArtifact referencedArtifact, String oldFQN) {
		IQueryAllArtifacts query = (IQueryAllArtifacts) mgrSession
				.makeQuery(IQueryAllArtifacts.class.getName());
		String newName = referencedArtifact.getFullyQualifiedName();
		try {
			Collection<IAbstractArtifact> artifacts = mgrSession
					.queryArtifact(query);
			for (IAbstractArtifact artifact : artifacts) {
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
					artifact.doSave(new TigerstripeNullProgressMonitor());
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e); // FIXME
		}
	}
}
