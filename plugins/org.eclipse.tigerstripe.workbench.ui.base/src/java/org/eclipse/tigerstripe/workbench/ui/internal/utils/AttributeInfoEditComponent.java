/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import static org.eclipse.jface.layout.GridLayoutFactory.fillDefaults;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.Field;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AttributeInfoEditComponent {

	private final boolean isReadOnly;
	private final FormToolkit toolkit;
	private final Shell shell;
	private final Handler handler;

	public AttributeInfoEditComponent(boolean isReadOnly, FormToolkit toolkit,
			Shell shell, Handler handler) {
		this.isReadOnly = isReadOnly;
		this.toolkit = toolkit;
		this.shell = shell;
		this.handler = handler;
	}

	private IField field;

	private boolean silentUpdate = false;

	private Button refByValueButton;

	private Button refByKeyButton;

	private Button refByKeyResultButton;

	private Button addAnno;

	private Button editAnno;

	private Button removeAnno;

	private Table annTable;

	private void createStereotypes(Composite parent) {
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

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		parent.setLayoutData(td);

		AttributeDetailsPageListener adapter = new AttributeDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		sectionClient = toolkit.createComposite(section);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		sectionClient.setLayout(layout);
		sectionClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		label.setEnabled(!isReadOnly);
		nameText = toolkit.createText(sectionClient, "", SWT.BORDER);
		nameText.setEnabled(!isReadOnly);
		nameText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		nameText.addModifyListener(adapter);

		label = toolkit.createLabel(sectionClient, "Description: ");
		label.setEnabled(!isReadOnly);
		commentText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		commentText.setEnabled(!isReadOnly);
		commentText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		commentText.addModifyListener(adapter);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		gd.heightHint = 70;
		gd.maxHeight = 70;
		commentText.setLayoutData(gd);

		label = toolkit.createLabel(sectionClient, "Type: ");
		label.setEnabled(!isReadOnly);

		Composite c = toolkit.createComposite(sectionClient);
		fillDefaults().numColumns(2).margins(1, 2).applyTo(c);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		typeText = toolkit.createText(c, "", SWT.BORDER);
		typeText.setEnabled(!isReadOnly);
		typeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		typeBrowseButton = toolkit.createButton(c, " Browse ", SWT.PUSH);
		typeBrowseButton.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		typeBrowseButton.setEnabled(!isReadOnly);
		typeBrowseButton.addSelectionListener(adapter);
		typeText.addModifyListener(adapter);
		typeText.addKeyListener(adapter);

		label = toolkit.createLabel(sectionClient, "Multiplicity: ");
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
		label.setEnabled(!isReadOnly);

		defaultValueText = new CCombo(sectionClient, SWT.SINGLE | SWT.BORDER);
		toolkit.adapt(this.defaultValueText, true, true);
		defaultValueText.setEnabled(!isReadOnly);
		defaultValueText.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		defaultValueText.addModifyListener(adapter);

		createStereotypes(sectionClient);

		section.setClient(sectionClient);
	}

	private Button publicButton;

	private Button protectedButton;

	private Button privateButton;

	private Button packageButton;

	private Composite sectionClient;

	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	private boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	private void updateButtonsState() {
		setSilentUpdate(true);
		IType fieldType = field.getType();
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
		if (field.getType().getTypeMultiplicity().isArray()) {
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

	public void update() {

		if (field == null) {
			return;
		}

		setSilentUpdate(true);
		nameText.setText(field.getName());

		if (field.getType() != null) {
			typeText.setText(Misc.removeJavaLangString(field.getType()
					.getFullyQualifiedName()));
			IModelComponent.EMultiplicity mult = field.getType()
					.getTypeMultiplicity();
			multiplicityCombo.select(IModelComponent.EMultiplicity
					.indexOf(mult));
			updateDefaultValueCombo();
		}

		setVisibility(field.getVisibility());
		commentText.setText(field.getComment() != null ? field.getComment()
				: "");

		if (optionalButton != null)
			optionalButton.setSelection(field.isOptional());
		readonlyButton.setSelection(field.isReadOnly());
		orderedButton.setSelection(field.isOrdered());
		uniqueButton.setSelection(field.isUnique());

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

		if (field.getDefaultValue() != null) {
			if (defaultValueText.getItemCount() != 0) {
				String[] items = defaultValueText.getItems();
				int i = 0, index = -1;
				for (String item : items) {
					if (item.equals(field.getDefaultValue())) {
						index = i;
					}
					i++;
				}
				if (index != -1) {
					defaultValueText.select(index);
				} else {
					defaultValueText.clearSelection();
					defaultValueText.setText(field.getDefaultValue());
				}
			} else {
				defaultValueText.setText(field.getDefaultValue());
			}
		} else {
			defaultValueText.setText("");
			defaultValueText.clearSelection();
		}

		handler.afterUpdate();

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

	public void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == optionalButton) {
			field.setOptional(optionalButton.getSelection());
			handler.stateModified();
		} else if (e.getSource() == readonlyButton) {
			field.setReadOnly(readonlyButton.getSelection());
			handler.stateModified();
		} else if (e.getSource() == orderedButton) {
			field.setOrdered(orderedButton.getSelection());
			handler.stateModified();
		} else if (e.getSource() == uniqueButton) {
			field.setUnique(uniqueButton.getSelection());
			handler.stateModified();
		} else if (e.getSource() == publicButton
				|| e.getSource() == privateButton
				|| e.getSource() == protectedButton
				|| e.getSource() == packageButton) {
			field.setVisibility(getVisibility());
			handler.stateModified();
		} else if (e.getSource() == multiplicityCombo) {
			IType type = field.getType();
			IModelComponent.EMultiplicity mult = IModelComponent.EMultiplicity
					.values()[multiplicityCombo.getSelectionIndex()];
			type.setTypeMultiplicity(mult);
			handler.stateModified();
		} else if (e.getSource() == typeBrowseButton) {
			browseButtonPressed();
		} else if (e.getSource() == refByKeyButton
				|| e.getSource() == refByKeyResultButton
				|| e.getSource() == refByValueButton) {
			if (refByKeyButton.getSelection()) {
				field.setRefBy(IField.REFBY_KEY);
			} else if (refByKeyResultButton.getSelection()) {
				field.setRefBy(IField.REFBY_KEYRESULT);
			} else {
				field.setRefBy(IField.REFBY_VALUE);
			}
			handler.stateModified();
		}
		updateButtonsState();
	}

	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			if (e.getSource() == nameText) {
				field.setName(nameText.getText().trim());
				handler.refresh();
			} else if (e.getSource() == typeText) {
				IType type = field.getType();
				type.setFullyQualifiedName(typeText.getText().trim());

				updateDefaultValueCombo();
			} else if (e.getSource() == commentText) {
				field.setComment(commentText.getText().trim());
			} else if (e.getSource() == defaultValueText) {
				if (defaultValueText.getText().trim().length() == 0) {
					field.setDefaultValue(null);
				} else
					field.setDefaultValue(defaultValueText.getText().trim());
			}
			updateButtonsState();
			handler.stateModified();
		}
	}

	private void updateDefaultValueCombo() {
		// Update the default value control based on the field type
		if (field.getType() != null) {
			Type type = (Type) field.getType();
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
			if (field == null) {
				return;
			}

			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(field
					.getProject(), Field.getSuitableTypes());
			dialog.setTitle("Artifact Type Selection");
			dialog.setMessage("Enter a filter (* = any number of characters)"
					+ " or an empty string for no filtering: ");

			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
					shell, new ArrayList<IAbstractArtifact>());
			if (artifacts.length != 0) {
				typeText.setText(artifacts[0].getFullyQualifiedName());
				handler.stateModified();
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void setFocus() {
		nameText.setFocus();
	}

	public void setIField(IField field) {
		this.field = field;
		setEnabled(field != null && !isReadOnly);
	}

	public IField getField() {
		return field;
	}

	public void setEnabled(boolean value) {
		ComponentUtils.setEnabledAll(sectionClient, value);
	}

	public Button getRefByValueButton() {
		return refByValueButton;
	}

	public Button getRefByKeyButton() {
		return refByKeyButton;
	}

	public Button getRefByKeyResultButton() {
		return refByKeyResultButton;
	}

	public Button getAddAnno() {
		return addAnno;
	}

	public Button getEditAnno() {
		return editAnno;
	}

	public Button getRemoveAnno() {
		return removeAnno;
	}

	public Table getAnnTable() {
		return annTable;
	}

	public Text getNameText() {
		return nameText;
	}

	public Text getTypeText() {
		return typeText;
	}

	public CCombo getDefaultValueText() {
		return defaultValueText;
	}

	public Button getTypeBrowseButton() {
		return typeBrowseButton;
	}

	public CCombo getMultiplicityCombo() {
		return multiplicityCombo;
	}

	public Button getOptionalButton() {
		return optionalButton;
	}

	public Button getReadonlyButton() {
		return readonlyButton;
	}

	public Button getOrderedButton() {
		return orderedButton;
	}

	public Button getUniqueButton() {
		return uniqueButton;
	}

	public Text getCommentText() {
		return commentText;
	}

	public Button getPublicButton() {
		return publicButton;
	}

	public Button getProtectedButton() {
		return protectedButton;
	}

	public Button getPrivateButton() {
		return privateButton;
	}

	public Button getPackageButton() {
		return packageButton;
	}

	public Composite getSectionClient() {
		return sectionClient;
	}

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
				handler.navigateToKeyPressed(e);
			}
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	public static interface Handler {

		void afterUpdate();

		void navigateToKeyPressed(KeyEvent e);

		void refresh();

		void stateModified();
	}

}
