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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;

public class AssociationCanonicalEditPolicy extends
		CanonicalConnectionEditPolicy {

	Association association = null;

	@Override
	protected void handleNotificationEvent(Notification arg0) {
		// all we need is to make sure the EditMapPart is refreshed.
		((MapEditPart) getHost().getParent()).refreshCanonicalPolicy();
		// if ( arg0.getFeature() instanceof EAttribute ) {
		// EAttribute attr = (EAttribute) arg0.getFeature();
		// if ( "isAbstract".equals(attr.getName())) {
		// List children = getHost().getChildren();
		// for( Object child : children ) {
		// if ( child instanceof AbstractLabelEditPart ) {
		// AbstractLabelEditPart part = (AbstractLabelEditPart) child;
		// part.refresh();
		// }
		// }
		// }
		// }
		super.handleNotificationEvent(arg0);
	}

	@Override
	public void activate() {
		association = (Association) ((AssociationEditPart) getHost())
				.resolveSemanticElement();
	}

	@Override
	public void deactivate() {
		association = null;
	}

	@Override
	protected List getSemanticConnectionsList() {
		// return the list of relationships or semantic elements that are to be
		// synchronized with the notation edges associated with the container
		return Collections.EMPTY_LIST;
	}

	@Override
	protected EObject getTargetElement(EObject relationship) {
		// given a semantic element that is to be represented as a notation edge
		// in the container, return the target element that this semantic
		// element
		// is attached to.
		return association.getZEnd();
	}

	@Override
	protected EObject getSourceElement(EObject relationship) {
		// given a semantic element that is to be represented as a notation edge
		// in the container, return the source element that this semantic
		// element
		// is attached to.
		return association.getAEnd();
	}

}
