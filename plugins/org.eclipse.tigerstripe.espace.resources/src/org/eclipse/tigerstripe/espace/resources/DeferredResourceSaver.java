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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.tigerstripe.espace.resources.internal.core.FileResourceUtils;

/**
 * @author Yuri Strot
 *
 */
public class DeferredResourceSaver extends Thread implements IResourceSaver {
	
	private static DeferredResourceSaver instance;
	private static boolean DEBUG = false;
	
	private DeferredResourceSaver() {
		resources = new HashMap<Resource, Boolean>();
	}
	
	private Map<Resource, Boolean> resources;
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
	
	public void resourceDirty(Resource resource, boolean updateTimestamp) {
		synchronized (resources) {
			if (DEBUG && updateTimestamp)
				System.out.println("DRS: added " + resource);
			resources.put(resource, updateTimestamp);
			resourceUpdated = true;
			synchronized (MONITOR) {
				MONITOR.notifyAll();
			}
			return;
		}
	}
	
	/**
	 * Remove resource from the list. 
	 * 
	 * @param resource resource to save
	 * @param saveAnyway if true, latest resource changes should be saved anyway.
	 */
	public void removeResource(Resource resource, boolean saveAnyway) {
		synchronized (resources) {
			if (DEBUG)
				System.out.println("DRS: removed " + resource);
			if (saveAnyway) {
				if (resources.containsKey(resource))
					resources.put(resource, false);
			}
			else
				resources.remove(resource);
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
		return FileResourceUtils.isSystemResource(resource);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.IResourceSaver#saveResources()
	 */
	public Resource[] saveResources() {
		synchronized (resources) {
			List<Resource> resourcesToUpdate = new ArrayList<Resource>();
			for (Resource resource : resources.keySet()) {
				save(resource);
				removeEmpty(resource);
				if (DEBUG)
					System.out.println("DRS: saved " + resource);
				if (resources.get(resource)) {
					resourcesToUpdate.add(resource);
					if (DEBUG)
						System.out.println("DRS: added to update " + resource);
				}
			}
			Resource[] result = resourcesToUpdate.toArray(
					new Resource[resourcesToUpdate.size()]);
			resources.clear();
			return result;
		}
	}
	
	public static void removeEmpty(Resource resource) {
		try {
			if (resource.getContents().size() == 0) {
				IFile file = WorkspaceSynchronizer.getFile(resource);
				if (file != null && file.exists()) {
					file.delete(true, new NullProgressMonitor());
				}
				else {
					File dFile = FileResourceUtils.getFile(resource);
					if (dFile != null && dFile.exists())
						dFile.delete();
				}
			}
		}
		catch (Exception e) {
        	ResourcesPlugin.log(e);
		}
	}
	
	public static void save(Resource resource) {
		if (resource.getContents().isEmpty()) {
			return;
		}
		try {
            resource.save(null);
        }
        catch (IOException e) {
        	ResourcesPlugin.log(e);
        }
	}
	
	protected void saveFiles() {
		try {
			IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			
				public void run(IProgressMonitor monitor) throws CoreException {
					manager.updateTimestamps(DeferredResourceSaver.this);
				}
			};
			org.eclipse.core.resources.ResourcesPlugin.getWorkspace().run(runnable, null);
		} catch (Exception e) {
        	ResourcesPlugin.log(e);
		}
	}

}
