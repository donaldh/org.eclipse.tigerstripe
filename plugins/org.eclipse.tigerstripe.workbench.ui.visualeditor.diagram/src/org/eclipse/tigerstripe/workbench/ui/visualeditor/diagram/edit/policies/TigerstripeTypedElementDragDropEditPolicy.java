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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement;

public class TigerstripeTypedElementDragDropEditPolicy extends
		DragDropEditPolicy {

	@Override
	public boolean understandsRequest(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			ChangeBoundsRequest cbr = (ChangeBoundsRequest) request;
			List<EditPart> editParts = cbr.getEditParts();
			for (EditPart part : editParts) {
				Object obj = part.getModel();
				if (obj instanceof Node) {
					Node node = (Node) obj;
					if (node.getElement() == null)
						return false;
					if (!(node.getElement() instanceof TypedElement))
						return false;
				}
			}
			return true;
		}
		return super.understandsRequest(request);
	}
}
