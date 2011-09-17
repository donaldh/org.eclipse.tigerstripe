package org.eclipse.tigerstripe.espace.resources;

import org.eclipse.core.resources.IResourceChangeListener;

public interface IResourceChangeListenerProvider {

	IResourceChangeListener get();
	
}
