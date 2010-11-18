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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.depsdiagram;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyAction;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyType;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

public enum TigerstripeTypes implements IDependencyType {

	PROJECT(new RGB(255, 243, 243)), MODULE_PROJECT(new RGB(243, 255, 244)), PHANTOM_PROJECT(
			new RGB(255, 254, 243)), DEPENDENCY(new RGB(243, 245, 255));

	private Color bgColor;
	private Color fgColor;

	private TigerstripeTypes(RGB color) {
		bgColor = new Color(null, color);
		fgColor = new Color(null, Utils.sate(color, -30));
	}

	public String getId() {
		return name();
	}

	public String getName() {
		return name();
	}

	public Color getDefaultBackgroundColor() {
		return bgColor;
	}

	public Color getDefaultForegroundColor() {
		return fgColor;
	}

	public Image getImage() {
		return Images.get(Images.TSPROJECT_FOLDER);
	}

	public List<IDependencyAction> getActions() {
		return Collections.emptyList();
	}
}