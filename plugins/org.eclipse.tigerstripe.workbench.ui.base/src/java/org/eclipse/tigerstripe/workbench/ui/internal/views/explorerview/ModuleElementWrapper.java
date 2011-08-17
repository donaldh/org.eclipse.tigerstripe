package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.workbench.IModuleElementWrapper;

public class ModuleElementWrapper extends ElementWrapper implements
		IModuleElementWrapper {
	private IJavaProject parent;

	public ModuleElementWrapper(Object object, IJavaProject realParent) {
		super(object);
		this.parent = realParent;
	}

	public IJavaProject getParent() {
		return parent;
	}

	public boolean isClassFile() {
		return getElement() instanceof IClassFile;
	}
}
