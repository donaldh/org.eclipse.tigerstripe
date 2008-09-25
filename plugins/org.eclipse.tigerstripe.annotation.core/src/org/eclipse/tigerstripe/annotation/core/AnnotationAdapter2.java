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

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.espace.core.Mode;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationAdapter2 extends AnnotationAdapter implements IAnnotationListener2 {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationListener2#annotationsAdded(org.eclipse.emf.ecore.resource.Resource, org.eclipse.tigerstripe.espace.core.Mode)
	 */
	public void annotationsAdded(Resource resource, Mode option) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationListener2#annotationsRemoved(org.eclipse.emf.ecore.resource.Resource)
	 */
	public void annotationsRemoved(Resource resource) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationListener2#annotationsUnregistered(org.eclipse.emf.ecore.resource.Resource)
	 */
	public void annotationsUnregistered(Resource resource) {
	}

}
