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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ClassDiagramDragDropEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.MapCanonicalEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.MapItemSemanticEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.DiagramPropertiesHelper;

/**
 * @generated
 */
public class MapEditPart extends DiagramEditPart {

	private List assocClassConnections = new ArrayList();

	/**
	 * @generated
	 */
	public static String MODEL_ID = "Tigerstripe"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 79;

	/**
	 * @generated
	 */
	public MapEditPart(View view) {
		super(view);
	}

	public void refreshCanonicalPolicy() {
		MapCanonicalEditPolicy canonicalPolicy = (MapCanonicalEditPolicy) getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
		canonicalPolicy.refresh();
	}

	@Override
	public void refresh() {
		refreshCanonicalPolicy();
		super.refresh();
	}

	public boolean addAssocClassConnection(
			AssociationClassConnectionEditPart assocClassConnEditPart) {
		return assocClassConnections.add(assocClassConnEditPart);
	}

	public boolean removeAssocClassConnection(
			AssociationClassConnectionEditPart assocClassConnEditPart) {
		return assocClassConnections.remove(assocClassConnEditPart);
	}

	/**
	 * this method is used to append the association class connections to the
	 * list of connections that are managed by the underlying DiagramEditPart
	 */
	@Override
	public List getConnections() {
		List result = new ArrayList();
		List connections = super.getConnections();
		result.addAll(connections);
		result.addAll(assocClassConnections);
		return result;
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new MapItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new MapCanonicalEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new ClassDiagramDragDropEditPolicy());
		// installEditPolicy(EditPolicyRoles.CREATION_ROLE,
		// new ClassDiagramCreationEditPolicy());
	}

	/*
	 * removes any association class that might be attached to an association
	 * from the diagram when the association is removed
	 */
	// protected void removeAssocClassFromAssoc(Association assoc) {
	// AssociationClass assocClass = assoc.getAssociationClassEnd();
	// localAcepRef = (AssociationClassEditPart) findEditPart(this, assocClass);
	// if (localAcepRef != null) {
	// localMepRef = this;
	// AbstractTransactionalCommand command = new AbstractTransactionalCommand(
	// localMepRef.getEditingDomain(), "Delete AssociationClass",
	// null) {
	// protected CommandResult doExecuteWithResult(
	// IProgressMonitor monitor, IAdaptable info)
	// throws ExecutionException {
	// localMepRef.removeChildVisual(localAcepRef);
	// return CommandResult.newOKCommandResult();
	// }
	// };
	// try {
	// OperationHistoryFactory.getOperationHistory().execute(command,
	// new NullProgressMonitor(), null);
	// } catch (ExecutionException e) {
	// EclipsePlugin.log(e);
	// }
	// }
	// }
	//
	/*
	 * restores an association class that tied to an association at the EMF
	 * model level to the diagram when the association is restored
	 */
	// protected void restoreAssocClassToAssoc(Association assoc) {
	// AssociationClass assocClass = assoc.getAssociationClassEnd();
	// localAcepRef = (AssociationClassEditPart) findEditPart(this, assocClass);
	// if (localAcepRef != null) {
	// localMepRef = this;
	// AbstractTransactionalCommand command = new AbstractTransactionalCommand(
	// localMepRef.getEditingDomain(), "Restore AssociationClass",
	// null) {
	// protected CommandResult doExecuteWithResult(
	// IProgressMonitor monitor, IAdaptable info)
	// throws ExecutionException {
	// localMepRef.addChildVisual(localAcepRef, 0);
	// return CommandResult.newOKCommandResult();
	// }
	// };
	// try {
	// OperationHistoryFactory.getOperationHistory().execute(command,
	// new NullProgressMonitor(), null);
	// } catch (ExecutionException e) {
	// EclipsePlugin.log(e);
	// }
	// }
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handleNotificationEvent(org.eclipse.emf.common.notify.Notification)
	 */
	// protected void handleNotificationEvent(Notification event) {
	// Object oldValue = event.getOldValue();
	// Object newValue = event.getNewValue();
	// if (event.getEventType() == Notification.REMOVE
	// && oldValue instanceof Association) {
	// Association assoc = (Association) oldValue;
	// // removeAssocClassFromAssoc(assoc);
	// } else if (event.getEventType() == Notification.ADD
	// && newValue instanceof Association) {
	// Association assoc = (Association) newValue;
	// // restoreAssocClassToAssoc(assoc);
	// super.handleNotificationEvent(event);
	// this.refresh();
	// }
	public void diagramPropertyChanged(DiagramProperty property) {
		String name = property.getName();

		if (DiagramPropertiesHelper.HIDEPACKAGESINCOMPARTMENTS.equals(name)
				|| DiagramPropertiesHelper.HIDEDEFAULTVALUES.equals(name)) {
			refreshCompartmentLabels();
		} else if (DiagramPropertiesHelper.HIDESTEREOTYPES.equals(name)) {
			refreshStereotypeLabels();
		} else if (DiagramPropertiesHelper.HIDEARTIFACTPACKAGES.equals(name)) {
			refreshNamePackageLabels();
		}
	}

	private void refreshStereotypeLabels() {
		refreshCompartmentLabels();

		Map map = (Map) ((Diagram) getModel()).getElement();
		List<AbstractArtifact> artifacts = map.getArtifacts();
		List<Association> associations = map.getAssociations();
		List<Dependency> dependencies = map.getDependencies();

		for (AbstractArtifact artifact : artifacts) {
			// then, for each artifact, get the edit part that goes along
			// with it
			IGraphicalEditPart editPart = (IGraphicalEditPart) findEditPart(
					this, artifact);
			if (editPart != null) {
				// if the edit part is not null, get a list of that edit
				// part's children
				List<AbstractEditPart> childEditParts = editPart.getChildren();
				for (AbstractEditPart childEditPart : childEditParts) {
					if (childEditPart instanceof TigerstripeStereotypeEditPart) {
						childEditPart.refresh();
					}
				}
			}
		}

		for (Association assoc : associations) {
			// then, for each artifact, get the edit part that goes along
			// with it
			IGraphicalEditPart editPart = (IGraphicalEditPart) findConnectionEditPart(assoc);
			if (editPart != null) {
				// if the edit part is not null, get a list of that edit
				// part's children
				List<AbstractEditPart> childEditParts = editPart.getChildren();
				for (AbstractEditPart childEditPart : childEditParts) {
					if (childEditPart instanceof TigerstripeStereotypeEditPart || 
							childEditPart instanceof AssociationNamePackageEditPart) {
						childEditPart.refresh();
					}
				}
			}
		}

		for (Dependency dep : dependencies) {
			// then, for each artifact, get the edit part that goes along
			// with it
			IGraphicalEditPart editPart = (IGraphicalEditPart) findConnectionEditPart(dep);
			if (editPart != null) {
				// if the edit part is not null, get a list of that edit
				// part's children
				List<AbstractEditPart> childEditParts = editPart.getChildren();
				for (AbstractEditPart childEditPart : childEditParts) {
					if (childEditPart instanceof DependencyNamePackageEditPart) {
						childEditPart.refresh();
					}
				}
			}
		}

	}

	private void refreshNamePackageLabels() {
		Map map = (Map) ((Diagram) getModel()).getElement();
		List<AbstractArtifact> artifacts = map.getArtifacts();
		for (AbstractArtifact artifact : artifacts) {
			// then, for each artifact, get the edit part that goes along
			// with it
			IGraphicalEditPart editPart = (IGraphicalEditPart) findEditPart(
					this, artifact);
			if (editPart != null) {
				// if the edit part is not null, get a list of that edit
				// part's children
				List<AbstractEditPart> childEditParts = editPart.getChildren();
				for (AbstractEditPart childEditPart : childEditParts) {
					// and then refresh each of the children
					if (childEditPart instanceof NamePackageInterface)
						childEditPart.refresh();
				}
			}
		}
		List<QualifiedNamedElement> linkElements = new ArrayList<QualifiedNamedElement>();
		linkElements.addAll(map.getAssociations());
		linkElements.addAll(map.getDependencies());
		for (QualifiedNamedElement element : linkElements) {
			if (element instanceof AssociationClass)
				continue;
			// then, for each artifact, get the edit part that goes along
			// with it
			IGraphicalEditPart editPart = (IGraphicalEditPart) findConnectionEditPart(element);
			if (editPart != null) {
				// if the edit part is not null, get a list of that edit
				// part's children
				List<AbstractEditPart> childEditParts = editPart.getChildren();
				for (AbstractEditPart childEditPart : childEditParts) {
					// and then refresh each of the children
					if (childEditPart instanceof NamePackageInterface)
						childEditPart.refresh();
				}
			}
		}

	}

	private void refreshCompartmentLabels() {
		Map map = (Map) ((Diagram) getModel()).getElement();
		List<AbstractArtifact> artifacts = map.getArtifacts();
		for (AbstractArtifact artifact : artifacts) {
			// then, for each artifact, get the edit part that goes along
			// with it
			IGraphicalEditPart editPart = (IGraphicalEditPart) findEditPart(
					this, artifact);
			if (editPart != null) {
				// if the edit part is not null, get a list of that edit
				// part's children
				List<AbstractEditPart> childEditParts = editPart.getChildren();
				for (AbstractEditPart childEditPart : childEditParts) {
					if (childEditPart instanceof ListCompartmentEditPart) {
						ListCompartmentEditPart list = (ListCompartmentEditPart) childEditPart;
						for (EditPart child : (List<EditPart>) list
								.getChildren()) {
							if (child instanceof TigerstripeAttributeEditPart
									|| child instanceof TigerstripeMethodEditPart 
									|| child instanceof TigerstripeLiteralEditPart) {
								child.refresh();
							}
						}
					}
				}
			}
		}

	}

	/** Finds an editpart given a starting editpart and an EObject */
	public EditPart findConnectionEditPart(EObject theElement) {
		if (theElement == null)
			return null;
		MapEditPart epStart = this;

		final View view = (View) ((IAdaptable) epStart).getAdapter(View.class);

		if (view != null) {
			EObject el = ViewUtil.resolveSemanticElement(view);

			if ((el != null) && el.equals(theElement))
				return epStart;
		}

		ListIterator childLI = epStart.getConnections().listIterator();
		while (childLI.hasNext()) {
			EditPart epChild = (EditPart) childLI.next();

			EditPart elementEP = findEditPart(epChild, theElement);
			if (elementEP != null)
				return elementEP;
		}
		return null;
	}

}
