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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.enumeration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.ArtifactSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormLabelProvider;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjEnumSpecificsSection extends ArtifactSectionPart {

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class GeneralInfoPageListener implements ModifyListener,
			KeyListener, SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

		public void keyPressed(KeyEvent e) {
			// empty
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
			if (e.character == '\r') {
				commit(false);
			}
		}

	}

	private boolean silentUpdate = false;

	private Text interfaceText;

	private CCombo baseTypeCombo;

	private Button isExtensible;

	public OssjEnumSpecificsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider, 0);

		setTitle("Implementation Specifics");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 3;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		getSection().clientVerticalSpacing = 5;

		Composite body = getBody();
		body.setLayout(layout);
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
		.getWorkbenchProfileSession().getActiveProfile().getProperty(
				IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.DISPLAY_OSSJSPECIFICS)) {
			createInterfaceName(getBody(), getToolkit());
			createExtensibleControl(getBody(), getToolkit());
		}
		createBaseTypeControl(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createInterfaceName(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Interface Package: ",
				SWT.WRAP);
		label.setEnabled(!isReadonly());
		interfaceText = toolkit.createText(parent, "");
		interfaceText.setEnabled(!isReadonly());
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		interfaceText.setLayoutData(td);
		interfaceText.addModifyListener(new GeneralInfoPageListener());
		label = toolkit.createLabel(parent, "", SWT.WRAP);
	}

	private void createBaseTypeControl(Composite parent, FormToolkit toolkit) {
		Label label = toolkit.createLabel(parent, "Base Type: ", SWT.WRAP);
		label.setEnabled(!isReadonly());
		baseTypeCombo = new CCombo(parent, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT);
		baseTypeCombo.setEnabled(!isReadonly());
		toolkit.adapt(this.baseTypeCombo, true, true);

		for (int i = 0; i < IEnumArtifact.baseTypeOptions.length; i++) {
			baseTypeCombo.add(IEnumArtifact.baseTypeOptions[i]);
		}
		baseTypeCombo.addSelectionListener(new GeneralInfoPageListener());
		label = toolkit.createLabel(parent, "", SWT.WRAP);
	}

	private void createExtensibleControl(Composite parent, FormToolkit toolkit) {
		Label label = toolkit.createLabel(parent, "", SWT.WRAP);
		isExtensible = toolkit
				.createButton(parent, "Is extensible?", SWT.CHECK);
		isExtensible.setEnabled(!isReadonly());
		isExtensible.addSelectionListener(new GeneralInfoPageListener());
		label = toolkit.createLabel(parent, "", SWT.WRAP);
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

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.

			if (e.getSource() == interfaceText) {
				IOssjArtifactSpecifics specifics = (IOssjArtifactSpecifics) getIArtifact()
						.getIStandardSpecifics();
				specifics.getInterfaceProperties().setProperty("package",
						interfaceText.getText().trim());
			}
			markPageModified();
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == baseTypeCombo) {
				IOssjEnumSpecifics specifics = (IOssjEnumSpecifics) getIArtifact()
						.getIStandardSpecifics();
				IType type = getIArtifact().makeField().makeType();
				type
						.setFullyQualifiedName(IEnumArtifact.baseTypeOptions[baseTypeCombo
								.getSelectionIndex()]);
				type.setTypeMultiplicity(EMultiplicity.ZERO_STAR);
				specifics.setBaseIType(type);
				markPageModified();
			} else if (e.getSource() == isExtensible) {
				IOssjEnumSpecifics specifics = (IOssjEnumSpecifics) getIArtifact()
						.getIStandardSpecifics();
				specifics.setExtensible(isExtensible.getSelection());
				markPageModified();
			}
		}
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		setSilentUpdate(true);
		IOssjEnumSpecifics specifics = (IOssjEnumSpecifics) getIArtifact()
				.getIStandardSpecifics();
		interfaceText.setText(specifics.getInterfaceProperties().getProperty(
				"package"));

		baseTypeCombo.select(indexOf(specifics.getBaseIType() != null ? Misc
				.removeJavaLangString(specifics.getBaseIType()
						.getFullyQualifiedName()) : ""));

		isExtensible.setSelection(specifics.getExtensible());

		setSilentUpdate(false);
	}

	private int indexOf(String type) {
		for (int i = 0; i < IEnumArtifact.baseTypeOptions.length; i++) {
			if (IEnumArtifact.baseTypeOptions[i].equalsIgnoreCase(type))
				return i;
		}
		return 0;
	}
}
