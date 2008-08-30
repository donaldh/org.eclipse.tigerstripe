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
package org.eclipse.tigerstripe.annotation.core;

/**
 * This exception is thrown to signal that annotation
 * constraint do not allow to create annotation
 *  
 * @author Yuri Strot
 */
public class AnnotationConstraintException extends AnnotationException {

	private static final long serialVersionUID = -4701735683124017376L;

    public AnnotationConstraintException(String message) {
    	super(message);
    }

    public AnnotationConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

}
