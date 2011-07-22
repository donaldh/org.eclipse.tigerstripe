package org.eclipse.tigerstripe.workbench.ui.dependencies.utils;

import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;

public abstract class AbstractSubject implements IDependencySubject {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IDependencySubject) {
			return getId().equals(((IDependencySubject) obj).getId());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
