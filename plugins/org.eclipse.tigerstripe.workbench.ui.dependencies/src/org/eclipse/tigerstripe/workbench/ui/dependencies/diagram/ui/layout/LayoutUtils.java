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

import java.util.Set;

import org.eclipse.draw2d.Animation;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;

public class LayoutUtils {

	public static void layout(EditPart root, boolean animate) {
		TopDownLayout layout = getLayout();
		if (animate)
			Animation.markBegin();
		Command command = layout.layoutEditParts((GraphicalEditPart) root);
		if (command.canExecute()) {
			command.execute();
		}
		if (animate)
			Animation.run(400);
	}

	public static void layout(Set<? extends GraphicalEditPart> parts,
			boolean animate) {

		if (parts.isEmpty()) {
			return;
		}

		TopDownLayout layout = getLayout();
		layout.setOnlyParts(parts);
		if (animate)
			Animation.markBegin();
		Command command = layout.layoutEditParts((GraphicalEditPart) parts
				.iterator().next().getParent());
		if (command.canExecute()) {
			command.execute();
		}
		if (animate)
			Animation.run(400);
	}

	private static TopDownLayout getLayout() {
		return new TopDownLayout();
	}

}
