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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.TSRuntimeContext;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author Eric Dillon
 * 
 * A bunch of convience methods related to Entity Artifacts
 * 
 */
public class ArtifactSelectionDialog {

	private final IAbstractArtifactInternal artifactModel;

	private String title;

	private String message;

	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public ArtifactSelectionDialog(IJavaElement initialElement,
			IAbstractArtifactInternal model) {
		this.artifactModel = model;
	}

	/**
	 * Opens up a dialog box with a list of available entities and returns the
	 * EntityOptions that have been selected.
	 * 
	 * @return EntityOption[] - Returns an array of EntityOption as selected
	 *         from the dialog
	 */
	public IAbstractArtifactInternal[] browseAvailableArtifacts(
			Shell parentShell,
			List selectedElements, TSRuntimeContext context) {

		ElementListSelectionDialog elsd = new ElementListSelectionDialog(
				parentShell, new AbstractArtifactLabelProvider());

		elsd.setTitle(getTitle());
		elsd.setMessage(getMessage());

		Object[] availableArtifacts = getAvailableArtifactsList(
				selectedElements, context);
		elsd.setElements(availableArtifacts);

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				IAbstractArtifactInternal[] result = new IAbstractArtifactInternal[objects.length];
				for (int i = 0; i < result.length; i++) {
					result[i] = (IAbstractArtifactInternal) objects[i];
				}

				return result;
			}
		}
		return new IAbstractArtifactInternal[0];
	}

	/**
	 * 
	 * @return
	 */
	private Object[] getAvailableArtifactsList(List selectedElements,
			TSRuntimeContext context) {

		Collection artifacts = new ArrayList();
		try {
			ITigerstripeModelProject project = context.getProjectHandle();
			IArtifactManagerSession session = project
					.getArtifactManagerSession();

			IQueryArtifactsByType query = (IQueryArtifactsByType) session
					.makeQuery(IQueryArtifactsByType.class.getName());
			query.setArtifactType(this.artifactModel.getClass().getName());

			artifacts = session.queryArtifact(query);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e); // FIXME
		}

		if (artifacts == null || artifacts.size() == 0)
			return new Object[0];

		List result = new ArrayList();
		for (Iterator iterArtifacts = artifacts.iterator(); iterArtifacts
				.hasNext();) {
			IAbstractArtifactInternal artifact = (IAbstractArtifactInternal) iterArtifacts
					.next();

			boolean keepGoing = true;
			for (Iterator iterSelected = selectedElements.iterator(); iterSelected
					.hasNext()
					&& keepGoing;) {
				IAbstractArtifactInternal name = (IAbstractArtifactInternal) iterSelected
						.next();
				if (artifact.getFullyQualifiedName().equals(
						name.getFullyQualifiedName())) {
					keepGoing = false;
				}
			}
			if (keepGoing) {
				result.add(artifact);
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
