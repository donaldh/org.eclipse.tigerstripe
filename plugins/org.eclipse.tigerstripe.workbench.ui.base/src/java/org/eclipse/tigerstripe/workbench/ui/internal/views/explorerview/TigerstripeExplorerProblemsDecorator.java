/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.ui.IWorkingSet;

public class TigerstripeExplorerProblemsDecorator extends
		TreeHierarchyLayoutProblemsDecorator {

	public TigerstripeExplorerProblemsDecorator() {
		super();
	}

	public TigerstripeExplorerProblemsDecorator(boolean isFlatLayout) {
		super(isFlatLayout);
	}

	@Override
	protected int computeAdornmentFlags(Object obj) {
		if (!(obj instanceof IWorkingSet))
			return super.computeAdornmentFlags(obj);

		IWorkingSet workingSet = (IWorkingSet) obj;
		IAdaptable[] elements = workingSet.getElements();
		int result = 0;
		for (int i = 0; i < elements.length; i++) {
			IAdaptable element = elements[i];
			int flags = super.computeAdornmentFlags(element);
			if ((flags & JavaElementImageDescriptor.ERROR) != 0)
				return JavaElementImageDescriptor.ERROR;
			if ((flags & JavaElementImageDescriptor.WARNING) != 0)
				result = JavaElementImageDescriptor.WARNING;
		}
		return result;
	}
}
