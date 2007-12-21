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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.eclipse.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author Eric Dillon
 * 
 * A bunch of convience methods related to Entity Artifacts
 * 
 */
public class ArtifactSelectionDialog {

	private AbstractArtifact artifactModel;

	private String title;

	private String message;

	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public ArtifactSelectionDialog(IJavaElement initialElement,
			AbstractArtifact model) {
		this.artifactModel = model;
	}

	/**
	 * Opens up a dialog box with a list of available entities and returns the
	 * EntityOptions that have been selected.
	 * 
	 * @return EntityOption[] - Returns an array of EntityOption as selected
	 *         from the dialog
	 */
	public AbstractArtifact[] browseAvailableArtifacts(Shell parentShell,
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
				AbstractArtifact[] result = new AbstractArtifact[objects.length];
				for (int i = 0; i < result.length; i++) {
					result[i] = (AbstractArtifact) objects[i];
				}

				return result;
			}
		}
		return new AbstractArtifact[0];
	}

	/**
	 * 
	 * @return
	 */
	private Object[] getAvailableArtifactsList(List selectedElements,
			TSRuntimeContext context) {

		Collection artifacts = new ArrayList();
		try {
			ITigerstripeProject project = context.getProjectHandle();
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
