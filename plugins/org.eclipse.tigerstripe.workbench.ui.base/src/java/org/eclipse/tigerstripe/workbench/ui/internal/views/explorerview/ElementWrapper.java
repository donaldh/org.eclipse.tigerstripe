package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.tigerstripe.workbench.IElementWrapper;

public class ElementWrapper implements IElementWrapper {

	private final Object element;

	public ElementWrapper(Object element) {
		this.element = element;
	}

	public Object getElement() {
		return element;
	}
}
