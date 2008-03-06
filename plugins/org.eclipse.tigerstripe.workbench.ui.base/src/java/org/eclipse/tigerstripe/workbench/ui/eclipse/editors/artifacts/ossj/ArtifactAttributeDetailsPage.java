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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj;

import java.util.ArrayList;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactComponent;
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
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactAttributeDetailsPage implements IDetailsPage {

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

	private OssjArtifactAttributesSection master;

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

	public ArtifactAttributeDetailsPage(boolean isReadOnly) {
		super();
		this.isReadOnly = isReadOnly;
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		createFieldInfo(parent);

		form.getToolkit().paintBordersFor(parent);
	}

	private void createAnnotations(final Composite parent, FormToolkit toolkit) {

		Label label = toolkit.createLabel(parent, "Annotations:");
		label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		Composite innerComposite = toolkit.createComposite(parent);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		innerComposite.setLayoutData(gd);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		innerComposite.setLayout(layout);

		annTable = toolkit.createTable(innerComposite, SWT.BORDER);
		annTable.setEnabled(!isReadOnly);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 70;
		annTable.setLayoutData(td);

		addAnno = toolkit.createButton(innerComposite, "Add", SWT.PUSH);
		addAnno.setEnabled(!isReadOnly);
		td = new TableWrapData(TableWrapData.FILL);
		addAnno.setLayoutData(td);

		editAnno = toolkit.createButton(innerComposite, "Edit", SWT.PUSH);
		editAnno.setEnabled(!isReadOnly);
		td = new TableWrapData(TableWrapData.FILL);
		editAnno.setLayoutData(td);

		removeAnno = toolkit.createButton(innerComposite, "Remove", SWT.PUSH);
		removeAnno.setEnabled(!isReadOnly);
		// exComposite.setClient(innerComposite);
	}

	// ============================================================
	private void setIField(IField field) {
		this.field = field;
	}

	private IField getField() {
		return field;
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

	private void createFieldInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		AttributeDetailsPageListener adapter = new AttributeDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		label.setEnabled(!isReadOnly);
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(!isReadOnly);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addModifyListener(adapter);

		label = toolkit.createLabel(sectionClient, "");

		label = toolkit.createLabel(sectionClient, "Description: ");
		label.setEnabled(!isReadOnly);
		commentText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		commentText.setEnabled(!isReadOnly);
		commentText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		commentText.addModifyListener(adapter);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 70;
		gd.widthHint = 300;
		commentText.setLayoutData(gd);
		label = toolkit.createLabel(sectionClient, ""); // padding

		label = toolkit.createLabel(sectionClient, "Type: ");
		label.setEnabled(!isReadOnly);
		typeText = toolkit.createText(sectionClient, "");
		typeText.setEnabled(!isReadOnly);
		typeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		typeBrowseButton = toolkit.createButton(sectionClient, "Browse",
				SWT.PUSH);
		typeBrowseButton.setEnabled(!isReadOnly);
		typeBrowseButton.addSelectionListener(adapter);
		typeText.addModifyListener(adapter);
		typeText.addKeyListener(adapter);

		label = toolkit.createLabel(sectionClient, "Multiplicity: ");
		label.setEnabled(!isReadOnly);
		multiplicityCombo = new CCombo(sectionClient, SWT.SINGLE
				| SWT.READ_ONLY | SWT.BORDER);
		multiplicityCombo.setEnabled(!isReadOnly);
		toolkit.adapt(this.multiplicityCombo, true, true);

		for (IModelComponent.EMultiplicity multVal : IModelComponent.EMultiplicity
				.values()) {
			multiplicityCombo.add(multVal.getLabel());
		}
		multiplicityCombo.addModifyListener(adapter);
		multiplicityCombo.addSelectionListener(adapter);
		multiplicityCombo.setVisibleItemCount(IModelComponent.EMultiplicity
				.values().length);

		label = toolkit.createLabel(sectionClient, "");

		label = toolkit.createLabel(sectionClient, "Visibility: ");
		label.setEnabled(!isReadOnly);
		Composite visiComposite = toolkit.createComposite(sectionClient);
		gLayout = new GridLayout();
		gLayout.numColumns = 4;
		visiComposite.setEnabled(!isReadOnly);
		visiComposite.setLayout(gLayout);
		visiComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
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

		label = toolkit.createLabel(sectionClient, "");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		label = toolkit.createLabel(sectionClient, "Qualifiers: ");
		label.setEnabled(!isReadOnly);
		Composite opComposite = toolkit.createComposite(sectionClient);
		gLayout = new GridLayout();
		gLayout.numColumns = 4;
		opComposite.setLayout(gLayout);
		opComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

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

		label = toolkit.createLabel(sectionClient, "");

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEREFBY_MODIFIERS)) {
			label = toolkit.createLabel(sectionClient, "");
			label.setEnabled(!isReadOnly);
			Composite refComposite = toolkit.createComposite(sectionClient);
			gLayout = new GridLayout();
			gLayout.numColumns = 3;
			refComposite.setLayout(gLayout);
			refComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			refByValueButton = toolkit.createButton(refComposite,
					IField.refByLabels[IField.REFBY_VALUE], SWT.RADIO);
			refByValueButton.addSelectionListener(adapter);
			refByKeyButton = toolkit.createButton(refComposite,
					IField.refByLabels[IField.REFBY_KEY], SWT.RADIO);
			refByKeyButton.addSelectionListener(adapter);
			refByKeyResultButton = toolkit.createButton(refComposite,
					IField.refByLabels[IField.REFBY_KEYRESULT], SWT.RADIO);
			refByKeyResultButton.addSelectionListener(adapter);
			label = toolkit.createLabel(sectionClient, "");
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		label = toolkit.createLabel(sectionClient, "Default Value: ");
		label.setEnabled(!isReadOnly);

		defaultValueText = new CCombo(sectionClient, SWT.SINGLE | SWT.BORDER);
		toolkit.adapt(this.defaultValueText, true, true);
		defaultValueText.setEnabled(!isReadOnly);
		defaultValueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		defaultValueText.addModifyListener(adapter);

		toolkit.createLabel(sectionClient, "");

		createAnnotations(sectionClient, form.getToolkit());

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
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
		if (part instanceof OssjArtifactAttributesSection) {
			master = (OssjArtifactAttributesSection) part;
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
					.getIArtifact().getTigerstripeProject(),
					new IAbstractArtifact[0]);
			dialog.setTitle("Artifact Type Selection");
			dialog.setMessage("Enter a filter (* = any number of characters)"
					+ " or an empty string for no filtering: ");

			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
					master.getSection().getShell(), new ArrayList());
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
