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
package org.eclipse.tigerstripe.espace.resources.core;

import org.eclipse.emf.ecore.xmi.XMLResource;

public interface IAnnotationResourceProcessor {

	public void postLoad(XMLResource resource);

	public void preSave(XMLResource resource);

	public void postSave(XMLResource resource);
}
