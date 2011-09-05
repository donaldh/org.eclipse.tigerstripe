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
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringDelegate;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * This listener propagates model changes to the annotation framework as needed
 * to ensure that the annotations "travel" with the artifacts and model
 * components as needed
 * 
 * @author erdillon
 * 
 */
public class TigerstripeRefactoringSupport implements
		IRefactoringChangesListener {

	private Map<ILazyObject, TigerstripeChanges> changes = new HashMap<ILazyObject, TigerstripeChanges>();

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.jdt.refactoring.
	 * IRefactoringChangesListener#changed(org.eclipse.core.runtime.IPath,
	 * org.eclipse.core.runtime.IPath, int)
	 */
	public void changed(IRefactoringDelegate delegate, ILazyObject oldObject,
			ILazyObject newObject, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			IModelComponent element = getModelComponent(oldObject);
			if (element != null) {
				try {
					TigerstripeChanges change = new TigerstripeChanges(element);
					changes.put(oldObject, change);
				} catch (TigerstripeException e) {
					// Do nothing
				}
			}
		} else if (kind == CHANGED) {
			TigerstripeChanges change = changes.remove(oldObject);
			if (change != null) {
				IModelComponent newElement = getModelComponent(newObject);
				if (newElement != null) {
					changed(delegate, change.getChanges(newElement));
				}
			}
		}
	}

	public void moved(IRefactoringDelegate delegate, ILazyObject[] objects,
			ILazyObject destination, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			for (int i = 0; i < objects.length; i++) {
				IModelComponent element = getModelComponent(objects[i]);
				if (element != null) {
					try {
						TigerstripeChanges change = new TigerstripeChanges(
								element);
						changes.put(objects[i], change);
					} catch (TigerstripeException e) {
						// Do nothing
					}
				}
			}
		} else if (kind == CHANGED) {
			Map<URI, URI> allChanges = new HashMap<URI, URI>();
			IModelComponent destElement = getModelComponent(destination);
			if (destElement != null) {
				String destFqn = RefactoringUtil.extractFqn(destElement);
				for (int i = 0; i < objects.length; i++) {
					TigerstripeChanges change = changes.remove(objects[i]);
					if (destElement != null && change != null) {
						String name = change.getOldName();
						String newFqn = destFqn + "." + name;
						try {
							ITigerstripeModelProject project = destElement
									.getProject();
							IAbstractArtifact newElement = project
									.getArtifactManagerSession()
									.getArtifactByFullyQualifiedName(newFqn);
							if (newElement != null) {
								allChanges
										.putAll(change.getChanges(newElement));
							}
						} catch (TigerstripeException e) {
							// Do nothing
						}
					}
				}
			}
			changed(delegate, allChanges);
		}
	}

	public void copied(IRefactoringDelegate delegate, ILazyObject[] objects,
			ILazyObject destination, Map<ILazyObject, String> newNames, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			for (int i = 0; i < objects.length; i++) {
				IModelComponent element = getModelComponent(objects[i]);
				if (element != null) {
					try {
						TigerstripeChanges change = new TigerstripeChanges(
								element);
						changes.put(objects[i], change);
					} catch (TigerstripeException e) {
						// Do nothing
					}
				}
			}
		} else if (kind == CHANGED) {
			Map<URI, URI> allChanges = new HashMap<URI, URI>();
			for (int i = 0; i < objects.length; i++) {
				TigerstripeChanges change = changes.remove(objects[i]);
				IResource destResource = (IResource) Platform
						.getAdapterManager().getAdapter(
								destination.getObject(), IResource.class);
				if (destResource != null && destResource instanceof IContainer) {
					IContainer destContainer = (IContainer) destResource;
					IResource resource = destContainer.findMember(newNames
							.get(objects[i]));
					if (resource != null) {
						IModelComponent newElement = (IModelComponent) Platform
								.getAdapterManager().getAdapter(resource,
										IModelComponent.class);
						if (change != null && newElement != null) {
							allChanges.putAll(change.getChanges(newElement));
						}
					}
				}
			}
			copied(delegate, allChanges);
		}
	}

	private void changed(IRefactoringDelegate delegate, Map<URI, URI> uris) {
		// inform annotation framework about changes
		if (uris.size() == 0)
			return;
		for (URI uri : uris.keySet())
			delegate.changed(uri, uris.get(uri), true);
	}

	private void copied(IRefactoringDelegate delegate, Map<URI, URI> uris) {
		if (uris.size() == 0)
			return;
		for (Entry<URI, URI> entry : uris.entrySet()) {
			URI fromUri = entry.getKey();
			URI toUri = entry.getValue();
			delegate.copied(fromUri, toUri, true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.jdt.refactoring.
	 * IRefactoringChangesListener#deleted(org.eclipse.core.runtime.IPath)
	 */
	public void deleted(IRefactoringDelegate delegate, ILazyObject object) {
		IModelComponent element = getModelComponent(object);
		if (element != null) {
			IResource resource = getResource(object);
			if (resource != null
					&& resource.equals((IResource) element
							.getAdapter(IResource.class))) {
				Set<URI> set = new HashSet<URI>();
				RefactoringUtil.collectAllUris(element, set);
				for (URI uri : set) {
					delegate.deleted(uri, true);
				}
			}
		}
	}

	protected IModelComponent getModelComponent(ILazyObject object) {
		Object obj = object.getObject();
		if (obj == null)
			return null;
		IModelComponent element = null;
		if (obj instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) obj;
			element = (IModelComponent) adaptable
					.getAdapter(IModelComponent.class);
		}
		if (element == null) {
			element = (IModelComponent) Platform.getAdapterManager()
					.getAdapter(obj, IModelComponent.class);
		}
		return element;
	}

	protected IResource getResource(ILazyObject object) {
		Object obj = object.getObject();
		if (obj == null)
			return null;
		IResource resource = null;
		if (obj instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) obj;
			resource = (IResource) adaptable.getAdapter(IResource.class);
		}
		if (resource == null) {
			resource = (IResource) Platform.getAdapterManager().getAdapter(obj,
					IResource.class);
		}
		return resource;
	}
}