package org.eclipse.tigerstripe.espace.resources.monitor;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.tigerstripe.espace.resources.IResourceChangeListenerProvider;

public class RsourceMonitorListenerProvider implements IResourceChangeListenerProvider {

	public IResourceChangeListener get() {
		return ResourcesMonitor.getInstance();
	}

}
