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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.RefactoringExecutionEvent;
import org.eclipse.ltk.core.refactoring.resource.MoveResourcesDescriptor;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceDescriptor;
import org.eclipse.tigerstripe.annotation.resource.ResourceURIConverter;
import org.eclipse.tigerstripe.annotation.ui.core.RefactoringSupport;

/**
 * @author Yuri Strot
 *
 */
public class ResourceRefactoringSupport extends RefactoringSupport {
	
	private IResource resource;
	private IResource[] resources;
	
	public ResourceRefactoringSupport() {
		addListeners();
	}

	protected void addListeners() {
		RefactoringCore.getHistoryService().addExecutionListener(new IRefactoringExecutionListener() {

			public void executionNotification(RefactoringExecutionEvent event) {
				RefactoringDescriptor des = event.getDescriptor().requestDescriptor(new NullProgressMonitor());
				if (des instanceof RenameResourceDescriptor) {
					processRename((RenameResourceDescriptor)des, event.getEventType());
				}
				if (des instanceof MoveResourcesDescriptor) {
					processMove((MoveResourcesDescriptor)des, event.getEventType());
				}
			}
		
		});
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			
			public void resourceChanged(IResourceChangeEvent event) {
				fireContainerUpdated();
			}
		
		});
		
	}
	
	public void processRename(RenameResourceDescriptor rrd, int eventType) {
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			resource = RefactoringUtil.getResource(rrd);
		}
		else if (eventType == RefactoringExecutionEvent.PERFORMED && resource != null){
			IPath newPath = RefactoringUtil.getNewPath(resource, rrd);
			if (newPath != null) {
				Map<URI, URI> changes = new HashMap<URI, URI>();
				IResource newResource = ResourcesPlugin.getWorkspace().getRoot().findMember(newPath);
				collectChanges(newResource, resource.getFullPath(), changes);
				fireRefactoringPerformed(changes);
			}
			resource = null;
		}
	}
	
	public void processMove(MoveResourcesDescriptor des, int eventType) {
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			resources = RefactoringUtil.getResources(des);
		}
		else if (eventType == RefactoringExecutionEvent.PERFORMED && resources != null){
			IResource destination = RefactoringUtil.getDestination(des);
			if (destination != null) {
				Map<URI, URI> changes = new HashMap<URI, URI>();
				for (int i = 0; i < resources.length; i++) {
					IPath oldPath = resources[i].getFullPath();
					IPath newPath = destination.getFullPath().append(oldPath.lastSegment());
					IResource newResource = ResourcesPlugin.getWorkspace().getRoot().findMember(newPath);
					collectChanges(newResource, oldPath, changes);
                }
				fireRefactoringPerformed(changes);
			}
			resources = null;
		}
	}
	
	protected static void collectChanges(IResource resource, IPath oldPath, Map<URI, URI> changes) {
		changes.put(ResourceURIConverter.toURI(oldPath), ResourceURIConverter.toURI(resource));
		if (resource instanceof IContainer) {
			IContainer container = (IContainer)resource;
			try {
				//TODO how we going on to work with phantom and team private resources?
	            IResource[] members = container.members(0);
	            for (int i = 0; i < members.length; i++) {
	            	IResource child = members[i];
	            	IPath childOldPath = oldPath.append(child.getFullPath().lastSegment());
	            	collectChanges(child, childOldPath, changes);
                }
            }
            catch (CoreException e) {
	            e.printStackTrace();
            }
		}
	}

}
