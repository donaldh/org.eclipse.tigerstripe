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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.useCase.header;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
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
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.contract.useCase.IUseCase;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.useCase.UseCaseEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class BodyContentSection extends TigerstripeSectionPart {

	private boolean silentUpdate;

	private Button bodyIsXMLButton;

	// private Label bodySchemaOrDTDLabel;

	// private Text bodySchemaOrDTDText;

	// private Button bodySchemaOrDTDBrowseButton;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DefaultPageListener implements ModifyListener, KeyListener,
			SelectionListener {

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

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

	}

	public BodyContentSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE | ExpandableComposite.COMPACT);
		setTitle("Details");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 3;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		try {
			createID(getBody(), getToolkit());
		} catch (TigerstripeException ee) {
			Status status = new Status(IStatus.WARNING,
					TigerstripePluginConstants.PLUGIN_ID, 111,
					"Unexpected Exception", ee);
			EclipsePlugin.log(status);
		}

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createID(Composite parent, FormToolkit toolkit)
			throws TigerstripeException {
		TableWrapData td = null;

		Composite innerComposite = toolkit.createComposite(parent);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		innerComposite.setLayoutData(td);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		innerComposite.setLayout(layout);

		DefaultPageListener listener = new DefaultPageListener();

		bodyIsXMLButton = toolkit.createButton(innerComposite,
				"Body is defined as an XML Document", SWT.CHECK);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 3;
		bodyIsXMLButton.setLayoutData(gd);
		bodyIsXMLButton.addSelectionListener(listener);

		// bodySchemaOrDTDLabel = toolkit.createLabel(innerComposite,
		// "XML Schema or DTD", SWT.WRAP);
		// gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		// gd.horizontalIndent = 15;
		// bodySchemaOrDTDLabel.setLayoutData(gd);
		//
		// bodySchemaOrDTDText = toolkit.createText(innerComposite, "");
		// gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
		// | GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		// bodySchemaOrDTDText.setLayoutData(gd);
		// bodySchemaOrDTDText.addModifyListener(listener);

		// bodySchemaOrDTDBrowseButton = toolkit.createButton(innerComposite,
		// "Browse", SWT.PUSH);
		// bodySchemaOrDTDBrowseButton.addSelectionListener(listener);
		// gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		// bodySchemaOrDTDBrowseButton.setLayoutData(gd);

		getToolkit().paintBordersFor(innerComposite);
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
			try {
				IUseCase handle = getUseCase();

				// if (e.getSource() == bodySchemaOrDTDText) {
				// handle.setBodySchemaOrDTD(bodySchemaOrDTDText.getText()
				// .trim());
				// }
				// markPageModified();
			} catch (TigerstripeException ee) {
				EclipsePlugin.log(ee);
			}
		}
	}

	protected void markPageModified() {
		UseCaseEditor editor = (UseCaseEditor) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		try {
			IUseCase handle = getUseCase();
			setSilentUpdate(true);
			bodyIsXMLButton.setSelection(handle.bodyIsXML());
			// if (handle.getBodySchemaOrDTD() != null)
			// bodySchemaOrDTDText.setText(handle.getBodySchemaOrDTD());
			// bodySchemaOrDTDLabel.setEnabled(handle.bodyIsXML());
			// bodySchemaOrDTDText.setEnabled(handle.bodyIsXML());
			// bodySchemaOrDTDBrowseButton.setEnabled(handle.bodyIsXML());
		} catch (TigerstripeException e) {
			Status status = new Status(IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID, 222,
					"Error refreshing form Body details section on Use Case", e);
			EclipsePlugin.log(status);
		} finally {
			setSilentUpdate(false);
		}
	}

	private void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == bodyIsXMLButton) {
			try {
				getUseCase().setBodyIsXML(bodyIsXMLButton.getSelection());
				updateForm();
				markPageModified();
			} catch (TigerstripeException ee) {
				EclipsePlugin.log(ee);
			}
			// } else if (e.getSource() == bodySchemaOrDTDBrowseButton) {
			// browseButtonSelected();
		}
	}

	private IUseCase getUseCase() throws TigerstripeException {
		return ((UseCaseEditor) getPage().getEditor()).getUseCase();
	}
}
