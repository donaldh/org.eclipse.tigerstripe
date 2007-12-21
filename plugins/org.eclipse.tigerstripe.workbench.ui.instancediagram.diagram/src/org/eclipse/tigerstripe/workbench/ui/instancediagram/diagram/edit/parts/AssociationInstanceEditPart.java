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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.AssociationInstanceItemSemanticEditPolicy;

/**
 * @generated
 */
public class AssociationInstanceEditPart extends ConnectionNodeEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3001;

	/**
	 * @generated
	 */
	public AssociationInstanceEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new AssociationInstanceItemSemanticEditPolicy());
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
		return new AssociationInstanceFigure();
	}

	/**
	 * @generated
	 */
	public class AssociationInstanceFigure extends
			org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx {

		/**
		 * @generated
		 */
		public AssociationInstanceFigure() {

		}

		protected org.eclipse.draw2d.PolylineDecoration getNavigableArrow() {
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

	@Override
	public EditPart getPrimaryChildEditPart() {
		return null;
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
		AssociationInstance association = (AssociationInstance) resolveSemanticElement();
		AssociationInstanceFigure associationFigure = (AssociationInstanceFigure) getFigure();
		if (association == null || associationFigure == null)
			return;
		// check to see if either the aEnd or the zEnd reference is to a class
		// instance that is part
		// of an association class; if so then this association edit part's view
		// is part of an
		// association class as well, so we need to update both parts
		boolean isAssocClassAEndAssoc = ((ClassInstance) association.getZEnd())
				.isAssociationClassInstance();
		boolean isAssocClassZEndAssoc = ((ClassInstance) association.getAEnd())
				.isAssociationClassInstance();
		ClassInstance classInstance = null;
		AssociationInstance aEndInstance = null;
		AssociationInstance zEndInstance = null;
		AssociationInstanceFigure aEndFigure = null;
		AssociationInstanceFigure zEndFigure = null;
		if (isAssocClassAEndAssoc) {
			classInstance = (ClassInstance) association.getZEnd();
			aEndInstance = association;
			aEndFigure = associationFigure;
		} else if (isAssocClassZEndAssoc) {
			classInstance = (ClassInstance) association.getAEnd();
			zEndInstance = association;
			zEndFigure = associationFigure;
		}
		if (isAssocClassAEndAssoc || isAssocClassZEndAssoc) {
			List<AssociationInstance> assocs = classInstance.getAssociations();
			for (AssociationInstance assoc : assocs) {
				if (assoc != association) {
					// get the other figure
					InstanceMapEditPart mapEditPart = (InstanceMapEditPart) this
							.getSource().getParent();
					AssociationInstanceEditPart otherEditPart = (AssociationInstanceEditPart) findConnectionEditPart(
							assoc, mapEditPart);
					if (otherEditPart != null) {
						AssociationInstanceFigure otherFigure = (AssociationInstanceFigure) otherEditPart
								.getFigure();
						if (isAssocClassAEndAssoc) {
							zEndInstance = assoc;
							zEndFigure = otherFigure;
						} else {
							aEndInstance = assoc;
							aEndFigure = otherFigure;
						}
						break;
					}
				}
			}
			if (aEndInstance != null) {
				// use the values gathered above to update both figures...
				if (aEndInstance.getAEndAggregation() == AggregationEnum.COMPOSITE_LITERAL) {
					// AEnd has a composition relationship with AEnd, set
					// diamond
					// for source decoration
					aEndFigure.setSourceDecoration(aEndFigure
							.getCompositionDiamond());
				} else if (aEndInstance.getAEndAggregation() == AggregationEnum.SHARED_LITERAL) {
					// AEnd has a shared relationship with AEnd, set open
					// diamond
					// for source decoration
					aEndFigure.setSourceDecoration(aEndFigure
							.getSharedDiamond());
				} else if (aEndInstance.getAEndAggregation() == AggregationEnum.NONE_LITERAL) {
					// if not aggregation, check to see if AEnd is navigable; if
					// so
					// then set an arrow for the source decoration, else (or if
					// the
					// ZEnd is also navigable) set no source decoration
					if (aEndInstance.isAEndIsNavigable()
							&& (zEndInstance == null || !zEndInstance
									.isZEndIsNavigable()))
						aEndFigure.setSourceDecoration(aEndFigure
								.getNavigableArrow());
					else
						aEndFigure.setSourceDecoration(null);
				}
			}
			if (zEndInstance != null) {
				if (zEndInstance.getZEndAggregation() == AggregationEnum.COMPOSITE_LITERAL) {
					// ZEnd has a composition relationship with ZEnd, set
					// diamond
					// for target decoration
					zEndFigure.setTargetDecoration(zEndFigure
							.getCompositionDiamond());
				} else if (zEndInstance.getZEndAggregation() == AggregationEnum.SHARED_LITERAL) {
					// ZEnd has a composition relationship with ZEnd, set
					// diamond
					// for target decoration
					zEndFigure.setTargetDecoration(zEndFigure
							.getSharedDiamond());
				} else if (zEndInstance.getZEndAggregation() == AggregationEnum.NONE_LITERAL) {
					// if not aggregation, check to see if ZEnd is navigable; if
					// so
					// then set an arrow for the target decoration, else (or if
					// the
					// AEnd is also navigable) then set no target decoration
					if (zEndInstance.isZEndIsNavigable()
							&& (aEndInstance == null || !aEndInstance
									.isAEndIsNavigable()))
						zEndFigure.setTargetDecoration(zEndFigure
								.getNavigableArrow());
					else
						zEndFigure.setTargetDecoration(null);
				}
			}
		} else {
			// else, is just an association, so proceed accordingly
			if (association.getAEndAggregation() == AggregationEnum.COMPOSITE_LITERAL) {
				// AEnd has a composition relationship with AEnd, set diamond
				// for source decoration
				associationFigure.setSourceDecoration(associationFigure
						.getCompositionDiamond());
			} else if (association.getAEndAggregation() == AggregationEnum.SHARED_LITERAL) {
				// AEnd has a shared relationship with AEnd, set open diamond
				// for source decoration
				associationFigure.setSourceDecoration(associationFigure
						.getSharedDiamond());
			} else if (association.getAEndAggregation() == AggregationEnum.NONE_LITERAL) {
				// if not aggregation, check to see if AEnd is navigable; if so
				// then set an arrow for the source decoration, else (or if the
				// ZEnd is also navigable) set no source decoration
				if (association.isAEndIsNavigable()
						&& !association.isZEndIsNavigable())
					associationFigure.setSourceDecoration(associationFigure
							.getNavigableArrow());
				else
					associationFigure.setSourceDecoration(null);
			}
			if (association.getZEndAggregation() == AggregationEnum.COMPOSITE_LITERAL) {
				// ZEnd has a composition relationship with ZEnd, set diamond
				// for target decoration
				associationFigure.setTargetDecoration(associationFigure
						.getCompositionDiamond());
			} else if (association.getZEndAggregation() == AggregationEnum.SHARED_LITERAL) {
				// ZEnd has a composition relationship with ZEnd, set diamond
				// for target decoration
				associationFigure.setTargetDecoration(associationFigure
						.getSharedDiamond());
			} else if (association.getZEndAggregation() == AggregationEnum.NONE_LITERAL) {
				// if not aggregation, check to see if ZEnd is navigable; if so
				// then set an arrow for the target decoration, else (or if the
				// AEnd is also navigable) then set no target decoration
				if (association.isZEndIsNavigable()
						&& !association.isAEndIsNavigable())
					associationFigure.setTargetDecoration(associationFigure
							.getNavigableArrow());
				else
					associationFigure.setTargetDecoration(null);
			}
		}
		super.refreshVisuals();
	}

	@Override
	public void handleNotificationEvent(Notification event) {
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
		super.handleNotificationEvent(event);
	}

	public EditPart findConnectionEditPart(EObject theElement,
			InstanceMapEditPart epStart) {
		if (theElement == null)
			return null;

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
