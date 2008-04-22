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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * Browses for Facets specified in the list of provided ITigerstripeProjects
 * 
 * @author Eric Dillon
 * @since 2.1
 */
public class BrowseForFacetsDialog {

	private ITigerstripeModelProject[] projects;

	private String title;

	private String message;

	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public BrowseForFacetsDialog(ITigerstripeModelProject[] projects) {
		this.projects = projects;
	}

	/**
	 * Opens up a dialog box with a list of available facets and returns an
	 * array of those that have been selected.
	 * 
	 * @param selectedElements -
	 *            contains a list of facets that should not be displayed
	 * @return
	 */
	public IFacetReference[] browseAvailableArtifacts(Shell parentShell,
			List<IFacetReference> selectedElements) throws TigerstripeException {

		ElementListSelectionDialog elsd = new ElementListSelectionDialog(
				parentShell, new FacetLabelProvider());

		elsd.setTitle(getTitle());
		elsd.setMessage(getMessage());

		Object[] availableFacets = getAvailableFacetsList(selectedElements);
		elsd.setElements(availableFacets);

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				IFacetReference[] result = new IFacetReference[objects.length];
				for (int i = 0; i < result.length; i++) {
					result[i] = (IFacetReference) objects[i];
				}

				return result;
			}
		}
		return new IFacetReference[0];
	}

	private List<IFacetReference> getAllFacetsFor(ITigerstripeModelProject project) {
		List<IFacetReference> result = new ArrayList<IFacetReference>();
		try {
			result.addAll(Arrays.asList(project.getFacetReferences()));
			for (ITigerstripeModelProject refProject : project
					.getReferencedProjects()) {
				result.addAll(getAllFacetsFor(refProject));
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	private Object[] getAvailableFacetsList(
			List<IFacetReference> selectedElements) throws TigerstripeException {

		List<IFacetReference> rawList = new ArrayList<IFacetReference>();
		for (ITigerstripeModelProject project : projects) {
			List<IFacetReference> refs = getAllFacetsFor(project);
			rawList.addAll(refs);
		}

		// Now removing from the list, all the selected elements
		// Also, removing duplicates that would be the result of having
		// overwriten
		// artifacts locally.
		List<IFacetReference> result = new ArrayList<IFacetReference>();
		for (IFacetReference ref : rawList) {
			if (!selectedElements.contains(ref)) {
				result.add(ref);
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
