package org.eclipse.tigerstripe.workbench;

import org.eclipse.jdt.core.IJavaProject;

public interface IModuleElementWrapper extends IElementWrapper {

	public IJavaProject getParent();

	public boolean isClassFile();
}
