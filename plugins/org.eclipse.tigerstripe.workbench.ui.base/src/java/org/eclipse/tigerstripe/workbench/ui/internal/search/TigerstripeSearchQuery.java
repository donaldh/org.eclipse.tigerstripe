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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.search.internal.ui.text.SearchResultUpdater;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.tigerstripe.workbench.ui.internal.search.TigerstripeSearchPage.SearchPatternData;

public class TigerstripeSearchQuery implements ISearchQuery {

	private SearchPatternData data;
	private TigerstripeSearchResult fResult;

	public TigerstripeSearchQuery(TigerstripeSearchPage.SearchPatternData data) {
		this.data = data;
	}

	public SearchPatternData getSearchPatternData() {
		return data;
	}

	public boolean canRerun() {
		return false;
	}

	public boolean canRunInBackground() {
		return true;
	}

	public String getLabel() {
		return "Tigerstripe Search";
	}

	public ISearchResult getSearchResult() {
		if (fResult == null) {
			fResult = new TigerstripeSearchResult(this);
			new SearchResultUpdater(fResult);
		}
		return fResult;
	}

	public IStatus run(IProgressMonitor monitor)
			throws OperationCanceledException {
		TigerstripeSearchResult tsResult = (TigerstripeSearchResult) getSearchResult();
		tsResult.removeAll();

		TigerstripeSearchEngine engine = TigerstripeSearchEngine.getInstance();
		TigerstripeSearchResultCollector collector = new TigerstripeSearchResultCollector(
				tsResult);
		return engine.search(data, collector, monitor);
	}

	public String getResultLabel() {
		TigerstripeSearchResult result = (TigerstripeSearchResult) getSearchResult();

		String label = "'" + getSearchPatternData().getSearchPattern() + "' - "
				+ result.getMatchCount() + " ";
		if (result.getMatchCount() > 1)
			label = label + "matches.";
		else
			label = label + "match.";
		return label;
	}
}
