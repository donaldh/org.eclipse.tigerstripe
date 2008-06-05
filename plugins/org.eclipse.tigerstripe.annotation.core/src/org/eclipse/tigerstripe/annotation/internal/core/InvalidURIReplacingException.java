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
package org.eclipse.tigerstripe.annotation.internal.core;

/**
 * @author Yuri Strot
 *
 */
public class InvalidURIReplacingException extends Exception {

	private static final long serialVersionUID = -208449298829663012L;
	
    public InvalidURIReplacingException() {
    	super();
    }

    public InvalidURIReplacingException(String message) {
    	super(message);
    }

    public InvalidURIReplacingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidURIReplacingException(Throwable cause) {
        super(cause);
    }

}
