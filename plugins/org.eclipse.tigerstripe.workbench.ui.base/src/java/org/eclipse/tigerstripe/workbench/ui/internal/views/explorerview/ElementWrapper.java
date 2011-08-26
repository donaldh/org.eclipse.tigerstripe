package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.tigerstripe.workbench.IElementWrapper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ElementWrapper implements IElementWrapper {

	private final Object element;

	private final ITigerstripeModelProject contextProject;

	public ElementWrapper(Object element,
			ITigerstripeModelProject contextProject) {
		this.element = element;
		this.contextProject = contextProject;
	}

	public Object getElement() {
		return element;
	}

	public ITigerstripeModelProject getContextProject() {
		return contextProject;
	}

	@Override
	public boolean equals(Object obj) {
		if (element instanceof IAbstractArtifact
				&& obj instanceof ElementWrapper) {
			ElementWrapper otherWrapper = (ElementWrapper) obj;
			if (otherWrapper.element instanceof IAbstractArtifact) {
				IAbstractArtifact otherElement = (IAbstractArtifact) otherWrapper.element;
				return ((IAbstractArtifact) element).getFullyQualifiedName()
						.equals(otherElement.getFullyQualifiedName());
			}

		}
		return element.equals(obj);
	}

	@Override
	public int hashCode() {
		if (element instanceof IAbstractArtifact) {
			return ((IAbstractArtifact) element).getFullyQualifiedName()
					.hashCode();
		}
		return element.hashCode();
	}
}
