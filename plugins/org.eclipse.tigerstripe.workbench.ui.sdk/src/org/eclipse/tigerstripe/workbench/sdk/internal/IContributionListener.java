package org.eclipse.tigerstripe.workbench.sdk.internal;

import org.eclipse.core.resources.IResourceChangeEvent;

public interface IContributionListener {

	public void resourceChanged(IResourceChangeEvent event);
	
}
