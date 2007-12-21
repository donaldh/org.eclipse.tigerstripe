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
package org.eclipse.tigerstripe.core.model.importing;

public interface Annotable {

	public final static int VISIBILITY_PUBLIC = 0;
	public final static int VISIBILITY_PROTECTED = 1;
	public final static int VISIBILITY_PRIVATE = 2;

	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String comment);

	public boolean isMapped();

	public final static int DELTA_UNKNOWN = -1;
	public final static int DELTA_UNCHANGED = 0;
	public final static int DELTA_ADDED = 1;
	public final static int DELTA_REMOVED = 2;
	public final static int DELTA_CHANGED = 3;

	public void setDelta(int delta);

	public int getDelta();

	public void setShouldIgnore(boolean ignore);

	/**
	 * Should this element be ignored during the import
	 * 
	 * @return
	 */
	public boolean shouldIgnore();

}
