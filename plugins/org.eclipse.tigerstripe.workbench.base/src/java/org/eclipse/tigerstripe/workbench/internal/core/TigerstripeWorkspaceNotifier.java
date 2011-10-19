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
package org.eclipse.tigerstripe.workbench.internal.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.annotation.AnnotationUtils;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;


/**
 * WorkspaceListener notifies this class, which manages a set of of listeners 
 * that it notifies (among which is ArtifactManager)
 * 
 *  * <b>History of changes</b> (Name: Modification): <br/>
 * Eric Dillon 	  :	Initial Creation <br/>
 * Navid Mehregani: Bug319793 - [performance] About 90 threads are started when three TS projects are imported<br/>
 * Dan and Navid  : Significant performance improvements per defect 324197<br/>
 * 
 */
public class TigerstripeWorkspaceNotifier implements IAnnotationListener {

	public static TigerstripeWorkspaceNotifier INSTANCE = new TigerstripeWorkspaceNotifier();

	private ListenerList listeners = new ListenerList(ListenerList.EQUALITY);

	public class FilteredListener {

		private ITigerstripeChangeListener listener;
		private int filter;

		public FilteredListener(ITigerstripeChangeListener listener, int filter) {
			this.listener = listener;
			this.filter = filter;
		}

		public ITigerstripeChangeListener getListener() {
			return this.listener;
		}

