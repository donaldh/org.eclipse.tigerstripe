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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationValidationContext;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationValidationContext implements IAnnotationValidationContext {
	
	private Annotation annotation;
	
	public AnnotationValidationContext(Annotation annotation) {
		this.annotation = annotation;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationValidationContext#createFailureStatus(java.lang.String)
	 */
	public IStatus createFailureStatus(String message) {
		return new Status(IStatus.ERROR, AnnotationPlugin.PLUGIN_ID, message);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationValidationContext#createFailureStatus(java.lang.String, java.lang.Throwable)
	 */
	public IStatus createFailureStatus(String message, Throwable exception) {
		return new Status(IStatus.ERROR, AnnotationPlugin.PLUGIN_ID, message, exception);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationValidationContext#createSuccessStatus()
	 */
	public IStatus createSuccessStatus() {
		return Status.OK_STATUS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationValidationContext#getAnnotation()
	 */
	public Annotation getAnnotation() {
		return annotation;
	}

}
