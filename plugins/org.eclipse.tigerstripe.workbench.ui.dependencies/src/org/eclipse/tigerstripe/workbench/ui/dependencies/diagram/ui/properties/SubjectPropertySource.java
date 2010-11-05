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
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.properties;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.ColorUtils;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.SubjectStyleService;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public class SubjectPropertySource implements IPropertySource {

	private final Subject model;
	private final SubjectStyleService styleService;

	public SubjectPropertySource(Subject model, SubjectStyleService styleService) {
		this.model = model;
		this.styleService = styleService;
	}

	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] {
			new ColorPropertyDescriptor(
					DependenciesPackage.Literals.SHAPE_STYLE__BACKGROUND_COLOR,
					"Background color"),
			new ColorPropertyDescriptor(
					DependenciesPackage.Literals.SHAPE_STYLE__FOREGROUND_COLOR,
					"Foreground color"), };

	public Object getEditableValue() {
		return model;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

	public Object getPropertyValue(Object id) {
		if (DependenciesPackage.Literals.SHAPE_STYLE__BACKGROUND_COLOR == id) {
			return ColorUtils.toRGB(styleService.getStyle(model)
					.getBackgroundColor());
		}
		if (DependenciesPackage.Literals.SHAPE_STYLE__FOREGROUND_COLOR == id) {
			return ColorUtils.toRGB(styleService.getStyle(model)
					.getForegroundColor());
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		return DependenciesPackage.Literals.SHAPE_STYLE__BACKGROUND_COLOR == id
				|| DependenciesPackage.Literals.SHAPE_STYLE__FOREGROUND_COLOR == id;
	}

	public void resetPropertyValue(Object id) {
		model.setUseCustomStyle(false);
	}

	public void setPropertyValue(Object id, Object value) {
		ShapeStyle style = styleService.getStyle(model);
		if (DependenciesPackage.Literals.SHAPE_STYLE__BACKGROUND_COLOR == id) {
			model.getStyle()
					.setBackgroundColor(ColorUtils.fromRGB((RGB) value));
			model.getStyle().setForegroundColor(style.getForegroundColor());
		} else if (DependenciesPackage.Literals.SHAPE_STYLE__FOREGROUND_COLOR == id) {
			model.getStyle()
					.setForegroundColor(ColorUtils.fromRGB((RGB) value));
			model.getStyle().setBackgroundColor(style.getBackgroundColor());
		}
		model.setUseCustomStyle(true);
	}

}
