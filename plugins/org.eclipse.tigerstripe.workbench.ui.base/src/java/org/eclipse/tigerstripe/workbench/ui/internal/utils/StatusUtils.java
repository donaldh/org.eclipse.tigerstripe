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
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;

public class StatusUtils {

	public static List<IStatus> flat(IStatus status) {
		return flat(status, new ArrayList<IStatus>());
	}

	private static List<IStatus> flat(IStatus status, List<IStatus> result) {
		IStatus[] children = status.getChildren();
		if (children.length > 0) {
			for (IStatus child : children) {
				flat(child, result);
			}
		} else {
			result.add(status);
		}
		return result;
	}

	public static int findMaxSeverity(Collection<IStatus> statuses) {
		int maxSeverity = IStatus.INFO;
		for (IStatus status : statuses) {
			int severity = status.getSeverity();
			if (severity > maxSeverity) {
				maxSeverity = severity;
			}
		}
		return maxSeverity;
	}
}
