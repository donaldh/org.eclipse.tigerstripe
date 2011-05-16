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
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ConstantInfoEditComponent {

	private final static String[] supportedPrimitiveTypes = { "int", "String" };

	private final boolean isReadOnly;
	private final FormToolkit toolkit;
	private final Handler handler;

	private ILiteral literal;

	private Button addAnno;

	private Button editAnno;

	private Button removeAnno;

	private Table annTable;

	private boolean silentUpdate = false;

	public ConstantInfoEditComponent(boolean isReadOnly, FormToolkit toolkit,
			Handler handler) {
		this.isReadOnly = isReadOnly;
		this.toolkit = toolkit;
		this.handler = handler;
	}

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

	private Text valueText;

	private CCombo baseTypeCombo;

	private Text commentText;

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		parent.setLayoutData(td);

		ConstantDetailsPageListener adapter = new ConstantDetailsPageListener();

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

		label = toolkit.createLabel(sectionClient, "Value: ");
		label.setEnabled(!isReadOnly);
		valueText = toolkit.createText(sectionClient, "", SWT.BORDER);
		valueText.setEnabled(!isReadOnly);
		valueText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		valueText.addModifyListener(adapter);

		label = toolkit.createLabel(sectionClient, "Type: ");
		label.setEnabled(!isReadOnly);
		baseTypeCombo = new CCombo(sectionClient, SWT.SINGLE | SWT.READ_ONLY
				| SWT.BORDER);
		baseTypeCombo.setEnabled(!isReadOnly);
		toolkit.adapt(this.baseTypeCombo, true, true);

		for (int i = 0; i < supportedPrimitiveTypes.length; i++) {
			baseTypeCombo.add(supportedPrimitiveTypes[i]);
		}
		baseTypeCombo.addSelectionListener(adapter);
		baseTypeCombo.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		createStereotypes(sectionClient);

		section.setClient(sectionClient);
	}

	// ===================================================================

	private Button publicButton;

	private Button protectedButton;

	private Button privateButton;

	private Composite sectionClient;

	public void update() {

		if (literal == null) {
			return;
		}

		setSilentUpdate(true);
		nameText.setText(literal.getName());

		String typeFqn = Misc.removeJavaLangString(literal.getType()
				.getFullyQualifiedName());

		for (int i = 0; i < supportedPrimitiveTypes.length; i++) {
			if (typeFqn.equals(supportedPrimitiveTypes[i])) {
				baseTypeCombo.select(i);
				if (literal.getContainingArtifact() instanceof EnumArtifact) {
					baseTypeCombo.setEnabled(false);
				}
			}
		}

		valueText.setText(literal.getValue());
		setVisibility(literal.getVisibility());
		commentText.setText(literal.getComment() != null ? literal.getComment()
				: "");

		handler.afterUpdate();

		setSilentUpdate(false);
	}

	private void setVisibility(EVisibility visibility) {
		if (visibility == null) {
			visibility = EVisibility.PUBLIC;
		}
		publicButton.setSelection(visibility.equals(EVisibility.PUBLIC));
		protectedButton.setSelection(visibility.equals(EVisibility.PROTECTED));
		privateButton.setSelection(visibility.equals(EVisibility.PRIVATE));
	}

	private EVisibility getVisibility() {
		if (publicButton.getSelection())
			return EVisibility.PUBLIC;
		else if (protectedButton.getSelection())
			return EVisibility.PROTECTED;
		else
			return EVisibility.PRIVATE;
	}

	private void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == publicButton || e.getSource() == privateButton
				|| e.getSource() == protectedButton) {
			literal.setVisibility(getVisibility());
		} else if (e.getSource() == baseTypeCombo) {
			IType type = literal.makeType();
			type.setFullyQualifiedName(baseTypeCombo.getItem(baseTypeCombo
					.getSelectionIndex()));
			type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
			;
			literal.setType(type);
		}
		handler.stateModified();
	}

	private void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			if (e.getSource() == nameText) {
				literal.setName(nameText.getText().trim());
				handler.refresh();
			} else if (e.getSource() == valueText) {
				literal.setValue(valueText.getText().trim());
				handler.refresh();
			} else if (e.getSource() == commentText) {
				literal.setComment(commentText.getText().trim());
			}

			handler.stateModified();
		}
	}

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class ConstantDetailsPageListener implements ModifyListener,
			KeyListener, SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.F3)
				handler.navigateToKeyPressed(e);
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	public void setLiteral(ILiteral literal) {
		this.literal = literal;
		setEnabled(literal != null && !isReadOnly);
	}

	public void setEnabled(boolean value) {
		ComponentUtils.setEnabledAll(sectionClient, value);
	}

	public ILiteral getLiteral() {
		return literal;
	}

	public void setFocus() {
		nameText.setFocus();
	}

	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	private boolean isSilentUpdate() {
		return this.silentUpdate;
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

	public Text getValueText() {
		return valueText;
	}

	public CCombo getBaseTypeCombo() {
		return baseTypeCombo;
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

	public Composite getSectionClient() {
		return sectionClient;
	}

	public static interface Handler {

		void afterUpdate();

		void refresh();

		void navigateToKeyPressed(KeyEvent e);

		void stateModified();

	}

}
