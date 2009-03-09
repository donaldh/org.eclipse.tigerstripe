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

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.annotation;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationPropertyProviderContribution;
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

public class AnnotationPropertyProviderDetailsPage implements IDetailsPage {


	/**
	 * An adapter that will listen for changes on the form
	 */
	private class AnnotationPropertyProviderDetailsPageListener implements ModifyListener,
			KeyListener, SelectionListener{

		
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

	private AnnotationPropertyProviderSection master;

	private AnnotationPropertyProviderContribution contribution;

	private boolean silentUpdate = false;

	private Text annotationPPClassText;

	private Text annotationPPPriorityText;

	private Text annotationPPContributorText;
	
	public AnnotationPropertyProviderDetailsPage() {
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
	private void setContribution(AnnotationPropertyProviderContribution contribution) {
		this.contribution = contribution;
	}

	private AnnotationPropertyProviderContribution getContribution() {
		return contribution;
	}



	private void createContributionInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		AnnotationPropertyProviderDetailsPageListener adapter = new AnnotationPropertyProviderDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Class: ");
		//label.setEnabled(!isReadOnly);
		annotationPPClassText = toolkit.createText(sectionClient, "");

		annotationPPClassText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		annotationPPClassText.addModifyListener(adapter);
		ConfigEditor editor = (ConfigEditor) ((TigerstripeFormPage) getForm()
				.getContainer()).getEditor();
//		if (!isReadOnly) {
			//nameEditListener = new TextEditListener(editor, "name",
			//		IModelChangeDelta.SET, this);
			//nameText.addModifyListener(nameEditListener);
//		}

		label = toolkit.createLabel(sectionClient, "");

		label = toolkit.createLabel(sectionClient, "Priority: ");
//		label.setEnabled(!isReadOnly);
		annotationPPPriorityText = toolkit.createText(sectionClient, "");
		annotationPPPriorityText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		annotationPPPriorityText.addModifyListener(adapter);
		annotationPPPriorityText.addKeyListener(adapter);

		label = toolkit.createLabel(sectionClient, "");
				
		label = toolkit.createLabel(sectionClient, "Contributor: ");
//		label.setEnabled(!isReadOnly);
		annotationPPContributorText = toolkit.createText(sectionClient, "");
		annotationPPContributorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		annotationPPContributorText.addModifyListener(adapter);
		annotationPPContributorText.addKeyListener(adapter);
		// You cannot ever edit the contrubutor class!
		annotationPPContributorText.setEnabled(false);
		
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
		annotationPPClassText.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof AnnotationPropertyProviderSection) {
			//if (nameEditListener != null)
			//	nameEditListener.reset();

			master = (AnnotationPropertyProviderSection) part;
			Table fieldsTable = master.getViewer().getTable();

			AnnotationPropertyProviderContribution selected = (AnnotationPropertyProviderContribution) fieldsTable.getSelection()[0].getData();
			setContribution(selected);
			updateForm();
		}
	}


	private void updateForm() {

		setSilentUpdate(true);
		annotationPPClassText.setText(getContribution().get_class());
		annotationPPClassText.setEnabled(!getContribution().isReadOnly());
		annotationPPPriorityText.setText(getContribution().getPriority());
		annotationPPPriorityText.setEnabled(!getContribution().isReadOnly());
		annotationPPContributorText.setText(getContribution().getContributor().toString());
		
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
//		if (e.getSource() == optionalButton) {
//			getField().setOptional(optionalButton.getSelection());
//			pageModified();
//		} else if (e.getSource() == readonlyButton) {
//			getField().setReadOnly(readonlyButton.getSelection());
//			pageModified();
//		} else if (e.getSource() == orderedButton) {
//			getField().setOrdered(orderedButton.getSelection());
//			pageModified();
//		} else if (e.getSource() == uniqueButton) {
//			getField().setUnique(uniqueButton.getSelection());
//			pageModified();
//		} else if (e.getSource() == publicButton
//				|| e.getSource() == privateButton
//				|| e.getSource() == protectedButton
//				|| e.getSource() == packageButton) {
//			getField().setVisibility(getVisibility());
//			pageModified();
//		} else if (e.getSource() == multiplicityCombo) {
//			IType type = getField().getType();
//			IModelComponent.EMultiplicity mult = IModelComponent.EMultiplicity
//					.values()[multiplicityCombo.getSelectionIndex()];
//			type.setTypeMultiplicity(mult);
//			pageModified();
//		} else if (e.getSource() == typeBrowseButton) {
//			browseButtonPressed();
//		} else if (e.getSource() == refByKeyButton
//				|| e.getSource() == refByKeyResultButton
//				|| e.getSource() == refByValueButton) {
//			if (refByKeyButton.getSelection()) {
//				contribution.setRefBy(IField.REFBY_KEY);
//				pageModified();
//			} else if (refByKeyResultButton.getSelection()) {
//				contribution.setRefBy(IField.REFBY_KEYRESULT);
//				pageModified();
//			} else {
//				contribution.setRefBy(IField.REFBY_VALUE);
//				pageModified();
//			}
//		}
//		updateButtonsState();
	}

	public void handleModifyText(ModifyEvent e) {
//		if (!isSilentUpdate()) {
//			// when updating the form, the changes to all fields should be
//			// ignored so that the form is not marked as dirty.
//			if (e.getSource() == nameText) {
//				getField().setName(nameText.getText().trim());
//				if (master != null) {
//					TableViewer viewer = master.getViewer();
//					viewer.refresh(getField());
//				}
//			} else if (e.getSource() == typeText) {
//				IType type = getField().getType();
//				type.setFullyQualifiedName(typeText.getText().trim());
//
//				updateDefaultValueCombo();
//			} else if (e.getSource() == commentText) {
//				getField().setComment(commentText.getText().trim());
//			} else if (e.getSource() == defaultValueText) {
//				if (defaultValueText.getText().trim().length() == 0) {
//					getField().setDefaultValue(null);
//				} else
//					getField().setDefaultValue(
//							defaultValueText.getText().trim());
//			}
//			updateButtonsState();
//			pageModified();
//		}
	}

	private void updateDefaultValueCombo() {
//		// Update the default value control based on the field type
//		if (getField().getType() != null) {
//			Type type = (Type) getField().getType();
//			IAbstractArtifact art = type.getArtifact();
//			if (art instanceof IEnumArtifact) {
//				IEnumArtifact enumArt = (IEnumArtifact) art;
//				String[] items = new String[enumArt.getLiterals().size()];
//				int i = 0;
//				for (ILiteral literal : enumArt.getLiterals()) {
//					items[i] = literal.getName();
//					i++;
//				}
//				defaultValueText.setItems(items);
//				defaultValueText.setEditable(false);
//			} else if (type.getFullyQualifiedName().equals("boolean")) {
//				defaultValueText.setItems(new String[] { "true", "false", "" });
//				defaultValueText.setEditable(false);
//				defaultValueText.select(2);
//			} else {
//				defaultValueText.setItems(new String[0]);
//				defaultValueText.setEditable(true);
//			}
//		}
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
