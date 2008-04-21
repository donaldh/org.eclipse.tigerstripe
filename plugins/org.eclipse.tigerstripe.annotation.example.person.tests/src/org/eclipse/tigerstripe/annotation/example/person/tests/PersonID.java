/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.example.person.tests;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.IAnnotable;

/**
 * @author Yuri Strot
 *
 */
public class PersonID implements IAnnotable {
	
	private String id;
	private URI uri;
	
	public PersonID(String id) {
		this.id = id;
		uri = URI.createGenericURI("person", id, null);
	}
	
	public String getId() {
	    return id;
    }
	
	public URI getUri() {
	    return uri;
	}

}
