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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies;

import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

public class DependenciesImageProvider {

	private final IDependencyKindResolver resolver;

	public DependenciesImageProvider(IDependencyKindResolver resolver) {
		this.resolver = resolver;
	}

	public Image getImage(Object obj) {
		switch (resolver.resolve(obj)) {
		case PROJECT:
			return Images.get(Images.TSPROJECT_FOLDER);
		case MODULE:
			return Images.get(Images.DEPS_INSTALLED_MODULE_ICON);
		case DEPENDENCY:
			return Images.get(Images.DEPS_DEPENDENCY_ICON);
		case UNKNOWN:
			return Images.get(Images.DEPS_UNKNOWN_ICON);
		default:
			return Images.get(Images.TSPROJECT_FOLDER);
		}
	}
}
