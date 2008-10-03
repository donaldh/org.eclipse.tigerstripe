/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.patterns;

import java.net.URL;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;

public interface IPattern {
	
	public interface IPatternAnnotation {
		public String getTarget();
		public EObject getAnnotationContent();
	}
	
	public int getIndex();
	
	public String getName();
	
	public String getUILabel();
	
	public String getIconPath();
	
	public URL getIconURL();
	
	public String getDisabledIconPath();
	
	public URL getDisabledIconURL();
	
	public String getDescription();
	
	public ImageDescriptor getImageDescriptor();
	
	public Collection<IPatternAnnotation> getPatternAnnotations();
	
	public IPatternBasedWizardValidator getWizardValidator();
	
}
