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
package org.eclipse.tigerstripe.workbench.ui.eclipse.search;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * @since 2.2.4
 */
public class SearchResultTableContentProvider implements
		IStructuredContentProvider, ITigerstripeSearchContentProvider {

	private final Object[] EMPTY_ARR = new Object[0];
	private TigerstripeSearchResult fResult;
	private TableViewer viewer;
	private TigerstripeSearchResultPage fPage;

	public void clear() {
		viewer.refresh();
	}

	public void elementsChanged(Object[] elements) {
		int elementLimit = getElementLimit();
		boolean tableLimited = elementLimit != -1;
		for (int i = 0; i < elements.length; i++) {
			if (fResult.getMatchCount(elements[i]) > 0) {
				if (viewer.testFindItem(elements[i]) != null)
					viewer.update(elements[i], null);
				else {
					if (!tableLimited
							|| viewer.getTable().getItemCount() < elementLimit)
						viewer.add(elements[i]);
				}
			} else
				viewer.remove(elements[i]);
		}
	}

	public SearchResultTableContentProvider(TigerstripeSearchResultPage page,
			TableViewer viewer) {
		this.viewer = viewer;
		this.fPage = page;
	}

	protected synchronized void initialize(TigerstripeSearchResult result) {
		fResult = result;
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof TigerstripeSearchResult) {
			int elementLimit = getElementLimit();
			Object[] elements = ((TigerstripeSearchResult) inputElement)
					.getElements();
			if (elementLimit != -1 && elements.length > elementLimit) {
				Object[] shownElements = new Object[elementLimit];
				System.arraycopy(elements, 0, shownElements, 0, elementLimit);
				return shownElements;
			}
			return elements;
		}
		return EMPTY_ARR;
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

}
