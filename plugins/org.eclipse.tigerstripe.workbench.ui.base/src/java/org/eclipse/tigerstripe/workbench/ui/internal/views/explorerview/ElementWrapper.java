package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IElementWrapper;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

public class ElementWrapper implements IElementWrapper {

	private final Object element;

	private URI uri;

	private final ITigerstripeModelProject contextProject;

	public ElementWrapper(Object element,
			ITigerstripeModelProject contextProject) {
		this.contextProject = contextProject;
		this.element = element;
		if (element instanceof IModelComponent) {
			try {
				uri = TigerstripeURIAdapterFactory
						.toURI((IModelComponent) element);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	public Object getElement() {
		return element;
	}

	public ITigerstripeModelProject getContextProject() {
		return contextProject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contextProject == null) ? 0 : contextProject.hashCode());
		if (uri != null) {
			result = prime * result + uri.hashCode();
		} else {
			result = prime * result
					+ ((element == null) ? 0 : element.hashCode());
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementWrapper other = (ElementWrapper) obj;
		if (contextProject == null) {
			if (other.contextProject != null)
				return false;
		} else if (!contextProject.equals(other.contextProject))
			return false;

		if (uri != null) {
			if (other.uri == null)
					return false;
			else if (!uri.equals(other.uri))
				return false;
		} else {
			if (element == null) {
				if (other.element != null)
					return false;
			} else if (!element.equals(other.element))
				return false;
		}

		return true;
	}
}
