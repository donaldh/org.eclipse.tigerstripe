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
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;

public class SchedulingUtils {

	/**
	 * Returns a multi-rule scheduling rule as a union of all resources
	 * 
	 * @param resources
	 * @return
	 */
	public static ISchedulingRule multiRuleFor(List<IResource> resources) {
		if (resources != null && resources.size() != 0) {
			MultiRule result = new MultiRule(resources
					.toArray(new IResource[resources.size()]));
			return result;
		} else
			return null;
	}
}
