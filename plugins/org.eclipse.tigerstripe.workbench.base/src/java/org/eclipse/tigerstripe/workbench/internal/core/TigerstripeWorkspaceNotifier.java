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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.set.SynchronizedSet;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

public class TigerstripeWorkspaceNotifier {

	public static TigerstripeWorkspaceNotifier INSTANCE = new TigerstripeWorkspaceNotifier();

	@SuppressWarnings("unchecked")
	private Set<FilteredListener> listeners = SynchronizedSet
			.decorate(new HashSet<FilteredListener>());

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

	private TigerstripeWorkspaceNotifier() {
		// this is a singleton

		readExtensionPoints();
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
						.createExecutableExtension("listener");
				addTigerstripeChangeListener(listener, level);
			} catch (CoreException e) {
				BasePlugin.log(e);
			}
		}
	}

	public void addTigerstripeChangeListener(
			ITigerstripeChangeListener listener, int filter) {
		listeners.add(new FilteredListener(listener, filter));
	}

	public void removeTigerstripeChangeListener(
			ITigerstripeChangeListener listener) {
		listeners.remove(new FilteredListener(listener, 0));
	}

	public void signalProjectAdded(final IAbstractTigerstripeProject newProject) {
		Job notifyProjectAdded = new Job("Handle Tigerstripe Project Added") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				broadcastProjectAdded(newProject);
				return Status.OK_STATUS;
			}
		};

		notifyProjectAdded.schedule();
	}

	public void signalProjectDeleted(final String oldProjectName) {
		Job notifyProjectRemoved = new Job("Handle Tigerstripe Project Removed") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				broadcastProjectDeleted(oldProjectName);
				return Status.OK_STATUS;
			}
		};

		notifyProjectRemoved.schedule();
	}

	private void broadcastProjectAdded(
			final IAbstractTigerstripeProject newProject) {
		for (final FilteredListener listener : listeners) {
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
		for (final FilteredListener listener : listeners) {
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
		Job notifyModelChanged = new Job("Handle Tigerstripe Model Change") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				broadcastModelChange(new IModelChangeDelta[] { delta });
				return Status.OK_STATUS;
			}
		};

		notifyModelChanged.schedule();
	}

	public void signalModelChange(final IModelChangeDelta[] deltas) {
		Job notifyModelChanged = new Job("Handle Tigerstripe Model Change") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				broadcastModelChange(deltas);
				return Status.OK_STATUS;
			}
		};

		notifyModelChanged.schedule();
	}

	private void broadcastModelChange(final IModelChangeDelta[] deltas) {
		for (final FilteredListener listener : listeners) {
			if (listener.select(ITigerstripeChangeListener.MODEL))
				SafeRunner.run(new ISafeRunnable() {

					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						listener.getListener().modelChanged(deltas);
					}

				});
		}
	}

}
