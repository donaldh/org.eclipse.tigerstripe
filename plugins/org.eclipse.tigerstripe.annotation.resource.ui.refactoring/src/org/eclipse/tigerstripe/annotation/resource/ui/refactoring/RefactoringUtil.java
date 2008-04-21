/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.resource.ui.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ltk.core.refactoring.resource.MoveResourcesDescriptor;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceDescriptor;

/**
 * @author Yuri Strot
 *
 */
public class RefactoringUtil {
	
	public static IResource getDestination(MoveResourcesDescriptor des) {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IPath destination = des.getDestinationPath();
		return root.findMember(destination);
	}
	
	public static IResource[] getResources(MoveResourcesDescriptor des) {
		List<IResource> resources = new ArrayList<IResource>();
		final IWorkspaceRoot root= ResourcesPlugin.getWorkspace().getRoot();
		IPath[] paths = des.getResourcePathsToMove();
		for (int i = 0; i < paths.length; i++) {
			IResource res = root.findMember(paths[i]);
			if (res != null)
				resources.add(res);
		}
		
		return resources.toArray(new IResource[resources.size()]);
	}
	
	public static IResource getResource(RenameResourceDescriptor rrd) {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IPath path = rrd.getResourcePath();
		return root.findMember(path);
	}
	
	public static IPath getNewPath(IResource resource, RenameResourceDescriptor rrd) {
		String name = rrd.getNewName();
		if (resource != null) {
			IPath path1 = resource.getFullPath();
			IPath path2 = path1.removeLastSegments(1).append(name);
			return path2;
		}
		return null;
	}
}