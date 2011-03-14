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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.util.CheckUtils;

public class ExecutionContext {

	private final IProgressMonitor monitor;
	private final Map<ICycle, Set<Object>> cycles = new HashMap<ExecutionContext.ICycle, Set<Object>>();

	public ExecutionContext(IProgressMonitor monitor) {
		this.monitor = CheckUtils.notNull(monitor, "monitor");
	}

	public IProgressMonitor getMonitor() {
		return monitor;
	}

	public boolean addToCycle(ICycle cycle, Object element) {
		Set<Object> set = cycles.get(cycle);
		if (set == null) {
			set = new HashSet<Object>();
			cycles.put(cycle, set);
		}
		return set.add(element);
	}

	public static interface ICycle {

	}
}
