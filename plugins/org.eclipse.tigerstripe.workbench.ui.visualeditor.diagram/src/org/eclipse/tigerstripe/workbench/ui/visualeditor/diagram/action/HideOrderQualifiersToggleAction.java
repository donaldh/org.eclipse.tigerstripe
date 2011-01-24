/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.action;

import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.DiagramPropertiesHelper;

public class HideOrderQualifiersToggleAction extends DiagramToggleAction {

	@Override
	protected String getTargetProperty() {
		return DiagramPropertiesHelper.HIDEORDERQUALIFIERS;
	}
}
