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
package org.eclipse.tigerstripe.annotation.ui.internal.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;

/**
 * @author Yuri Strot
 *
 */
public class CreateSpecificTypeAnnotationAction extends Action {
	
	private Object object;
	private AnnotationType type;
	
	public CreateSpecificTypeAnnotationAction(Object object, AnnotationType type) {
		super(type.getName());
		ImageDescriptor image = AnnotationUIPlugin.getManager().getImage(type);
		setImageDescriptor(image);
		this.object = object;
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		EObject content = type.createInstance();
		AnnotationPlugin.getManager().addAnnotation(object, content);
	}

}
