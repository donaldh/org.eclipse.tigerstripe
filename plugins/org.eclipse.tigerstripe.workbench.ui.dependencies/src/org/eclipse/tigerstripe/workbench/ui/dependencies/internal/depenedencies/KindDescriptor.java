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
package org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies;

import org.eclipse.swt.graphics.Color;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyType;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle;

public class KindDescriptor {

	private final IDependencyType externalType;
	private final Kind kind;
	private final ShapeStyle defaultStyle;

	public KindDescriptor(IDependencyType externalType, Kind kind) {
		this.externalType = externalType;
		this.kind = kind;
		defaultStyle = ModelsFactory.INSTANCE.createDefaultSubjectStyle();
		Color fgColor = externalType.getDefaultForegroundColor();
		if (fgColor != null) {
			defaultStyle.setForegroundColor(ColorUtils.toColorAsInt(0,
					fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue()));
		}

		Color bgColor = externalType.getDefaultBackgroundColor();
		if (bgColor != null) {
			defaultStyle.setBackgroundColor(ColorUtils.toColorAsInt(0,
					bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));
		}
	}

	public IDependencyType getExternalType() {
		return externalType;
	}

	public Kind getKind() {
		return kind;
	}

	public ShapeStyle getDefaultStyle() {
		return defaultStyle;
	}

}
