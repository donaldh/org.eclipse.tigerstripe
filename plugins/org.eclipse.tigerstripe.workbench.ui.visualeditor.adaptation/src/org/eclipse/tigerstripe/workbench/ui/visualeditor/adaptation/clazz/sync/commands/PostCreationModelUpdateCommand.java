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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest.ConnectionViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.PreferencesHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.VisualeditorRelationshipUtils;

/**
 * This command is composed with a CreateViewAndElementRequest to populate the
 * newly created EObject based on the corresponding artifact
 * 
 * This is used during a Drag-n-Drop operation
 * 
 * @author Eric Dillon
 * 
 */
public class PostCreationModelUpdateCommand extends
		AbstractTransactionalCommand {

	// private CreateViewAndElementRequest createRequest;

	// private DropObjectsRequest dropRequest;

	private List<IAbstractArtifact> artifacts = new ArrayList<IAbstractArtifact>();

	private List<ViewAndElementDescriptor> descriptors;

	private List<EObject> eObjects = new ArrayList<EObject>();

	private ITigerstripeModelProject tsProject;

	private final EditPart editPart;

	public PostCreationModelUpdateCommand(TransactionalEditingDomain domain,
			List<ViewAndElementDescriptor> descriptors,
			List<IAbstractArtifact> artifacts,
			ITigerstripeModelProject tsProject, EditPart editPart) {
		super(domain, "PostCreationModelUpdateCommand", null);

		this.descriptors = descriptors;
		this.artifacts = artifacts;
		this.tsProject = tsProject;
		this.editPart = editPart;
	}

	public PostCreationModelUpdateCommand(TransactionalEditingDomain domain,
			QualifiedNamedElement eObject, IAbstractArtifact iArtifact,
			ITigerstripeModelProject tsProject) {
		super(domain, "PostCreationModelUpdateCommand", null);
		this.artifacts.add(iArtifact);
		this.eObjects.add(eObject);
		this.tsProject = tsProject;
		this.editPart = null;
	}

	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		boolean canExecute = super.canExecute();
		return canExecute;
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {

		if (descriptors != null) {
			// this means the constructor based on descriptors was used
			for (ViewDescriptor desc : descriptors) {
				if (desc instanceof ViewAndElementDescriptor) {
					ViewAndElementDescriptor descriptor = (ViewAndElementDescriptor) desc;
					EObject eObj = (EObject) descriptor
							.getCreateElementRequestAdapter().getAdapter(
									EObject.class);
					eObjects.add(eObj);
				} else if (desc instanceof ConnectionViewAndElementDescriptor) {
					ConnectionViewAndElementDescriptor descriptor = (ConnectionViewAndElementDescriptor) desc;
					EObject eObj = (EObject) descriptor
							.getCreateElementRequestAdapter().getAdapter(
									EObject.class);
					eObjects.add(eObj);
				}
			}
		}

		// The update needs to occur in 2 steps.
		// First one pass to set up the names/packages on the newly created
		// objects
		// so we can cross-reference them, and THEN update all the
		// attr/meth/assocs...

		int index = 0;
		EObject previousObject = null;
		for (EObject obj : eObjects) {
			if (obj instanceof QualifiedNamedElement) {
				// set name properly

				try {
					BaseETAdapter.setIgnoreNotify(true);

					QualifiedNamedElement eArtifact = (QualifiedNamedElement) obj;
					IAbstractArtifact iArtifact = artifacts.get(index);
					try {
						eArtifact.setCorrespondingProject(iArtifact
								.getProject());
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
					eArtifact.setName(iArtifact.getName());
					eArtifact.setPackage(iArtifact.getPackage());
				} finally {
					BaseETAdapter.setIgnoreNotify(false);
				}
			}

			if (!(obj instanceof AssociationClass)) { // we skip because we
				// know
				// the following
				// AssociationClassClass is
				// following and needs the
				// same stuff
				index++;
			}

			if (obj instanceof AssociationClassClass) {
				((AssociationClass) previousObject)
						.setAssociatedClass((AssociationClassClass) obj);
			}

			previousObject = obj;
		}

		// second pass now.
		index = 0;
		for (EObject obj : eObjects) {
			if (obj instanceof QualifiedNamedElement) {
				// set name properly
				QualifiedNamedElement eArtifact = (QualifiedNamedElement) obj;
				IAbstractArtifact iArtifact = artifacts.get(index);
				Map map = (Map) eArtifact.eContainer();

				buildConnections(iArtifact, eArtifact, map, tsProject);
			}
			if (!(obj instanceof AssociationClass)) // we skip because we know
				// the following
				// AssociationClassClass is
				// following and needs the
				// same stuff
				index++;
		}
		// now update the relationships between these objects
		// During this operation, the ETAdapters still need to ignore
		// update and not push them to the TS domain, or else a round trip
		// occurs and everybody gets confused (duplicated attr, meth, etc...)
		boolean ignore = BaseETAdapter.ignoreNofigy();
		BaseETAdapter.setIgnoreNotify(true);
		try {
			updateRelationships();
		} finally {
			BaseETAdapter.setIgnoreNotify(ignore);
		}

		return CommandResult.newOKCommandResult();
	}

	/**
	 * Delegates the post processing of the specifics of each type of artifact
	 * and builds all the necessary connections
	 * 
	 * @param iArtifact
	 * @param eArtifact
	 * @param map
	 */
	private void buildConnections(IAbstractArtifact iArtifact,
			QualifiedNamedElement eArtifact, Map map,
			ITigerstripeModelProject tsProject) {

		BasePostCreationElementUpdater updater = null;

		final boolean hideExtends = isHideExtends();

		// Take care of the outgoing connections first
		if (iArtifact instanceof IQueryArtifact) {
			updater = new PostCreationQueryArtifactUpdater(iArtifact,
					(AbstractArtifact) eArtifact, map, tsProject, hideExtends);
		} else if (iArtifact instanceof ISessionArtifact) {
			updater = new PostCreationSessionArtifactUpdater(iArtifact,
					(AbstractArtifact) eArtifact, map, tsProject, hideExtends);
		} else if (iArtifact instanceof IManagedEntityArtifact) {
			updater = new PostCreationEntityArtifactUpdater(iArtifact,
					(AbstractArtifact) eArtifact, map, tsProject, hideExtends);
		} else if (iArtifact instanceof IEventArtifact) {
			updater = new PostCreationNotificationArtifactUpdater(iArtifact,
					(AbstractArtifact) eArtifact, map, tsProject, hideExtends);
		} else if (iArtifact instanceof IEnumArtifact) {
			updater = new PostCreationEnumArtifactUpdater(iArtifact,
					(AbstractArtifact) eArtifact, map, tsProject, hideExtends);
		} else if (iArtifact instanceof IDatatypeArtifact) {
			updater = new PostCreationDatatypeArtifactUpdater(iArtifact,
					(AbstractArtifact) eArtifact, map, tsProject, hideExtends);
		} else if (iArtifact instanceof IUpdateProcedureArtifact) {
			updater = new PostCreationUpdateProcedureArtifactUpdater(iArtifact,
					(AbstractArtifact) eArtifact, map, tsProject, hideExtends);
		} else if (iArtifact instanceof IDependencyArtifact) {
			updater = new PostCreationDependencyArtifactUpdater(iArtifact,
					(Dependency) eArtifact, map, tsProject);
		} else if (iArtifact instanceof IAssociationClassArtifact
				&& eArtifact instanceof AssociationClass) {
			// if here, then we know that we are on the AssociationClass
			// part of the AssociationClass (not the AssociationClassClass part)
			updater = new PostCreationAssociationClassArtifactUpdater(
					iArtifact, (AssociationClass) eArtifact, map, tsProject);
		} else if (iArtifact instanceof IExceptionArtifact) {
			updater = new PostCreationExceptionArtifactUpdater(iArtifact,
					(AbstractArtifact) eArtifact, map, tsProject, hideExtends);
		} else if (iArtifact instanceof IAssociationArtifact
				&& eArtifact instanceof Association) {
			// if here, then we know that we are on an Association (and not on
			// an AssociationClassClass, which is part of an AssociationClass,
			// which is a type of Association)
			updater = new PostCreationAssociationArtifactUpdater(iArtifact,
					(Association) eArtifact, map, tsProject);
		}

		if (updater != null) {
			try {
				updater.updateConnections();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	private boolean isHideExtends() {
		if (editPart == null) {
			return false;
		}
		Iterator<?> it = editPart.getRoot().getChildren().iterator();
		if (!it.hasNext()) {
			return false;
		}
		Object next = it.next();
		if (next instanceof EditPart) {
			Object model = ((EditPart) next).getModel();
			if (model instanceof Diagram) {
				return PreferencesHelper
						.isHideExtendsRelationship(PreferencesHelper
								.getStore((Diagram) model));
			}
		}
		return false;
	}

	protected void updateRelationships() {
		List<String> alreadySeen = new ArrayList<String>();
		for (EObject eObject : eObjects) {
			if (!(eObject instanceof AbstractArtifact))
				continue;
			AbstractArtifact eArtifact = (AbstractArtifact) eObject;
			Map map = (Map) eArtifact.eContainer();
			Object[] returnObjects = null;
			IAbstractArtifact iArtifact = null;
			try {
				iArtifact = eArtifact.getCorrespondingIArtifact();
				returnObjects = VisualeditorRelationshipUtils
						.getPossibleRelationshipsForArtifact(tsProject, map,
								iArtifact);
			} catch (TigerstripeException e) {
			}
			Set<IRelationship> possibleRelationships = null;
			HashMap<String, QualifiedNamedElement> nodesInMap = null;
			if (returnObjects != null && returnObjects.length == 4) {
				// extract the returned values from the object array
				possibleRelationships = (Set<IRelationship>) returnObjects[0];
				nodesInMap = (HashMap<String, QualifiedNamedElement>) returnObjects[3];
			}
			for (IRelationship relationship : possibleRelationships) {
				String aEndName = relationship.getRelationshipAEnd().getType()
						.getFullyQualifiedName();
				String zEndName = relationship.getRelationshipZEnd().getType()
						.getFullyQualifiedName();
				if (aEndName.equals(iArtifact.getFullyQualifiedName())) {
					// this eObject corresponds to the aEnd of this
					// relationship, so if we've already loaded the
					// relationships for the class with a fully
					// qualified name that matches the zEndName,
					// skip creation of this relationship
					if (alreadySeen.contains(zEndName))
						continue;
				} else if (zEndName.equals(iArtifact.getFullyQualifiedName())) {
					// this eObject corresponds to the zEnd of this
					// relationship, so if we've already loaded the
					// relationships for the class with a fully
					// qualified name that matches the aEndName,
					// skip creation of this relationship
					if (alreadySeen.contains(aEndName))
						continue;
				}
				if (relationship instanceof IAssociationClassArtifact) {
					IAssociationClassArtifact assocClassArtifact = (IAssociationClassArtifact) relationship;
					AssociationClass assocClass = VisualeditorFactory.eINSTANCE
							.createAssociationClass();
					AssociationClassClass assocClassClass = VisualeditorFactory.eINSTANCE
							.createAssociationClassClass();
					assocClass.setAssociatedClass(assocClassClass);
					// it's critical that the assocClassClass be added to the
					// map before we try to call the initializeAssociationClass
					// method, otherwise the call to initializeAssociationClass
					// can fail with a NullPointerException if there is one (or
					// more) Enumeration attributes included in the
					// AssociationClass iArtifact
					map.getArtifacts().add(assocClassClass);
					VisualeditorRelationshipUtils.initializeAssociationClass(
							assocClass, assocClassArtifact, nodesInMap);
					map.getAssociations().add(assocClass);
				} else if (relationship instanceof IAssociationArtifact) {
					IAssociationArtifact assocArtifact = (IAssociationArtifact) relationship;
					Association assoc = VisualeditorFactory.eINSTANCE
							.createAssociation();
					VisualeditorRelationshipUtils.initializeAssociation(assoc,
							assocArtifact, nodesInMap);
					map.getAssociations().add(assoc);
				} else if (relationship instanceof IDependencyArtifact) {
					IDependencyArtifact depArtifact = (IDependencyArtifact) relationship;
					Dependency dependency = VisualeditorFactory.eINSTANCE
							.createDependency();
					VisualeditorRelationshipUtils.initializeDependency(
							dependency, depArtifact, nodesInMap);
					map.getDependencies().add(dependency);
				}
			}
			alreadySeen.add(eArtifact.getFullyQualifiedName());
		}
	}

}
