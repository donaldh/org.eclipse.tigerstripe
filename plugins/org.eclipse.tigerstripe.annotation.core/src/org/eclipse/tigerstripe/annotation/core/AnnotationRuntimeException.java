/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.core;

public class AnnotationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AnnotationRuntimeException() {
    	super();
    }

    public AnnotationRuntimeException(String message) {
    	super(message);
    }

    public AnnotationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationRuntimeException(Throwable cause) {
        super(cause);
    }

}
