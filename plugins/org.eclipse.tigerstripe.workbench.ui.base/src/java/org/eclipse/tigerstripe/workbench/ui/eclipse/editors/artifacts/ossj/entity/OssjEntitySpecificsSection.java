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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.entity;

import java.util.ArrayList;

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
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.ArtifactSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormLabelProvider;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjEntitySpecificsSection extends ArtifactSectionPart {

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

	private Text primaryText;

	private CCombo extensibilityCombo;

	private Button isSessionFactoryMethodsButton;

	private Button typeBrowseButton;

	public OssjEntitySpecificsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IOssjArtifactFormLabelProvider labelProvider,
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
			createSingleExtensionTypeButton(getBody(), getToolkit());
			createSessionBasedFactoriesButton(getBody(), getToolkit());
		}
		createPrimaryKeyName(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createInterfaceName(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Interface Package: ",
				SWT.NULL);
		label.setEnabled(!isReadonly());
		interfaceText = toolkit.createText(parent, "");
		interfaceText.setEnabled(!isReadonly());
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		interfaceText.setLayoutData(td);
		interfaceText.addModifyListener(new GeneralInfoPageListener());
		label = toolkit.createLabel(parent, "", SWT.WRAP);
	}

	private void createPrimaryKeyName(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Primary Key Type: ",
				SWT.NULL);
		label.setEnabled(!isReadonly());
		primaryText = toolkit.createText(parent, "");
		primaryText.setEnabled(!isReadonly());
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		primaryText.setLayoutData(td);
		primaryText.addModifyListener(new GeneralInfoPageListener());

		typeBrowseButton = toolkit.createButton(parent, "Browse", SWT.PUSH);
		typeBrowseButton.setEnabled(!isReadonly());
		typeBrowseButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				browseButtonPressed();
			}
		});

		// label = toolkit.createLabel(parent, "", SWT.WRAP);
	}

	private void browseButtonPressed() {

		try {
			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
					getIArtifact().getTigerstripeProject(),
					new IAbstractArtifact[] { PrimitiveTypeArtifact.MODEL,
							DatatypeArtifact.MODEL });
			dialog.setTitle("Artifact Type Selection");
			dialog.setMessage("Enter a filter (* = any number of characters)"
					+ " or an empty string for no filtering: ");

			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
					getSection().getShell(), new ArrayList());
			if (artifacts.length != 0) {
				primaryText.setText(artifacts[0].getFullyQualifiedName());
				markPageModified();
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void createSingleExtensionTypeButton(Composite parent,
			FormToolkit toolkit) {

		Label label = toolkit.createLabel(parent, "Extensibility: ", SWT.WRAP);
		label.setEnabled(!isReadonly());
		extensibilityCombo = new CCombo(parent, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT);
		extensibilityCombo.setEnabled(!isReadonly());
		toolkit.adapt(this.extensibilityCombo, true, true);

		for (int i = 0; i < IOssjEntitySpecifics.EXT_OPTIONS.length; i++) {
			extensibilityCombo.add(IOssjEntitySpecifics.EXT_OPTIONS[i]);
		}
		extensibilityCombo.addSelectionListener(new GeneralInfoPageListener());
		toolkit.createLabel(parent, "");
	}

	private void createSessionBasedFactoriesButton(Composite parent,
			FormToolkit toolkit) {
		toolkit.createLabel(parent, "");
		isSessionFactoryMethodsButton = toolkit.createButton(parent,
				"Factory methods on " + ArtifactMetadataFactory.INSTANCE.getMetadata(
						ISessionArtifactImpl.class.getName()).getLabel(), SWT.CHECK);
		isSessionFactoryMethodsButton.setEnabled(!isReadonly());
		isSessionFactoryMethodsButton
				.setToolTipText("If checked, no factory method will be generated for the attribute types");
		isSessionFactoryMethodsButton
				.addSelectionListener(new GeneralInfoPageListener());
		toolkit.createLabel(parent, "");
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		IOssjEntitySpecifics specifics = (IOssjEntitySpecifics) getIArtifact()
				.getIStandardSpecifics();
		if (e.getSource() == extensibilityCombo) {
			String sel = IOssjEntitySpecifics.EXT_OPTIONS[extensibilityCombo
					.getSelectionIndex()];
			specifics.setExtensibilityType(sel);
			markPageModified();
		} else if (e.getSource() == isSessionFactoryMethodsButton) {
			specifics.setSessionFactoryMethods(isSessionFactoryMethodsButton
					.getSelection());
			markPageModified();
		}
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
			} else if (e.getSource() == primaryText) {
				IOssjEntitySpecifics specifics = (IOssjEntitySpecifics) getIArtifact()
						.getIStandardSpecifics();
				specifics.setPrimaryKey(primaryText.getText().trim());
			}
			markPageModified();
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
		IOssjEntitySpecifics specifics = (IOssjEntitySpecifics) getIArtifact()
				.getIStandardSpecifics();
		if (interfaceText != null)
			interfaceText.setText(specifics.getInterfaceProperties()
					.getProperty("package"));

		primaryText.setText(specifics.getPrimaryKey() != null ? Misc
				.removeJavaLangString(specifics.getPrimaryKey()) : "");

		if (extensibilityCombo != null) {
			String ext = specifics.getExtensibilityType();
			for (int i = 0; i < IOssjEntitySpecifics.EXT_OPTIONS.length; i++) {
				if (IOssjEntitySpecifics.EXT_OPTIONS[i].equals(ext)) {
					extensibilityCombo.select(i);
				}
			}
		}

		if (isSessionFactoryMethodsButton != null) {
			isSessionFactoryMethodsButton.setSelection(specifics
					.isSessionFactoryMethods());
		}
		setSilentUpdate(false);
	}
}
