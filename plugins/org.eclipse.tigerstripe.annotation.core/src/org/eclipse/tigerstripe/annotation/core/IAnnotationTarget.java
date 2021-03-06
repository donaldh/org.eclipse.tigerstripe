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
 * @author Yuri Strot
 *
 */
public interface IAnnotationTarget {
	
	/**
	 * @return the object
	 */
	public Object getAdaptedObject();
	
	/**
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * @return the type
	 */
	public AnnotationType getType();

}
