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
package org.eclipse.tigerstripe.espace.resources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.espace.resources.core.EObjectRouter;
import org.eclipse.tigerstripe.espace.resources.internal.core.ResourceStorage;

/**
 * @author Yuri Strot
 *
 */
public class ResourceManager {
	
	private ResourceStorage storage;
	private List<ResourceState> resources;
	
	public ResourceManager(ResourceStorage storage) {
		this.storage = storage;
		resources = new ArrayList<ResourceState>();
		addResourceDeltaListener();
	}
	
	/**
	 * @return true if index should be rebuild and false otherwise
	 */
	public boolean updateState() {
		boolean rebuild = false;
		for (ResourceState state : resources) {
			rebuild |= storage.updateResource(state.getResource(), state.isAdded());
		}
		resources = new ArrayList<ResourceState>();
		return rebuild;
	}
	
	protected void addResourceDeltaListener() {
		org.eclipse.core.resources.ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			
			public void resourceChanged(IResourceChangeEvent event) {
				try {
					if (event.getDelta() != null) {
						event.getDelta().accept(new IResourceDeltaVisitor() {
							
							public boolean visit(IResourceDelta delta) throws CoreException {
								IResource resource = delta.getResource();
								switch (delta.getKind()) {
									case IResourceDelta.ADDED:
										updateResource(resource, true);
										break;
									case IResourceDelta.CHANGED:
										break;
									case IResourceDelta.REMOVED:
										updateResource(resource, false);
										break;
								}
								return true;
							}
						});
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		
		});
	}
	
	protected synchronized void updateResource(IResource resource, boolean added) {
		if (resource instanceof IFile) {
			IFile file = (IFile)resource;
			String ext = file.getFileExtension();
			if (ext != null && ext.toLowerCase().equals(EObjectRouter.ANNOTATION_FILE_EXTENSION)) {
				resources.add(new ResourceState(resource, added));
			}
		}
	}
	
	private static class ResourceState {
		
		private IResource resource;
		private boolean added;
		
		public ResourceState(IResource resource, boolean added) {
			this.resource = resource;
			this.added = added;
		}
		
		/**
		 * @return the resource
		 */
		public IResource getResource() {
			return resource;
		}
		
		/**
		 * @return the added
		 */
		public boolean isAdded() {
			return added;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ResourceState) {
				ResourceState state = (ResourceState)obj;
				return state.added == added && state.resource.equals(resource);
			}
			return false;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return resource.hashCode() ^ (added ? 0 : 1);
		}
		
	}

}
