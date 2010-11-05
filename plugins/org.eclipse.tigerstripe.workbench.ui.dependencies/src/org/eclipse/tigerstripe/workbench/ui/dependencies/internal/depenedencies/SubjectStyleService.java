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

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;

public class SubjectStyleService {

	private final ShapeStyle defaultStyle;
	private final Registry registry;

	public SubjectStyleService(Registry registry) {
		this.registry = registry;
		defaultStyle = ModelsFactory.INSTANCE.createDefaultSubjectStyle();
	}

	public ShapeStyle getStyle(Subject subject) {

		if (subject.isUseCustomStyle()) {
			return subject.getStyle();
		}

		Kind kind = subject.getKind();

		if (kind == null) {
			return defaultStyle;
		}

		if (kind.isUseCustomStyle()) {
			return kind.getStyle();
		}

		KindDescriptor kindDsc = registry.getKindType(kind.getId());

		if (kindDsc == null) {
			return defaultStyle;
		}

		return kindDsc.getDefaultStyle();
	}

}
