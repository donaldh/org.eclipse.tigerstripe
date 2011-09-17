package org.eclipse.tigerstripe.espace.resources.internal.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.espace.resources.IResourceChangeListenerProvider;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;

public class ResourceChangeListenersLoader {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<IResourceChangeListener> load() {

		final IConfigurationElement[] elements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"org.eclipse.tigerstripe.espace.resources.listener");

		TreeMap<Integer, Object> listeners = new TreeMap<Integer, Object>();
		
		for (IConfigurationElement e : elements) {
			IResourceChangeListener listener;
			try {
				IResourceChangeListenerProvider prov = (IResourceChangeListenerProvider) e.createExecutableExtension("class");
				listener = prov.get();
			} catch (CoreException ex) {
				ResourcesPlugin.log(ex);
				continue;
			}
			Integer order = Integer.valueOf(e.getAttribute("order"));
			Object prev = listeners.get(order);
			if (prev == null) {
				listeners.put(order, listener);
			} else if (prev instanceof IResourceChangeListener) {
				HashSet<IResourceChangeListener> set = new HashSet<IResourceChangeListener>();
				set.add((IResourceChangeListener) prev);
				set.add(listener);
				listeners.put(order, set);
			} else if (prev instanceof Set) {
				((Set) prev).add(listener);
			}
		}
		List<IResourceChangeListener> result = new ArrayList<IResourceChangeListener>(elements.length);
		for (Object obj : listeners.values()) {
			if (obj instanceof IResourceChangeListener) {
				result.add((IResourceChangeListener) obj);
			} else if (obj instanceof Set) {
				result.addAll((Set) obj);
			}
		}
		return result;
	}
}
