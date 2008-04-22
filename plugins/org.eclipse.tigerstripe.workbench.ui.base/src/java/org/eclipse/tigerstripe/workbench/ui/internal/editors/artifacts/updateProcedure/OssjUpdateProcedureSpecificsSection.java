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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.updateProcedure;

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
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjUpdateProcedureSpecifics;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IOssjArtifactFormLabelProvider;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjUpdateProcedureSpecificsSection extends ArtifactSectionPart {

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class GeneralInfoPageListener implements ModifyListener,
			KeyListener, SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

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

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

	}

	private boolean silentUpdate = false;

	private Text interfaceText;

	private Button isSingleTypeExtensionOnly;

	private Button isSessionFactoryMethodsButton;

	public OssjUpdateProcedureSpecificsSection(TigerstripeFormPage page,
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
		createInterfaceName(getBody(), getToolkit());
		createSingleExtensionTypeButton(getBody(), getToolkit());
		createSessionBasedFactoriesButton(getBody(), getToolkit());

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
	}

	private void createSessionBasedFactoriesButton(Composite parent,
			FormToolkit toolkit) {
		toolkit.createLabel(parent, "");
		toolkit.createLabel(parent, "");
		isSessionFactoryMethodsButton = toolkit.createButton(parent,
				"Factory methods on " + ArtifactMetadataFactory.INSTANCE.getMetadata(
						ISessionArtifactImpl.class.getName()).getLabel(), SWT.CHECK);
		isSessionFactoryMethodsButton.setEnabled(!isReadonly());
		isSessionFactoryMethodsButton
				.setToolTipText("If checked, no factory method will be generated for the attribute types");
		isSessionFactoryMethodsButton
				.addSelectionListener(new GeneralInfoPageListener());
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

	protected void handleWidgetSelected(SelectionEvent e) {
		IOssjUpdateProcedureSpecifics specifics = (IOssjUpdateProcedureSpecifics) getIArtifact()
				.getIStandardSpecifics();
		if (e.getSource() == isSingleTypeExtensionOnly) {
			specifics.setSingleExtensionType(isSingleTypeExtensionOnly
					.getSelection());
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

		IOssjUpdateProcedureSpecifics specifics = (IOssjUpdateProcedureSpecifics) getIArtifact()
				.getIStandardSpecifics();
		interfaceText.setText(specifics.getInterfaceProperties().getProperty(
				"package"));
		isSingleTypeExtensionOnly.setSelection(specifics
				.isSingleExtensionType());
		isSessionFactoryMethodsButton.setSelection(specifics
				.isSessionFactoryMethods());

		setSilentUpdate(false);
	}
}
