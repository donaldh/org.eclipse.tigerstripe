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
package org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredList.FilterMatcher;
import org.eclipse.ui.internal.misc.StringMatcher;

/**
 * @author Eric Dillon
 * 
 * A bunch of convenience methods related to Entity Artifacts
 * 
 */
public class BrowseForArtifactDialog {

	private ITigerstripeModelProject project;

	private IAbstractArtifact[] artifactModels;

	private String title;

	private String message;

	private int fWidth = 140;

	private int fHeight = 18;

	private boolean includePrimitiveTypes = true;

	public void setIncludePrimitiveTypes(boolean includePrimitiveTypes) {
		this.includePrimitiveTypes = includePrimitiveTypes;
	}

	public boolean isIncludePrimitiveTypes() {
		return this.includePrimitiveTypes;
	}

	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public BrowseForArtifactDialog(ITigerstripeModelProject project,
			IAbstractArtifact model) {
		this.project = project;
		artifactModels = new IAbstractArtifact[1];
		artifactModels[0] = model;
	}

	public BrowseForArtifactDialog(ITigerstripeModelProject project,
			IAbstractArtifact[] models) {
		this.project = project;
		this.artifactModels = models;
	}

	/**
	 * Opens up a dialog box with a list of available entities and returns the
	 * EntityOptions that have been selected.
	 * 
	 * @return EntityOption[] - Returns an array of EntityOption as selected
	 *         from the dialog
	 */
	public AbstractArtifact[] browseAvailableArtifacts(Shell parentShell,
			List selectedElements) throws TigerstripeException {

		final AbstractArtifactLabelProvider labelProvider = new AbstractArtifactLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof PrimitiveTypeArtifact) {
					String labelText = super.getText(element);
					// if starts with "primitive.", remove that prefix
					if (labelText
							.startsWith(ITigerstripeConstants.BASEPRIMITIVE_PACKAGE
									+ ".")) {
						int lastPos = labelText.lastIndexOf(".") + 1;
						labelText = labelText.substring(lastPos);
						// and pad with a space (to move to the front of the
						// list)
						return " " + labelText;
					}
					// otherwise, just add a space (so that all primitives
					// appear at the head
					// of the list when sorted)
					return " " + super.getText(element);
				}
				return super.getText(element);
			}
		};
		ElementListSelectionDialog elsd = new ElementListSelectionDialog(
				parentShell, labelProvider) {
			@Override
			public void create() {
				super.create();
				// set the filter matcher so that it will put a "*" in front of
				// the
				// pattern as well as at the end (the default filter matcher
				// that is
				// used in a filtered list only puts the "*" at the end of the
				// pattern)
				fFilteredList.setFilterMatcher(new FilterMatcher() {
					private StringMatcher fMatcher;

					public void setFilter(String pattern, boolean ignoreCase,
							boolean ignoreWildCards) {
						fMatcher = new StringMatcher('*' + pattern + '*',
								ignoreCase, ignoreWildCards);
					}

					public boolean match(Object element) {
						return fMatcher.match(labelProvider.getText(element));
					}
				});
			}
		};

		elsd.setTitle(getTitle());
		elsd.setMessage(getMessage());
		elsd.setSize(fWidth, fHeight);

		Object[] availableArtifacts = getAvailableArtifactsList(selectedElements);
		elsd.setElements(availableArtifacts);

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				AbstractArtifact[] result = new AbstractArtifact[objects.length];
				for (int i = 0; i < result.length; i++) {
					result[i] = (AbstractArtifact) objects[i];
				}

				return result;
			}
		}
		return new AbstractArtifact[0];
	}

	private Collection<IPrimitiveTypeArtifact> getBuiltinPrimitiveTypes()
			throws TigerstripeException {

		if (isIncludePrimitiveTypes())
			return TigerstripeProjectFactory.INSTANCE.getPhantomProject()
					.getReservedPrimitiveTypes();
		else
			return new ArrayList<IPrimitiveTypeArtifact>();

	}

	/**
	 * 
	 * @return
	 */
	private Object[] getAvailableArtifactsList(List selectedElements)
			throws TigerstripeException {

		Collection<IAbstractArtifact> artifacts = new LinkedList<IAbstractArtifact>();
		IArtifactManagerSession session = project.getArtifactManagerSession();

		// If we were given an empty array of artifactModels, just
		// return ALL artifacts.
		if (artifactModels.length == 0) {
			IQueryAllArtifacts query = (IQueryAllArtifacts) session
					.makeQuery(IQueryAllArtifacts.class.getName());
			artifacts.addAll(session.queryArtifact(query));
			artifacts.addAll(getBuiltinPrimitiveTypes());
		} else {
			for (IAbstractArtifact art : this.artifactModels) {
				IQueryArtifactsByType query = (IQueryArtifactsByType) session
						.makeQuery(IQueryArtifactsByType.class.getName());
				query.setArtifactType(art.getClass().getName());
				artifacts.addAll(session.queryArtifact(query));
				if (art instanceof IPrimitiveTypeArtifact) {
					artifacts.addAll(getBuiltinPrimitiveTypes());
				}
			}
		}

		if (artifacts == null || artifacts.size() == 0)
			return new Object[0];

		// Now removing from the list, all the selected elements
		// Also, removing duplicates that would be the result of having
		// overwriten
		// artifacts locally.
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
		List<String> nameList = new ArrayList<String>(); // used to avoid
															// duplicates
		for (Iterator iterArtifacts = artifacts.iterator(); iterArtifacts
				.hasNext();) {
			AbstractArtifact artifact = (AbstractArtifact) iterArtifacts.next();

			boolean keepGoing = true;
			for (Iterator iterSelected = selectedElements.iterator(); iterSelected
					.hasNext()
					&& keepGoing;) {
				AbstractArtifact name = (AbstractArtifact) iterSelected.next();
				if (artifact.getFullyQualifiedName().equals(
						name.getFullyQualifiedName())) {
					keepGoing = false;
				}
			}
			if (keepGoing) {
				if (!nameList.contains(artifact.getFullyQualifiedName())) { // no
					// duplicates
					result.add(artifact);
					nameList.add(artifact.getFullyQualifiedName());
				}
			}
		}

		return result.toArray();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
