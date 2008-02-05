/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.model;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;

public interface IWorkingCopy {

	public boolean isWorkingCopy();

	public boolean isDirty();

	public IWorkingCopy makeWorkingCopy(IProgressMonitor monitor)
			throws TigerstripeException;

	public Object getOriginal() throws TigerstripeException;

	public void addArtifactChangeListener(IArtifactChangeListener listener);

	public void removeArtifactChangeListener(IArtifactChangeListener listener);
}
