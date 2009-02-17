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

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution;
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

public class AnnotationDetailsPage implements IDetailsPage {


	/**
	 * An adapter that will listen for changes on the form
	 */
	private class AnnotationDetailsPageListener implements ModifyListener,
			KeyListener, SelectionListener, IDoubleClickListener{

		public void doubleClick(DoubleClickEvent event) {
			if (event.getSource() == targetViewer) {
				editTargetButtonPressed();
			}
		}
		
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

	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			// determine new sort column and direction
			TableColumn sortColumn = targetViewer.getTable().getSortColumn();
			TableColumn currentColumn = (TableColumn) e.widget;
			int dir = targetViewer.getTable().getSortDirection();

			if (sortColumn == currentColumn) {
				dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
			} else {
				targetViewer.getTable().setSortColumn(currentColumn);
				dir = SWT.UP;
			}

			targetViewer.getTable().setSortDirection(dir);
			targetViewer.setSorter(new AnnotationDetailsSorter(dir, sortColumn.getText()));
			refresh();

		}
	};
	
	private IManagedForm form;

	private AnnotationSection master;

	private AnnotationTypeContribution contribution;

	private boolean silentUpdate = false;

	private Text annotationNameText;

	private Text annotationEClassText;

	private Text annotationNamespaceText;

	private Text annotationUniqueText;
	
	private Text annotationContributorText;
	
	private Table targetsTable;
	private TableViewer targetViewer;
	private Button addArgButton;
	private Button editArgButton;
	private Button removeArgButton;

	public AnnotationDetailsPage() {
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
	private void setContribution(AnnotationTypeContribution contribution) {
		this.contribution = contribution;
	}

	private AnnotationTypeContribution getContribution() {
		return contribution;
	}



	private void createContributionInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		AnnotationDetailsPageListener adapter = new AnnotationDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		//label.setEnabled(!isReadOnly);
		annotationNameText = toolkit.createText(sectionClient, "");

		annotationNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		annotationNameText.addModifyListener(adapter);
		TigerstripeFormEditor editor = (TigerstripeFormEditor) ((TigerstripeFormPage) getForm()
				.getContainer()).getEditor();
//		if (!isReadOnly) {
			//nameEditListener = new TextEditListener(editor, "name",
			//		IModelChangeDelta.SET, this);
			//nameText.addModifyListener(nameEditListener);
//		}

		label = toolkit.createLabel(sectionClient, "");


		label = toolkit.createLabel(sectionClient, "eClass: ");
//		label.setEnabled(!isReadOnly);
		annotationEClassText = toolkit.createText(sectionClient, "");
		annotationEClassText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		annotationEClassText.addModifyListener(adapter);
		annotationEClassText.addKeyListener(adapter);
		
		label = toolkit.createLabel(sectionClient, "");


		label = toolkit.createLabel(sectionClient, "epacakge-uri: ");
//		label.setEnabled(!isReadOnly);
		annotationNamespaceText = toolkit.createText(sectionClient, "");
		annotationNamespaceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		annotationNamespaceText.addModifyListener(adapter);
		annotationNamespaceText.addKeyListener(adapter);

		label = toolkit.createLabel(sectionClient, "");
		
		

		label = toolkit.createLabel(sectionClient, "unique: ");
//		label.setEnabled(!isReadOnly);
		annotationUniqueText = toolkit.createText(sectionClient, "");
		annotationUniqueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		annotationUniqueText.addModifyListener(adapter);
		annotationUniqueText.addKeyListener(adapter);

		label = toolkit.createLabel(sectionClient, "");
		
		label = toolkit.createLabel(sectionClient, "Contributor: ");
//		label.setEnabled(!isReadOnly);
		annotationContributorText = toolkit.createText(sectionClient, "");
		annotationContributorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		annotationContributorText.addModifyListener(adapter);
		annotationContributorText.addKeyListener(adapter);
		// You cannot ever edit the contrubutor class!
		annotationContributorText.setEnabled(false);
		
		label = toolkit.createLabel(sectionClient, "");

		createTargetsTable(sectionClient);
		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
	}


	private void createTargetsTable(Composite parent) {
		FormToolkit toolkit = form.getToolkit();

		toolkit.createLabel(parent, "Targets").setLayoutData(
				new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		Composite composite = toolkit.createComposite(parent);
		GridLayout tw = new GridLayout();
		tw.numColumns = 2;
		composite.setLayout(tw);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		composite.setLayoutData(gd);

		AnnotationDetailsPageListener adapter = new AnnotationDetailsPageListener();
		targetsTable = toolkit.createTable(composite, SWT.NULL);
		targetsTable.addSelectionListener(adapter);
		GridData td = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_BEGINNING);
		td.verticalSpan = 5;
		td.heightHint = 140;
		targetsTable.setLayoutData(td);
		targetsTable.setHeaderVisible(true);
		targetsTable.setLinesVisible(true);

		TableColumn targetsTypeColumn = new TableColumn(targetsTable, SWT.NULL);
		targetsTypeColumn.setWidth(350);
		targetsTypeColumn.setText("Type");
		
		TableColumn targetsUniqColumn = new TableColumn(targetsTable, SWT.NULL);
		targetsUniqColumn.setWidth(50);
		targetsUniqColumn.setText("Unique");
		
		
		targetsTypeColumn.addListener(SWT.Selection,  listener);
		targetsUniqColumn.addListener(SWT.Selection,  listener);
		
		addArgButton = toolkit.createButton(composite, "Add", SWT.PUSH);
		addArgButton
				.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		addArgButton.addSelectionListener(adapter);

		editArgButton = toolkit.createButton(composite, "Edit", SWT.PUSH);
		editArgButton.setEnabled(false);
		editArgButton
				.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		editArgButton.addSelectionListener(adapter);


		removeArgButton = toolkit.createButton(composite, "Remove", SWT.PUSH);
		removeArgButton.setEnabled(false);
		removeArgButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL));
		removeArgButton.addSelectionListener(adapter);

		targetViewer = new TableViewer(targetsTable);
		targetViewer.setContentProvider(new TargetContentProvider());
		targetViewer.setLabelProvider(new TargetLabelProvider());
		targetViewer.addDoubleClickListener(adapter);
		targetViewer.getTable().setSortColumn(targetsTypeColumn);
		toolkit.paintBordersFor(composite);
	}
	
	class TargetContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			AnnotationTypeContribution type = (AnnotationTypeContribution) inputElement;
			return type.getTargets().toArray();
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class TargetLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		
		public String getColumnText(Object obj, int index) {
			AnnotationTypeContribution.Target field = (AnnotationTypeContribution.Target) obj;
				if (index == 1){
					return field.getUnique();
				}
				return field.getType();

		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}	
	private void editTargetButtonPressed() {
		
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
		annotationNameText.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof AnnotationSection) {
			//if (nameEditListener != null)
			//	nameEditListener.reset();

			master = (AnnotationSection) part;
			Table fieldsTable = master.getViewer().getTable();

			AnnotationTypeContribution selected = (AnnotationTypeContribution) fieldsTable.getSelection()[0].getData();
			setContribution(selected);
			updateForm();
		}
	}


	private void updateForm() {

		setSilentUpdate(true);
		annotationNameText.setText(getContribution().getName());
		annotationNameText.setEnabled(!getContribution().isReadOnly());
		annotationEClassText.setText(getContribution().getEClass());
		annotationEClassText.setEnabled(!getContribution().isReadOnly());
		annotationUniqueText.setText(getContribution().getUniq());
		annotationUniqueText.setEnabled(!getContribution().isReadOnly());
		annotationNamespaceText.setText(getContribution().getNamespace());
		annotationNamespaceText.setEnabled(!getContribution().isReadOnly());
		annotationContributorText.setText(getContribution().getContributor());
		
		targetViewer.setInput(getContribution()); 
		targetsTable.setEnabled(!getContribution().isReadOnly());
		addArgButton.setEnabled(!getContribution().isReadOnly());
		editArgButton.setEnabled(!getContribution().isReadOnly());
		removeArgButton.setEnabled(!getContribution().isReadOnly());
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
