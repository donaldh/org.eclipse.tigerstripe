package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

/**
 * The class is used for caching and reusing artifact proxy instances, 
 * because creation of proxy is expensive procedure.
 */
public class InstanceManager {

	private static final Map<Class<?>, Deque<Object>> unusedProxyInstancies = new HashMap<Class<?>, Deque<Object>>();

	
	public static synchronized void dispose(IModelComponent component) {
		if (component == null) {
			return;
		}
		if (Proxy.isProxyClass(component.getClass())) {
			InvocationHandler handler = Proxy.getInvocationHandler(component);
			if (handler instanceof ContextProjectAwareProxy) {
				ContextProjectAwareProxy ctxHandler = (ContextProjectAwareProxy) handler;
				if (ctxHandler.obj == null) {
					return;
				}
				Class<?> clazz = ctxHandler.obj.getClass();
				Deque<Object> proxies = unusedProxyInstancies.get(clazz);
				ctxHandler.context = null;
				ctxHandler.obj = null;
				if (proxies == null) {
					proxies = new LinkedList<Object>();
					unusedProxyInstancies.put(clazz, proxies);
				}
				proxies.addLast(component);
			}
		}
	}
	
	public static synchronized void dispose(Collection<? extends IModelComponent> components) {
		for (IModelComponent component : components) {
			dispose(component);
		}
	}

	public static synchronized Object findUnusedProxy(Object forClass) {
		Deque<Object> proxies = unusedProxyInstancies.get(forClass);
		if (proxies == null || proxies.isEmpty()) {
			return null;
		}
		return proxies.removeFirst();
	}
	
}
