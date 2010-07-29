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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.stereotypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.Stereotype;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeAttributeFactory;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeScopeDetails;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.StereotypeAttributeEditDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.ProfileEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class StereotypeDetailsPage implements IDetailsPage {

	// Set the table column property names
	private final String CHECKED_COLUMN = "checked";

	private final String ARTIFACTYPE_COLUMN = "artifact type";

	// Set column names
	private String[] columnNames = new String[] { CHECKED_COLUMN,
			ARTIFACTYPE_COLUMN, };

	public List<String> getColumnNames() {
		return Arrays.asList(this.columnNames);
	}

	private TableViewer artifactTableViewer;

	// protected TableViewer requiresViewer;
	//
	// protected Button addReqButton;
	//
	// protected Button removeReqButton;
	//
	// protected TableViewer excludesViewer;
	//
	// protected Button addExcButton;
	//
	// protected Button removeExcButton;

	protected TableViewer attributesViewer;

	protected Button addAttrButton;

	protected Button editAttrButton;

	protected Button removeAttrButton;

	private Text nameText;

	private Text versionText;

	private Text descriptionText;

	// private Text parentText;

	// private Button visibilityButton;

	private Button methodLevelButton;

	private Button attributeLevelButton;

	private Button literalLevelButton;

	private Button argumentLevelButton;

	private Button associationEndLevelButton;

	class RequiresContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (stereotype != null)
				return stereotype.getRequiresList();
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class ExcludesContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (stereotype != null)
				return stereotype.getExcludesList();
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class AttributesContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (stereotype != null)
				return stereotype.getIAttributes();
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class ReqsExcLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String str = (String) obj;
			return str;
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	class AttributeLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			IStereotypeAttribute attr = (IStereotypeAttribute) obj;
			return attr.getName();
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

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

	protected StereotypesSection getMaster() {
		return master;
	}

	protected IWorkbenchProfile getProfile() throws TigerstripeException {
		return ((ProfileEditor) getMaster().getPage().getEditor()).getProfile();
	}

	protected IManagedForm form;

	private StereotypesSection master;

	private IStereotype stereotype;

	private boolean silentUpdate = false;

	private Composite client;

	public StereotypeDetailsPage(StereotypesSection master) {
		super();
		this.master = master;
	}

	public void createContents(Composite parent) {
		parent.setLayout(TigerstripeLayoutFactory
				.createDetailsTableWrapLayout());
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		parent.setLayoutData(td);

		FormToolkit toolkit = form.getToolkit();

		Section section = TigerstripeLayoutFactory.createSection(parent,
				toolkit, Section.TITLE_BAR, "Stereotype Details", null);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite body = toolkit.createComposite(section);
		body.setLayout(TigerstripeLayoutFactory.createFormTableWrapLayout(1,
				false));
		body.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		client = toolkit.createComposite(body);
		client.setLayout(TigerstripeLayoutFactory
				.createFormPaneTableWrapLayout(2, false));
		client.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		createStereotypeInfo(toolkit, client);
		createAttributesDefinitions(toolkit, client);

		section.setClient(body);
		toolkit.paintBordersFor(parent);
	}

	protected void handleWidgetSelected(SelectionEvent e) {

		StereotypeScopeDetails details = (StereotypeScopeDetails) getIStereotype()
				.getStereotypeScopeDetails();
		// if (e.getSource() == visibilityButton) {
		// boolean sel = visibilityButton.getSelection();
		// getIStereotype().setVisible(sel);
		// pageModified();
		// } else
		if (e.getSource() == methodLevelButton) {
			details.setMethodLevel(methodLevelButton.getSelection());
			pageModified();
		} else if (e.getSource() == attributeLevelButton) {
			details.setAttributeLevel(attributeLevelButton.getSelection());
			pageModified();
		} else if (e.getSource() == literalLevelButton) {
			details.setLiteralLevel(literalLevelButton.getSelection());
			pageModified();
		} else if (e.getSource() == argumentLevelButton) {
			details.setArgumentLevel(argumentLevelButton.getSelection());
			pageModified();
		} else if (e.getSource() == associationEndLevelButton) {
			details.setAssociationEndLevel(associationEndLevelButton
					.getSelection());
			pageModified();
		}
	}

	// ============================================================
	private void setIStereotype(IStereotype stereotype) {
		this.stereotype = stereotype;
	}

	protected IStereotype getIStereotype() {
		return stereotype;
	}

	// ============================================================
	protected void createStereotypeInfo(FormToolkit toolkit, Composite parent) {
		StereotypeDetailsPageListener adapter = new StereotypeDetailsPageListener();

		toolkit.createLabel(parent, "Name: ");
		nameText = toolkit.createText(parent, "");
		nameText.setEnabled(ProfileEditor.isEditable());
		nameText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		nameText.addModifyListener(adapter);
		nameText.setToolTipText("Name of the stereotype, as seen by end-user.");

		toolkit.createLabel(parent, "Version: ");
		versionText = toolkit.createText(parent, "");
		versionText.setEnabled(ProfileEditor.isEditable());
		versionText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		versionText.addModifyListener(adapter);
		versionText.setToolTipText("Version of this stereotype definition.");

		toolkit.createLabel(parent, "Description: ");
		descriptionText = toolkit.createText(parent, "", SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		descriptionText.setEnabled(ProfileEditor.isEditable());
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		gd.heightHint = 60;
		descriptionText.setLayoutData(gd);
		descriptionText.addModifyListener(adapter);
		descriptionText
				.setToolTipText("Meaning for this stereotype, presented as tooltip to the end-user.");

		toolkit.createLabel(parent, "Scope:");
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		c.setLayout(TigerstripeLayoutFactory.createFormPaneTableWrapLayout(3,
				false));

		methodLevelButton = toolkit.createButton(c, "Method", SWT.CHECK);
		methodLevelButton.setEnabled(ProfileEditor.isEditable());
		methodLevelButton.addSelectionListener(adapter);
		methodLevelButton
				.setToolTipText("Mark this stereotype applicable to any Artifact Method, regardless of containing Artifact Type");

		attributeLevelButton = toolkit.createButton(c, "Attribute", SWT.CHECK);
		attributeLevelButton.setEnabled(ProfileEditor.isEditable());
		attributeLevelButton.addSelectionListener(adapter);
		attributeLevelButton
				.setToolTipText("Mark this stereotype applicable to any Artifact Attribute, regardless of containing Artifact Type");

		literalLevelButton = toolkit.createButton(c, "Literal", SWT.CHECK);
		literalLevelButton.setEnabled(ProfileEditor.isEditable());
		literalLevelButton.addSelectionListener(adapter);
		literalLevelButton
				.setToolTipText("Mark this stereotype applicable to any "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								IEnumArtifactImpl.class.getName()).getLabel(
								null)
						+ " Literal, regardless of containing Artifact Type");

		argumentLevelButton = toolkit.createButton(c, "Argument", SWT.CHECK);
		argumentLevelButton.setEnabled(ProfileEditor.isEditable());
		argumentLevelButton.addSelectionListener(adapter);
		argumentLevelButton
				.setToolTipText("Mark this stereotype applicable to any Method Argument, regardless of containing Artifact Type");

		associationEndLevelButton = toolkit.createButton(c, "Association End",
				SWT.CHECK);
		associationEndLevelButton.setEnabled(ProfileEditor.isEditable());
		associationEndLevelButton.addSelectionListener(adapter);
		associationEndLevelButton
				.setToolTipText("Mark this stereotype applicable to any Association End.");
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		associationEndLevelButton.setLayoutData(td);

		createArtifactTypeTable(parent, toolkit);
	}

	private void createArtifactTypeTable(Composite parent, FormToolkit toolkit) {
		TableWrapData gd = null;

		Table table = toolkit.createTable(parent, SWT.SINGLE | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
		table.setEnabled(ProfileEditor.isEditable());
		gd = new TableWrapData(TableWrapData.FILL_GRAB);
		gd.colspan = 2;
		table.setLayoutData(gd);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		column.setText(" ");
		column.setWidth(20);

		// 2nd column with task Description
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Artifact Type");
		column.setWidth(300);

		artifactTableViewer = new TableViewer(table);
		artifactTableViewer.setUseHashlookup(true);

		artifactTableViewer.setColumnProperties(columnNames);

		// Create the cell editor
		CellEditor[] editors = new CellEditor[columnNames.length];

		// Column 1 : Completed (Checkbox)
		editors[0] = new CheckboxCellEditor(table);

		// Column 2 : none
		editors[1] = null;

		// Assign the cell editors to the viewer
		artifactTableViewer.setCellEditors(editors);
		artifactTableViewer
				.setLabelProvider(new SelectedArtifactLabelProvider());
		artifactTableViewer
				.setContentProvider(new ProfileArtifactTypesContentProvider());
		artifactTableViewer
				.setCellModifier(new SelectedArtifactTypeCellModifier(this));
		// artifactTableViewer.setInput(stereotype);
	}

	protected void createAttributesDefinitions(FormToolkit toolkit,
			Composite parent) {
		toolkit.createLabel(parent, "Attributes:");

		Composite sectionClient = toolkit.createComposite(parent);
		sectionClient.setLayout(TigerstripeLayoutFactory
				.createFormPaneTableWrapLayout(2, false));
		sectionClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Table t = toolkit.createTable(sectionClient, SWT.NULL);
		t.setEnabled(ProfileEditor.isEditable());

		Composite buttonClient = toolkit.createComposite(sectionClient);
		buttonClient.setLayout(TigerstripeLayoutFactory
				.createButtonsTableWrapLayout());
		buttonClient.setLayoutData(new TableWrapData(TableWrapData.FILL));

		attributesViewer = new TableViewer(t);
		attributesViewer.setContentProvider(new AttributesContentProvider());
		attributesViewer.setLabelProvider(new AttributeLabelProvider());
		attributesViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						if (event.getSource() == attributesViewer) {
							if (attributesViewer.getSelection().isEmpty()) {
								if (removeAttrButton != null)
									removeAttrButton.setEnabled(false);
								if (editAttrButton != null)
									editAttrButton.setEnabled(false);
							} else {
								if (ProfileEditor.isEditable()) {
									if (removeAttrButton != null)
										removeAttrButton.setEnabled(true);
									if (editAttrButton != null)
										editAttrButton.setEnabled(true);
								}
							}
						}
					}
				});
		attributesViewer.setInput(stereotype);

		ViewerSorter nameSorter = new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object label1, Object label2) {
				return ((IStereotypeAttribute) label1).getName()
						.compareToIgnoreCase(
								((IStereotypeAttribute) label2).getName());
			}
		};
		attributesViewer.setSorter(nameSorter);
		if (ProfileEditor.isEditable()) {
			attributesViewer.addDoubleClickListener(new IDoubleClickListener() {
				public void doubleClick(DoubleClickEvent event) {
					handleAttributeEdit();
				}
			});
		}

		addAttrButton = toolkit.createButton(buttonClient, "Add", SWT.PUSH);
		// support for testing
		addAttrButton.setData("name", "Add_Attribute");
		addAttrButton.setEnabled(ProfileEditor.isEditable());
		addAttrButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (ProfileEditor.isEditable()) {
			addAttrButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					addAttrButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}

		editAttrButton = toolkit.createButton(buttonClient, "Edit", SWT.PUSH);
		editAttrButton.setEnabled(ProfileEditor.isEditable()
				&& !attributesViewer.getSelection().isEmpty());
		editAttrButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (ProfileEditor.isEditable()) {
			editAttrButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					editAttrButtonSelected();
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}

		removeAttrButton = toolkit.createButton(buttonClient, "Remove",
				SWT.PUSH);
		removeAttrButton.setEnabled(ProfileEditor.isEditable()
				&& !attributesViewer.getSelection().isEmpty());
		removeAttrButton.setLayoutData(new TableWrapData());
		removeAttrButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeAttrButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.maxHeight = td.heightHint = buttonClient.computeSize(SWT.DEFAULT,
				SWT.DEFAULT).y;
		t.setLayoutData(td);

		toolkit.paintBordersFor(sectionClient);
	}

	protected void handleAttributeEdit() {
		TableItem[] selectedItems = attributesViewer.getTable().getSelection();
		IStereotypeAttribute oldAttr = (IStereotypeAttribute) selectedItems[0]
				.getData();

		StereotypeAttributeEditDialog dialog = new StereotypeAttributeEditDialog(
				null, oldAttr, Arrays.asList(stereotype.getAttributes()),
				stereotype);
		dialog.setEdit(true);

		if (dialog.open() == Window.OK) {
			IStereotypeAttribute newAttr = dialog.getStereotypeAttribute();
			if (oldAttr == newAttr) {
				// the type has not changed so the instance remains the same
				// no need to do anything
				attributesViewer.refresh(true);
			} else {
				try {
					stereotype.removeAttribute(oldAttr);
					stereotype.addAttribute(newAttr);
					attributesViewer.setInput(stereotype);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
			pageModified();
		}
	}

	protected void addAttrButtonSelected(SelectionEvent event) {
		IStereotypeAttribute newAttr = StereotypeAttributeFactory
				.makeAttribute(IStereotypeAttribute.STRING_ENTRY_KIND);
		StereotypeAttributeEditDialog dialog = new StereotypeAttributeEditDialog(
				null, newAttr, Arrays.asList(stereotype.getAttributes()),
				stereotype);

		if (dialog.open() == Window.OK) {
			newAttr = dialog.getStereotypeAttribute();
			try {
				stereotype.addAttribute(newAttr);
				// Only add to viewer if this worked!
				attributesViewer.add(newAttr);
				attributesViewer.setInput(stereotype);
				attributesViewer.refresh(true);
				pageModified();
			} catch (TigerstripeException e) {
				// ignore here
				EclipsePlugin.log(e);
			}
			pageModified();
		}
	}

	protected void editAttrButtonSelected() {
		handleAttributeEdit();
	}

	protected void removeAttrButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = attributesViewer.getTable().getSelection();
		IStereotypeAttribute[] selectedLabels = new IStereotypeAttribute[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (IStereotypeAttribute) selectedItems[i]
					.getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message + "these " + selectedLabels.length
					+ " attributes?";
		} else {
			message = message + "this attribute?";
		}

		MessageDialog msgDialog = new MessageDialog(master.getSection()
				.getShell(), "Remove Attribute", null, message,
				MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			try {
				stereotype.removeAttributes(selectedLabels);
				attributesViewer.remove(selectedLabels);
				master.markPageModified();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	protected void addReqButtonSelected(SelectionEvent event) {
		// try {
		// StereotypeSelectionDialog dialog = new StereotypeSelectionDialog(
		// null, "", Arrays.asList(stereotype.getRequiresList()),
		// getProfile());
		// if (dialog.open() == dialog.OK) {
		// requiresViewer.add(dialog.getStereotypeName());
		// stereotype.addToRequiresList(dialog.getStereotypeName());
		// master.markPageModified();
		// }
		// } catch (TigerstripeException e) {
		// EclipsePlugin.log(e);
		// }
	}

	protected void removeReqButtonSelected(SelectionEvent event) {

	}

	protected void addExcButtonSelected(SelectionEvent event) {
		// try {
		// StereotypeSelectionDialog dialog = new StereotypeSelectionDialog(
		// null, "", Arrays.asList(stereotype.getExcludesList()),
		// getProfile());
		// if (dialog.open() == dialog.OK) {
		// excludesViewer.add(dialog.getStereotypeName());
		// stereotype.addToExcludesList(dialog.getStereotypeName());
		// master.markPageModified();
		// }
		// } catch (TigerstripeException e) {
		// EclipsePlugin.log(e);
		// }
	}

	protected void removeExcButtonSelected(SelectionEvent event) {

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
		if (part instanceof StereotypesSection) {
			Table fieldsTable = master.getViewer().getTable();

			IStereotype selected = (IStereotype) fieldsTable.getSelection()[0]
					.getData();
			setIStereotype(selected);
			artifactTableViewer.setInput(getIStereotype());
			updateForm();
		}
	}

	protected void updateForm() {
		setSilentUpdate(true);
		IStereotype st = getIStereotype();
		nameText.setText(st.getName());
		versionText.setText(st.getVersion());
		descriptionText.setText(st.getDescription());
		// visibilityButton.setSelection(st.isVisible());
		// parentText.setText(st.getParentStereotype());

		methodLevelButton.setSelection(st.getStereotypeScopeDetails()
				.isMethodLevel());
		attributeLevelButton.setSelection(st.getStereotypeScopeDetails()
				.isAttributeLevel());
		literalLevelButton.setSelection(st.getStereotypeScopeDetails()
				.isLiteralLevel());
		argumentLevelButton.setSelection(st.getStereotypeScopeDetails()
				.isArgumentLevel());
		associationEndLevelButton.setSelection(st.getStereotypeScopeDetails()
				.isAssociationEndLevel());

		attributesViewer.setInput(st);
		attributesViewer.refresh(true);

		// requiresViewer.setInput(st);
		// requiresViewer.refresh(true);
		// excludesViewer.setInput(st);
		// excludesViewer.refresh(true);

		artifactTableViewer.refresh(true);

		/*
		 * FIXME Just workaround to avoid appearing scrolls on details part.
		 */
		int height = client.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		master.setMinimumHeight(height);

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
				getIStereotype().setName(nameText.getText().trim());
				if (master != null) {
					TableViewer viewer = master.getViewer();
					viewer.refresh(getIStereotype());
				}
				pageModified();
			} else if (e.getSource() == versionText) {
				getIStereotype().setVersion(versionText.getText().trim());
				pageModified();
			} else if (e.getSource() == descriptionText) {
				getIStereotype().setDescription(
						descriptionText.getText().trim());
				pageModified();
				// } else if (e.getSource() == parentText) {
				// getIStereotype().setParentStereotype(
				// parentText.getText().trim());
				// pageModified();
			}
		}
	}

	class ProfileArtifactTypesContentProvider implements
			IStructuredContentProvider {

		private SelectedArtifactType[] selectedTypes;

		public Object[] getElements(Object inputElement) {
			if (selectedTypes == null)
				return new SelectedArtifactType[0];
			return selectedTypes;
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			Stereotype stereotype = (Stereotype) newInput;

			if (stereotype != null) {
				IWorkbenchProfile profile = stereotype.getProfile();
				CoreArtifactSettingsProperty property = (CoreArtifactSettingsProperty) profile
						.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
				String[] availableArtifactTypes = property
						.getEnabledArtifactTypes();

				String[] artifactLevelTypes = stereotype
						.getStereotypeScopeDetails().getArtifactLevelTypes();

				ArrayList<SelectedArtifactType> selectedTypesList = new ArrayList<SelectedArtifactType>();
				for (String availableType : availableArtifactTypes) {
					SelectedArtifactType type = new SelectedArtifactType();
					type.isSelected = false;
					type.isSelected = Arrays.asList(artifactLevelTypes)
							.contains(availableType);
					type.artifactTypeName = availableType;
					selectedTypesList.add(type);
				}

				selectedTypes = selectedTypesList
						.toArray(new SelectedArtifactType[selectedTypesList
								.size()]);
			}
		}

	}

	public class SelectedArtifactType {
		public String artifactTypeName;

		public boolean isSelected;
	}
}
