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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author Yuri Strot
 *
 */
public class ProviderContext {
	
	private IAnnotationProvider provider;
	private String id;
	private String type;
	private String targetDescription;
	private String[] delegates;
	
	private static final String ANNOTATION_ATTR_CLASS = "class";
	private static final String ANNOTATION_ATTR_TYPE = "type";
	private static final String ANNOTATION_ATTR_TARGET_DESCRIPTION = "targetDescription";
	private static final String ANNOTATION_DELEGATE_NAME = "delegate";
	private static final String ANNOTATION_ATTR_ID = "id";
	
	/**
	 * @throws CoreException 
	 * 
	 */
	public ProviderContext(IConfigurationElement config) throws CoreException {
        provider = (IAnnotationProvider)config.createExecutableExtension(ANNOTATION_ATTR_CLASS);
        id = config.getAttribute(ANNOTATION_ATTR_ID);
        type = config.getAttribute(ANNOTATION_ATTR_TYPE);
        targetDescription = config.getAttribute(ANNOTATION_ATTR_TARGET_DESCRIPTION);
        initDelegates(config);
	}
	
	protected void initDelegates(IConfigurationElement config) {
		List<String> list = new ArrayList<String>();
        IConfigurationElement[] configs = config.getChildren();
		for (int i = 0; i < configs.length; i++) {
			if (configs[i].getName().equals(ANNOTATION_DELEGATE_NAME)) {
				String type = configs[i].getAttribute(ANNOTATION_ATTR_TYPE);
				list.add(type);
			}
		}
		delegates = list.toArray(new String[list.size()]);
	}
	
	/**
	 * @return the targetDescription
	 */
	public String getTargetDescription() {
		return targetDescription;
	}
	
	/**
	 * @return the provider
	 */
	public IAnnotationProvider getProvider() {
		return provider;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return the delegates
	 */
	public String[] getDelegates() {
		return delegates;
	}

}
