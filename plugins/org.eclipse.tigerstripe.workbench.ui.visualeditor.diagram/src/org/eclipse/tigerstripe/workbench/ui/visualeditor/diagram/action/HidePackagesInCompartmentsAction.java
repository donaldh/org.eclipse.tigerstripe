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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.action;

import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.DiagramPropertiesHelper;

public class HidePackagesInCompartmentsAction extends DiagramToggleAction {

	@Override
	protected String getTargetProperty() {
		return DiagramPropertiesHelper.HIDEPACKAGESINCOMPARTMENTS;
	}
}
