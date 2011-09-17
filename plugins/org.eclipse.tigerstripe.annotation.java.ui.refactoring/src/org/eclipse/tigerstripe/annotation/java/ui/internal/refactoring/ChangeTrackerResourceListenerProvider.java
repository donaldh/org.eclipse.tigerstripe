package org.eclipse.tigerstripe.annotation.java.ui.internal.refactoring;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.tigerstripe.espace.resources.IResourceChangeListenerProvider;

public class ChangeTrackerResourceListenerProvider implements IResourceChangeListenerProvider {

	public IResourceChangeListener get() {
		return ChangesTracker.getInstance();
	}
}
