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
package org.eclipse.tigerstripe.espace.resources.format;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.tigerstripe.espace.resources.internal.format.AnnotationResourceHandler;

/**
 * @author Yuri Strot
 * 
 */
public class AnnotationResourceFactory extends XMIResourceFactoryImpl {

	public AnnotationResourceFactory() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl#createResource(
	 * org.eclipse.emf.common.util.URI)
	 */
	@Override
	public Resource createResource(URI uri) {
		AnnotationXMIResource resource = new AnnotationXMIResource(uri);

		XMLResource.ResourceHandler resourceHandler = AnnotationResourceHandler
				.getInstance();

		resource.getDefaultLoadOptions().put(
				XMLResource.OPTION_RESOURCE_HANDLER, resourceHandler);
		resource.getDefaultSaveOptions().put(
				XMLResource.OPTION_RESOURCE_HANDLER, resourceHandler);

		return resource;
	}
}