		public boolean select(int flag) {
			return (filter & flag) == flag;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FilteredListener) {
				FilteredListener other = (FilteredListener) obj;
				return listener == other.getListener();
			}
			return false;
		}
	}
	
	private abstract static class BroadcastNotificationJob extends Job {

		public BroadcastNotificationJob(String name) {
			super(name);
			setPriority(Job.INTERACTIVE);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			broadcast();
			return Status.OK_STATUS;
		}
		
		@Override
		public boolean belongsTo(Object family) {
			return family == ITigerstripeChangeListener.NOTIFY_JOB_FAMILY;
		}

		protected abstract void broadcast();
	}

	private TigerstripeWorkspaceNotifier() {
		// this is a singleton

		readExtensionPoints();

		AnnotationPlugin.getManager().addAnnotationListener(this);
	}

	private void readExtensionPoints() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();

		IConfigurationElement[] elements = registry
				.getConfigurationElementsFor("org.eclipse.tigerstripe.workbench.base.tigerstripeChangeListener");
		for (IConfigurationElement element : elements) {
			try {
				String levelStr = element.getAttribute("level");
				int level = ITigerstripeChangeListener.ALL;
				if ("MODEL".equals(levelStr))
					level = ITigerstripeChangeListener.MODEL;
				else if ("PROJECT".equals(levelStr))
					level = ITigerstripeChangeListener.PROJECT;
				ITigerstripeChangeListener listener = (ITigerstripeChangeListener) element
						.createExecutableExtension("class");
				addTigerstripeChangeListener(listener, level);
			} catch (CoreException e) {
				BasePlugin.log(e);
			}
		}
	}

	public void addTigerstripeChangeListener(
			ITigerstripeChangeListener listener, int filter) {
		FilteredListener fListener = new FilteredListener(listener, filter);
		listeners.add(fListener); // ListenerList avoids duplicates
	}

	public void removeTigerstripeChangeListener(
			ITigerstripeChangeListener listener) {
		listeners.remove(new FilteredListener(listener, 0));
	}

	public void signalArtifactResourceChanged(final IResource changedArtifactResource){
		
		Object[] listenersArray = listeners.getListeners();
		for (Object l : listenersArray) {
			final FilteredListener listener = (FilteredListener) l;
			if (listener.select(ITigerstripeChangeListener.ARTIFACT_RESOURCES))
				listener.getListener().artifactResourceChanged(changedArtifactResource);
		}
	}
	
	
	public void signalArtifactResourceAdded(final IResource addedArtifactResource){
		
		Object[] listenersArray = listeners.getListeners();
		for (Object l : listenersArray) {
			final FilteredListener listener = (FilteredListener) l;
			if (listener.select(ITigerstripeChangeListener.ARTIFACT_RESOURCES))
				listener.getListener().artifactResourceAdded(addedArtifactResource);
		}
	}
	
	
	public void signalArtifactResourceRemoved(final IResource removedArtifactResource){
		

		Object[] listenersArray = listeners.getListeners();
		for (Object l : listenersArray) {
			final FilteredListener listener = (FilteredListener) l;
			if (listener.select(ITigerstripeChangeListener.ARTIFACT_RESOURCES))
				listener.getListener().artifactResourceRemoved(removedArtifactResource);
		}
	}
	
	public void signalDescriptorChanged(final IResource changedDescriptor){
		final Job notifyDescriptorChanged = new BroadcastNotificationJob("Handle Tigerstripe Descriptor Change") {
			
			@Override
			protected void broadcast() {
				broadcastDescriptorChanged(changedDescriptor);
			}
		};
		notifyDescriptorChanged.setRule(ResourcesPlugin.getWorkspace().getRoot());
		notifyDescriptorChanged.schedule();
	}
	
	private void broadcastDescriptorChanged(
			final IResource changedDescriptor) {

		Object[] listenersArray = listeners.getListeners();
		for (Object l : listenersArray) {
			final FilteredListener listener = (FilteredListener) l;
			if (listener.select(ITigerstripeChangeListener.PROJECT))
				SafeRunner.run(new ISafeRunnable() {

					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						listener.getListener().descriptorChanged(changedDescriptor);
					}

				});
		}
	}
	
	
	public void signalProjectAdded(final IAbstractTigerstripeProject newProject) {
		new BroadcastNotificationJob("Handle Tigerstripe Project Added") {
			
			@Override
			protected void broadcast() {
				broadcastProjectAdded(newProject);
			}
		}.schedule();
	}

	public void signalProjectDeleted(final String oldProjectName) {
		new BroadcastNotificationJob("Handle Tigerstripe Project Removed") {
			
			@Override
			protected void broadcast() {
				broadcastProjectDeleted(oldProjectName);
			}
		}.schedule();
	}

	private void broadcastProjectAdded(
			final IAbstractTigerstripeProject newProject) {

		Object[] listenersArray = listeners.getListeners();
		for (Object l : listenersArray) {
			final FilteredListener listener = (FilteredListener) l;
			if (listener.select(ITigerstripeChangeListener.PROJECT))
				SafeRunner.run(new ISafeRunnable() {

					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						listener.getListener().projectAdded(newProject);
					}

				});
		}
	}

	private void broadcastProjectDeleted(final String oldProjectName) {
		Object[] listenersArray = listeners.getListeners();
		for (Object l : listenersArray) {
			final FilteredListener listener = (FilteredListener) l;
			if (listener.select(ITigerstripeChangeListener.PROJECT))
				SafeRunner.run(new ISafeRunnable() {

					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						listener.getListener().projectDeleted(oldProjectName);
					}

				});
		}
	}

	public void signalModelChange(final IModelChangeDelta delta) {
		
		Object[] listenersArray = listeners.getListeners();
		for (Object l : listenersArray) {
			final FilteredListener listener = (FilteredListener) l;
			if (listener.select(ITigerstripeChangeListener.MODEL)) {
				IModelChangeDelta[] deltas = new IModelChangeDelta[] { delta };
				listener.getListener().modelChanged(deltas);
			}
				
		}
	}

	public void signalModelChange(final IModelChangeDelta[] deltas) {
		
		Object[] listenersArray = listeners.getListeners();
		for (Object l : listenersArray) {
			final FilteredListener listener = (FilteredListener) l;
			if (listener.select(ITigerstripeChangeListener.MODEL)) {
				listener.getListener().modelChanged(deltas);
			}
				
		}
		
	}

	public void annotationAdded(Annotation annotation) {

		if (!AnnotationUtils.isModelAnnotation(annotation))
			return;

		ModelAnnotationChangeDelta delta = new ModelAnnotationChangeDelta(
				IModelAnnotationChangeDelta.ADD,
				annotation);
		broadcastModelAnnotationChange(new IModelAnnotationChangeDelta[] { delta });
	}

	public void annotationChanged(Annotation annotation) {

		if (AnnotationUtils.isModelAnnotation(annotation)) {
			ModelAnnotationChangeDelta delta = new ModelAnnotationChangeDelta(
					IModelAnnotationChangeDelta.CHANGED, annotation);
			broadcastModelAnnotationChange(new IModelAnnotationChangeDelta[] { delta });
		}
	}

	public void annotationRemoved(Annotation annotation) {
		if (AnnotationUtils.isModelAnnotation(annotation)) {
			ModelAnnotationChangeDelta delta = new ModelAnnotationChangeDelta(
					IModelAnnotationChangeDelta.REMOVE, annotation);
			broadcastModelAnnotationChange(new IModelAnnotationChangeDelta[] { delta });
		}
	}

	private void broadcastModelAnnotationChange(
			final IModelAnnotationChangeDelta[] deltas) {
		Object[] listenersArray = listeners.getListeners();
		for (Object l : listenersArray) {
			final FilteredListener listener = (FilteredListener) l;
			if (listener.select(ITigerstripeChangeListener.ANNOTATION))
				SafeRunner.run(new ISafeRunnable() {

					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						listener.getListener().annotationChanged(deltas);
					}

				});
		}
	}
	
	public void activeFacetChanged(ITigerstripeModelProject project) {
	    broadcastFacetChange(project);
	}
	
	private void broadcastFacetChange(final ITigerstripeModelProject project) {
	       Object[] listenersArray = listeners.getListeners();
	       for (Object l : listenersArray) {
	            final FilteredListener listener = (FilteredListener) l;
	            if (listener.select(ITigerstripeChangeListener.FACET))
	                SafeRunner.run(new ISafeRunnable() {

	                    public void handleException(Throwable exception) {
	                        BasePlugin.log(exception);
	                    }

	                    public void run() throws Exception {
	                        listener.getListener().activeFacetChanged(project);
	                    }

	                });
	        }
	}

}
