/*******************************************************************************
 * Created with Tigerstripe(tm) Workbench v.0.7.0.201106291647
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
public abstract class A1
 {


	/**
 * 
	 *	@tigerstripe.association-aEnd
	 *    aggregation = "none"
	 *    changeable = "none"
	 *    multiplicity = "1"
	 *    isOrdered = "false"
	 *    isUnique = "false"
	 *    isNavigable = "false"
	 *
 *
     * 
	 */	
	public com.mycompany.Entity1 entity1 = null;

	/**
 * 
	 *	@tigerstripe.association-zEnd
	 *    aggregation = "none"
	 *    changeable = "none"
	 *    multiplicity = "1"
	 *    isOrdered = "false"
	 *    isUnique = "false"
	 *    isNavigable = "true"
	 *
 *
 * 
	 */	
	public com.mycompany.Entity3 entity3 = null;
	
}