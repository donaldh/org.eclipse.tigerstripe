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

import org.eclipse.core.runtime.IStatus;

/**
 * <p>
 * Interface providing contextual information to
 * {@link IAnnotationConstraint}s about the annotation validation currently in progress.
 * </p>
 * 
 * @author Yuri Strot
 */
public interface IAnnotationValidationContext {
	
	/**
	 * Return annotation that can be added
	 * 
	 * @return annotation that can be added
	 */
	public Annotation getAnnotation();
	
	/**
	 * Return annotated object
	 * 
	 * @return annotated object
	 */
	public Object getAnnotatedObject();
	
	
	/**
	 * Creates a status object indicating successful evaluation of the
	 * current constraint on the current annotation.
	 * 
	 * @return the "success" status
	 */
	public IStatus createSuccessStatus();
	
	/**
	 * Creates a status object indicating unsuccessful evaluation of the
	 * current constraint on the current annotation. The status will have
	 * the exception and message.
	 * 
	 * @param message
	 * @param exception
	 * @return the status indicating a constraint violation
	 */
	public IStatus createFailureStatus(String message, Throwable exception);
	
	/**
	 * Creates a status object indicating unsuccessful evaluation of the
	 * current constraint on the current annotation.
	 * 
	 * @param message
	 * @return the status indicating a constraint violation
	 */
	public IStatus createFailureStatus(String message);

}
