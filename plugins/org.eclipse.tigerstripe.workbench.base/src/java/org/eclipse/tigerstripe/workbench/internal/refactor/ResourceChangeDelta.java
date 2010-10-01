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
package org.eclipse.tigerstripe.workbench.internal.refactor;

import static org.eclipse.tigerstripe.workbench.internal.core.util.CheckUtils.notNull;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

public class ResourceChangeDelta {

	private final Type type;
	private final IResource originalResource;
	private final IPath destinationPath;

	public enum Type {
		UNKNOWN, MOVE, COPY, CREATE, DELETE
	}

	public ResourceChangeDelta(Type type, IResource originalResource,
			IPath destinationPath) {
		this.type = notNull(type, "type");
		this.originalResource = notNull(originalResource, "originalResource");
		this.destinationPath = notNull(destinationPath, "destinationPath");
	}

	public Type getType() {
		return type;
	}

	public IResource getOriginalResource() {
		return originalResource;
	}

	public IPath getDestinationPath() {
		return destinationPath;
	}
}
