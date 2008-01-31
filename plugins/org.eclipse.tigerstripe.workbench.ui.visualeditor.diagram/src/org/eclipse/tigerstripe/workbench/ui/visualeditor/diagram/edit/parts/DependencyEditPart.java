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
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.DependencyCanonicalEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.DependencyItemSemanticEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeVisualIDRegistry;

/**
 * @generated NOT
 */
public class DependencyEditPart extends ConnectionNodeEditPart implements
		TigerstripeEditableEntityEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3008;

	/**
	 * @generated
	 */
	public DependencyEditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new DependencyCanonicalEditPolicy());
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new DependencyItemSemanticEditPolicy());
	}

	@Override
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(TigerstripeVisualIDRegistry
				.getType(DependencyNamePackageEditPart.VISUAL_ID));
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
		return new DependencyConnectionFigure();
	}

	/**
	 * @generated
	 */
	public class DependencyConnectionFigure extends
			org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx {

		/**
		 * @generated
		 */
		public DependencyConnectionFigure() {

			this.setLineStyle(org.eclipse.draw2d.Graphics.LINE_DASH);
			setTargetDecoration(createTargetDecoration());
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

	}

	protected void removeDependency(Dependency dep) {
		DestroyElementRequest der = new DestroyElementRequest(dep, false);
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

		if (event.getNotifier() instanceof Dependency
				&& event.getEventType() == Notification.SET
				&& event.getFeature() instanceof EReferenceImpl
				&& event.getNewValue() == null) {
			Dependency dep = (Dependency) event.getNotifier();
			removeDependency(dep);
		}
		super.handleNotificationEvent(event);
	}

}
