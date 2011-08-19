package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.tigerstripe.workbench.IReferencedProjectElementWrapper;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ReferencedProjectElementWrapper extends ElementWrapper implements
		IReferencedProjectElementWrapper {
	private final ITigerstripeModelProject parent;

	public ReferencedProjectElementWrapper(Object object,
			ITigerstripeModelProject realParent) {
		super(object);
		this.parent = realParent;
	}

	public ITigerstripeModelProject getParent() {
		return parent;
	}
}
