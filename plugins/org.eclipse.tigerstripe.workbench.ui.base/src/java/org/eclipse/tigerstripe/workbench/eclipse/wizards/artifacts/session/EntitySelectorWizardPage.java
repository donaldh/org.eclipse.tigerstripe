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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.ArtifactSelectorDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.IArtifactLabelProvider;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EntitySelectorWizardPage extends NewContainerWizardPage {

	private static final String PAGE_NAME = "EntitySelectorWizardPage"; //$NON-NLS-1$

	// This is used in the selection box for plugins
	private class EntityOptionListLabelProvider extends LabelProvider implements
			IArtifactLabelProvider {

		private Image managedEntityImage;

		public EntityOptionListLabelProvider() {
			super();
			managedEntityImage = TigerstripePluginImages
					.get(TigerstripePluginImages.ENTITY_ICON);
		}

		public String getName(Object element) {
			if (element == null)
				return "";

			EntityOption entity = (EntityOption) element;
			return entity.getFullyQualifiedName();
		}

		public Object getProperty(Object Element, String propertyName) {
			// TODO Auto-generated method stub
			return null;
		}

		public void setProperty(Object Element, String propertyName,
				Object property) {
			// TODO Auto-generated method stub

		}

		@Override
		public Image getImage(Object element) {
			return managedEntityImage;
		}

		public String getPackage(Object element) {
			if (element == null)
				return "";

			EntityOption entity = (EntityOption) element;
			return entity.getPackage();
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "unknown";

			EntityOption entity = (EntityOption) element;
			return entity.getFullyQualifiedName();
		}
	}

	private class EntityFieldsAdapter implements IStringButtonAdapter,
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

	private ListDialogField entitiesDialogField;
	private IJavaElement initialJElement;

	private final static int ADD_BUTTON_IDX = 0;
	private TSRuntimeContext tsRuntimeContext;

	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public EntitySelectorWizardPage() {
		super(PAGE_NAME);

		setTitle("Managed Entity Selection");
		setDescription("Select Entities to be managed through this new Session.");

		EntityFieldsAdapter adapter = new EntityFieldsAdapter();

		String[] addButtons = new String[] { /* 0 */"Add", //$NON-NLS-1$
				/* 2 */"Remove" //$NON-NLS-1$
		};

		entitiesDialogField = new ListDialogField(adapter, addButtons,
				new EntityOptionListLabelProvider());
		entitiesDialogField.setDialogFieldListener(adapter);
		entitiesDialogField.setLabelText("Managed Entities");
		entitiesDialogField.setRemoveButtonIndex(1);

	}

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

		createEntitiesListControls(composite, nColumns);

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

	private void createEntitiesListControls(Composite composite, int nColumns) {
		entitiesDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) entitiesDialogField.getListControl(null)
				.getLayoutData();
		gd.heightHint = convertHeightInCharsToPixels(3);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.widthHint = getMaxFieldWidth();
	}

	/**
	 * Returns the chosen entities.
	 * 
	 * @return a list of chosen super interfaces. The list's elements are of
	 *         type <code>String</code>
	 */
	public List getEntities() {
		return entitiesDialogField.getElements();
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
	public void setEntities(List entities, boolean canBeModified) {
		entitiesDialogField.setElements(entities);
		entitiesDialogField.setEnabled(canBeModified);
	}

	protected void entityPageCustomButtonPressed(ListDialogField field,
			int index) {
		if (index == ADD_BUTTON_IDX) {
			// Add entities to the selection
			EntityOption[] selectedEOptions = browseAvailableEntities();
			ArrayList list = new ArrayList();

			for (int i = 0; i < selectedEOptions.length; i++) {
				list.add(selectedEOptions[i]);
			}
			field.addElements(list);
		}
	}

	protected void openPropertiesForEntity(ListDialogField field) {
		List selectedElements = field.getSelectedElements();
		Object[] entityOptions = selectedElements.toArray();

		EntityOption target = (EntityOption) ((EntityOption) entityOptions[0])
				.clone();
		EntityOptionsCompleteDialog dialog = new EntityOptionsCompleteDialog(
				getShell(), target);
		if (dialog.open() == Window.OK) {
			((EntityOption) entityOptions[0]).setProperties(target
					.getProperties());
		}

	}

	/**
	 * Opens up a dialog box with a list of available entities and returns the
	 * EntityOptions that have been selected.
	 * 
	 * @return EntityOption[] - Returns an array of EntityOption as selected
	 *         from the dialog
	 */
	private EntityOption[] browseAvailableEntities() {

		// ElementListSelectionDialog elsd = new ElementListSelectionDialog(
		ArtifactSelectorDialog elsd = new ArtifactSelectorDialog(getShell(),
				new EntityOptionListLabelProvider());

		elsd.setTitle("Managed Entity Artifacts");
		elsd
				.setMessage("Select a set of Entities to be managed through this interface.");
		Object[] availableEntityOptions = getAvailableEntityOptionsList();
		elsd.setElements(availableEntityOptions);

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				EntityOption[] result = new EntityOption[objects.length];
				for (int i = 0; i < result.length; i++) {
					result[i] = (EntityOption) objects[i];
				}

				return result;
			}
		}
		return new EntityOption[0];
	}

	/**
	 * return an array containing EntityOptions as available based on the TS
	 * Descriptor
	 * 
	 * @return
	 */
	private Object[] getAvailableEntityOptionsList() {

		// Get the already selected entities, so the user won't be presented
		// with
		// already selected entities.
		List selectedOptions = entitiesDialogField.getElements();

		TSRuntimeContext context = getTSRuntimeContext();
		Collection entities = new ArrayList();
		try {
			ITigerstripeModelProject project = context.getProjectHandle();
			IArtifactManagerSession session = project
					.getArtifactManagerSession();

			IQueryArtifactsByType query = (IQueryArtifactsByType) session
					.makeQuery(IQueryArtifactsByType.class.getName());
			query.setArtifactType(ManagedEntityArtifact.MODEL.getClass()
					.getName());

			entities = session.queryArtifact(query);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e); // FIXME
		}

		if (entities == null || entities.size() == 0)
			return new Object[0];

		List result = new ArrayList();

		for (Iterator iter = entities.iterator(); iter.hasNext();) {
			ManagedEntityArtifact entity = (ManagedEntityArtifact) iter.next();

			boolean keepGoing = true;
			for (Iterator iterSelected = selectedOptions.iterator(); iterSelected
					.hasNext()
					&& keepGoing;) {
				EntityOption opt = (EntityOption) iterSelected.next();
				if (opt.getFullyQualifiedName().equals(
						entity.getFullyQualifiedName())) {
					keepGoing = false;
				}
			}

			if (keepGoing) {
				result.add(new EntityOption(entity));
			}
		}

		return result.toArray();
	}

	public class EntityOption {

		@Override
		public Object clone() {
			EntityOption result = new EntityOption(this.managedEntityArtifact);

			result.setProperties(this.getProperties());
			return result;
		}

		public String getPackage() {
			return managedEntityArtifact.getPackage();
		}

		public void setProperties(Properties prop) {
			this.properties = (Properties) prop.clone();
		}

		public ManagedEntityArtifact managedEntityArtifact;

		private final String[] labels = { "Create", "Delete", "Get", "Set",
				"Query-by-Template", "Bulk-Atomic-Create", "Bulk-Atomic-Get",
				"Bulk-Atomic-Set", "Bulk-Atomic-Delete",
				"Bulk-BestEffort-Create", "Bulk-BestEffort-Get",
				"Bulk-BestEffort-Set", "Bulk-BestEffort-Delete" };

		private final boolean[] defaultValues = { true, true, true, true,
				false, false, false, false, false, false, false, false, false };

		private Properties properties;

		public EntityOption(ManagedEntityArtifact managedEntityArtifact) {
			this.managedEntityArtifact = managedEntityArtifact;
			this.properties = new Properties();

			for (int i = 0; i < this.labels.length; i++) {
				this.properties.setProperty(labels[i], String
						.valueOf(defaultValues[i]));
			}
		}

		public String[] getLabels() {
			return this.labels;
		}

		public Properties getProperties() {
			return this.properties;
		}

		public String getFullyQualifiedName() {
			return this.managedEntityArtifact.getFullyQualifiedName();
		}
	}
}