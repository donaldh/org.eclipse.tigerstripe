/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateViewAndOptionallyElementCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;

public class MapEditPartCreateViewAndOptionallyElementCommand extends
		CreateViewAndOptionallyElementCommand {

	public MapEditPartCreateViewAndOptionallyElementCommand(
			IAdaptable elementAdapter, IGraphicalEditPart containerEP,
			Point location, PreferencesHint preferencesHint) {
		super(elementAdapter, containerEP, location, preferencesHint);
	}

	@Override
	protected boolean useExistingView(View view) {
		if (view != null) {
			return true;
		} else {
			return super.useExistingView(view);
		}
	}
}
