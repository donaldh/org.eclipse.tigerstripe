/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.builders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

/**
 * Helper class for auditor testing
 * 
 * @author erdillon
 * 
 */
public class AuditorHelper {

	public static IMarker[] getMarkers(IAbstractTigerstripeProject project)
			throws CoreException {
		IProject iProject = (IProject) project.getAdapter(IProject.class);

		return iProject.findMarkers(BuilderConstants.MARKER_ID, true,
				IResource.DEPTH_INFINITE);
	}

	public static IMarker[] getMarkers(int severity,
			IAbstractTigerstripeProject project) throws CoreException {
		IMarker[] allMarkers = getMarkers(project);
		List<IMarker> filteredMarkers = new ArrayList<IMarker>();
		for (IMarker marker : allMarkers) {
			if ((Integer) marker.getAttribute(IMarker.SEVERITY) == severity) {
				filteredMarkers.add(marker);
			}
		}
		return filteredMarkers.toArray(new IMarker[filteredMarkers.size()]);
	}

	public static void forceFullBuildNow(IAbstractTigerstripeProject project)
			throws CoreException {
		IProject iProject = (IProject) project.getAdapter(IProject.class);

		iProject.build(IncrementalProjectBuilder.FULL_BUILD, null);
	}
}
