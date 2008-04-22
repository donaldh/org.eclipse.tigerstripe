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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.ui.internal.search.TigerstripeSearchPage.SearchPatternData;

public class TigerstripeSearchResultCollector {

	private TigerstripeSearchResult result;
	private TigerstripeSearchQuery query;

	public TigerstripeSearchResultCollector(TigerstripeSearchResult result) {
		this.result = result;
		query = (TigerstripeSearchQuery) result.getQuery();
	}

	private SearchPatternData getData() {
		return query.getSearchPatternData();
	}

	public IStatus acceptModelComponent(IModelComponent component) {
		if (component instanceof IAbstractArtifact)
			return acceptArtifact((IAbstractArtifact) component);
		else if (component instanceof IField)
			return acceptField((IField) component);
		else if (component instanceof IAssociationEnd)
			return acceptAssociationEnd((IAssociationEnd) component);
		else if (component instanceof IMethod)
			return acceptMethod((IMethod) component);
		else if (component instanceof ILiteral)
			return acceptLabel((ILiteral) component);
		return Status.CANCEL_STATUS;
	}

	private IStatus acceptArtifact(IAbstractArtifact artifact) {
		SearchPatternData data = getData();
		if (data.getSearchFor() == ITigerstripeSearchConstants.TYPE
				&& data.matchPattern(artifact.getFullyQualifiedName())) {
			if (data.getLimitTo() == ITigerstripeSearchConstants.DECLARATIONS
					|| data.getLimitTo() == ITigerstripeSearchConstants.ALL_OCCURRENCES)
				result.addMatch(new ArtifactMatch(artifact, 0, 0,
						ArtifactMatch.DECLARATION_MATCH));
		}
		return org.eclipse.core.runtime.Status.OK_STATUS;
	}

	private IStatus acceptField(IField field) {
		SearchPatternData data = getData();
		String fieldType = field.getType().getFullyQualifiedName();

		if (data.getSearchFor() == ITigerstripeSearchConstants.TYPE
				&& data.matchPattern(fieldType)) {
			if (data.getLimitTo() == ITigerstripeSearchConstants.REFERENCES
					|| data.getLimitTo() == ITigerstripeSearchConstants.ALL_OCCURRENCES)
				result.addMatch(new ArtifactMatch(field, 0, 0,
						ArtifactMatch.REFERENCE_MATCH));
		} else if (data.getSearchFor() == ITigerstripeSearchConstants.FIELD
				&& data.matchPattern(field.getName())) {
			result.addMatch(new ArtifactMatch(field, 0, 0,
					ArtifactMatch.REFERENCE_MATCH));
		}
		return org.eclipse.core.runtime.Status.OK_STATUS;
	}

	private IStatus acceptLabel(ILiteral literal) {
		SearchPatternData data = getData();
		String labelType = literal.getType().getFullyQualifiedName();
		if (data.getSearchFor() == ITigerstripeSearchConstants.TYPE
				&& data.matchPattern(labelType)) {
			if (data.getLimitTo() == ITigerstripeSearchConstants.REFERENCES
					|| data.getLimitTo() == ITigerstripeSearchConstants.ALL_OCCURRENCES)
				result.addMatch(new ArtifactMatch(literal, 0, 0,
						ArtifactMatch.REFERENCE_MATCH));
		} else if (data.getSearchFor() == ITigerstripeSearchConstants.LITERAL
				&& data.matchPattern(literal.getName())) {
			result.addMatch(new ArtifactMatch(literal, 0, 0,
					ArtifactMatch.REFERENCE_MATCH));
		}
		return org.eclipse.core.runtime.Status.OK_STATUS;
	}

	private IStatus acceptMethod(IMethod method) {
		SearchPatternData data = getData();
		String methodType = method.getReturnType().getFullyQualifiedName();
		if (data.getSearchFor() == ITigerstripeSearchConstants.TYPE) {
			if (data.matchPattern(methodType)) {
				if (data.getLimitTo() == ITigerstripeSearchConstants.REFERENCES
						|| data.getLimitTo() == ITigerstripeSearchConstants.ALL_OCCURRENCES)
					result.addMatch(new ArtifactMatch(method, 0, 0,
							ArtifactMatch.REFERENCE_MATCH));
			}

			for (IArgument arg : method.getArguments()) {
				String argType = arg.getType().getFullyQualifiedName();
				if (data.matchPattern(argType)) {
					if (data.getLimitTo() == ITigerstripeSearchConstants.REFERENCES
							|| data.getLimitTo() == ITigerstripeSearchConstants.ALL_OCCURRENCES)
						result.addMatch(new ArtifactMatch(method, 0, 0,
								ArtifactMatch.REFERENCE_MATCH));
				}
			}

			for (IException excp : method.getExceptions()) {
				String excpType = excp.getFullyQualifiedName();
				if (data.matchPattern(excpType)) {
					if (data.getLimitTo() == ITigerstripeSearchConstants.REFERENCES
							|| data.getLimitTo() == ITigerstripeSearchConstants.ALL_OCCURRENCES)
						result.addMatch(new ArtifactMatch(method, 0, 0,
								ArtifactMatch.REFERENCE_MATCH));
				}
			}
		} else if (data.getSearchFor() == ITigerstripeSearchConstants.METHOD
				&& data.matchPattern(method.getName())) {
			result.addMatch(new ArtifactMatch(method, 0, 0,
					ArtifactMatch.REFERENCE_MATCH));
		}

		return org.eclipse.core.runtime.Status.OK_STATUS;
	}

	private IStatus acceptAssociationEnd(IAssociationEnd assocEnd) {
		SearchPatternData data = getData();
		String endType = assocEnd.getType().getFullyQualifiedName();
		if (data.getSearchFor() == ITigerstripeSearchConstants.TYPE
				&& data.matchPattern(endType)) {
			if (data.getLimitTo() == ITigerstripeSearchConstants.REFERENCES
					|| data.getLimitTo() == ITigerstripeSearchConstants.ALL_OCCURRENCES)
				result.addMatch(new ArtifactMatch(assocEnd, 0, 0,
						ArtifactMatch.REFERENCE_MATCH));
		}
		return org.eclipse.core.runtime.Status.OK_STATUS;
	}
}
