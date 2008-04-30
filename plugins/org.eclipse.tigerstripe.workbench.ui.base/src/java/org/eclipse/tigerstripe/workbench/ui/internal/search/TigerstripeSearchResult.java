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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;

public class TigerstripeSearchResult extends AbstractTextSearchResult implements
		ISearchResult, IFileMatchAdapter {

	private TigerstripeSearchQuery query;

	public Match[] computeContainedMatches(AbstractTextSearchResult result,
			IFile file) {
		IAbstractArtifact artifact = TSExplorerUtils.getArtifactFor(file);
		if (artifact != null)
			return getMatches(artifact);
		return new Match[0];
	}

	public IFile getFile(Object element) {
		if (element instanceof IFile)
			return (IFile) element;
		else if (element instanceof IModelComponent) {
			if (element instanceof IAbstractArtifact) {
				return (IFile) ((IAbstractArtifact) element)
						.getAdapter(IResource.class);
			}
		}
		return null;
	}

	public TigerstripeSearchResult(TigerstripeSearchQuery query) {
		this.query = query;
	}

	@Override
	public IEditorMatchAdapter getEditorMatchAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFileMatchAdapter getFileMatchAdapter() {
		// TODO Auto-generated method stub
		return this;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLabel() {
		return query.getResultLabel();
	}

	public ISearchQuery getQuery() {
		return query;
	}

	public String getTooltip() {
		// TODO Auto-generated method stub
		return getLabel();
	}

}
