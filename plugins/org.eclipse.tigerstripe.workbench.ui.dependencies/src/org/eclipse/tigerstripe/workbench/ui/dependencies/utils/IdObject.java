/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.utils;

import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;

public abstract class IdObject implements IDependencySubject {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IDependencySubject) {
			return getId().equals(((IDependencySubject) obj).getId());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	public final String getId() {
		return getType().getId() + "." + internalId();
	}

	protected abstract String internalId();

}
