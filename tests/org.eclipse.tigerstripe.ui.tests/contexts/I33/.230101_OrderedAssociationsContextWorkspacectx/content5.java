/*******************************************************************************
 * Created with Tigerstripe(tm) Workbench v.0.5.0.201102012332
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * DO NOT EDIT THIS FILE - Created with Tigerstripe(tm) Workbench
 *
 *******************************************************************************/
package com.mycompany;


/**
 * 
 *
 * @tigerstripe.association
 *		isAbstract = "false"
 *
 * * 
 */
public abstract class Association
 {


	/**
 * 
	 *	@tigerstripe.association-aEnd
	 *    aggregation = "none"
	 *    changeable = "none"
	 *    multiplicity = "0..*"
	 *    isOrdered = "true"
	 *    isUnique = "true"
	 *    isNavigable = "false"
	 *
 *
     * 
	 */	
	public com.mycompany.A a = null;

	/**
 * 
	 *	@tigerstripe.association-zEnd
	 *    aggregation = "none"
	 *    changeable = "none"
	 *    multiplicity = "1"
	 *    isOrdered = "false"
	 *    isUnique = "true"
	 *    isNavigable = "true"
	 *
 *
 * 
	 */	
	public com.mycompany.B b = null;
	
}