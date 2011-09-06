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
package org.eclipse.tigerstripe.espace.resources.internal.format;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.BasicResourceHandler;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;
import org.eclipse.tigerstripe.espace.resources.core.IAnnotationResourceProcessor;

public class AnnotationResourceHandler extends BasicResourceHandler {
	
	private static final String EXTPT_PREFIX = "org.eclipse.tigerstripe.espace.resources.";
	private static final String ANNOTATION_RESOURCE_PROCESSOR_EXT_POINT = EXTPT_PREFIX
			+ "resourceProcessor";
	private static final String PROCESSOR_ATTR_CLASS = "class";

	private static AnnotationResourceHandler theInstance;
	
	private List<IAnnotationResourceProcessor> processors;

	private AnnotationResourceHandler() {
		loadProcessors();
	}
	
	public static AnnotationResourceHandler getInstance() {
		if (theInstance == null) {
			theInstance = new AnnotationResourceHandler();
		}
		return theInstance;
	}

	@Override
	public void postLoad(XMLResource resource, InputStream inputStream,
			Map<?, ?> options) {
		for (IAnnotationResourceProcessor processor : processors) {
			processor.postLoad(resource);
		}
		super.postLoad(resource, inputStream, options);
	}

	@Override
	public void preSave(XMLResource resource, OutputStream outputStream,
			Map<?, ?> options) {
		for (IAnnotationResourceProcessor processor : processors) {
			processor.preSave(resource);
		}
		super.preSave(resource, outputStream, options);
	}

	protected void loadProcessors() {
		processors = new ArrayList<IAnnotationResourceProcessor>();
		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ANNOTATION_RESOURCE_PROCESSOR_EXT_POINT);
		for (IConfigurationElement config : configs) {
			try {
				IAnnotationResourceProcessor processor = (IAnnotationResourceProcessor) config
						.createExecutableExtension(PROCESSOR_ATTR_CLASS);
				processors.add(processor);
			} catch (Exception e) {
				ResourcesPlugin.log(e);
			}
		}
	}
}
