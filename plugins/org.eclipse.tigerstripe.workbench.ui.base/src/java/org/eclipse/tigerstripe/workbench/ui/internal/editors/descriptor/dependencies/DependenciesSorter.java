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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class DependenciesSorter extends ViewerSorter {

	private final ITextProvider textProvider;
	private final IDependencyKindResolver resolver;

	public DependenciesSorter(ITextProvider textProvider,
			IDependencyKindResolver resolver) {
		this.textProvider = textProvider;
		this.resolver = resolver;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		return compare(e1, e2);
	}

	public int compare(Object o1, Object o2) {
		DependencyKind k1 = resolver.resolve(o1);
		DependencyKind k2 = resolver.resolve(o2);

		if (k1 == k2) {
			return textProvider.getText(o1).compareTo(textProvider.getText(o2));
		} else {
			return k1.ordinal() - k2.ordinal();
		}
	}

}
