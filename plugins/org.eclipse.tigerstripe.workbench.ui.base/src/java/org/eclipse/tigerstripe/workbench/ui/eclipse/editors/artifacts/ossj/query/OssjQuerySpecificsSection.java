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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.query;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
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
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjQuerySpecifics;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.ArtifactSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormLabelProvider;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjQuerySpecificsSection extends ArtifactSectionPart {

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class GeneralInfoPageListener implements ModifyListener,
			KeyListener, SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

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
			if (e.character == '\r') {
				commit(false);
			}
		}

		public void widgetSelected(SelectionEvent e) {
			try {
				handleWidgetSelected(e);
			} catch (TigerstripeException ee) {
				Status status = new Status(IStatus.WARNING,
						TigerstripePluginConstants.PLUGIN_ID, 111,
						"Unexpected Exception", ee);
				EclipsePlugin.log(status);
			}
		}

	}

	private boolean silentUpdate = false;

	private Text interfaceText;

	private Text returnedEntityTypeText;

	private Button isSingleTypeExtensionOnly;

	private Button isSessionFactoryMethodsButton;

	private Button typeBrowseButton;

	public OssjQuerySpecificsSection(TigerstripeFormPage page,
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
				.getIWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.DISPLAY_OSSJSPECIFICS)) {

			createInterfaceName(getBody(), getToolkit());
			createSingleExtensionTypeButton(getBody(), getToolkit());
			createSessionBasedFactoriesButton(getBody(), getToolkit());
		}
		createReturnedEntityTypeName(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createSingleExtensionTypeButton(Composite parent,
			FormToolkit toolkit) {
		toolkit.createLabel(parent, "");
		isSingleTypeExtensionOnly = toolkit.createButton(parent,
				"single extension only", SWT.CHECK);
		isSingleTypeExtensionOnly.setEnabled(!isReadonly());
		isSingleTypeExtensionOnly
				.addSelectionListener(new GeneralInfoPageListener());
		toolkit.createLabel(parent, "");
	}

	private void createSessionBasedFactoriesButton(Composite parent,
			FormToolkit toolkit) {
		toolkit.createLabel(parent, "");
		isSessionFactoryMethodsButton = toolkit.createButton(parent,
				"Factory methods on Session Facade", SWT.CHECK);
		isSessionFactoryMethodsButton.setEnabled(!isReadonly());
		isSessionFactoryMethodsButton
				.addSelectionListener(new GeneralInfoPageListener());
		isSessionFactoryMethodsButton
				.setToolTipText("If checked, no factory method will be generated for the attribute types");
		toolkit.createLabel(parent, "");
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

	private void createReturnedEntityTypeName(Composite parent,
			FormToolkit toolkit) {
		TableWrapData td = null;

		Label label = toolkit.createLabel(parent, "Returned Type: ", SWT.WRAP);
		label.setEnabled(!isReadonly());
		returnedEntityTypeText = toolkit.createText(parent, "");
		returnedEntityTypeText.setEnabled(!isReadonly());
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		returnedEntityTypeText.setLayoutData(td);
		GeneralInfoPageListener listener = new GeneralInfoPageListener();
		returnedEntityTypeText.addModifyListener(listener);
		returnedEntityTypeText.addKeyListener(listener);

		typeBrowseButton = toolkit.createButton(parent, "Browse", SWT.PUSH);
		typeBrowseButton.setEnabled(!isReadonly());
		typeBrowseButton.addSelectionListener(listener);
	}

	public void handleWidgetSelected(SelectionEvent e)
			throws TigerstripeException {
		IOssjQuerySpecifics specifics = (IOssjQuerySpecifics) getIArtifact()
				.getIStandardSpecifics();
		if (e.getSource() == typeBrowseButton) {
			browseButtonPressed();
		} else if (e.getSource() == isSingleTypeExtensionOnly) {
			specifics.setSingleExtensionType(isSingleTypeExtensionOnly
					.getSelection());
			markPageModified();
		} else if (e.getSource() == isSessionFactoryMethodsButton) {
			specifics.setSessionFactoryMethods(isSessionFactoryMethodsButton
					.getSelection());
			markPageModified();
		}
	}

	private void browseButtonPressed() throws TigerstripeException {
		BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
				getIArtifact().getIProject(), new IAbstractArtifact[] {
						ManagedEntityArtifact.MODEL, DatatypeArtifact.MODEL });
		dialog.setTitle("Returned Type");
		dialog.setMessage("Select the type of the returned Entities.");

		IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
				getSection().getShell(), Arrays.asList(new Object[0]));
		if (artifacts.length != 0) {
			returnedEntityTypeText
					.setText(artifacts[0].getFullyQualifiedName());
			IOssjQuerySpecifics specifics = (IOssjQuerySpecifics) getIArtifact()
					.getIStandardSpecifics();
			IType type = getIArtifact().makeField().makeType();
			type.setFullyQualifiedName(returnedEntityTypeText.getText().trim());
			specifics.setReturnedEntityIType(type);
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
			} else if (e.getSource() == returnedEntityTypeText) {
				IOssjQuerySpecifics specifics = (IOssjQuerySpecifics) getIArtifact()
						.getIStandardSpecifics();
				IType type = getIArtifact().makeField().makeType();
				type.setFullyQualifiedName(returnedEntityTypeText.getText()
						.trim());
				specifics.setReturnedEntityIType(type);
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

		IOssjQuerySpecifics specifics = (IOssjQuerySpecifics) getIArtifact()
				.getIStandardSpecifics();

		if (interfaceText != null) {
			interfaceText.setText(specifics.getInterfaceProperties()
					.getProperty("package"));
		}
		returnedEntityTypeText
				.setText(specifics.getReturnedEntityIType() != null ? specifics
						.getReturnedEntityIType().getFullyQualifiedName() : "");

		if (isSingleTypeExtensionOnly != null) {
			isSingleTypeExtensionOnly.setSelection(specifics
					.isSingleExtensionType());
		}

		if (isSessionFactoryMethodsButton != null) {
			isSessionFactoryMethodsButton.setSelection(specifics
					.isSessionFactoryMethods());
		}
		setSilentUpdate(false);
	}
}
