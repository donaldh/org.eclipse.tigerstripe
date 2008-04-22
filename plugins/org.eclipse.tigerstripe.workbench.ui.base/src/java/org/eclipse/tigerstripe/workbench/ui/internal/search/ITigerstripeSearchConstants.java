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
package org.eclipse.tigerstripe.workbench.ui.internal.search;

public interface ITigerstripeSearchConstants {

	// search for
	public final static int TYPE = 0;
	public final static int METHOD = 1;
	public final static int PACKAGE = 2;
	public final static int FIELD = 3;
	public final static int LITERAL = 4;

	// limit to
	public final static int DECLARATIONS = 5;
	public final static int IMPLEMENTORS = 6;
	public final static int REFERENCES = 7;
	public final static int ALL_OCCURRENCES = 8;

}
