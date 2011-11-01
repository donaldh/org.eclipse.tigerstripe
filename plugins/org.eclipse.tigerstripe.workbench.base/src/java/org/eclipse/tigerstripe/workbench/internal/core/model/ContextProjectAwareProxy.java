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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ContextProjectAwareProxy implements
		java.lang.reflect.InvocationHandler {

	private final Object obj;
	private final ITigerstripeModelProject context;

	public static Object newInstance(Object obj,
			ITigerstripeModelProject context) {
		if (obj instanceof IContextProjectAware) {
			return obj;
		}
		return java.lang.reflect.Proxy.newProxyInstance(obj.getClass()
				.getClassLoader(), collectRequiredInterfaces(obj),
				new ContextProjectAwareProxy(obj, context));
	}

	private static Class<?>[] collectRequiredInterfaces(Object obj) {
		Class<?>[] interfaces = obj.getClass().getInterfaces();
		Class<?>[] result = Arrays.copyOf(interfaces, interfaces.length + 1);
		result[interfaces.length] = IContextProjectAware.class;
		return result;
	}

	private ContextProjectAwareProxy(Object obj,
			ITigerstripeModelProject context) {
		this.obj = obj;
		this.context = context;
	}

	public Object invoke(Object proxy, java.lang.reflect.Method m, Object[] args)
			throws Throwable {
		Object result = null;
		try {
			if (IContextProjectAware.class.equals(m.getDeclaringClass())) {
				if ("getContextProject".equals(m.getName())) {
					return context;
				}
			} else if (IAnnotationCapable.class.equals(m.getDeclaringClass())) {
				IAnnotationCapable ac = new AnnotationCapable(proxy);
				result = m.invoke(ac, args);
			} else {
				result = m.invoke(getObject(), args);
				if (result != null && needToProxyResult(m)) {
					result = proxyResult(result);
				}
			}
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		} catch (Exception e) {
			throw new RuntimeException("unexpected invocation exception: "
					+ e.getMessage());
		}
		return result;
	}

	private boolean needToProxyResult(java.lang.reflect.Method method) {
		if (method.getAnnotation(ProvideModelComponents.class) != null) {
			return true;
		} else {
			try {
				java.lang.reflect.Method m = getObject().getClass().getMethod(
						method.getName(), method.getParameterTypes());
				if (m.getAnnotation(ProvideModelComponents.class) != null) {
					return true;
				}
			} catch (NoSuchMethodException e) {
			}
		}
		return false;
	}

	private Object proxyResult(Object result) {
		if (result instanceof Collection<?>) {
			return processCollection((Collection<?>) result);
		} else {
			return newInstance(result, context);
		}
	}

	private <T> Collection<T> processCollection(Collection<T> collection) {
		if (!collection.isEmpty()) {
			List<T> newCollection = new ArrayList<T>(collection.size());
			for (T element : collection) {
				newCollection.add((T) newInstance(element, context));
			}
			return newCollection;
		}
		return collection;
	}

	public Object getObject() {
		return obj;
	}
}
