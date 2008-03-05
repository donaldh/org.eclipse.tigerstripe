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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.stereotypes;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.Stereotype;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeAttributeFactory;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeScopeDetails;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.StereotypeAttributeEditDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.ProfileEditor;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

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

	public StereotypeDetailsPage(StereotypesSection master) {
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

		createAttributesDefinitions(parent);
		createRequiresDefinitions(parent);
		createExcludesDefinitions(parent);

		form.getToolkit().paintBordersFor(parent);
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
			details.setAssociationEndLevel(associationEndLevelButton.getSelection());
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
	protected Composite createStereotypeInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		StereotypeDetailsPageListener adapter = new StereotypeDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		section.setDescription("Annotations details:");

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(ProfileEditor.isEditable());
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addModifyListener(adapter);
		nameText.setToolTipText("Name of the annotation, as seen by end-user.");

		label = toolkit.createLabel(sectionClient, "");
		label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));

		label = toolkit.createLabel(sectionClient, "Version: ");
		versionText = toolkit.createText(sectionClient, "");
		versionText.setEnabled(ProfileEditor.isEditable());
		versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		versionText.addModifyListener(adapter);
		versionText.setToolTipText("Version of this annotation definition.");

		label = toolkit.createLabel(sectionClient, "");
		label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));

		label = toolkit.createLabel(sectionClient, "Description: ");
		descriptionText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		descriptionText.setEnabled(ProfileEditor.isEditable());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 60;
		descriptionText.setLayoutData(gd);
		descriptionText.addModifyListener(adapter);
		descriptionText
				.setToolTipText("Meaning for this annotation, presented as tooltip to the end-user.");

		label = toolkit.createLabel(sectionClient, "");
		label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));

		// label = toolkit.createLabel(sectionClient, "");
		// visibilityButton = toolkit.createButton(sectionClient,
		// "Annotation is visible", SWT.CHECK);
		// visibilityButton.setEnabled(ProfileEditor.isEditable());
		// visibilityButton.addSelectionListener(adapter);
		// gd = new GridData(GridData.FILL_HORIZONTAL
		// | GridData.HORIZONTAL_ALIGN_BEGINNING);
		// gd.horizontalSpan = 2;
		// visibilityButton.setLayoutData(gd);
		// visibilityButton
		// .setToolTipText("Only visible annotations are selectable by the
		// end-user.");
		//
		// label = toolkit.createLabel(sectionClient, "Parent: ");
		// parentText = toolkit.createText(sectionClient, "");
		// parentText.setEnabled(ProfileEditor.isEditable());
		// parentText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// parentText.addModifyListener(adapter);
		// parentText
		// .setToolTipText("Name of the parent annotation. All attributes are
		// inherited.");
		//
		// label = toolkit.createLabel(sectionClient, "");
		// label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));

		label = toolkit.createLabel(sectionClient, "Scope:");
		Composite scopeComp = toolkit.createComposite(sectionClient, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		scopeComp.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		scopeComp.setLayout(layout);

		methodLevelButton = toolkit.createButton(scopeComp, "Method",
				SWT.CHECK);
		methodLevelButton.setEnabled(ProfileEditor.isEditable());
		methodLevelButton.addSelectionListener(adapter);
		methodLevelButton
				.setToolTipText("Mark this annotation applicable to any Artifact Method, regardless of containing Artifact Type");

		attributeLevelButton = toolkit.createButton(scopeComp, "Attribute",
				SWT.CHECK);
		attributeLevelButton.setEnabled(ProfileEditor.isEditable());
		attributeLevelButton.addSelectionListener(adapter);
		attributeLevelButton
				.setToolTipText("Mark this annotation applicable to any Artifact Attribute, regardless of containing Artifact Type");

		literalLevelButton = toolkit.createButton(scopeComp, "Literal",
				SWT.CHECK);
		literalLevelButton.setEnabled(ProfileEditor.isEditable());
		literalLevelButton.addSelectionListener(adapter);
		literalLevelButton
				.setToolTipText("Mark this annotation applicable to any "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								IEnumArtifactImpl.class.getName()).getLabel()
						+ " Literal, regardless of containing Artifact Type");

		argumentLevelButton = toolkit.createButton(scopeComp, "Argument",
				SWT.CHECK);
		argumentLevelButton.setEnabled(ProfileEditor.isEditable());
		argumentLevelButton.addSelectionListener(adapter);
		argumentLevelButton
				.setToolTipText("Mark this annotation applicable to any Method Argument, regardless of containing Artifact Type");

		associationEndLevelButton = toolkit.createButton(scopeComp, "Assoc. End",
				SWT.CHECK);
		associationEndLevelButton.setEnabled(ProfileEditor.isEditable());
		associationEndLevelButton.addSelectionListener(adapter);
		associationEndLevelButton
				.setToolTipText("Mark this annotation applicable to any Association End.");

		createArtifactTypeTable(scopeComp, toolkit);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		return sectionClient;
	}

	private void createArtifactTypeTable(Composite parent, FormToolkit toolkit) {
		GridData gd = null;

		Table table = toolkit.createTable(parent, SWT.SINGLE | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
		table.setEnabled(ProfileEditor.isEditable());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 5;
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

	protected void createAttributesDefinitions(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite sectionClient = toolkit.createComposite(parent);
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		sectionClient.setLayoutData(twd);

		Label l = toolkit.createLabel(sectionClient, "Attributes:");
		toolkit.createLabel(sectionClient, "");

		Table t = toolkit.createTable(sectionClient, SWT.NULL);
		t.setEnabled(ProfileEditor.isEditable());
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 140;
		t.setLayoutData(td);

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

		addAttrButton = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
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

		editAttrButton = toolkit.createButton(sectionClient, "Edit", SWT.PUSH);
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

		removeAttrButton = toolkit.createButton(sectionClient, "Remove",
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

		l = toolkit.createLabel(sectionClient, "", SWT.NULL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 40;
		l.setLayoutData(td);

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

	protected void createRequiresDefinitions(Composite parent) {
		// FormToolkit toolkit = form.getToolkit();
		// Composite sectionClient = toolkit.createComposite(parent);
		// TableWrapLayout twlayout = new TableWrapLayout();
		// twlayout.numColumns = 2;
		// sectionClient.setLayout(twlayout);
		// TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		// sectionClient.setLayoutData(twd);
		//
		// Label l = toolkit.createLabel(sectionClient, "Required
		// Annotations:");
		// toolkit.createLabel(sectionClient, "");
		//
		// Table t = toolkit.createTable(sectionClient, SWT.NULL);
		// t.setEnabled(ProfileEditor.isEditable());
		// TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		// td.rowspan = 2;
		// td.heightHint = 140;
		// t.setLayoutData(td);
		//
		// requiresViewer = new TableViewer(t);
		// requiresViewer.setContentProvider(new RequiresContentProvider());
		// requiresViewer.setLabelProvider(new ReqsExcLabelProvider());
		//
		// requiresViewer.setInput(stereotype);
		//
		// ViewerSorter nameSorter = new ViewerSorter() {
		// public int compare(Viewer viewer, Object label1, Object label2) {
		// return ((String) label1).compareToIgnoreCase(((String) label2));
		// }
		// };
		// requiresViewer.setSorter(nameSorter);
		//
		// addReqButton = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
		// addReqButton.setEnabled(ProfileEditor.isEditable());
		// addReqButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		// if (ProfileEditor.isEditable()) {
		// addReqButton.addSelectionListener(new SelectionListener() {
		// public void widgetSelected(SelectionEvent event) {
		// addReqButtonSelected(event);
		// }
		//
		// public void widgetDefaultSelected(SelectionEvent event) {
		// // empty
		// }
		// });
		// }
		// removeReqButton = toolkit.createButton(sectionClient, "Remove",
		// SWT.PUSH);
		// removeReqButton.setEnabled(ProfileEditor.isEditable());
		// removeReqButton.setLayoutData(new TableWrapData());
		// removeReqButton.addSelectionListener(new SelectionListener() {
		// public void widgetSelected(SelectionEvent event) {
		// removeReqButtonSelected(event);
		// }
		//
		// public void widgetDefaultSelected(SelectionEvent event) {
		// // empty
		// }
		// });
		//
		// l = toolkit.createLabel(sectionClient, "", SWT.NULL);
		// td = new TableWrapData(TableWrapData.FILL_GRAB);
		// td.heightHint = 40;
		// l.setLayoutData(td);
		//
		// toolkit.paintBordersFor(sectionClient);
	}

	protected void createExcludesDefinitions(Composite parent) {
		// FormToolkit toolkit = form.getToolkit();
		// Composite sectionClient = toolkit.createComposite(parent);
		// TableWrapLayout twlayout = new TableWrapLayout();
		// twlayout.numColumns = 2;
		// sectionClient.setLayout(twlayout);
		// TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		// sectionClient.setLayoutData(twd);
		//
		// Label l = toolkit.createLabel(sectionClient, "Excluded
		// Annotations:");
		// toolkit.createLabel(sectionClient, "");
		//
		// Table t = toolkit.createTable(sectionClient, SWT.NULL);
		// t.setEnabled(ProfileEditor.isEditable());
		// TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		// td.rowspan = 2;
		// td.heightHint = 140;
		// t.setLayoutData(td);
		//
		// excludesViewer = new TableViewer(t);
		// excludesViewer.setContentProvider(new ExcludesContentProvider());
		// excludesViewer.setLabelProvider(new ReqsExcLabelProvider());
		//
		// excludesViewer.setInput(stereotype);
		//
		// ViewerSorter nameSorter = new ViewerSorter() {
		// public int compare(Viewer viewer, Object label1, Object label2) {
		// return ((String) label1).compareToIgnoreCase(((String) label2));
		// }
		// };
		// excludesViewer.setSorter(nameSorter);
		//
		// addExcButton = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
		// addExcButton.setEnabled(ProfileEditor.isEditable());
		// addExcButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		// if (ProfileEditor.isEditable()) {
		// addExcButton.addSelectionListener(new SelectionListener() {
		// public void widgetSelected(SelectionEvent event) {
		// addExcButtonSelected(event);
		// }
		//
		// public void widgetDefaultSelected(SelectionEvent event) {
		// // empty
		// }
		// });
		// }
		// removeExcButton = toolkit.createButton(sectionClient, "Remove",
		// SWT.PUSH);
		// removeExcButton.setEnabled(ProfileEditor.isEditable());
		// removeExcButton.setLayoutData(new TableWrapData());
		// removeExcButton.addSelectionListener(new SelectionListener() {
		// public void widgetSelected(SelectionEvent event) {
		// removeExcButtonSelected(event);
		// }
		//
		// public void widgetDefaultSelected(SelectionEvent event) {
		// // empty
		// }
		// });
		//
		// l = toolkit.createLabel(sectionClient, "", SWT.NULL);
		// td = new TableWrapData(TableWrapData.FILL_GRAB);
		// td.heightHint = 40;
		// l.setLayoutData(td);
		//
		// toolkit.paintBordersFor(sectionClient);
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
		// TableItem[] selectedItems = requiresViewer.getTable().getSelection();
		// String[] selectedLabels = new String[selectedItems.length];
		//
		// for (int i = 0; i < selectedItems.length; i++) {
		// selectedLabels[i] = (String) selectedItems[i].getData();
		// }
		//
		// String message = "Do you really want to remove ";
		// if (selectedLabels.length > 1) {
		// message = message + "these " + selectedLabels.length
		// + " required annotations?";
		// } else {
		// message = message + "this required annotation?";
		// }
		//
		// MessageDialog msgDialog = new MessageDialog(master.getSection()
		// .getShell(), "Remove Required Annotation", null, message,
		// MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);
		//
		// if (msgDialog.open() == 0) {
		// try {
		// stereotype.removeFromRequiresList(selectedLabels);
		// requiresViewer.remove(selectedLabels);
		// master.markPageModified();
		// } catch (TigerstripeException e) {
		// EclipsePlugin.log(e);
		// }
		// }
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
		// TableItem[] selectedItems = excludesViewer.getTable().getSelection();
		// String[] selectedLabels = new String[selectedItems.length];
		//
		// for (int i = 0; i < selectedItems.length; i++) {
		// selectedLabels[i] = (String) selectedItems[i].getData();
		// }
		//
		// String message = "Do you really want to remove ";
		// if (selectedLabels.length > 1) {
		// message = message + "these " + selectedLabels.length
		// + " excluded annotations?";
		// } else {
		// message = message + "this excluded annotation?";
		// }
		//
		// MessageDialog msgDialog = new MessageDialog(master.getSection()
		// .getShell(), "Remove Excluded Annotation", null, message,
		// MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);
		//
		// if (msgDialog.open() == 0) {
		// try {
		// stereotype.removeFromExcludesList(selectedLabels);
		// excludesViewer.remove(selectedLabels);
		// master.markPageModified();
		// } catch (TigerstripeException e) {
		// EclipsePlugin.log(e);
		// }
		// }
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
			master = (StereotypesSection) part;
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
