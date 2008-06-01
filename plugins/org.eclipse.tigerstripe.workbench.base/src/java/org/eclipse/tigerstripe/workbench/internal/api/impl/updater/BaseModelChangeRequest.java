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

import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;

/**
 * Base class model change requests
 * 
 * @author Eric Dillon
 * 
 */
public abstract class BaseModelChangeRequest implements IModelChangeRequest {

	private ModelUpdaterImpl targetModelUpdater;

	private Object source;

	private IArtifactManagerSession mgrSession;

	/**
	 * Executes this on the given session
	 * 
	 * @param mgrSession
	 * @throws TigerstripeException
	 */
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		this.mgrSession = mgrSession;
	}

	/**
	 * returns true if this request can be executed on the given session
	 * 
	 * @param mgrSession
	 * @return
	 */
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		this.mgrSession = mgrSession;
		return true;
	}

	protected IArtifactManagerSession getMgrSession() {
		return this.mgrSession;
	}

	public void setTargetModelUpdater(ModelUpdaterImpl modelUpdater) {
		this.targetModelUpdater = modelUpdater;
	}

	public ModelUpdaterImpl getTargetModelUpdater() {
		return targetModelUpdater;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public IModelChangeDelta getCorrespondingDelta() {
		return ModelChangeDelta.UNKNOWNDELTA;
	}
}
