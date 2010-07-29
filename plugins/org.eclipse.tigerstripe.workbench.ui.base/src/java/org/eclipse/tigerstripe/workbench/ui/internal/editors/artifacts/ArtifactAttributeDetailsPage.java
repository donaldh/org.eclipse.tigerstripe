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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactComponent;
import org.eclipse.tigerstripe.workbench.internal.core.model.Field;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener.IURIBaseProviderPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactAttributeDetailsPage implements IDetailsPage,
		IURIBaseProviderPage {

	private StereotypeSectionManager stereotypeMgr;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class AttributeDetailsPageListener implements ModifyListener,
			KeyListener, SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.F3) {
				navigateToKeyPressed(e);
			}
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	private IManagedForm form;

	private ArtifactAttributesSection master;

	private IField field;

	private boolean silentUpdate = false;

	private Button refByValueButton;

	private Button refByKeyButton;

	private Button refByKeyResultButton;

	private Button addAnno;

	private Button editAnno;

	private Button removeAnno;

	private Table annTable;

	private boolean isReadOnly = false;

	private TextEditListener nameEditListener;

	public ArtifactAttributeDetailsPage(ArtifactAttributesSection master,
			boolean isReadOnly) {
		super();
		this.master = master;
		this.isReadOnly = isReadOnly;
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		parent.setLayoutData(td);

		int height = createFieldInfo(parent);
		/*
		 * FIXME Just workaround to avoid appearing scrolls on details part.
		 */
		master.setMinimumHeight(height);

		form.getToolkit().paintBordersFor(parent);
	}

	private void createStereotypes(Composite parent, FormToolkit toolkit) {
		toolkit.createLabel(parent, "Stereotypes:");

		Composite innerComposite = toolkit.createComposite(parent);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		innerComposite.setLayoutData(gd);
		TableWrapLayout layout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		innerComposite.setLayout(layout);

		annTable = toolkit.createTable(innerComposite, SWT.BORDER);
		annTable.setEnabled(!isReadOnly);

		Composite buttonsClient = toolkit.createComposite(innerComposite);
		buttonsClient.setLayoutData(new TableWrapData(TableWrapData.FILL));
		layout = new TableWrapLayout();
		layout.topMargin = layout.bottomMargin = 0;
		buttonsClient.setLayout(layout);

		addAnno = toolkit.createButton(buttonsClient, "Add", SWT.PUSH);
		// Support for testing
		addAnno.setData("name", "Add_Stereotype_Attribute");
		addAnno.setEnabled(!isReadOnly);
		addAnno.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		editAnno = toolkit.createButton(buttonsClient, "Edit", SWT.PUSH);
		editAnno.setData("name", "Edit_Stereotype_Attribute");
		editAnno.setEnabled(!isReadOnly);
		editAnno.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		removeAnno = toolkit.createButton(buttonsClient, "Remove", SWT.PUSH);
		// Support for testing
		removeAnno.setData("name", "Remove_Stereotype_Attribute");
		removeAnno.setEnabled(!isReadOnly);
		removeAnno.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Point p = buttonsClient.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = p.y;
		annTable.setLayoutData(td);
	}

	// ============================================================
	private void setIField(IField field) {
		this.field = field;
	}

	private IField getField() {
		return field;
	}

	public URI getBaseURI() {
		return (URI) getField().getAdapter(URI.class);
	}

	public ITigerstripeModelProject getProject() {
		try {
			if (getField() != null)
				return getField().getProject();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	// ============================================================
	private Text nameText;

	private Text typeText;

	private CCombo defaultValueText;

	private Button typeBrowseButton;

	private CCombo multiplicityCombo;

	private Button optionalButton;

	private Button readonlyButton;

	private Button orderedButton;

	private Button uniqueButton;

	private Text commentText;

	private int createFieldInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		AttributeDetailsPageListener adapter = new AttributeDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		layout.bottomMargin = layout.topMargin = 0;
		sectionClient.setLayout(layout);
		sectionClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		label.setEnabled(!isReadOnly);
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(!isReadOnly);
		nameText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		nameText.addModifyListener(adapter);
		TigerstripeFormEditor editor = (TigerstripeFormEditor) ((TigerstripeFormPage) getForm()
				.getContainer()).getEditor();
		if (!isReadOnly) {
			nameEditListener = new TextEditListener(editor, "name",
					IModelChangeDelta.SET, this);
			nameText.addModifyListener(nameEditListener);
		}

		label = toolkit.createLabel(sectionClient, "Description: ");
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		label.setEnabled(!isReadOnly);
		commentText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		commentText.setEnabled(!isReadOnly);
		commentText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		commentText.addModifyListener(adapter);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		gd.heightHint = 70;
		gd.maxHeight = 70;
		commentText.setLayoutData(gd);

		label = toolkit.createLabel(sectionClient, "Type: ");
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		label.setEnabled(!isReadOnly);

		Composite c = toolkit.createComposite(sectionClient);
		layout = TigerstripeLayoutFactory.createClearTableWrapLayout(2, false);
		layout.horizontalSpacing = 5;
		c.setLayout(layout);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		typeText = toolkit.createText(c, "");
		typeText.setEnabled(!isReadOnly);
		typeText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		typeBrowseButton = toolkit.createButton(c, "Browse", SWT.PUSH);
		typeBrowseButton.setEnabled(!isReadOnly);
		typeBrowseButton.addSelectionListener(adapter);
		typeText.addModifyListener(adapter);
		typeText.addKeyListener(adapter);

		label = toolkit.createLabel(sectionClient, "Multiplicity: ");
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		label.setEnabled(!isReadOnly);
		multiplicityCombo = new CCombo(sectionClient, SWT.SINGLE
				| SWT.READ_ONLY | SWT.BORDER);
		multiplicityCombo.setEnabled(!isReadOnly);
		multiplicityCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		toolkit.adapt(this.multiplicityCombo, true, true);

		for (IModelComponent.EMultiplicity multVal : IModelComponent.EMultiplicity
				.values()) {
			multiplicityCombo.add(multVal.getLabel());
		}
		multiplicityCombo.addModifyListener(adapter);
		multiplicityCombo.addSelectionListener(adapter);
		multiplicityCombo.setVisibleItemCount(IModelComponent.EMultiplicity
				.values().length);

		label = toolkit.createLabel(sectionClient, "Visibility: ");
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		label.setEnabled(!isReadOnly);
		Composite visiComposite = toolkit.createComposite(sectionClient);
		layout = new TableWrapLayout();
		layout.leftMargin = 0;
		layout.rightMargin = 0;
		layout.numColumns = 4;
		visiComposite.setEnabled(!isReadOnly);
		visiComposite.setLayout(layout);
		visiComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		publicButton = toolkit.createButton(visiComposite, "Public", SWT.RADIO);
		publicButton.setEnabled(!isReadOnly);
		publicButton.addSelectionListener(adapter);
		protectedButton = toolkit.createButton(visiComposite, "Protected",
				SWT.RADIO);
		protectedButton.setEnabled(!isReadOnly);
		protectedButton.addSelectionListener(adapter);
		privateButton = toolkit.createButton(visiComposite, "Private",
				SWT.RADIO);
		privateButton.setEnabled(!isReadOnly);
		privateButton.addSelectionListener(adapter);
		packageButton = toolkit.createButton(visiComposite, "Package",
				SWT.RADIO);
		packageButton.setEnabled(!isReadOnly);
		packageButton.addSelectionListener(adapter);

		label = toolkit.createLabel(sectionClient, "Qualifiers: ");
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		label.setEnabled(!isReadOnly);
		Composite opComposite = toolkit.createComposite(sectionClient);
		layout = new TableWrapLayout();
		layout.leftMargin = 0;
		layout.rightMargin = 0;
		layout.numColumns = 4;
		opComposite.setLayout(layout);
		opComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		if (prop.getPropertyValue(IOssjLegacySettigsProperty.ENABLE_ISOPTIONAL)) {
			optionalButton = toolkit.createButton(opComposite, "Optional",
					SWT.CHECK);
			optionalButton.setEnabled(!isReadOnly);
			optionalButton.addSelectionListener(adapter);
		}

		readonlyButton = toolkit.createButton(opComposite, "Readonly",
				SWT.CHECK);
		readonlyButton.setEnabled(!isReadOnly);
		readonlyButton.addSelectionListener(adapter);
		orderedButton = toolkit.createButton(opComposite, "Ordered", SWT.CHECK);
		orderedButton.setEnabled(!isReadOnly);
		orderedButton.addSelectionListener(adapter);
		uniqueButton = toolkit.createButton(opComposite, "Unique", SWT.CHECK);
		uniqueButton.setEnabled(!isReadOnly);
		uniqueButton.addSelectionListener(adapter);

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEREFBY_MODIFIERS)) {
			label.setEnabled(!isReadOnly);
			Composite refComposite = toolkit.createComposite(sectionClient);
			layout = new TableWrapLayout();
			layout.numColumns = 3;
			refComposite.setLayout(layout);
			refComposite.setLayoutData(new TableWrapData(
					TableWrapData.FILL_GRAB));
			refByValueButton = toolkit.createButton(refComposite,
					IField.refByLabels[IField.REFBY_VALUE], SWT.RADIO);
			refByValueButton.addSelectionListener(adapter);
			refByKeyButton = toolkit.createButton(refComposite,
					IField.refByLabels[IField.REFBY_KEY], SWT.RADIO);
			refByKeyButton.addSelectionListener(adapter);
			refByKeyResultButton = toolkit.createButton(refComposite,
					IField.refByLabels[IField.REFBY_KEYRESULT], SWT.RADIO);
			refByKeyResultButton.addSelectionListener(adapter);
		}

		label = toolkit.createLabel(sectionClient, "Default Value: ");
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		label.setEnabled(!isReadOnly);

		defaultValueText = new CCombo(sectionClient, SWT.SINGLE | SWT.BORDER);
		toolkit.adapt(this.defaultValueText, true, true);
		defaultValueText.setEnabled(!isReadOnly);
		defaultValueText.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		defaultValueText.addModifyListener(adapter);

		createStereotypes(sectionClient, form.getToolkit());

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		return sectionClient.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
	}

	// ===================================================================

	private Button publicButton;

	private Button protectedButton;

	private Button privateButton;

	private Button packageButton;

	public void initialize(IManagedForm form) {
		this.form = form;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	protected void pageModified() {
		master.markPageModified();
	}

	public boolean isDirty() {
		return master.isDirty();
	}

	public void commit(boolean onSave) {
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void setFocus() {
		nameText.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof ArtifactAttributesSection) {
			if (nameEditListener != null)
				nameEditListener.reset();

			Table fieldsTable = master.getViewer().getTable();

			IField selected = (IField) fieldsTable.getSelection()[0].getData();
			setIField(selected);
			ArtifactEditorBase editor = (ArtifactEditorBase) master.getPage()
					.getEditor();

			if (stereotypeMgr == null) {
				stereotypeMgr = new StereotypeSectionManager(addAnno, editAnno,
						removeAnno, annTable, (IStereotypeCapable) getField(),
						master.getSection().getShell(), editor);
				stereotypeMgr.delegate();
			} else {
				stereotypeMgr
						.setArtifactComponent((ArtifactComponent) getField());
			}
			updateForm();
		}
	}

	private void updateButtonsState() {
		setSilentUpdate(true);
		Type fieldType = (Type) field.getType();
		if (fieldType.isEntityType()) {
			if (refByKeyButton != null)
				refByKeyButton.setEnabled(!isReadOnly);
			if (refByKeyResultButton != null)
				refByKeyResultButton.setEnabled(!isReadOnly);
			if (refByValueButton != null)
				refByValueButton.setEnabled(!isReadOnly);
		} else {
			if (refByKeyButton != null)
				refByKeyButton.setEnabled(false);
			if (refByKeyResultButton != null)
				refByKeyResultButton.setEnabled(false);
			if (refByValueButton != null)
				refByValueButton.setEnabled(false);
		}
		if (getField().getType().getTypeMultiplicity().isArray()) {
			orderedButton.setEnabled(true);
			uniqueButton.setEnabled(true);
		} else {
			orderedButton.setEnabled(false);
			orderedButton.setSelection(false);
			field.setOrdered(false);
			uniqueButton.setEnabled(false);
			uniqueButton.setSelection(true);
			field.setUnique(true);
		}
		setSilentUpdate(false);
	}

	private void updateForm() {

		setSilentUpdate(true);
		IField field = getField();
		nameText.setText(getField().getName());

		if (field.getType() != null) {
			typeText.setText(Misc.removeJavaLangString(field.getType()
					.getFullyQualifiedName()));
			IModelComponent.EMultiplicity mult = field.getType()
					.getTypeMultiplicity();
			multiplicityCombo.select(IModelComponent.EMultiplicity
					.indexOf(mult));
			updateDefaultValueCombo();
		}

		setVisibility(getField().getVisibility());
		commentText.setText(getField().getComment() != null ? getField()
				.getComment() : "");

		if (optionalButton != null)
			optionalButton.setSelection(getField().isOptional());
		readonlyButton.setSelection(getField().isReadOnly());
		orderedButton.setSelection(getField().isOrdered());
		uniqueButton.setSelection(getField().isUnique());

		if (refByKeyButton != null)
			refByKeyButton.setSelection(false);
		if (refByKeyResultButton != null)
			refByKeyResultButton.setSelection(false);
		if (refByValueButton != null)
			refByValueButton.setSelection(false);
		switch (field.getRefBy()) {
		case IField.REFBY_KEY:
			if (refByKeyButton != null)
				refByKeyButton.setSelection(true);
			break;
		case IField.REFBY_KEYRESULT:
			if (refByKeyResultButton != null)
				refByKeyResultButton.setSelection(true);
			break;
		default:
			if (refByValueButton != null)
				refByValueButton.setSelection(true);
			break;
		}

		if (stereotypeMgr != null) {
			stereotypeMgr.refresh();
		}

		if (getField().getDefaultValue() != null) {
			if (defaultValueText.getItemCount() != 0) {
				String[] items = defaultValueText.getItems();
				int i = 0, index = -1;
				for (String item : items) {
					if (item.equals(getField().getDefaultValue())) {
						index = i;
					}
					i++;
				}
				if (index != -1) {
					defaultValueText.select(index);
				} else {
					defaultValueText.setText("");
					defaultValueText.clearSelection();
				}
			} else {
				defaultValueText.setText(getField().getDefaultValue());
			}
		} else {
			defaultValueText.setText("");
			defaultValueText.clearSelection();
		}

		updateButtonsState();
		setSilentUpdate(false);
	}

	private void setVisibility(EVisibility visibility) {
		publicButton.setSelection(visibility.equals(EVisibility.PUBLIC));
		protectedButton.setSelection(visibility.equals(EVisibility.PROTECTED));
		privateButton.setSelection(visibility.equals(EVisibility.PRIVATE));
		packageButton.setSelection(visibility.equals(EVisibility.PACKAGE));
	}

	private EVisibility getVisibility() {
		if (publicButton.getSelection())
			return EVisibility.PUBLIC;
		else if (protectedButton.getSelection())
			return EVisibility.PROTECTED;
		else if (privateButton.getSelection())
			return EVisibility.PRIVATE;
		else
			return EVisibility.PACKAGE;
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	private boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	public void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == optionalButton) {
			getField().setOptional(optionalButton.getSelection());
			pageModified();
		} else if (e.getSource() == readonlyButton) {
			getField().setReadOnly(readonlyButton.getSelection());
			pageModified();
		} else if (e.getSource() == orderedButton) {
			getField().setOrdered(orderedButton.getSelection());
			pageModified();
		} else if (e.getSource() == uniqueButton) {
			getField().setUnique(uniqueButton.getSelection());
			pageModified();
		} else if (e.getSource() == publicButton
				|| e.getSource() == privateButton
				|| e.getSource() == protectedButton
				|| e.getSource() == packageButton) {
			getField().setVisibility(getVisibility());
			pageModified();
		} else if (e.getSource() == multiplicityCombo) {
			IType type = getField().getType();
			IModelComponent.EMultiplicity mult = IModelComponent.EMultiplicity
					.values()[multiplicityCombo.getSelectionIndex()];
			type.setTypeMultiplicity(mult);
			pageModified();
		} else if (e.getSource() == typeBrowseButton) {
			browseButtonPressed();
		} else if (e.getSource() == refByKeyButton
				|| e.getSource() == refByKeyResultButton
				|| e.getSource() == refByValueButton) {
			if (refByKeyButton.getSelection()) {
				field.setRefBy(IField.REFBY_KEY);
				pageModified();
			} else if (refByKeyResultButton.getSelection()) {
				field.setRefBy(IField.REFBY_KEYRESULT);
				pageModified();
			} else {
				field.setRefBy(IField.REFBY_VALUE);
				pageModified();
			}
		}
		updateButtonsState();
	}

	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			if (e.getSource() == nameText) {
				getField().setName(nameText.getText().trim());
				if (master != null) {
					TableViewer viewer = master.getViewer();
					viewer.refresh(getField());
				}
			} else if (e.getSource() == typeText) {
				IType type = getField().getType();
				type.setFullyQualifiedName(typeText.getText().trim());

				updateDefaultValueCombo();
			} else if (e.getSource() == commentText) {
				getField().setComment(commentText.getText().trim());
			} else if (e.getSource() == defaultValueText) {
				if (defaultValueText.getText().trim().length() == 0) {
					getField().setDefaultValue(null);
				} else
					getField().setDefaultValue(
							defaultValueText.getText().trim());
			}
			updateButtonsState();
			pageModified();
		}
	}

	private void updateDefaultValueCombo() {
		// Update the default value control based on the field type
		if (getField().getType() != null) {
			Type type = (Type) getField().getType();
			IAbstractArtifact art = type.getArtifact();
			if (art instanceof IEnumArtifact) {
				IEnumArtifact enumArt = (IEnumArtifact) art;
				String[] items = new String[enumArt.getLiterals().size()];
				int i = 0;
				for (ILiteral literal : enumArt.getLiterals()) {
					items[i] = literal.getName();
					i++;
				}
				defaultValueText.setItems(items);
				defaultValueText.setEditable(false);
			} else if (type.getFullyQualifiedName().equals("boolean")) {
				defaultValueText.setItems(new String[] { "true", "false", "" });
				defaultValueText.setEditable(false);
				defaultValueText.select(2);
			} else {
				defaultValueText.setItems(new String[0]);
				defaultValueText.setEditable(true);
			}
		}
	}

	/**
	 * Opens up a dialog box to browse for Artifacts
	 * 
	 * @since 1.1
	 */
	private void browseButtonPressed() {

		try {
			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(master
					.getIArtifact().getTigerstripeProject(), Field
					.getSuitableTypes());
			dialog.setTitle("Artifact Type Selection");
			dialog.setMessage("Enter a filter (* = any number of characters)"
					+ " or an empty string for no filtering: ");

			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
					master.getSection().getShell(),
					new ArrayList<IAbstractArtifact>());
			if (artifacts.length != 0) {
				typeText.setText(artifacts[0].getFullyQualifiedName());
				pageModified();
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void navigateToKeyPressed(KeyEvent e) {
		master.navigateToKeyPressed(e);
	}

	public Table getAnnTable() {
		return annTable;
	}

	public IManagedForm getForm() {
		return form;
	}

}
