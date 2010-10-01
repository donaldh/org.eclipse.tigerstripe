/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.refactor;

import static org.eclipse.tigerstripe.workbench.internal.core.util.CheckUtils.notNull;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

public class ResourceRefactorRequest extends RefactorRequest {

	private final IResource resource;
	private final IResource destination;

	public ResourceRefactorRequest(IResource resource, IResource destination) {
		this.resource = notNull(resource, "resource");
		this.destination = notNull(destination, "destination");
	}

	@Override
	public IStatus isValid() {
		return Status.OK_STATUS;
	}

	@Override
	public IRefactorCommand getCommand(IProgressMonitor monitor)
			throws TigerstripeException {
		return super.getCommand(monitor);
	}

	public IResource getResource() {
		return resource;
	}

	@Override
	public int hashCode() {
		return resource.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return resource.equals(obj);
	}

	public IResource getDestination() {
		return destination;
	}
}
