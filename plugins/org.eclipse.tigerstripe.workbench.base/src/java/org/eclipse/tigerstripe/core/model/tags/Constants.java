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
package org.eclipse.tigerstripe.core.model.tags;

/**
 * All Constant tags names
 * 
 * @author Eric Dillon
 * 
 */
public interface Constants {

	// tags
	public final static String TS = "tigerstripe";
	public final static String SEPARATOR = ".";

	/**
	 * Properties
	 * 
	 * @tigerstripe.property ts.id = "<PROPERTY_ID>" name0 = "value0" name1 =
	 *                       "value1"
	 * 
	 * @see org.eclipse.tigerstripe.core.model.tags.Util
	 */
	public final static String PROPERTY = TS + SEPARATOR + "property";
	public final static String TS_ID = "ts.id";

	/**
	 * Stereotypes
	 * 
	 * @tigerstripe.stereotype st.id = "<STEREOTYPE_NAME>" attr0 = "value0"
	 *                         attr1 = "value1"
	 * 
	 * @see org.eclipse.tigerstripe.core.model.tags.Util
	 */
	public final static String STEREOTYPE = TS + SEPARATOR + "stereotype";
	public final static String ST_NAME = "st.name";
	public final static String ST_ARG_NAME = "st.arg.name";
	public final static String ARG_STEREOTYPE = TS + SEPARATOR
			+ "arg-stereotype";
	public final static String METH_RETURN_STEREOTYPE = TS + SEPARATOR
			+ "ret-stereotype";

	// These are the labels for the methods and CRUDs
	public final static String BULKATOMIC_LABEL = "bulkAtomic";
	public final static String BULKBESTEFFORT_LABEL = "bulkBestEffort";

}
