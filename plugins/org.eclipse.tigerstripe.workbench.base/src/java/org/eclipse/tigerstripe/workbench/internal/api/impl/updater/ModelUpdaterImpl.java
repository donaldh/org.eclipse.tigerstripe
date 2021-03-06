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
package org.eclipse.tigerstripe.workbench.internal.api.impl.updater;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;

public class ModelUpdaterImpl implements IModelUpdater {

	private List<IModelChangeListener> listeners = new ArrayList<IModelChangeListener>();

	private IArtifactManagerSession mgrSession;
	private ModelChangeRequestFactory factory;

	public ModelUpdaterImpl(IArtifactManagerSession mgrSession) {
		this.mgrSession = mgrSession;
	}

	public IArtifactManagerSession getSession() {
		return this.mgrSession;
	}

	public void handleChangeRequest(final IModelChangeRequest request)
			throws TigerstripeException {

		final BaseModelChangeRequest baseRequest = (BaseModelChangeRequest) request;

		if (baseRequest.canExecute(mgrSession)) {
				Job job = new Job("Model updating...") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							baseRequest.execute(mgrSession);
							notifyListeners(request);
							return Status.OK_STATUS;
						} catch (TigerstripeException e) {
							return new Status(IStatus.ERROR,
									BasePlugin.PLUGIN_ID, e.getMessage(), e);
						}
					}
				};
				job.setRule(ResourcesPlugin.getWorkspace().getRoot());
				job.setUser(true);
				job.schedule();			
		}
	}

	private void notifyListeners(IModelChangeRequest request) {
		for (IModelChangeListener listener : listeners) {
			listener.notifyModelChanged(request);
		}

		// push to TigerstripeWorkspaceNotifier no matter what
		if (request.getCorrespondingDelta() != null)
			TigerstripeWorkspaceNotifier.INSTANCE.signalModelChange(request
					.getCorrespondingDelta());
	}

	public IModelChangeRequestFactory getRequestFactory() {
		if (factory == null) {
			factory = new ModelChangeRequestFactory(this);
		}
		return factory;
	}

	public void addModelChangeListener(IModelChangeListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeModelChangeListener(IModelChangeListener listener) {
		listeners.remove(listener);
	}

	public void handleChangeRequestSynch(IModelChangeRequest request)
			throws TigerstripeException {
		
		final BaseModelChangeRequest baseRequest = (BaseModelChangeRequest) request;

		if (baseRequest.canExecute(mgrSession)) {
			baseRequest.execute(mgrSession);
			notifyListeners(request);
		}
	}
}
