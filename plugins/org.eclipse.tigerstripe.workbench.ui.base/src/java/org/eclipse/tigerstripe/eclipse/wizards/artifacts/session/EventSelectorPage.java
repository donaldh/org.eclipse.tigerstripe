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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EventSelectorPage extends WizardPage {
	private List selectedEventsList;

	private ArtifactManager artifactManager;

	private Map selectedEventArtifactsMap;
	private Map availableEventArtifactsMap;
	private Button removeQuery;

	/**
	 * Constructor for NewProjectWizardPage.
	 * 
	 * @param pageName
	 */
	public EventSelectorPage(ISelection selection, ImageDescriptor image,
			ArtifactManager artifactManager) {
		super("wizardPage", "Event Selection", image);
		setTitle("Event Selection");

		setDescription("Select one or more Events for this API.");
		this.artifactManager = artifactManager;
	}

	private ArtifactManager getArtifactManager() {
		return this.artifactManager;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;

		// Post the explanation message
		Composite textExplanation = new Composite(container, SWT.NULL);
		GridLayout explainLayout = new GridLayout();
		textExplanation.setLayout(explainLayout);
		explainLayout.numColumns = 1;

		final Label explain = new Label(textExplanation, SWT.NONE);
		explain.setLayoutData(new GridData(GridData.BEGINNING));
		explain.setText("Please select a set of Events from your project"
				+ "that will be emitted by this new interface session.");

		// create the entity list panel
		Group eventPanel = new Group(container, SWT.NULL);
		eventPanel.setText("Events");
		final GridLayout eventGridLayout = new GridLayout();
		eventGridLayout.numColumns = 2;
		eventPanel.setLayout(eventGridLayout);
		eventPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				+ GridData.FILL_VERTICAL));

		selectedEventsList = new List(eventPanel, SWT.MULTI | SWT.BORDER);
		selectedEventsList.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				+ GridData.FILL_VERTICAL));
		selectedEventsList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updatePage();
			}
		});

		final Composite buttonsContainer = new Composite(eventPanel, SWT.NULL);
		GridLayout buttonsLayout = new GridLayout();
		buttonsContainer.setLayout(buttonsLayout);
		buttonsLayout.numColumns = 1;
		buttonsLayout.verticalSpacing = 1;

		final Button addQuery = new Button(buttonsContainer, SWT.NONE);
		addQuery.setText("Add");
		addQuery.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				+ GridData.HORIZONTAL_ALIGN_END));
		addQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventArtifact[] selectedEvents = browseAvailableEvents();

				if (selectedEvents.length != 0) {
					// add the selected entities to the list
					addEventsToSelection(selectedEvents);
				}
				updatePage();
			}
		});

		removeQuery = new Button(buttonsContainer, SWT.NONE);
		removeQuery.setText("Remove");
		removeQuery.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				+ GridData.HORIZONTAL_ALIGN_END));
		removeQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				EventArtifact[] queriesToRemove = highlightedFromList();

				if (queriesToRemove != null && queriesToRemove.length != 0) {
					// add the selected entities to the list
					removeEventsFromSelection(queriesToRemove);
				}
				updatePage();
			}
		});

		initializeContent();
		updatePage();

		setControl(container);
	}

	/**
	 * Initialize the content of the form
	 * 
	 * This will create the list EntityOptions to select from, and an empty list
	 * of selected ones.
	 */
	private void initializeContent() {
		initializeEventsList();

	}

	/**
	 * Opens up a dialog box with a list of available entities and returns the
	 * EntityOptions that have been selected.
	 * 
	 * @return EntityOption[] - Returns an array of EntityOption as selected
	 *         from the dialog
	 */
	private EventArtifact[] browseAvailableEvents() {

		ElementListSelectionDialog elsd = new ElementListSelectionDialog(
				getShell(), new AbstractArtifactLabelProvider());
		elsd.setElements(availableEventArtifactsMap.values().toArray());

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				EventArtifact[] result = new EventArtifact[objects.length];
				for (int i = 0; i < result.length; i++) {
					result[i] = (EventArtifact) objects[i];
				}

				return result;
			}
		}
		return new EventArtifact[0];
	}

	/**
	 * Returns an array containing the EventArtifact corresponding to the
	 * selected entities in the main page list
	 * 
	 * @return EntityOption[] - An array containing the EntityOptions
	 *         corresponding to the selected labels on the main page's list
	 */
	private EventArtifact[] highlightedFromList() {

		String[] selectedLabels = selectedEventsList.getSelection();

		if (selectedLabels == null || selectedLabels.length == 0)
			return new EventArtifact[0];

		EventArtifact[] result = new EventArtifact[selectedLabels.length];

		for (int i = 0; i < selectedLabels.length; i++) {
			EventArtifact query = (EventArtifact) selectedEventArtifactsMap
					.get(selectedLabels[i]);
			result[i] = query;
		}

		return result;
	}

	/**
	 * Adds an array of EntityOption objects to the list of selected
	 * EntityOptions
	 * 
	 * @param entityOptions -
	 *            EntityOption[] the array of EntityOptions to add
	 */
	private void addEventsToSelection(EventArtifact[] queries) {
		if (queries == null)
			return;

		for (int i = 0; i < queries.length; i++) {
			if (!selectedEventArtifactsMap.containsKey(queries[i]
					.getFullyQualifiedName())) {
				selectedEventArtifactsMap.put(queries[i]
						.getFullyQualifiedName(), queries[i]);
			}
		}
		refreshList();
	}

	/**
	 * Remove an array of EntityOption objects from the list of selected
	 * EntityOptions
	 * 
	 * @param entityOptions -
	 *            EntityOption[] the array of EntityOptions to remove
	 */
	private void removeEventsFromSelection(EventArtifact[] queries) {
		if (queries == null)
			return;

		for (int i = 0; i < queries.length; i++) {
			if (selectedEventArtifactsMap.containsKey(queries[i]
					.getFullyQualifiedName())) {
				selectedEventArtifactsMap.remove(queries[i]
						.getFullyQualifiedName());
			}
		}
		refreshList();
	}

	/**
	 * Initialize the lists of Available and Selected Entities
	 * 
	 */
	private void initializeEventsList() {

		this.selectedEventArtifactsMap = new HashMap();
		this.availableEventArtifactsMap = new HashMap();

		Collection queries = getArtifactManager()
				.getArtifactsByModel(EventArtifact.MODEL, true,
						new TigerstripeNullProgressMonitor());

		if (queries == null || queries.size() == 0)
			return;

		for (Iterator iter = queries.iterator(); iter.hasNext();) {
			EventArtifact query = (EventArtifact) iter.next();

			if (!availableEventArtifactsMap.containsKey(query
					.getFullyQualifiedName())) {
				availableEventArtifactsMap.put(query.getFullyQualifiedName(),
						query);
			}
		}
	}

	private void updatePage() {

		int listSelectionCount = selectedEventsList.getSelectionCount();

		if (listSelectionCount == 0) {
			removeQuery.setEnabled(false);
		} else {
			removeQuery.setEnabled(true);
		}

		setErrorMessage(null);
		setPageComplete(true);
	}

	private void refreshList() {
		selectedEventsList.deselectAll();
		selectedEventsList.removeAll();

		for (Iterator iter = selectedEventArtifactsMap.values().iterator(); iter
				.hasNext();) {
			EventArtifact artifact = (EventArtifact) iter.next();
			selectedEventsList.add(artifact.getFullyQualifiedName());
		}
	}

	public Collection getSelectedEvents() {
		return this.selectedEventArtifactsMap.values();
	}

}