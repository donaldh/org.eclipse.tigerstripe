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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jdt.ui.wizards.NewContainerWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.EventArtifact;
import org.eclipse.tigerstripe.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.eclipse.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.ArtifactSelectionDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EventSelectorWizardPage extends NewContainerWizardPage {

	private static final String PAGE_NAME = "EventSelectorWizardPage"; //$NON-NLS-1$

	// This is used in the selection box for plugins
	private class EventListLabelProvider extends LabelProvider {

		private Image eventImage;

		public EventListLabelProvider() {
			super();
			eventImage = TigerstripePluginImages
					.get(TigerstripePluginImages.NOTIFICATION_ICON);
		}

		@Override
		public Image getImage(Object element) {
			return eventImage;
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "unknown";

			EventArtifact event = (EventArtifact) element;
			return event.getFullyQualifiedName();
		}
	}

	private class EventFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
			entityPageCustomButtonPressed(field, index);
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	// private Button pluginProperties;
	// private Label tigerstripeVersion;
	// private Text targetProjectField;
	// private Text outputDirectoryField;
	// private List selectedPluginList;

	private ListDialogField eventsDialogField;
	private IJavaElement initialJElement;

	private final static int ADD_BUTTON_IDX = 0;
	// private final static int PROPERTIES_BUTTON_IDX = 1;
	private final static int REMOVE_BUTTON_IDX = 2;

	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public EventSelectorWizardPage() {
		super(PAGE_NAME);

		setTitle("Notifications Selection");
		setDescription("Select Notifications to be emitted by this new Session.");

		EventFieldsAdapter adapter = new EventFieldsAdapter();

		String[] addButtons = new String[] { /* 0 */"Add", //$NON-NLS-1$
				/* 1 */null, /* 2 */"Remove" //$NON-NLS-1$
		};

		eventsDialogField = new ListDialogField(adapter, addButtons,
				new EventListLabelProvider());
		eventsDialogField.setDialogFieldListener(adapter);
		eventsDialogField.setLabelText("Notifications");
		eventsDialogField.setRemoveButtonIndex(REMOVE_BUTTON_IDX);
	}

	// -------- Initialization ---------
	private TSRuntimeContext tsRuntimeContext;

	/**
	 * This tries to initialize the TSRuntime context based on the given
	 * IJavaElement
	 * 
	 * @param jElement
	 */
	protected void initTSRuntimeContext(IJavaElement jElement) {
		this.tsRuntimeContext = new TSRuntimeContext();
		this.tsRuntimeContext.readTSDescriptor(jElement);
	}

	protected TSRuntimeContext getTSRuntimeContext() {
		return this.tsRuntimeContext;
	}

	// -------- UI Creation ---------

	/*
	 * @see WizardPage#createControl
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createEventsListControls(composite, nColumns);

		setControl(composite);

		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setFocus();
			// when this page becomes visible, it needs initialize it's root
			NewSessionWizardPage mainPage = (NewSessionWizardPage) getWizard()
					.getPage(NewSessionWizardPage.PAGE_NAME);
			this.initialJElement = mainPage.getJElement();
			initContainerPage(this.initialJElement);
			initTSRuntimeContext(this.initialJElement);
		}
	}

	/**
	 * Sets the focus to the package name input field.
	 */
	protected void setFocus() {
	}

	/**
	 * Creates a separator line. Expects a <code>GridLayout</code> with at
	 * least 1 column.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(
				composite, nColumns, convertHeightInCharsToPixels(1));
	}

	private void createEventsListControls(Composite composite, int nColumns) {
		eventsDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) eventsDialogField.getListControl(null)
				.getLayoutData();
		gd.heightHint = convertHeightInCharsToPixels(3);
		gd.widthHint = getMaxFieldWidth();
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
	}

	/**
	 * Returns the chosen entities.
	 * 
	 * @return a list of chosen super interfaces. The list's elements are of
	 *         type <code>String</code>
	 */
	public List getEvents() {
		return eventsDialogField.getElements();
	}

	/**
	 * Sets the super interfaces.
	 * 
	 * @param interfacesNames
	 *            a list of super interface. The method requires that the list's
	 *            elements are of type <code>String</code>
	 * @param canBeModified
	 *            if <code>true</code> the super interface field is editable;
	 *            otherwise it is read-only.
	 */
	public void setEvents(List events, boolean canBeModified) {
		eventsDialogField.setElements(events);
		eventsDialogField.setEnabled(canBeModified);
	}

	protected void entityPageCustomButtonPressed(ListDialogField field,
			int index) {
		if (index == ADD_BUTTON_IDX) {
			// Add entities to the selection
			EventArtifact[] selectedEvents = browseAvailableEvents();
			ArrayList list = new ArrayList();

			for (int i = 0; i < selectedEvents.length; i++) {
				list.add(selectedEvents[i]);
			}
			field.addElements(list);
		}
	}

	/**
	 * Opens up a dialog box with a list of available entities and returns the
	 * EntityOptions that have been selected.
	 * 
	 * @return EntityOption[] - Returns an array of EntityOption as selected
	 *         from the dialog
	 */
	private EventArtifact[] browseAvailableEvents() {

		ArtifactSelectionDialog dialog = new ArtifactSelectionDialog(
				this.initialJElement, EventArtifact.MODEL);

		dialog.setTitle("Notification Artifacts");
		dialog
				.setMessage("Select a set of Notifications that will be emitted by this interface.");
		AbstractArtifact[] selectedArtifacts = dialog.browseAvailableArtifacts(
				getShell(), this.eventsDialogField.getElements(),
				getTSRuntimeContext());

		EventArtifact[] result = new EventArtifact[selectedArtifacts.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = (EventArtifact) selectedArtifacts[i];
		}

		return result;
	}

}