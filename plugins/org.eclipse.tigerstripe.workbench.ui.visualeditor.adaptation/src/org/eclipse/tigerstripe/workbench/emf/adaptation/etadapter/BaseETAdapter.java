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
package org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

/**
 * A top level abstract class for ETAdapters
 * 
 * @author Eric Dillon
 * 
 */
public abstract class BaseETAdapter implements ETAdapter {

	private static boolean ignoreNotify = false;

	public static void setIgnoreNotify(boolean ignore) {
		ignoreNotify = ignore;
	}

	public static boolean ignoreNofigy() {
		return ignoreNotify;
	}

	private Notifier targetNotifier;

	private IModelUpdater modelUpdater;

	private ClassDiagramSynchronizer synchronizer;

	public BaseETAdapter(IModelUpdater modelUpdater,
			ClassDiagramSynchronizer synchronizer) {
		this.modelUpdater = modelUpdater;
		this.synchronizer = synchronizer;
	}

	public ClassDiagramSynchronizer getSynchronizer() {
		return this.synchronizer;
	}

	public Notifier getTarget() {
		return targetNotifier;
	}

	public boolean isAdapterForType(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void notifyChanged(Notification arg0) {
	}

	public void setTarget(Notifier arg0) {
		targetNotifier = arg0;
	}

	protected IModelUpdater getModelUpdater() {
		return this.modelUpdater;
	}

	protected void postChangeRequest(IModelChangeRequest request)
			throws TigerstripeException {
		if (!ignoreNofigy()) {
			// We ignore the request. This is used when PostActions are running
			// on DnD
			// so we don't round-trip upon each post action.
			request.setSource(synchronizer);
			getModelUpdater().handleChangeRequest(request);
		}
	}
}
