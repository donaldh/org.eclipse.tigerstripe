/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.model;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.internal.core.util.Predicate;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class ModelUtils {

	public static boolean equalsByFQN(IAbstractArtifact a1, IAbstractArtifact a2) {
		if (a1 == null || a2 == null) {
			return false;
		}

		return a1.getFullyQualifiedName().equals(a2.getFullyQualifiedName());
	}

	public static Map<String, Object> mapWritablePropertiesExclude(Class<?> beanClass, Object bean, 
			String... exclude) {
		
		final HashSet<String> props = new HashSet<String>(Arrays.asList(exclude));
		
		return mapProperties(beanClass, bean, new Predicate() {
			
			public boolean evaluate(Object obj) {
				PropertyDescriptor pd = (PropertyDescriptor)obj;
				return !props.contains(pd.getName()) && pd.getWriteMethod() != null;
			}
		});
	}
	
	public static Map<String, Object> mapProperties(Class<?> beanClass, Object bean, Predicate predicate) {
		BeanInfo beanInfo = getBeanInfo(beanClass);
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		Map<String, Object> result = new HashMap<String, Object>();
		for (PropertyDescriptor pd : propertyDescriptors) {
			try {
				if (pd instanceof IndexedPropertyDescriptor) {
					continue;
				}
				Method readMethod = pd.getReadMethod();
				if (readMethod == null) {
					continue;
				}
				if (!predicate.evaluate(pd)) {
					continue;
				}
				result.put(pd.getName(), readMethod.invoke(bean));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}
	
	public static void setProperties(Class<?> beanClass, Object bean, Map<String, Object> properties) {
		BeanInfo beanInfo = getBeanInfo(beanClass);
		
		Map<String, PropertyDescriptor> pdMap = new HashMap<String, PropertyDescriptor>();
		
		for (PropertyDescriptor pd : beanInfo
				.getPropertyDescriptors()) {
			pdMap.put(pd.getName(), pd);
		}

		for (Entry<String, Object> e : properties.entrySet()) {
			PropertyDescriptor pd = pdMap.get(e.getKey());
			if (pd == null) {
				continue;
			}
			Method writeMethod = pd.getWriteMethod();
			if (writeMethod == null) {
				continue;
			}
			try {
				writeMethod.invoke(bean, e.getValue());
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}
	
	public static void copyAsBeans(Class<?> beanClass, Object from,
			Object to, Predicate predicate) {
		BeanInfo beanInfo = getBeanInfo(beanClass);
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		Class<?> toClass = to.getClass();
		for (PropertyDescriptor pd : propertyDescriptors) {
			try {
				if (pd instanceof IndexedPropertyDescriptor) {
					continue;
				}
				Method readMethod = pd.getReadMethod();
				if (readMethod == null) {
					continue;
				}
				Method writeMethodToClass = pd.getWriteMethod();
				if (writeMethodToClass == null) {
					continue;
				}
				if (!predicate.evaluate(pd)) {
					continue;
				}
				Method writeMethod;
				try {
					writeMethod = toClass.getMethod(
							writeMethodToClass.getName(),
							writeMethodToClass.getParameterTypes());
				} catch (NoSuchMethodException ex) {
					continue;
				}
				writeMethod.invoke(to, readMethod.invoke(from));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void copyAsBeans(Class<?> beanClass, Object from, Object to) {
		copyAsBeans(beanClass, from, to, new Predicate() {
			
			public boolean evaluate(Object obj) {
				return true;
			}
		});
	}

	public static void copyAsBeansInclude(Class<?> beanClass, Object from,
			Object to, String... include) {
		copyAsBeansInternral(beanClass, from, to, include, true);
	}
	
	public static void copyAsBeansExclude(Class<?> beanClass, Object from,
			Object to, String... exclude) {
		copyAsBeansInternral(beanClass, from, to, exclude, false);
	}

	private static void copyAsBeansInternral(Class<?> beanClass, Object from,
			Object to, String[] props, final boolean include) {
		final Set<String> propsSet = new HashSet<String>(Arrays.asList(props));
		copyAsBeans(beanClass, from, to, new Predicate() {
			
			public boolean evaluate(Object obj) {
				return include && propsSet.contains(((PropertyDescriptor)obj).getName());
			}
		});
	}

	private static BeanInfo getBeanInfo(Class<?> beanClass) {
		try {
			return Introspector.getBeanInfo(beanClass);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

}
