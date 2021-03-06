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

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.artifactMetadata;

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
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactIconContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.ConfigEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactIconDetailsPage implements IDetailsPage {


	/**
	 * An adapter that will listen for changes on the form
	 */
	private class ArtifactIconDetailsPageListener implements ModifyListener,
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

	private ArtifactIconSection master;

	private ArtifactIconContribution contribution;

	private boolean silentUpdate = false;

	private Text artifactTypeText;
	
	private Text iconText;
	private Text iconNewText;
	private Text iconGreyText;
	
	
	private Button iconBrowseButton;
	private Button iconNewBrowseButton;
	private Button iconGreyBrowseButton;

	private Text artifactMetadataContributorText;

	public ArtifactIconDetailsPage() {
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

		form.getToolkit().paintBordersFor(parent);
	}


	// ============================================================
	private void setContribution(ArtifactIconContribution contribution) {
		this.contribution = contribution;
	}

	private ArtifactIconContribution getContribution() {
		return contribution;
	}



	private void createContributionInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		ArtifactIconDetailsPageListener adapter = new ArtifactIconDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Artifact Type: ");
		//label.setEnabled(!isReadOnly);
		artifactTypeText = toolkit.createText(sectionClient, "");

		artifactTypeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		artifactTypeText.addModifyListener(adapter);
		ConfigEditor editor = (ConfigEditor) ((TigerstripeFormPage) getForm()
				.getContainer()).getEditor();
//		if (!isReadOnly) {
			//nameEditListener = new TextEditListener(editor, "name",
			//		IModelChangeDelta.SET, this);
			//nameText.addModifyListener(nameEditListener);
//		}

		label = toolkit.createLabel(sectionClient, "");
		
		
		label = toolkit.createLabel(sectionClient, "Icon: ");
		iconText = toolkit.createText(sectionClient, "");
		iconText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		iconBrowseButton = toolkit.createButton(sectionClient, "Browse",
				SWT.PUSH);

		iconBrowseButton.addSelectionListener(adapter);
		
		label = toolkit.createLabel(sectionClient, "Icon New: ");
		iconNewText = toolkit.createText(sectionClient, "");
		iconNewText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		iconNewBrowseButton = toolkit.createButton(sectionClient, "Browse",
				SWT.PUSH);

		iconNewBrowseButton.addSelectionListener(adapter);
		
		label = toolkit.createLabel(sectionClient, "Icon Grey: ");
		iconGreyText = toolkit.createText(sectionClient, "");
		iconGreyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		iconGreyBrowseButton = toolkit.createButton(sectionClient, "Browse",
				SWT.PUSH);

		iconGreyBrowseButton.addSelectionListener(adapter);

		
		iconText.addModifyListener(adapter);
		iconText.addKeyListener(adapter);
		iconNewText.addModifyListener(adapter);
		iconNewText.addKeyListener(adapter);
		iconGreyText.addModifyListener(adapter);
		iconGreyText.addKeyListener(adapter);
		
		label = toolkit.createLabel(sectionClient, "Contributor: ");
//		label.setEnabled(!isReadOnly);
		artifactMetadataContributorText = toolkit.createText(sectionClient, "");
		artifactMetadataContributorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		artifactMetadataContributorText.addModifyListener(adapter);
		artifactMetadataContributorText.addKeyListener(adapter);
		// You cannot ever edit the contrubutor class!
		artifactMetadataContributorText.setEnabled(false);
		
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
		artifactTypeText.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof ArtifactIconSection) {
			//if (nameEditListener != null)
			//	nameEditListener.reset();

			master = (ArtifactIconSection) part;
			Table fieldsTable = master.getViewer().getTable();

			ArtifactIconContribution selected = (ArtifactIconContribution) fieldsTable.getSelection()[0].getData();
			setContribution(selected);
			updateForm();
		}
	}


	private void updateForm() {

		setSilentUpdate(true);
		artifactTypeText.setText(getContribution().getArtifactType());
		artifactTypeText.setEnabled(!getContribution().isReadOnly());
		
		iconText.setText(getContribution().getIcon());
		iconText.setEnabled(!getContribution().isReadOnly());
		
		iconNewText.setText(getContribution().getIcon_new());
		iconNewText.setEnabled(!getContribution().isReadOnly());
		
		iconGreyText.setText(getContribution().getIcon_gs());
		iconGreyText.setEnabled(!getContribution().isReadOnly());
		
		artifactMetadataContributorText.setText(getContribution().getContributor().toString());
		iconBrowseButton.setEnabled(!getContribution().isReadOnly());
		
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

//		try {
//			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(master
//					.getIArtifact().getTigerstripeProject(),
//					Field.getSuitableTypes());
//			dialog.setTitle("Artifact Type Selection");
//			dialog.setMessage("Enter a filter (* = any number of characters)"
//					+ " or an empty string for no filtering: ");
//
//			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
//					master.getSection().getShell(),
//					new ArrayList<IAbstractArtifact>());
//			if (artifacts.length != 0) {
//				typeText.setText(artifacts[0].getFullyQualifiedName());
//				pageModified();
//			}
//		} catch (TigerstripeException e) {
//			EclipsePlugin.log(e);
//		}
	}

	private void navigateToKeyPressed(KeyEvent e) {
		master.navigateToKeyPressed(e);
	}

	public IManagedForm getForm() {
		return form;
	}

}
