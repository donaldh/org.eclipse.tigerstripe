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
 * Default <code>IAnnotationListener</code> implementation
 * 
 * @author Yuri Strot
 */
public class AnnotationAdapter implements IAnnotationListener {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationListener#annotationAdded(org.eclipse.tigerstripe.annotation.core.Annotation)
	 */
	public void annotationAdded(Annotation annotation) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationListener#annotationsChanged(org.eclipse.tigerstripe.annotation.core.Annotation[])
	 */
	public void annotationsChanged(Annotation[] annotations) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationListener#annotationsRemoved(org.eclipse.tigerstripe.annotation.core.Annotation[])
	 */
	public void annotationsRemoved(Annotation[] annotations) {
	}

}
