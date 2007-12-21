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
package org.eclipse.tigerstripe.api.impl.updater;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeListener;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.external.TigerstripeException;

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

	public void handleChangeRequest(IModelChangeRequest request)
			throws TigerstripeException {

		BaseModelChangeRequest baseRequest = (BaseModelChangeRequest) request;

		if (baseRequest.canExecute(mgrSession)) {
			baseRequest.execute(mgrSession);
			notifyListeners(request);
		}
	}

	private void notifyListeners(IModelChangeRequest request) {
		for (IModelChangeListener listener : listeners) {
			listener.notifyModelChanged(request);
		}
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
}
