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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;


/**
 * Annotation type provide <code>EObject</code> objects, that can be used as
 * annotation content. Annotation type should be registered with the
 * <code>org.eclipse.tigerstripe.annotation.core.annotationType</code>
 * extension point.
 * 
 * @author Yuri Strot
 */
public class AnnotationType {
	
	private static final String ATTR_NAME = "name";
	private static final String ATTR_DESCRIPTION = "description";
	private static final String ATTR_URI = "epackage-uri";
	private static final String ATTR_TYPE = "eclass";
	private static final String ATTR_ID = "id";
	
	private static final String ELEMENT_TARGET = "target";
	private static final String ATTR_TARGET_TYPE = "type";
	
	private String id;
	private String name;
	private String desciption;
	private EClass clazz;
	
	private String[] targets;
	
	public AnnotationType(IConfigurationElement definition) {
		name = definition.getAttribute(ATTR_NAME);
		id = definition.getAttribute(ATTR_ID);
		desciption = definition.getAttribute(ATTR_DESCRIPTION);
		clazz = getClass(definition);
		initTargets(definition);
	}
	
	protected void initTargets(IConfigurationElement definition) {
		List<String> targets = new ArrayList<String>();
		IConfigurationElement[] children = definition.getChildren();
		for (int i = 0; i < children.length; i++) {
			IConfigurationElement target = children[i];
			if (ELEMENT_TARGET.equals(target.getName())) {
				String type = target.getAttribute(ATTR_TARGET_TYPE);
				if (type != null)
					targets.add(type);
			}
		}
		this.targets = targets.toArray(new String[targets.size()]);
	}

	private static EClass getClass(IConfigurationElement type) {
		String uri = type.getAttribute(ATTR_URI);
		String name = type.getAttribute(ATTR_TYPE);
		EPackage pkg = getPackage(uri);
		return (EClass) pkg.getEClassifier(name);
	}

	private static EPackage getPackage(String uri) {
		return EPackage.Registry.INSTANCE.getEPackage(uri);
	}
	
	/**
	 * Create annotation content instance
	 * 
	 * @return annotation content
	 */
	public EObject createInstance() {
		EObject object = clazz.getEPackage().getEFactoryInstance().create(clazz);
		return object;
    }
	
	/**
	 * @return annotation content <code>EClass</code>
	 */
	public EClass getClazz() {
		return clazz;
	}
	
	/**
	 * Return annotation type id
	 * 
	 * @return
	 */
	public String getId() {
	    return id;
    }
	
	/**
	 * Return anotation type description
	 * 
	 * @return
	 */
	public String getDesciption() {
	    return desciption;
    }
	
	/**
	 * Return annotation type name
	 * 
	 * @return
	 */
	public String getName() {
	    return name;
    }
	
	/**
	 * @return the targets
	 */
	public String[] getTargets() {
		return targets;
	}

}
