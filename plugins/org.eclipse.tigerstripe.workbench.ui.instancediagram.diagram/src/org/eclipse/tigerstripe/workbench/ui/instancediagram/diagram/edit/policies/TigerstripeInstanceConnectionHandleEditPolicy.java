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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionHandleEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandle.HandleDirection;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.TigerstripeInstanceConnectionHandle;

public class TigerstripeInstanceConnectionHandleEditPolicy extends
		ConnectionHandleEditPolicy {

	@Override
	protected List getHandleFigures() {
		List list = new ArrayList(1);

		String tooltip = buildTooltip(HandleDirection.OUTGOING);
		if (tooltip != null) {
			list.add(new TigerstripeInstanceConnectionHandle(
					(IGraphicalEditPart) getHost(), HandleDirection.OUTGOING,
					tooltip));
		}

		return list;
	}

}
