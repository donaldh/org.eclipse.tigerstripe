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
 * This exception is thrown to signal that annotation targets
 * do not allow to add specified <code>EObject</code> to the
 * some annotable object.
 * 
 * @author Yuri Strot
 */
public class IllegalAnnotationTargetException extends AnnotationException {

	private static final long serialVersionUID = 3677899865355061155L;

	public IllegalAnnotationTargetException() {
    	super();
    }

    public IllegalAnnotationTargetException(String message) {
    	super(message);
    }

}
