/*******************************************************************************
 * Created with Tigerstripe(tm) Workbench v.0.7.0.201102281826
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * DO NOT EDIT THIS FILE - Created with Tigerstripe(tm) Workbench
 *
 *******************************************************************************/
package test_new;


/**
 * 
 *
 * @tigerstripe.dependency
 *		isAbstract = "false"
 *
 *
 * @tigerstripe.stereotype st.name = "ST_for_AssociationEnd"
 * 
 */
public abstract class Dependency
 {


	/**
	 *	@tigerstripe.dependency-aEnd
     *    type = "test_new.Event"
	 */	
	public String _aEnd = null;
	
	/**
	 *	@tigerstripe.dependency-zEnd
     *    type = "test_new.Enum"
	 */	
	public String _zEnd = null;
	
}