/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.layout;

import org.eclipse.draw2d.Animation;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;

public class LayoutUtils {

	public static void layout(EditPart root, boolean animate) {
		TopDownLayout layout = new TopDownLayout();
		if (animate)
			Animation.markBegin();
		Command command = layout.layoutEditParts((GraphicalEditPart) root);
		if (command.canExecute()) {
			command.execute();
		}
		if (animate)
			Animation.run(400);
	}

}
