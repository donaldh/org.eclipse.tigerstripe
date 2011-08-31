package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ContextProjectAwareProxy implements
		java.lang.reflect.InvocationHandler {

	private final Object obj;
	private final ITigerstripeModelProject context;

	public static Object newInstance(Object obj,
			ITigerstripeModelProject context) {
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
			} else {
				result = m.invoke(obj, args);
				if (result != null) {
					if (IAssociationArtifact.class
							.equals(m.getDeclaringClass())) {
						if ("getAEnd".equals(m.getName())
								|| "getZEnd".equals(m.getName())) {
							result = newInstance(result, context);
						} else if ("getAssociationEnds".equals(m.getName())) {
							result = processResult(result);
						}
					} else if (IAbstractArtifact.class.equals(m
							.getDeclaringClass())) {
						if ("getLiterals".equals(m.getName())
								|| "getFields".equals(m.getName())
								|| "getMethods".equals(m.getName())) {
							result = processResult(result);
						}

						if (result != null
								&& "getExtendedArtifact".equals(m.getName())) {
							result = newInstance(result, context);
						}
					} else if ((ILiteral.class.equals(m.getDeclaringClass())
							|| IField.class.equals(m.getDeclaringClass()) || IMethod.class
							.equals(m.getDeclaringClass()))
							&& "getContainingArtifact".equals(m.getName())) {
						result = newInstance(result, context);
					}
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

	private Object processResult(Object result) {
		if (result instanceof Collection<?>
				&& !((Collection<?>) result).isEmpty()) {
			return processCollection((Collection<?>) result);
		}
		return result;
	}

	private <T> Collection<T> processCollection(Collection<T> collection) {
		List<T> newCollection = new ArrayList<T>(collection.size());
		for (T element : collection) {
			newCollection.add((T) newInstance(element, context));
		}
		return newCollection;
	}
}
