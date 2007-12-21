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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.render.editparts.RenderedDiagramRootEditPart;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.AssociationCanonicalEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.AssociationItemSemanticEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.AssociationOpenEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeVisualIDRegistry;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;

/**
 * @generated NOT
 */
public class AssociationEditPart extends ConnectionNodeEditPart implements
		TigerstripeEditableEntityEditPart, PropertyAwarePart,
		AssociationEnabledActionPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3001;

	/**
	 * @generated
	 */
	public AssociationEditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new AssociationCanonicalEditPolicy());
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new AssociationItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.OPEN_ROLE,
				new AssociationOpenEditPolicy());
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model so
	 * you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
	@Override
	protected Connection createConnectionFigure() {
		return new AssociationFigure();
	}

	@Override
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(TigerstripeVisualIDRegistry
				.getType(AssociationNamePackageEditPart.VISUAL_ID));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		/*
		 * check to see what sorts of decorations to apply based on the
		 * properties of the Association model object. If either end is a
		 * "composition" relationship, set to a diamond. If either end is
		 * navigable, then set to an arrow. If an end has a composition
		 * relationship and is navigable, then will display a diamond (although
		 * this condition should not occur)
		 */
		Association association = (Association) resolveSemanticElement();
		AssociationFigure associationFigure = (AssociationFigure) getFigure();
		if (association == null || associationFigure == null)
			return;
		if (association.getAEndAggregation() == AggregationEnum.COMPOSITE_LITERAL) {
			// AEnd has a composition relationship with AEnd, set diamond for
			// source decoration
			associationFigure.setSourceDecoration(associationFigure
					.getCompositionDiamond());
		} else if (association.getAEndAggregation() == AggregationEnum.SHARED_LITERAL) {
			// AEnd has a shared relationship with AEnd, set open diamond for
			// source decoration
			associationFigure.setSourceDecoration(associationFigure
					.getSharedDiamond());
		} else if (association.getAEndAggregation() == AggregationEnum.NONE_LITERAL) {
			// if not aggregation, check to see if ZEnd is navigable; if so then
			// set an arrow for
			// the source decoration, else set no source decoration
			if (association.isAEndIsNavigable()
					&& !association.isZEndIsNavigable())
				associationFigure.setSourceDecoration(associationFigure
						.getNavigableArrow());
			else
				associationFigure.setSourceDecoration(null);
		}
		if (association.getZEndAggregation() == AggregationEnum.COMPOSITE_LITERAL) {
			// ZEnd has a composition relationship with ZEnd, set diamond for
			// target decoration
			associationFigure.setTargetDecoration(associationFigure
					.getCompositionDiamond());
		} else if (association.getZEndAggregation() == AggregationEnum.SHARED_LITERAL) {
			// ZEnd has a composition relationship with ZEnd, set diamond for
			// target decoration
			associationFigure.setTargetDecoration(associationFigure
					.getSharedDiamond());
		} else if (association.getZEndAggregation() == AggregationEnum.NONE_LITERAL) {
			// if not aggregation, check to see if AEnd is navigable; if so then
			// set an arrow for
			// the target decoration, else set no target decoration
			if (association.isZEndIsNavigable()
					&& !association.isAEndIsNavigable())
				associationFigure.setTargetDecoration(associationFigure
						.getNavigableArrow());
			else
				associationFigure.setTargetDecoration(null);
		}
		super.refreshVisuals();
	}

	protected void removeAssociation(Association assoc) {
		DestroyElementRequest der = new DestroyElementRequest(assoc, false);
		DestroyElementCommand dec = new DestroyElementCommand(der);
		if (dec.canExecute()) {
			try {
				OperationHistoryFactory.getOperationHistory().execute(dec,
						new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	@Override
	public void handleNotificationEvent(Notification event) {

		if ((event.getEventType() == Notification.SET || event.getEventType() == Notification.UNSET)
				&& event.getFeature() instanceof EReference
				&& (((EReference) event.getFeature()).getName().equals("zEnd") || ((EReference) event
						.getFeature()).getName().equals("aEnd"))
				&& event.getOldValue() != null) {
			// this ensures that the connection is re-drawn when one of the ends
			// is changed.
			RenderedDiagramRootEditPart blah = (RenderedDiagramRootEditPart) getParent();
			((MapEditPart) blah.getContents()).refreshCanonicalPolicy();
			// Change here for #664. Was refreshing the MapEditPart instead of
			// only
			// refreshing the canonical edit policy. It seems this was causing
			// the problem
			// where only the first assoc/assocClass/Dep would be removed.
		}

		/*
		 * if the feature being updated is not a reference and the newValue
		 * being set is not null then refresh the visuals (this is where we end
		 * up when we are updating the decorations on the Association ends due
		 * to changes in the model's values)
		 */
		if (!(event.getFeature() instanceof EReferenceImpl)
				&& event.getNewValue() != null) {
			refreshVisuals();
		}
		/*
		 * else if the event is a "SET" event and either of the references (aEnd
		 * or zEnd) are being set to null, then we are deleting either the
		 * source or the target object; in these cases, create a new command to
		 * delete the Association and run the command...
		 */
		else if (event.getNotifier() instanceof Association
				&& event.getEventType() == Notification.SET
				&& event.getFeature() instanceof EReferenceImpl
				&& !((EReferenceImpl) event.getFeature()).getName().equals(
						"associationClassEnd") && event.getNewValue() == null) {
			Association assoc = (Association) event.getNotifier();
			removeAssociation(assoc);
		}
		super.handleNotificationEvent(event);
	}

	/**
	 * @generated
	 */
	public class AssociationFigure extends
			org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx {

		/**
		 * @generated NOT
		 */
		public AssociationFigure() {

			// setTargetDecoration(createTargetDecoration());
		}

		/**
		 * @generated
		 */
		private org.eclipse.draw2d.PolylineDecoration createTargetDecoration() {
			org.eclipse.draw2d.PolylineDecoration df = new org.eclipse.draw2d.PolylineDecoration();
			// dispatchNext?

			org.eclipse.draw2d.geometry.PointList pl = new org.eclipse.draw2d.geometry.PointList();
			pl.addPoint(-2, 2);
			pl.addPoint(0, 0);
			pl.addPoint(-2, -2);
			df.setTemplate(pl);
			df.setScale(getMapMode().DPtoLP(7), getMapMode().DPtoLP(3));

			return df;
		}

		protected org.eclipse.draw2d.PolylineDecoration getNavigableArrow() {
			return createTargetDecoration();
		}

		private org.eclipse.draw2d.PolygonDecoration getCompositionDiamond() {
			org.eclipse.draw2d.PolygonDecoration df = new org.eclipse.draw2d.PolygonDecoration();
			org.eclipse.draw2d.geometry.PointList pl = new org.eclipse.draw2d.geometry.PointList();
			pl.addPoint(-1, 1);
			pl.addPoint(0, 0);
			pl.addPoint(-1, -1);
			pl.addPoint(-2, 0);
			df.setTemplate(pl);
			df.setScale(getMapMode().DPtoLP(7), getMapMode().DPtoLP(3));
			return df;
		}

		private org.eclipse.draw2d.PolygonDecoration getSharedDiamond() {
			org.eclipse.draw2d.PolygonDecoration df = getCompositionDiamond();
			df.setBackgroundColor(ColorConstants.white);
			return df;
		}

	}

	// As a result of implementing PropertyAwarePart, this method is called
	// whenever a local property is changed on this part
	public void propertyChanged(DiagramProperty property) {
		if (NamedElementPropertiesHelper.ASSOC_DETAILS.equals(property
				.getName())) {
			for (Object obj : getChildren()) {
				if (obj instanceof AssociationAEndNameEditPart) {
					AssociationAEndNameEditPart part = (AssociationAEndNameEditPart) obj;
					part.refresh();
				} else if (obj instanceof AssociationZEndNameEditPart) {
					AssociationZEndNameEditPart part = (AssociationZEndNameEditPart) obj;
					part.refresh();
				} else if (obj instanceof AssociationNamePackageEditPart) {
					AssociationNamePackageEditPart part = (AssociationNamePackageEditPart) obj;
					part.refresh();
				}
			}
		}
	}

}
