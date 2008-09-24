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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.tigerstripe.espace.resources.internal.core.IndexUtils;

/**
 * @author Yuri Strot
 *
 */
public class DeferredResourceSaver extends Thread {
	
	private static DeferredResourceSaver instance;
	private static boolean DEBUG = false;
	
	private DeferredResourceSaver() {
		resources = new ArrayList<Resource>();
	}
	
	private List<Resource> resources;
	private boolean resourceUpdated;
	private Object MONITOR = new Object();
	private IResourceTimestampManager manager;
	
	public static DeferredResourceSaver getInstance() {
		if (instance == null) {
			instance = new DeferredResourceSaver();
			instance.start();
		}
		return instance;
	}
	
	public void setTimestampManager(IResourceTimestampManager manager) {
		this.manager = manager;
	}
	
	public void resourceDirty(Resource resource) {
		synchronized (resources) {
			if (DEBUG)
				System.out.println("DRS: added " + resource);
			resources.add(resource);
			resourceUpdated = true;
			synchronized (MONITOR) {
				MONITOR.notifyAll();
			}
			return;
		}
	}
	
	protected boolean needToWait() {
		synchronized (resources) {
			if (resourceUpdated) {
				resourceUpdated = false;
				return true;
			}
			return false;
		}
	}
	
	public boolean isDirty() {
		synchronized (resources) {
			return resources.size() > 0;
		}
	}
	
	protected Resource[] getResources() {
		synchronized (resources) {
			return resources.toArray(new Resource[resources.size()]);
		}
	}
	
	protected void removeSaved(Resource[] resources) {
		synchronized (this.resources) {
			for (Resource resource : resources) {
				this.resources.remove(resource);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while(true) {
			try {
				synchronized (MONITOR) {
					MONITOR.wait(500);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (needToWait())
				continue;
			if (isDirty()) {
				saveFiles();
			}
		}
	}
	
	protected boolean isIgnoreTimestamp(Resource resource) {
		return IndexUtils.isSystemResource(resource);
	}
	
	protected void saveFiles() {
		final Resource[] resources = getResources();
		List<Resource> timestampResourceList = new ArrayList<Resource>();
		for (Resource resource : resources) {
			if (!isIgnoreTimestamp(resource))
				timestampResourceList.add(resource);
		}
		final Resource[] timestampResources = timestampResourceList.toArray(
				new Resource[timestampResourceList.size()]);
		
		try {
			IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			
				public void run(IProgressMonitor monitor) throws CoreException {
					manager.updateTimestamps(new Runnable() {
					
						public void run() {
							for (Resource resource : resources) {
								save(resource);
								removeEmpty(resource);
							}
						}
					}, timestampResources);
				}
			};
			IProgressMonitor monitor = new NullProgressMonitor();
			ISchedulingRule rule = createRule(resources);
			if (true)
				org.eclipse.core.resources.ResourcesPlugin.getWorkspace().run(runnable, monitor);
			else
				org.eclipse.core.resources.ResourcesPlugin.getWorkspace().run(
						runnable, rule, IWorkspace.AVOID_UPDATE, monitor);
		} catch (Exception e) {
        	ResourcesPlugin.log(e);
		}
		removeSaved(resources);
	}
	
	protected ISchedulingRule createRule(Resource[] resources) {
		ISchedulingRule rule = null;
		for (int i = 0; i < resources.length; i++) {
			IFile file = WorkspaceSynchronizer.getFile(resources[i]);
			if (file != null) {
				IResource resource = file.getParent();
				if (resource == null)
					resource = file;
				if (rule == null) {
					rule = resource;
				}
				else {
					rule = MultiRule.combine(rule, resource);
				}
			}
		}
		return rule;
	}
	
	protected void removeEmpty(Resource resource) {
		try {
			if (resource.getContents().size() == 0) {
				IFile file = WorkspaceSynchronizer.getFile(resource);
				if (file != null && file.exists()) {
					file.delete(true, new NullProgressMonitor());
				}
				else {
					File dFile = IndexUtils.getFile(resource);
					if (dFile != null && dFile.exists())
						dFile.delete();
				}
			}
		}
		catch (Exception e) {
        	ResourcesPlugin.log(e);
		}
	}
	
	protected static void save(Resource resource) {
		if (DEBUG)
			System.out.println("DRS: saved " + resource);
    	try {
            resource.save(null);
        }
        catch (IOException e) {
        	ResourcesPlugin.log(e);
        }
	}

}
