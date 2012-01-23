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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ContextualModelProject;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.FacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ContextProjectAwareProxy implements
		java.lang.reflect.InvocationHandler, IContextProjectAware {

	private static final Signature IMC_GET_PROJECT_SIG = findSignatureByMarker(
			AbstractArtifact.class, Contextual.class);

	private static final Signature ITYPE_GET_ARTIFACT_MANAGER_SIG = findSignatureByMarker(
			Type.class, Contextual.class);

	volatile Object obj;
	volatile ITigerstripeModelProject context;


	public static void changeTraget(Object proxy, Object target) {
		ContextProjectAwareProxy handler = (ContextProjectAwareProxy) Proxy.getInvocationHandler(proxy);
		handler.obj = target;
	}
	
	public static boolean isContextualProxy(Object obj) {

		if (obj == null) {
			return false;
		}
		return Proxy.isProxyClass(obj.getClass())
				&& obj instanceof IContextProjectAware;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T expose(T proxy) { 
		
		if (Proxy.isProxyClass(proxy.getClass())) {
			InvocationHandler handler = Proxy.getInvocationHandler(proxy);
			if (handler instanceof ContextProjectAwareProxy) {
				return (T) ((ContextProjectAwareProxy) handler).getObject();
			}
		}
		return proxy;
	}
	
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
	private synchronized static <T> T makeInstance(T obj, ITigerstripeModelProject context) {
		Class<? extends Object> clazz = obj.getClass();
		Object unusedProxy = InstanceManager.findUnusedProxy(clazz);
		if (unusedProxy == null) {
			return (T) java.lang.reflect.Proxy.newProxyInstance(clazz
					.getClassLoader(), collectRequiredInterfaces(obj),
					new ContextProjectAwareProxy(obj, context));
		} else {
			ContextProjectAwareProxy h = (ContextProjectAwareProxy) Proxy.getInvocationHandler(unusedProxy);
			h.context = context;
			h.obj = obj;
			return (T) unusedProxy;
		}
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

	@SuppressWarnings("unchecked")
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
			} else if (isFilterMethod(m)) {
				Method notFilterMethod = getNotFilterMethod(m);
				Collection<ArtifactComponent> members = (Collection<ArtifactComponent>) notFilterMethod
						.invoke(getObject());
				members = (Collection<ArtifactComponent>) proxyResult(members);
				ArtifactManager artifactManager = ((AbstractArtifact) getObject())
						.getArtifactManager();
				return Collections
						.unmodifiableCollection(filterFacetExcludedComponents(
								new ContextualArtifactManager(artifactManager,
										context), members));
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
			throw new RuntimeException(e);
		}
		return result;
	}

	private static Collection<? extends Object> filterFacetExcludedComponents(ArtifactManager artifactManager,
			Collection<? extends Object> components) {
		ArrayList<Object> result = new ArrayList<Object>();
		for (Object component : components) {
			try {
				boolean isInActiveFacet = true;
				if (artifactManager != null
						&& artifactManager.getActiveFacet() != null) {
					IFacetReference ref = artifactManager.getActiveFacet();
					if (ref.getFacetPredicate() instanceof FacetPredicate) {
						FacetPredicate predicate = (FacetPredicate) ref
								.getFacetPredicate();
						isInActiveFacet = 
								   !predicate.isExcludedByStereotype((IStereotypeCapable) component)
								&& !predicate.isExcludedByAnnotation((IAnnotationCapable) component);
					}
				}

				if (isInActiveFacet)
					result.add(component);
			} catch (Exception e) {
				TigerstripeRuntime.logErrorMessage(
						"Error while evaluating isInActiveFacet for "
								+ component + ": "
								+ e.getMessage(), e);
			}
		}
		return result;
	}

	@SuppressWarnings("unused")
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
	
	private Map<Integer, Method> membersMethods = new HashMap<Integer, Method>();

	private boolean isFilterMethod(java.lang.reflect.Method method) {
		MemberAccess annotation = method.getAnnotation(MemberAccess.class);
		return annotation != null && annotation.filter();
	}

	private Method getNotFilterMethod(java.lang.reflect.Method method) {
		MemberAccess annotation = method.getAnnotation(MemberAccess.class);
		int type = annotation.type();
		Method notFilteredMethod = membersMethods.get(type);
		if (notFilteredMethod == null) {
			for (java.lang.reflect.Method m : method.getDeclaringClass()
					.getMethods()) {
				MemberAccess a = m.getAnnotation(MemberAccess.class);
				if (a != null) {
					if (a.type() == type && !a.filter())
						notFilteredMethod = m;
				}
			}
		}
		return notFilteredMethod;
	}

}
