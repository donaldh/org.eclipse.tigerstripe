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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

public abstract class BaseAnnotable implements Annotable {
	private String name;
	private String comment;

	private boolean ignore;

	public BaseAnnotable(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String comment) {
		this.comment = comment;
	}

	public String getDescription() {
		return this.comment;
	}

	public void setShouldIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public boolean shouldIgnore() {
		return this.ignore;
	}
}
