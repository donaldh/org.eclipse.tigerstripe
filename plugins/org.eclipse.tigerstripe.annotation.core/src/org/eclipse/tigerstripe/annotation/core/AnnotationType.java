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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationTarget;
import org.eclipse.tigerstripe.annotation.internal.core.ProviderTarget;

/**
 * Annotation type provide <code>EObject</code> objects, that can be used as
 * annotation content. Annotation type should be registered with the
 * <code>org.eclipse.tigerstripe.annotation.core.annotationType</code> extension
 * point.
 * 
 * @author Yuri Strot
 */
public class AnnotationType {

	private static final String ATTR_NAME = "name";
	private static final String ATTR_DESCRIPTION = "description";
	private static final String ATTR_URI = "epackage-uri";
	private static final String ATTR_TYPE = "eclass";
	private static final String ATTR_UNIQUE = "unique";

	private static final String ELEMENT_TARGET = "target";
	private static final String ATTR_TARGET_TYPE = "type";
	private static final String ATTR_TARGET_UNIQUE = "unique";

	private String name;
	private String description;
	private EClass clazz;
	private boolean unique;

	private String[] targets;

	private Map<String, Boolean> uniques = new HashMap<String, Boolean>();

	public AnnotationType(IConfigurationElement definition) {
		name = definition.getAttribute(ATTR_NAME);
		description = definition.getAttribute(ATTR_DESCRIPTION);
		unique = Boolean.valueOf(definition.getAttribute(ATTR_UNIQUE));
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
				String unique = target.getAttribute(ATTR_TARGET_UNIQUE);
				Boolean value = unique == null ? Boolean.TRUE : Boolean
						.valueOf(unique);
				uniques.put(type, value);
				if (type != null)
					targets.add(type);
			}
		}
		this.targets = targets.toArray(new String[targets.size()]);
	}

	private static EClass getClass(IConfigurationElement type) {
		String uri = type.getAttribute(ATTR_URI);
		String name = type.getAttribute(ATTR_TYPE);
		return AnnotationUtils.getClass(uri, name);
	}

	/**
	 * Create annotation content instance
	 * 
	 * @return annotation content
	 */
	public EObject createInstance() {
		EObject object = clazz.getEPackage().getEFactoryInstance()
				.create(clazz);
		return object;
	}

	/**
	 * @return annotation content <code>EClass</code>
	 */
	public EClass getClazz() {
		return clazz;
	}

	public boolean isTargetUnique(String type) {
		return uniques.get(type).booleanValue();
	}

	/**
	 * Return annotation type description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
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

	public String getId() {
		return AnnotationUtils.getInstanceClassName(getClazz())
				.getFullClassName();
	}

	/**
	 * Returns <code>false</code> if in the absence of 'targets' the annotation
	 * may beattached multiply, or returns <code>true</code> otherwise (the
	 * default)
	 * 
	 * @return <code>false</code> if in the absence of 'targets' the annotation
	 *         may beattached multiply, or returns <code>true</code> otherwise
	 */
	public boolean isUnique() {
		return unique;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AnnotationType) {
			AnnotationType type = (AnnotationType) obj;
			return type.getClazz().equals(type.getClazz());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getClazz().hashCode();
	}

	public IAnnotationTarget[] getTargets(Object object,
			ProviderTarget[] targets) {
		Map<ProviderTarget, Object> map = new HashMap<ProviderTarget, Object>();
		for (int i = 0; i < targets.length; i++) {
			ProviderTarget target = targets[i];
			Object adapted = target.adapt(object);
			if (adapted != null) {
				map.put(target, adapted);
			}
		}
		List<IAnnotationTarget> annotationTargets = new ArrayList<IAnnotationTarget>();

		if (this.targets.length == 0) {
			for (ProviderTarget target : map.keySet()) {
				Object adapted = map.get(target);
				if (AnnotationPlugin.getManager().isPossibleToAdd(adapted,
						getClazz())) {
					AnnotationTarget annotationTarget = new AnnotationTarget(
							adapted, target.getDescription(), this);
					annotationTargets.add(annotationTarget);
				}
			}
		} else {
			for (int i = 0; i < this.targets.length; i++) {
				String className = this.targets[i];
				for (ProviderTarget target : map.keySet()) {
					Object adapted = map.get(target);
					if (AnnotationPlugin.getManager().isPossibleToAdd(adapted,
							getClazz())
							&& isSuperClass(className, adapted)) {
						AnnotationTarget annotationTarget = new AnnotationTarget(
								adapted, target.getDescription(), this);
						annotationTargets.add(annotationTarget);
					}
				}
			}
		}
		return annotationTargets
				.toArray(new IAnnotationTarget[annotationTargets.size()]);
	}

	private static boolean isSuperClass(String className, Object object) {
		try {
			Class<?> clazz = object.getClass();
			Class<?> parentClass = Class.forName(className, true, clazz
					.getClassLoader());
			return parentClass.isAssignableFrom(clazz);
		} catch (Exception e) {
		}
		return false;
	}

}
