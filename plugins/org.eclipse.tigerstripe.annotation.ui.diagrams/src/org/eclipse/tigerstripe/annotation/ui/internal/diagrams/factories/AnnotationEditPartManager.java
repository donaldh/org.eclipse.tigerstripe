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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.factories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.AnnotationEditPart;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.IAnnotationEditPartProvider;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.TextAnnotationEditPart;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationEditPartManager {
	
	public static final Class<? extends AnnotationEditPart> DEFAULT_PART_CLASS = TextAnnotationEditPart.class;
	
	private static final String PROVIDERS_EXTPT = 
		"org.eclipse.tigerstripe.annotation.ui.diagrams.annotationEditpartProviders";
	private static final String PROVIDER_EXTPT_NAME = "provider";
	private static final String ATTR_CLASS = "class";
	
	private IAnnotationEditPartProvider[] getProviders() {
		if (providers == null) {
			List<IAnnotationEditPartProvider> providers =
				new ArrayList<IAnnotationEditPartProvider>();
			IConfigurationElement[] configs = Platform.getExtensionRegistry(
				).getConfigurationElementsFor(PROVIDERS_EXTPT);
	        for (IConfigurationElement config : configs) {
	        	try {
	        		if (PROVIDER_EXTPT_NAME.equals(config.getName())) {
	        			IAnnotationEditPartProvider provider = 
	        				(IAnnotationEditPartProvider)config.createExecutableExtension(ATTR_CLASS);
		        		providers.add(provider);
	        		}
	            }
	            catch (Exception e) {
	            	AnnotationPlugin.log(e);
	            }
	        }
	        this.providers = providers.toArray(
	        		new IAnnotationEditPartProvider[providers.size()]);
		}
		return providers;
	}
	
	public Class<? extends AnnotationEditPart> getAnnotationEditPartClass(Annotation annotation) {
		IAnnotationEditPartProvider[] providers = getProviders();
		for (int i = 0; i < providers.length; i++) {
			Class<? extends AnnotationEditPart> clazz = 
				providers[i].getAnnotationEditPartClass(annotation);
			if (clazz != null)
				return clazz;
		}
		return DEFAULT_PART_CLASS;
	}
	
	private IAnnotationEditPartProvider[] providers;

}
