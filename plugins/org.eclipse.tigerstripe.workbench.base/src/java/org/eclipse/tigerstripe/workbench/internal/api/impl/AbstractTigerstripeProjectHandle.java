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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.IWorkingCopy;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

public abstract class AbstractTigerstripeProjectHandle extends
		WorkingCopyManager implements IAbstractTigerstripeProject, IWorkingCopy {

	public String getName() {
		
		if (getIContainer()==null)
			return null;
		
		return getIContainer().getName();  // Modified from getIProject().getName() due to Bugzilla 319896
	}

	// we keep track of a TStamp on the hanlde when we create it to know
	// when the handle is invalid because the underlying URI actually changed
	private long handleTStamp;

	protected URI projectContainerURI;

	// The folder for this project handle
	protected File projectContainer;

	public AbstractTigerstripeProjectHandle(URI projectContainerURI) {
		this.projectContainerURI = projectContainerURI;

		if (projectContainerURI == null)// Read-only tigerstripe project from
			// module
			return;

		File file = new File(projectContainerURI);
		if (file != null && file.exists()) {
			handleTStamp = file.lastModified();
		}
	}

	public File getBaseDir() {
		if (getProjectContainerURI() != null)
			return new File(getProjectContainerURI());
		return null;
	}

	public URI getProjectContainerURI() {
		return this.projectContainerURI;
	}

	public IPath getLocation() {
		Path path = new Path(new File(getURI()).getAbsolutePath());
		return path;
	}

	public IPath getFullPath() {
		String name = getName();
		if (name == null) {
			return null;
		}
		return ResourcesPlugin.getWorkspace().getRoot().findMember(name)
				.getFullPath();
	}

	public URI getURI() {
		return getProjectContainerURI();
	}

	public abstract boolean exists();

	/**
	 * Tries and locate the project descriptor for this project
	 * 
	 * @return
	 */
	protected abstract boolean findProjectDescriptor();

	public File getProjectDescriptorFile() {

		return projectContainer;
	}

	public abstract void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException;

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null)
			return false;

		if (arg0.getClass() == this.getClass())
			return ((AbstractTigerstripeProjectHandle) arg0).getURI().equals(
					this.getURI());
		return false;
	}

	@Override
	public int hashCode() {
		if (getURI() != null) {
			return getURI().hashCode();
		}
		return super.hashCode();
	}

	public long handleTStamp() {
		return this.handleTStamp;
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == IProject.class) {
			try {
				return getIProject();
			} catch (TigerstripeException e) {
				return null;
			}
		} else if (adapter == IJavaProject.class) {
			try {
				IProject project = getIProject();

				// Note that this will be null for the PhantomProject
				if (project != null) {
					return JavaCore.create(project);
				}
			} catch (TigerstripeException e) {
				return null;
			}
		}
		return null;
	}

	private IProject getIProject()
			throws TigerstripeException {

		IContainer container = getIContainer();

		if (container instanceof IProject) {
			return (IProject) container;
		}

		/**
		 * Project also can be child folder in other project. In this case this
		 * container will be IFolder instead of IProject
		 */

		if (container != null) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			for (IProject project : root.getProjects()) {
				if (project.getLocation().equals(container.getLocation())) {
					return project;
				}
			}
		}

		throw new TigerstripeException("Can't resolve " + this.getLocation()
				+ " as Eclipse IProject");
	}
	
	// Introduced as a result of Bugzilla 319896
	private IContainer getIContainer() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		File file = new File(this.getLocation().toOSString());
		IPath path = new Path(file.getAbsolutePath());
		return root.getContainerForLocation(path);
	}

	public void delete(final boolean force, IProgressMonitor monitor)
			throws TigerstripeException {

		if (monitor == null)
			monitor = new NullProgressMonitor();

		final IProject project = (IProject) getAdapter(IProject.class);
		if (project != null) {
			try {
				ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor)
							throws CoreException {
						project.delete(force, monitor);
					}
				}, project, IResource.NONE, monitor);
			} catch (CoreException e) {
				new TigerstripeException(
						"An error occured while trying to delete project:"
								+ e.getMessage(), e);
			}
		}
	}

	public boolean containsErrors() {
		IProject project = (IProject) this.getAdapter(IProject.class);
		try {
			IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true,
					IResource.DEPTH_INFINITE);
			for (int i = 0; i < markers.length; i++) {
				if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(
						IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
					return true;
				}
			}
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
		return false;
	}

}
