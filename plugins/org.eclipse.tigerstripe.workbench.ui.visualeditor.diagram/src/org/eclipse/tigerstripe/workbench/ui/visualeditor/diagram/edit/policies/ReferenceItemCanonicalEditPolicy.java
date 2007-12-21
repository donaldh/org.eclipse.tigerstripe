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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;

public class ReferenceItemCanonicalEditPolicy extends
		CanonicalConnectionEditPolicy {

	@Override
	protected void handleNotificationEvent(Notification arg0) {
		// All we need is to make sure that the EditMap will be refreshed
		((MapEditPart) getHost().getParent().getChildren().get(0))
				.refreshCanonicalPolicy();
		super.handleNotificationEvent(arg0);
	}

	@Override
	protected List getSemanticConnectionsList() {
		return new ArrayList();
	}

	@Override
	protected EObject getSourceElement(EObject arg0) {
		if (arg0 instanceof Reference) {
			Reference ref = (Reference) arg0;
			return ref.eContainer();
		}
		return null;
	}

	@Override
	protected EObject getTargetElement(EObject arg0) {
		if (arg0 instanceof Reference) {
			Reference ref = (Reference) arg0;
			return ref.getZEnd();
		}
		return null;
	}

}
