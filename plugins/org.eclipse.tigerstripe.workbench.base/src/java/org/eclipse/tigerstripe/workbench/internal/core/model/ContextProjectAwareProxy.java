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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.internal.api.impl.ContextualModelProject;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ContextProjectAwareProxy implements
		java.lang.reflect.InvocationHandler, IContextProjectAware {

	private static final Signature IMC_GET_PROJECT_SIG = findSignatureByMarker(
			AbstractArtifact.class, Contextual.class);

	private static final Signature ITYPE_GET_ARTIFACT_MANAGER_SIG = findSignatureByMarker(
			Type.class, Contextual.class);

	private final Object obj;
	private volatile ITigerstripeModelProject context;

	public static <T> T newInstance(T obj, ITigerstripeModelProject context) {
		if (obj instanceof IContextProjectAware) {
			return obj;
		}
		return makeInstance(obj, context);
	}

	private static Signature findSignatureByMarker(Class<?> clazz,
			Class<? extends Annotation> marker) {
		for (java.lang.reflect.Method m : clazz.getMethods()) {
			if (m.isAnnotationPresent(marker)) {
				return new Signature(m);
			}
		}
		throw new RuntimeException("No method found in class "
				+ clazz.getName() + " for annotation " + marker.getName());
	}

	public static <T> T newInstanceOrChangeContext(T obj,
			ITigerstripeModelProject context) {
		if (obj instanceof IContextProjectAware) {
			ContextProjectAwareProxy handler = (ContextProjectAwareProxy) Proxy
					.getInvocationHandler(obj);
			handler.context = context;
			return obj;
		}
		return makeInstance(obj, context);
	}

	@SuppressWarnings("unchecked")
	private static <T> T makeInstance(T obj, ITigerstripeModelProject context) {
		return (T) java.lang.reflect.Proxy.newProxyInstance(obj.getClass()
				.getClassLoader(), collectRequiredInterfaces(obj),
				new ContextProjectAwareProxy(obj, context));
	}

	private static Class<?>[] collectRequiredInterfaces(Object obj) {
		HashSet<Class<?>> ifaces = new HashSet<Class<?>>();
		collectionIfaces(obj.getClass(), ifaces);
		ifaces.add(IContextProjectAware.class);
		return ifaces.toArray(new Class[0]);
	}

	private static void collectionIfaces(Class<?> clazz, Set<Class<?>> ifaces) {
		for (Class<?> iface : clazz.getInterfaces()) {
			ifaces.add(iface);
			collectionIfaces(iface, ifaces);
		}
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			collectionIfaces(superclass, ifaces);
		}
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
			if (IMC_GET_PROJECT_SIG.same(m)
					&& (proxy instanceof IModelComponent)) {
				ITigerstripeModelProject proj = (ITigerstripeModelProject) m
						.invoke(getObject(), args);
				if (proj == null) {
					return null;
				} else {
					return new ContextualModelProject(proj, context);
				}
			} else if (ITYPE_GET_ARTIFACT_MANAGER_SIG.same(m)
					&& (proxy instanceof IType)) {
				ArtifactManager artifactManager = (ArtifactManager) m.invoke(
						getObject(), args);
				if (artifactManager == null) {
					return null;
				} else {
					return new ContextualArtifactManager(artifactManager,
							context);
				}
			}

			if (IContextProjectAware.class.equals(m.getDeclaringClass())) {
				return m.invoke(this, args);
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

	private boolean sameSignature(Method m1, Method m2) {
		return m1.getName().equals(m2.getName())
				&& Arrays
						.equals(m1.getParameterTypes(), m2.getParameterTypes());
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
				if (element != null) {
					newCollection.add(newInstance(element, context));
				} else {
					newCollection.add(element);
				}
			}
			return newCollection;
		}
		return collection;
	}

	public Object getObject() {
		return obj;
	}

	public ITigerstripeModelProject getContextProject() {
		return context;
	}
	
	private static final class Signature {

		public final String name;
		public final Class<?>[] argTypes;

		public Signature(Method m) {
			this(m.getName(), m.getParameterTypes());
		}
		
		public Signature(String name, Class<?>[] argTypes) {
			this.name = name;
			this.argTypes = argTypes;
		}

		public boolean same(Method m) {
			return name.equals(m.getName())
					&& Arrays.equals(argTypes, m.getParameterTypes());
		}
	}
	
	@Override
	public String toString() {
		return "Proxy for '" + obj +"' in context " + context.getName();
	}

}
