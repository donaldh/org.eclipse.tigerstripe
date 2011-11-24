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
package org.eclipse.tigerstripe.workbench.ui.internal.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;

public class SearchResultTreeContentProvider implements ITreeContentProvider,
		ITigerstripeSearchContentProvider {

	private final Object[] EMPTY_ARR = new Object[0];

	private TigerstripeSearchResult fResult;
	private Map<Object, Set<Object>> fChildrenMap;
	private final TigerstripeSearchResultPage fPage;

	protected synchronized void initialize(TigerstripeSearchResult result) {
		fResult = result;
		fChildrenMap = new HashMap<Object, Set<Object>>();
		if (result != null) {
			Object[] elements = result.getElements();
			for (int i = 0; i < elements.length; i++) {
				insert(elements[i], false);
			}
		}
	}

	public Object[] getChildren(Object parentElement) {
		Set<Object> children = fChildrenMap.get(parentElement);
		if (children == null)
			return EMPTY_ARR;
		return children.toArray();
	}

	public Object getParent(Object element) {
		if (element instanceof IAbstractArtifact) {
			IResource res = (IResource) ((IAbstractArtifact) element)
					.getAdapter(IResource.class);
			if (res != null)
				return res.getParent();
			else {
				// since we don't have a resource, maybe it is because this is
				// coming from a module or from the Phantom Project
				IAbstractArtifactInternal art = (IAbstractArtifactInternal) element;
				ArtifactManager mgr = art.getArtifactManager();
				if (mgr instanceof ModuleArtifactManager) {
					ModuleArtifactManager mMgr = (ModuleArtifactManager) mgr;
					return mMgr;
				} else if (mgr.getTSProject() instanceof PhantomTigerstripeProject)
					return mgr;
			}
		} else if (element instanceof IField) {
			IField field = (IField) element;
			return field.getContainingArtifact();
		} else if (element instanceof IMethod) {
			IMethod method = (IMethod) element;
			return method.getContainingArtifact();
		} else if (element instanceof ILiteral) {
			ILiteral literal = (ILiteral) element;
			return literal.getContainingArtifact();
		} else if (element instanceof IAssociationEnd) {
			IAssociationEnd end = (IAssociationEnd) element;
			return end.getContainingArtifact();
		}

		if (element instanceof IProject)
			return null;
		if (element instanceof IResource) {
			IResource resource = (IResource) element;
			return resource.getParent();
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	private final TreeViewer fTreeViewer;

	public SearchResultTreeContentProvider(TigerstripeSearchResultPage page,
			TreeViewer viewer) {
		this.fPage = page;
		this.fTreeViewer = viewer;
	}

	public void clear() {
		initialize(fResult);
		fTreeViewer.refresh();
	}

	public Object[] getElements(Object inputElement) {
		Object[] children = getChildren(inputElement);
		int elementLimit = getElementLimit();
		if (elementLimit != -1 && elementLimit < children.length) {
			Object[] limitedChildren = new Object[elementLimit];
			System.arraycopy(children, 0, limitedChildren, 0, elementLimit);
			return limitedChildren;
		}
		return children;
	}

	private int getElementLimit() {
		return fPage.getElementLimit().intValue();
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof TigerstripeSearchResult) {
			initialize((TigerstripeSearchResult) newInput);
		}
	}

	public void elementsChanged(Object[] elements) {
		for (int i = 0; i < elements.length; i++) {
			if (fResult.getMatchCount(elements[i]) > 0)
				insert(elements[i], true);
			else
				remove(elements[i], true);
		}
	}

	protected void insert(Object child, boolean refreshViewer) {

		Object parent = getParent(child);
		while (parent != null) {
			if (insertChild(parent, child)) {
				if (refreshViewer)
					fTreeViewer.add(parent, child);
			} else {
				if (refreshViewer)
					fTreeViewer.refresh(parent);
				return;
			}
			child = parent;
			parent = getParent(child);
		}
		if (insertChild(fResult, child)) {
			if (refreshViewer)
				fTreeViewer.add(fResult, child);
		}
	}

	/**
	 * returns true if the child already was a child of parent.
	 * 
	 * @param parent
	 * @param child
	 * @return Returns <code>trye</code> if the child was added
	 */
	private boolean insertChild(Object parent, Object child) {
		Set<Object> children = fChildrenMap.get(parent);
		if (children == null) {
			children = new HashSet<Object>();
			fChildrenMap.put(parent, children);
		}
		return children.add(child);
	}

	protected void remove(Object element, boolean refreshViewer) {
		// precondition here: fResult.getMatchCount(child) <= 0

		if (hasChildren(element)) {
			if (refreshViewer)
				fTreeViewer.refresh(element);
		} else {
			if (fResult.getMatchCount(element) == 0) {
				fChildrenMap.remove(element);
				Object parent = getParent(element);
				if (parent != null) {
					removeFromSiblings(element, parent);
					remove(parent, refreshViewer);
				} else {
					removeFromSiblings(element, fResult);
					if (refreshViewer)
						fTreeViewer.refresh();
				}
			} else {
				if (refreshViewer) {
					fTreeViewer.refresh(element);
				}
			}
		}
	}

	private void removeFromSiblings(Object element, Object parent) {
		Set<Object> siblings = fChildrenMap.get(parent);
		if (siblings != null) {
			siblings.remove(element);
		}
	}

}
