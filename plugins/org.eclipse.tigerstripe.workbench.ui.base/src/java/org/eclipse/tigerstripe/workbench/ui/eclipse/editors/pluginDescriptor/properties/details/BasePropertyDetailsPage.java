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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties.details;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties.GlobalPropertiesSection;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public abstract class BasePropertyDetailsPage implements IDetailsPage {

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class GlobalPropertyDetailsPageListener implements ModifyListener,
			KeyListener, SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			// empty
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	protected IManagedForm form;

	private GlobalPropertiesSection master;

	private IPluginProperty property;

	private boolean silentUpdate = false;

	public BasePropertyDetailsPage() {
		super();
	}

	// public void createContents(Composite parent) {
	// TableWrapLayout twLayout = new TableWrapLayout();
	// twLayout.numColumns = 1;
	// parent.setLayout(twLayout);
	// TableWrapData td = new TableWrapData(TableWrapData.FILL);
	// td.heightHint = 200;
	// parent.setLayoutData(td);
	//
	// createPropertyInfo(parent, true);
	//
	// form.getToolkit().paintBordersFor(parent);
	// }

	// ============================================================
	private void setIPluggablePluginProperty(IPluginProperty property) {
		this.property = property;
	}

	protected IPluginProperty getIPluggablePluginProperty() {
		return property;
	}

	// ============================================================
	private Text nameText;

	private Text tipToolText;

	protected Composite createPropertyInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		GlobalPropertyDetailsPageListener adapter = new GlobalPropertyDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(PluginDescriptorEditor.isEditable());
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addModifyListener(adapter);
		nameText.setToolTipText("Name of the property");

		label = toolkit.createLabel(sectionClient, "");
		label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));

		label = toolkit.createLabel(sectionClient, "TipTool Text: ");
		tipToolText = toolkit.createText(sectionClient, "");
		tipToolText.setEnabled(PluginDescriptorEditor.isEditable());
		tipToolText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tipToolText.addModifyListener(adapter);
		tipToolText
				.setToolTipText("Some text displayed to the user when hovering over this property when using the plugin.");

		label = toolkit.createLabel(sectionClient, "");
		label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		return sectionClient;
	}

	// ===================================================================
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
		if (part instanceof GlobalPropertiesSection) {
			master = (GlobalPropertiesSection) part;
			Table fieldsTable = master.getViewer().getTable();

			IPluginProperty selected = (IPluginProperty) fieldsTable
					.getSelection()[0].getData();
			setIPluggablePluginProperty(selected);
			updateForm();
		}
	}

	protected void updateForm() {
		setSilentUpdate(true);
		IPluginProperty prop = getIPluggablePluginProperty();
		nameText.setText(prop.getName());
		tipToolText.setText(prop.getTipToolText());
		setSilentUpdate(false);
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	protected void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	protected boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			if (e.getSource() == nameText) {
				getIPluggablePluginProperty()
						.setName(nameText.getText().trim());
				if (master != null) {
					TableViewer viewer = master.getViewer();
					viewer.refresh(getIPluggablePluginProperty());
				}
			} else if (e.getSource() == tipToolText) {
				getIPluggablePluginProperty().setTipToolText(
						tipToolText.getText().trim());
			}
			pageModified();
		}
	}
}
