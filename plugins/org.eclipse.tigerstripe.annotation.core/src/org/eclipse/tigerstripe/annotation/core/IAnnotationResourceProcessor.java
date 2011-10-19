/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.core;

import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * Use for handling process of the loading and saving annotations resources.
 * Typical example of the using is support the legacy format of the annotation files.
 * 
 * Instances of this interface should be contributed through the
 * 'org.eclipse.tigerstripe.annotation.core.resourceProcessor' extension point
 */
public interface IAnnotationResourceProcessor {

	public void postLoad(XMLResource resource);

	public void preSave(XMLResource resource);

	public void postSave(XMLResource resource);
}
