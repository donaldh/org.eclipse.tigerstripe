/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.model;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

/**
 * A Listener that will be notified of changes on artifacts
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface IArtifactChangeListener {

	// Various masks to be set thru the IArtifactManagerSession that will
	// condition
	// what the underlying manager will broadcast or not
	public static final int NOTIFY_NONE = 0x0;

	public static final int NOTIFY_REMOVED = 0x1;

	public static final int NOTIFY_ADDED = 0x1 << 2;

	public static final int NOTIFY_CHANGED = 0x1 << 4;

	public static final int NOTIFY_RENAMED = 0x1 << 6;

	public static final int NOTIFY_RELOADED = 0x1 << 8;

	public static final int NOTIFY_ALL = NOTIFY_ADDED | NOTIFY_REMOVED
			| NOTIFY_CHANGED | NOTIFY_RENAMED | NOTIFY_RELOADED;

	/**
	 * Called right before an artifact is removed so that a listener can cope
	 * with the deletion
	 * 
	 * @param artifact
	 */
	public void artifactRemoved(IAbstractArtifact artifact);

	/**
	 * Called right after a new artifact has been added
	 * 
	 * @param artifact
	 */
	public void artifactAdded(IAbstractArtifact artifact);

	public void artifactChanged(IAbstractArtifact artifact, IAbstractArtifact oldArtifact);

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN);

	public void managerReloaded();
}
