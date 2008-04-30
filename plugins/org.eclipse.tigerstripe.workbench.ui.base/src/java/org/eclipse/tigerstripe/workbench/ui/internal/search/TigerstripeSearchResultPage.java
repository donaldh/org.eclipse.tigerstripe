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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.TSOpenAction;
import org.eclipse.ui.part.IShowInTargetList;

public class TigerstripeSearchResultPage extends AbstractTextSearchViewPage
		implements ISearchResultPage, IAdaptable {

	private ITigerstripeSearchContentProvider fContentProvider;
	private static final int DEFAULT_ELEMENT_LIMIT = 1000;

	private static final String[] SHOW_IN_TARGETS = new String[] { TigerstripeExplorerPart.AEXPLORER_ID };
	public static final IShowInTargetList SHOW_IN_TARGET_LIST = new IShowInTargetList() {
		public String[] getShowInTargetIds() {
			return SHOW_IN_TARGETS;
		}
	};

	public TigerstripeSearchResultPage() {
		super();
		setElementLimit(new Integer(DEFAULT_ELEMENT_LIMIT));
	}

	public TigerstripeSearchResultPage(int supportedLayouts) {
		super(supportedLayouts);
		setElementLimit(new Integer(DEFAULT_ELEMENT_LIMIT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (IShowInTargetList.class.equals(adapter))
			return SHOW_IN_TARGET_LIST;
		return null;
	}

	@Override
	protected void handleOpen(OpenEvent event) {
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) event
					.getSelection();
			TSOpenAction action = new TSOpenAction(getSite());
			action.selectionChanged(ssel);
			action.run();
		}
	}

	@Override
	protected void clear() {
		if (fContentProvider != null)
			fContentProvider.clear();
	}

	@Override
	protected void configureTableViewer(TableViewer viewer) {
		viewer.setUseHashlookup(true);
		viewer.setLabelProvider(new SearchResultLabelProvider(true));
		viewer.setContentProvider(new SearchResultTableContentProvider(this,
				viewer));
		fContentProvider = (ITigerstripeSearchContentProvider) viewer
				.getContentProvider();
	}

	@Override
	protected void configureTreeViewer(TreeViewer viewer) {
		viewer.setUseHashlookup(true);
		viewer.setLabelProvider(new SearchResultLabelProvider());
		viewer.setContentProvider(new SearchResultTreeContentProvider(this,
				viewer));
		fContentProvider = (ITigerstripeSearchContentProvider) viewer
				.getContentProvider();
	}

	@Override
	protected void elementsChanged(Object[] objects) {
		if (fContentProvider != null)
			fContentProvider.elementsChanged(objects);

	}

}
