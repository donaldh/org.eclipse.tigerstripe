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
package org.eclipse.tigerstripe.api.utils;

public final class TigerstripeErrorLevel {

	public static final TigerstripeErrorLevel INFO = new TigerstripeErrorLevel();
	public static final TigerstripeErrorLevel WARNING = new TigerstripeErrorLevel();
	public static final TigerstripeErrorLevel ERROR = new TigerstripeErrorLevel();

	private TigerstripeErrorLevel() {
	}

	@Override
	public String toString() {
		if (this == INFO)
			return "Info";
		else if (this == WARNING)
			return "Warning";
		else if (this == ERROR)
			return "Error";
		return "Unknown";
	}
}
