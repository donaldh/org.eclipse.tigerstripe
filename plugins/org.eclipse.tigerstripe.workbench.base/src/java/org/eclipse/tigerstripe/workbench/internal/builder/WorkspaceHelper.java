/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;

/**
 * A convenience class to handle Tigerstripe related content from workspace
 * changes
 * 
 * @author erdillon
 * 
 */
public class WorkspaceHelper {

	/**
	 * Splits the content of the given resource delta into 3 collections.
	 * 
	 * @param delta
	 * @param itemsToRemove
	 * @param itemsChanged
	 * @param itemsAdded
	 */
	public static void buildResourcesLists(IResourceDelta delta,
			Collection<IResource> itemsRemoved,
			Collection<IResource> itemsChanged, Collection<IResource> itemsAdded) {
		if (delta == null) // occurs when deleting project
			return;

		if (delta.getKind() == IResourceDelta.REMOVED) {
			itemsRemoved.add(delta.getResource());
		} else if (delta.getKind() == IResourceDelta.CHANGED
				&& delta.getAffectedChildren().length == 0) {
			itemsChanged.add(delta.getResource());
		} else if (delta.getKind() == IResourceDelta.ADDED
				&& delta.getAffectedChildren().length == 0) {
			itemsAdded.add(delta.getResource());
		} else if (delta.getKind() == IResourceDelta.ADDED
				&& delta.getResource() instanceof IProject) {
			itemsAdded.add(delta.getResource());
		}
		IResourceDelta[] children = delta.getAffectedChildren();
		for (int i = 0; i < children.length; i++) {
			buildResourcesLists(children[i], itemsRemoved, itemsChanged,
					itemsAdded);
		}
	}

}
