package org.eclipse.tigerstripe.workbench;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;


public interface IElementWrapper {

	public Object getElement();

	public ITigerstripeModelProject getContextProject();
}
