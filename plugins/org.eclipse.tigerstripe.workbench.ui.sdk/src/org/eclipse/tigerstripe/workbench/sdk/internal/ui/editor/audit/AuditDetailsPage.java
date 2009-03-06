/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.audit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationUsageExtractor;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AuditContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.common.CommonDetailsPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AuditDetailsPage extends CommonDetailsPage implements IDetailsPage {


	/**
	 * An adapter that will listen for changes on the form
	 */
	private class AuditDetailsPageListener implements ModifyListener,
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

	private AuditSection master;

	private AuditContribution contribution;

	private boolean silentUpdate = false;

	private Text auditNameText;

	private Text auditClassText;

	private Button classBrowseButton;

	private Text auditContributorText;

	private AnnotationUsageExtractor extractor;
	
	public AuditDetailsPage() {
		super();
		
		
	}

	
	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		createContributionInfo(parent);
		createUsageTable(parent, form.getToolkit());

		form.getToolkit().paintBordersFor(parent);
	}


	// ============================================================
	private void setAuditContribution(AuditContribution contribution) {
		this.contribution = contribution;
	}

	private AuditContribution getContribution() {
		return contribution;
	}



	private void createContributionInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		AuditDetailsPageListener adapter = new AuditDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		auditNameText = toolkit.createText(sectionClient, "");
		auditNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		auditNameText.addModifyListener(adapter);
		TigerstripeFormEditor editor = (TigerstripeFormEditor) ((TigerstripeFormPage) getForm()
				.getContainer()).getEditor();
		label = toolkit.createLabel(sectionClient, "");


		label = toolkit.createLabel(sectionClient, "Audit Class: ");
		auditClassText = toolkit.createText(sectionClient, "");
		auditClassText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		classBrowseButton = toolkit.createButton(sectionClient, "Browse",
				SWT.PUSH);
		classBrowseButton.addSelectionListener(adapter);
		auditClassText.addModifyListener(adapter);
		auditClassText.addKeyListener(adapter);

				
		label = toolkit.createLabel(sectionClient, "Contributor: ");
		auditContributorText = toolkit.createText(sectionClient, "");
		auditContributorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		auditContributorText.addModifyListener(adapter);
		auditContributorText.addKeyListener(adapter);
		auditContributorText.setEnabled(false);
		
		label = toolkit.createLabel(sectionClient, "");

		
		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
	}


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
		auditNameText.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof AuditSection) {
			//if (nameEditListener != null)
			//	nameEditListener.reset();

			master = (AuditSection) part;
			extractor = master.getProvider().getExtractor();
			Table fieldsTable = master.getViewer().getTable();

			AuditContribution selected = (AuditContribution) fieldsTable.getSelection()[0].getData();
			setAuditContribution(selected);
			updateForm();
		}
	}


	private void updateForm() {

		setSilentUpdate(true);
		auditNameText.setText(getContribution().getName());
		auditNameText.setEnabled(!getContribution().isReadOnly());
		auditClassText.setText(getContribution().getAuditorClass());
		auditClassText.setEnabled(!getContribution().isReadOnly());
		auditContributorText.setText(getContribution().getContributor().toString());
		classBrowseButton.setEnabled(!getContribution().isReadOnly());
		
		usageViewer.setInput(extractor.getAuditMap().get(getContribution()));
		
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


	public void handleWidgetSelected(SelectionEvent e) {

	}

	public void handleModifyText(ModifyEvent e) {
	}

	/**
	 * Opens up a dialog box to browse for Artifacts
	 * 
	 * @since 1.1
	 */
	private void browseButtonPressed() {

	}

	private void navigateToKeyPressed(KeyEvent e) {
		master.navigateToKeyPressed(e);
	}

	public IManagedForm getForm() {
		return form;
	}

}
