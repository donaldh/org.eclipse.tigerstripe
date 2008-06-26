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
import org.eclipse.tigerstripe.annotation.internal.core.ProviderTarget;

/**
 * @author Yuri Strot
 *
 */
public class ProviderContext {
	
	private IAnnotationProvider provider;
	private String id;
	private ProviderTarget target;
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
        String type = config.getAttribute(ANNOTATION_ATTR_TYPE);
        String targetDescription = config.getAttribute(ANNOTATION_ATTR_TARGET_DESCRIPTION);
        target = new ProviderTarget(type, targetDescription);
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
	 * @return the target
	 */
	public ProviderTarget getTarget() {
		return target;
	}
	
	/**
	 * @return the delegates
	 */
	public String[] getDelegates() {
		return delegates;
	}

}
