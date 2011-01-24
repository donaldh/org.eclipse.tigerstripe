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

import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionHandleEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandle.HandleDirection;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.TigerstripeInstanceConnectionHandle;

public class TigerstripeInstanceConnectionHandleEditPolicy extends
		ConnectionHandleEditPolicy {

	@Override
	protected boolean isPreferenceOn() {
		// BUG#333182: Ensure default settings load
		DiagramUIPlugin
				.getInstance()
				.getPreferenceStore()
				.getDefaultBoolean(
						IPreferenceConstants.PREF_SHOW_CONNECTION_HANDLES);
		return super.isPreferenceOn();
	}

	@Override
	protected List getHandleFigures() {
		String tooltip = buildTooltip(HandleDirection.OUTGOING);
		if (tooltip != null) {
			return Collections
					.singletonList(new TigerstripeInstanceConnectionHandle(
							(IGraphicalEditPart) getHost(),
							HandleDirection.OUTGOING, tooltip));
		} else {
			return Collections.EMPTY_LIST;
		}
	}

}
