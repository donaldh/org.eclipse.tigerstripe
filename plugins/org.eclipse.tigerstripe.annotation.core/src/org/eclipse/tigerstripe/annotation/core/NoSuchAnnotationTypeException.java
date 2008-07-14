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
 * This exception is thrown to signal that there is
 * no annotation types defined for some <code>EClass</code> 
 * 
 * @author Yuri Strot
 */
public class NoSuchAnnotationTypeException extends AnnotationException {

	private static final long serialVersionUID = 3677899865355061155L;

	public NoSuchAnnotationTypeException() {
    	super();
    }

    public NoSuchAnnotationTypeException(String message) {
    	super(message);
    }

}
