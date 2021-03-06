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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ReconnectRequest;

public class MapGraphicalNodeEditPolicy extends
		TigerstripeGraphicalNodeEditPolicy {

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return super.getReconnectSourceCommand(request);
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return super.getReconnectTargetCommand(request);
	}

}
