package org.eclipse.tigerstripe.annotation.internal.core;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.tigerstripe.espace.resources.IResourceChangeListenerProvider;
import org.eclipse.tigerstripe.espace.resources.core.EMFDatabase;

public class EMFDatabaseHolder implements IResourceChangeListenerProvider {

	public static final EMFDatabase INSTANCE = new EMFDatabase();
	
	public IResourceChangeListener get() {
		return INSTANCE;
	}
}
