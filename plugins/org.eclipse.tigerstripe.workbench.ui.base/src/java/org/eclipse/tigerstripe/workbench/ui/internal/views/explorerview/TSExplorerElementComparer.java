package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import java.lang.reflect.Proxy;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;

public class TSExplorerElementComparer implements IElementComparer {

	public int hashCode(Object element) {
		// Bug 300041 Method has overloaded method hashCode that causes
		// improper TSExplorer refresh for artifact methods. In order to have
		// proper TS Explorer refresh we need to return hashCode as it was
		// defined in ArtifactComponent.
		if (element instanceof Method) {
			final int prime = 31;
			int result = 1;
			String name = ((Method) element).getName();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		return element.hashCode();
	}

	public boolean equals(Object a, Object b) {
		// Bug 300041 Method has overloaded method equals that causes
		// improper TSExplorer refresh for artifact methods. In order to have
		// proper TS Explorer refresh we need to perform equals as it was
		// defined in ArtifactComponent.

		if (a == b) {
			return true;
		}

		if (a instanceof IMethod && b instanceof IMethod) {
			if (Proxy.isProxyClass(a.getClass())) {
				return a.equals(b);
			} else if (Proxy.isProxyClass(b.getClass())) {
				return b.equals(a);
			}

			URI uriA = (URI) ((IMethod) a).getAdapter(URI.class);
			URI uriB = (URI) ((IMethod) b).getAdapter(URI.class);

			return uriA == null ? false : uriA.equals(uriB);
		}
		return a.equals(b);
	}

}
