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
 * Interface of all constraint implementations provided via the
 * <code>org.eclipse.tigerstripe.annotation.core.constraints</code> extension point.
 * 
 * @author Yuri Strot
 */
public interface IAnnotationConstraint {
	
	/**
	 * <p>
	 * Validates an annotation in the specified context.  The
	 * {@link IAnnotationValidationContext#getAnnotation() annotation} of the validation operation
	 * is available from the context object.
	 * </p>
	 * <p>
	 * <b>Note</b> that it is best to use the
	 * {@link IAnnotationValidationContext#createSuccessStatus()} and
	 * {@link IAnnotationValidationContext#createFailureStatus(String)} methods of the context
	 * object to create the status object returned from this method.
	 * </p><p>
	 * @see IAnnotationValidationContext
	 * 
	 * @param context annotation validation context
	 * @return the status of validation of the annotation
	 */
	public IStatus validate(IAnnotationValidationContext context);

}
