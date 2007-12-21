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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.ui.IContributorResourceAdapter;

/**
 * This class is the top level class for all abstract nodes specific to the
 * Tigerstripe Explorer.
 * 
 * Abstract nodes present a logical view of files on the file system within a
 * project. E.g. diagrams are grouped as a single node as opposed to 2 files.
 * 
 * Logical nodes are created from a key/trigger file (e.g. the diagam file for
 * GMF diagrm) and may contain a set of filtered resources that should not be
 * displayed by the Explorer (e.g. the model file for the GMF diagram)
 * 
 * @author Eric Dillon
 * 
 */
public abstract class AbstractLogicalExplorerNode extends ResourceMapping {

	/**
	 * Gets the list of underlying resources logically grouped by this node
	 * 
	 * @return
	 */
	public abstract IResource[] getUnderlyingResources();

	/**
	 * Returns the base label for this logical node
	 * 
	 * @return
	 */
	public abstract String getText();

	/**
	 * Returns the image to use for this logical node
	 * 
	 * @return
	 */
	public abstract Image getImage();

	/**
	 * Whether the passed resource should be considered the key resource for
	 * this logical node
	 * 
	 * @param resource
	 * @return
	 */
	public abstract boolean isKeyResource(IResource resource);

	/**
	 * Whether the given resource should be filtered out in the explorer because
	 * it is an underlying resource for this logical node
	 * 
	 * @param resource
	 * @return
	 */
	public abstract boolean shouldFilterResource(IResource resource);

	/**
	 * Returns the key resource for this logical node
	 * 
	 * @return
	 */
	public abstract IResource getKeyResource();

	/**
	 * Creates an instance of this
	 * 
	 * @param resource
	 * @return
	 */
	public abstract AbstractLogicalExplorerNode makeInstance(IResource resource);

	/**
	 * Return the identifier for the editor to use when opening this logical
	 * node
	 * 
	 * @return
	 */
	public abstract String getEditor();

	/**
	 * Whether this node can be renamed (i.e. will a rename action be displayed)
	 * in the explorer.
	 * 
	 * @return
	 */
	public boolean canBeRenamed() {
		return false;
	}

	/**
	 * This method is called to perform the actual rename action
	 * 
	 * @param newName -
	 *            the new name for the node
	 */
	public abstract void performRename(String newName, IProgressMonitor monitor)
			throws CoreException, TigerstripeException;

	public boolean canBeMoved() {
		return false;
	}

	/**
	 * This method is called to perform the actual move action
	 * 
	 */
	public abstract void performMove(IContainer targetLocation,
			IProgressMonitor monitor) throws CoreException,
			TigerstripeException;

	@Override
	public Object getModelObject() {
		return getKeyResource();
	}

	@Override
	public String getModelProviderId() {
		return LogicalExplorerNodeModelProvider.ID;
	}

	@Override
	public IProject[] getProjects() {
		return new IProject[] { getKeyResource().getProject() };
	}

	@Override
	public ResourceTraversal[] getTraversals(ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException {
		return new ResourceTraversal[] { new ResourceTraversal(
				getUnderlyingResources(), IResource.DEPTH_INFINITE,
				IResource.NONE) };
	}

	@Override
	public Object getAdapter(Class adapter) {
		// if ( adapter == IResource.class ) {
		// return getKeyResource();
		// } else
		if (adapter == IContributorResourceAdapter.class)
			return new IContributorResourceAdapter() {
				public IResource getAdaptedResource(IAdaptable adaptable) {
					if (adaptable instanceof AbstractLogicalExplorerNode) {
						AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) adaptable;
						return node.getKeyResource();
					}
					return null;
				}

			};
		// TODO Auto-generated method stub
		return super.getAdapter(adapter);
	}

	public IResource getMostRecentlyTouchedResource() {
		IResource mostRecentlyTouched = null;
		IResource[] allRes = getUnderlyingResources();
		for (IResource res : allRes) {
			if (mostRecentlyTouched == null) {
				mostRecentlyTouched = res;
				continue;
			} else if (res.getLocalTimeStamp() > mostRecentlyTouched
					.getLocalTimeStamp()) {
				mostRecentlyTouched = res;
			}
		}
		return mostRecentlyTouched;
	}
}
