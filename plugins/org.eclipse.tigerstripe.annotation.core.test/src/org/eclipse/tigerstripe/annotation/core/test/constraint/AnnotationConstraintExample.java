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
package org.eclipse.tigerstripe.annotation.core.test.constraint;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationConstraint;
import org.eclipse.tigerstripe.annotation.core.IAnnotationValidationContext;

/**
 * This constraint do not allow to create annotation for project1
 * if project2 already have annotations 
 * 
 * @author Yuri Strot
 */
public class AnnotationConstraintExample implements IAnnotationConstraint {
	
	private static final URI PROJECT_URI = URI.createURI("resource:/project1");
	
	/**
	 * Enable constraint only in case of test environment
	 */
	private static boolean ENABLE = false;
	
	public static void enable() {
		ENABLE = true;
	}
	
	public static void disable() {
		ENABLE = false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationConstraint#validate(org.eclipse.tigerstripe.annotation.core.IAnnotationValidationContext)
	 */
	public IStatus validate(IAnnotationValidationContext context) {
		if (ENABLE) {
			Annotation annotation = context.getAnnotation();
			URI uri = annotation.getUri();
			if (PROJECT_URI.equals(uri)) {
				IProject project2 = ResourcesPlugin.getWorkspace().getRoot().getProject("project2");
				Annotation[] annotations = AnnotationPlugin.getManager().getAnnotations(project2, false);
				if (annotations.length > 0) {
					return context.createFailureStatus("Can't create annotation for project1 because project2 " + 
							"already have annotations.");
				}
			}
		}
		return context.createSuccessStatus();
	}

}
