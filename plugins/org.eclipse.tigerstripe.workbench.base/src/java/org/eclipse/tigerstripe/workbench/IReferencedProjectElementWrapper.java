package org.eclipse.tigerstripe.workbench;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IReferencedProjectElementWrapper extends IElementWrapper {

	public ITigerstripeModelProject getParent();

}
