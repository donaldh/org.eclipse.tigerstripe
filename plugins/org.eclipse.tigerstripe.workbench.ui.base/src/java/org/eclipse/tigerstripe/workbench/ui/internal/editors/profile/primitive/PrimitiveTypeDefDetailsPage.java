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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.primitive;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.ProfileEditor;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class PrimitiveTypeDefDetailsPage implements IDetailsPage {

	private Text nameText;
	private Text packageNameText;
	private Text descriptionText;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class StereotypeDetailsPageListener implements ModifyListener,
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
		}

	}

	protected PrimitiveTypeDefsSection getMaster() {
		return master;
	}

	protected IWorkbenchProfile getProfile() throws TigerstripeException {
		return ((ProfileEditor) getMaster().getPage().getEditor()).getProfile();
	}

	protected IManagedForm form;

	private PrimitiveTypeDefsSection master;

	private IPrimitiveTypeDef primitiveTypeDef;

	private boolean silentUpdate = false;

	public PrimitiveTypeDefDetailsPage(PrimitiveTypeDefsSection master) {
		super();
		this.master = master;
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		Composite sectionClient = createStereotypeInfo(parent);

		form.getToolkit().paintBordersFor(parent);
	}

	protected void handleWidgetSelected(SelectionEvent e) {
	}

	// ============================================================
	private void setIPrimitiveTypeDef(IPrimitiveTypeDef primitiveTypeDef) {
		this.primitiveTypeDef = primitiveTypeDef;
	}

	protected IPrimitiveTypeDef getIPrimitiveTypeDef() {
		return primitiveTypeDef;
	}

	// ============================================================
	protected Composite createStereotypeInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		StereotypeDetailsPageListener adapter = new StereotypeDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 2;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(ProfileEditor.isEditable());
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addModifyListener(adapter);
		nameText
				.setToolTipText("Name of the primitive type, as seen by end-user.");

		label = toolkit.createLabel(sectionClient, "Package: ");
		packageNameText = toolkit.createText(sectionClient, "");
		packageNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		packageNameText.addModifyListener(adapter);
		packageNameText
				.setToolTipText("Package of the primitive type, as seen by end-user.");
		packageNameText.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Description: ");
		descriptionText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		descriptionText.setEnabled(ProfileEditor.isEditable());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 60;
		descriptionText.setLayoutData(gd);
		descriptionText.addModifyListener(adapter);
		descriptionText
				.setToolTipText("Meaning for this primitive type, presented as tooltip to the end-user.");

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
		if (part instanceof PrimitiveTypeDefsSection) {
			master = (PrimitiveTypeDefsSection) part;
			Table fieldsTable = master.getViewer().getTable();

			IPrimitiveTypeDef selected = (IPrimitiveTypeDef) fieldsTable
					.getSelection()[0].getData();
			setIPrimitiveTypeDef(selected);
			updateForm();
		}
	}

	protected void updateForm() {
		setSilentUpdate(true);
		IPrimitiveTypeDef st = getIPrimitiveTypeDef();
		nameText.setText(st.getName());
		packageNameText.setText(st.getPackageName());
		descriptionText.setText(st.getDescription());

		// nameText.setEditable(ProfileEditor.isEditable() && !st.isReserved());
		nameText.setEnabled(ProfileEditor.isEditable() && !st.isReserved());
		// descriptionText.setEditable(ProfileEditor.isEditable() &&
		// !st.isReserved());
		descriptionText.setEnabled(ProfileEditor.isEditable()
				&& !st.isReserved());
		setSilentUpdate(false);
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

	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			if (e.getSource() == nameText) {
				getIPrimitiveTypeDef().setName(nameText.getText().trim());
				if (master != null) {
					TableViewer viewer = master.getViewer();
					viewer.refresh(getIPrimitiveTypeDef());
				}
				pageModified();
			} else if (e.getSource() == descriptionText) {
				getIPrimitiveTypeDef().setDescription(
						descriptionText.getText().trim());
				pageModified();
			} else if (e.getSource() == packageNameText) {
				getIPrimitiveTypeDef().setPackageName(
						packageNameText.getText().trim());
				pageModified();
			}
		}
	}
}
