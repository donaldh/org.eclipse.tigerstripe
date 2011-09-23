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
package org.eclipse.tigerstripe.espace.resources.monitor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.espace.core.Mode;
import org.eclipse.tigerstripe.espace.resources.core.EObjectRouter;

/**
 * @author Yuri Strot
 * 
 * 
 */
public class ResourcesMonitor implements IResourceChangeListener {

	private static ResourcesMonitor INSTANCE;

	public static ResourcesMonitor getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ResourcesMonitor();
			initialize();
		}
		return INSTANCE;
	}

	private static void initialize() {
		Job job = new Job("Initializing resource monitor...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					IWorkspace workspace = ResourcesPlugin.getWorkspace();
					IProject[] projects = workspace.getRoot().getProjects();
					monitor.beginTask("Initializing", projects.length);
					for (IProject project : projects) {
						project.accept(new IResourceVisitor() {

							public boolean visit(IResource resource)
									throws CoreException {
								INSTANCE.processResource(resource, true);
								return true;
							}
						});
						if (monitor.isCanceled()) {
							return Status.CANCEL_STATUS;
						}
						monitor.worked(1);
					}
				} catch (Exception e) {
					return new Status(IStatus.ERROR,
							ResourcesMonitorPlugin.PLUGIN_ID, e.getMessage(), e);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	public Resource getResource(IResource resource) {
		URI uri = URI.createPlatformResourceURI(resource.getFullPath()
				.toString(), false);
		ResourceSet rset = new ResourceSetImpl();
		return rset.createResource(uri);
	}

	public void resourceChanged(IResourceChangeEvent event) {
		try {
			if (event.getDelta() != null) {
				event.getDelta().accept(new IResourceDeltaVisitor() {

					public boolean visit(IResourceDelta delta)
							throws CoreException {
						IResource resource = delta.getResource();
						switch (delta.getKind()) {
						case IResourceDelta.ADDED:
							processResource(resource, true);
							break;
						case IResourceDelta.CHANGED:
							break;
						case IResourceDelta.REMOVED:
							processResource(resource, false);
							break;
						}
						return true;
					}
				});
			}
		} catch (CoreException e) {
			ResourcesMonitorPlugin.log(e);
		}
	}

	protected synchronized void processResource(IResource resource,
			boolean added) {
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			String ext = file.getFileExtension();
			if (ext != null
					&& ext.toLowerCase().equals(
							EObjectRouter.ANNOTATION_FILE_EXTENSION)) {
				notifyFileModification(file, added);
			}
		}
	}

	protected void notifyFileModification(final IFile file, final boolean added) {
		String operation = added ? "Add" : "Remove";
		String article = added ? "to" : "from";
		String name = operation + " annotation file (" + file.getFullPath()
				+ ") " + article + " the Annotation Framework Database";
		final Resource resource = getResource(file);
		Job job = new Job(name) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					if (added)
						AnnotationPlugin.getManager().addAnnotations(resource,
								Mode.READ_WRITE);
					else
						AnnotationPlugin.getManager().removeAnnotations(
								resource);
				} catch (Exception e) {
					return new Status(IStatus.ERROR,
							ResourcesMonitorPlugin.PLUGIN_ID, e.getMessage(), e);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
}
