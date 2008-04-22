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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.session;

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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactSelectionDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class QuerySelectorWizardPage extends NewContainerWizardPage {

	private static final String PAGE_NAME = "QuerySelectorWizardPage"; //$NON-NLS-1$

	// This is used in the selection box for plugins
	private class QueryListLabelProvider extends LabelProvider {

		private Image queryImage;

		public QueryListLabelProvider() {
			super();
			queryImage = Images.get(Images.QUERY_ICON);
		}

		@Override
		public Image getImage(Object element) {
			return queryImage;
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "unknown";

			QueryArtifact query = (QueryArtifact) element;
			return query.getFullyQualifiedName();
		}
	}

	private class QueryFieldsAdapter implements IStringButtonAdapter,
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

	// private Button pluginProperties;
	// private Label tigerstripeVersion;
	// private Text targetProjectField;
	// private Text outputDirectoryField;
	// private List selectedPluginList;

	private ListDialogField queriesDialogField;

	private IJavaElement initialJElement;

	private final static int ADD_BUTTON_IDX = 0;

	// private final static int PROPERTIES_BUTTON_IDX = 1;
	private final static int REMOVE_BUTTON_IDX = 2;

	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public QuerySelectorWizardPage() {
		super(PAGE_NAME);

		setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IQueryArtifactImpl.class.getName()).getLabel()
				+ " Selection");
		setDescription("Select "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IQueryArtifactImpl.class.getName()).getLabel()
				+ "(s) to be managed through this new "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						ISessionArtifactImpl.class.getName()).getLabel());

		QueryFieldsAdapter adapter = new QueryFieldsAdapter();

		String[] addButtons = new String[] { /* 0 */"Add", //$NON-NLS-1$
				/* 1 */null, /* 2 */"Remove" //$NON-NLS-1$
		};

		queriesDialogField = new ListDialogField(adapter, addButtons,
				new QueryListLabelProvider());
		queriesDialogField.setDialogFieldListener(adapter);
		queriesDialogField.setLabelText(ArtifactMetadataFactory.INSTANCE
				.getMetadata(IQueryArtifactImpl.class.getName()).getLabel()
				+ "(s)");
		queriesDialogField.setRemoveButtonIndex(REMOVE_BUTTON_IDX);

	}

	// -------- Initialization ---------

	/**
	 * The wizard owning this page is responsible for calling this method with
	 * the current selection. The selection is used to initialize the fields of
	 * the wizard page.
	 * 
	 * @param selection
	 *            used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);

		initContainerPage(jelem);
		initPage(jelem);
	}

	private void initPage(IJavaElement jElement) {

		List initQueries = new ArrayList(5);
		setQueries(initQueries, true);

		// save this for now if we have it. If null, it'll need to be updated
		// once the user have selected a valid TS project
		initialJElement = jElement;
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

		createQueriesListControls(composite, nColumns);

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

	private void createQueriesListControls(Composite composite, int nColumns) {
		queriesDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) queriesDialogField.getListControl(null)
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
	public List getQueries() {
		return queriesDialogField.getElements();
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
	public void setQueries(List queries, boolean canBeModified) {
		queriesDialogField.setElements(queries);
		queriesDialogField.setEnabled(canBeModified);
	}

	/**
	 * Resolves the TS Descriptor based on the Container/Project selected in the
	 * first page of the wizard.
	 * 
	 * Unless the descriptor has been resolved, the artifacts cannot be
	 * presented to the user for selection
	 * 
	 * @return true if the TS descriptor was successfully read, false otherwise
	 */
	// private boolean resolveTSDescriptor() {
	// readTSDescriptor(this.initialJElement);
	// return true;
	// }
	protected void entityPageCustomButtonPressed(ListDialogField field,
			int index) {
		if (index == ADD_BUTTON_IDX) {
			// Add entities to the selection
			QueryArtifact[] selectedEOptions = browseAvailableQueries();
			ArrayList list = new ArrayList();

			for (int i = 0; i < selectedEOptions.length; i++) {
				list.add(selectedEOptions[i]);
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
	private QueryArtifact[] browseAvailableQueries() {

		ArtifactSelectionDialog dialog = new ArtifactSelectionDialog(
				this.initialJElement, QueryArtifact.MODEL);

		dialog.setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IQueryArtifactImpl.class.getName()).getLabel() + " Artifacts");
		dialog
				.setMessage("Select a set of " + ArtifactMetadataFactory.INSTANCE.getMetadata(
						IQueryArtifactImpl.class.getName()).getLabel() + "(s) that will be available through this " + ArtifactMetadataFactory.INSTANCE.getMetadata(
								ISessionArtifactImpl.class.getName()).getLabel());
		AbstractArtifact[] selectedArtifacts = dialog.browseAvailableArtifacts(
				getShell(), this.queriesDialogField.getElements(),
				getTSRuntimeContext());

		QueryArtifact[] result = new QueryArtifact[selectedArtifacts.length];
		for (int i = 0; i < selectedArtifacts.length; i++) {
			result[i] = (QueryArtifact) selectedArtifacts[i];
		}

		return result;
		// ElementListSelectionDialog elsd = new ElementListSelectionDialog(
		// getShell(), new QueryListLabelProvider());
		//		
		// Object[] availableQueries= getAvailableQueriesList();
		// elsd.setElements(availableQueries);
		//
		// if (elsd.open() == InputDialog.OK) {
		//
		// Object[] objects = elsd.getResult();
		// if (objects != null && objects.length != 0) {
		// QueryArtifact[] result = new QueryArtifact[objects.length];
		// for (int i = 0; i < result.length; i++) {
		// result[i] = (QueryArtifact) objects[i];
		// }
		//
		// return result;
		// }
		// }
		// return new QueryArtifact[0];
	}

	// /**
	// * return an array containing EntityOptions as available based on the TS
	// * Descriptor
	// *
	// * @return
	// */
	// private Object[] getAvailableQueriesList() {
	// if (!resolveTSDescriptor()) {
	// return new Object[0];
	// }
	//
	// Collection queries = ArtifactManager.getManager().getArtifactsByModel(
	// QueryArtifact.MODEL);
	//
	// if (queries == null || queries.size() == 0) {
	// return new Object[0];
	// }
	//
	// return queries.toArray();
	// }
}