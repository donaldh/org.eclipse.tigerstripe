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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceDiagramEditorPlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.NewTSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.dialogs.SelectionDialog;

public class ClassInstanceEditDialog extends NewTSMessageDialog {

	private IAbstractArtifact artifact;
	private IArtifactManagerSession artMgrSession;
	private final DiagramGraphicalViewer mapViewer;
	private final InstanceMapEditPart mapEditPart;
	private ClassInstance instance = null;
	private String instanceName = "";
	private Text instanceNameField;
	private final HashMap<String, List<Object>> selectionMap = new HashMap<String, List<Object>>();
	private final Set<String> instanceNames = new HashSet<String>();
	private final Map<String, Instance> instanceMap = new HashMap<String, Instance>();
	private final List<Entry> entries = new ArrayList<Entry>();

	// Set the table column property names
	private final String SET_COLUMN = "";
	private final String FIELDNAME_COLUMN = "Name";
	private final String FIELDSOURCE_COLUMN = "Source Class";
	private final String FIELDTYPE_COLUMN = "Type";
	private final String FIELDVALUE_COLUMN = "Value";
	// Set column names
	private final String[] columnNames = new String[] { SET_COLUMN,
			FIELDNAME_COLUMN, FIELDSOURCE_COLUMN, FIELDTYPE_COLUMN,
			FIELDVALUE_COLUMN };
	private final String[] columnLabels = new String[] { "SELECTED", "NAME",
			"SOURCE", "TYPE", "VALUE" };
	private final List<String> columnNamesAsList = Arrays.asList(columnNames);
	private final String[] booleanVals = new String[] { "", "true", "false" };
	// column indexes
	private final int SET_COLUMN_IDX = 0;
	private final int FIELDNAME_COLUMN_IDX = 1;
	private final int FIELDSOURCE_COLUMN_IDX = 2;
	private final int FIELDTYPE_COLUMN_IDX = 3;
	private final int FIELDVALUE_COLUMN_IDX = 4;
	// and the table/table viewer references
	private TableViewer tableViewer;
	private Table table;
	private CellEditor[] cellEditors;
	private CellEditor textCellEditor;
	private CellEditor checkBoxCellEditor;

	private Composite area;

	private final Map<IType, List<String>> newReferenceInstances = new HashMap<IType, List<String>>();

	private ClassInstanceEditDialog(Shell parentShell,
			IAbstractArtifact anArtifact, ClassInstance anInstance,
			InstanceMapEditPart mapEditPart) {
		super(parentShell, "", "Edit instance name/attributes");
		this.instance = anInstance;
		this.artifact = anArtifact;
		this.mapEditPart = mapEditPart;

		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);

		mapViewer = (DiagramGraphicalViewer) mapEditPart.getViewer();
		DiagramEditDomain domain = (DiagramEditDomain) mapViewer
				.getEditDomain();
		IResource res = (IResource) domain.getEditorPart().getEditorInput()
				.getAdapter(IResource.class);
		IAbstractTigerstripeProject aProject = (IAbstractTigerstripeProject) res
				.getProject().getAdapter(IAbstractTigerstripeProject.class);
		if (!(aProject instanceof ITigerstripeModelProject))
			throw new RuntimeException("non-Tigerstripe Project found");
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		try {
			artMgrSession = project.getArtifactManagerSession();
			if (artifact == null && instance != null) {
				artifact = artMgrSession
						.getArtifactByFullyQualifiedName(instance
								.getFullyQualifiedName());
			}
		} catch (TigerstripeException e) {
			throw new RuntimeException("matching IAbstractArtifact not found");
		}
	}

	public ClassInstanceEditDialog(Shell parentShell,
			IAbstractArtifact artifact, InstanceMapEditPart mapEditPart) {
		this(parentShell, artifact, null, mapEditPart);
	}

	public ClassInstanceEditDialog(Shell parentShell, ClassInstance instance,
			InstanceMapEditPart mapEditPart) {
		this(parentShell, null, instance, mapEditPart);
	}

	@Override
	protected void okPressed() {
		if (area != null) {
			area.forceFocus();
		}
		super.okPressed();
	}

	protected void updateOkButton(Object eventSource) {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		if (okButton == null)
			return;
		if (eventSource instanceof Text) {
			instanceName = ((Text) eventSource).getText();
			Matcher classNameMatcher = TigerstripeValidationUtils.classNamePattern
					.matcher(instanceName);
			Matcher elementNameMatcher = TigerstripeValidationUtils.elementNamePattern
					.matcher(instanceName);
			if (instanceName == null || "".equals(instanceName)) {
				okButton.setEnabled(false);
				setInfoMessage("Enter an instance name");
			} else if (!classNameMatcher.matches()
					&& !elementNameMatcher.matches()) {
				okButton.setEnabled(false);
				setErrorMessage("'" + instanceName
						+ "' is not a legal instance name");
			} else if (instanceNames.contains(instanceName)
					&& instanceMap.get(instanceName) != instance) {
				okButton.setEnabled(false);
				setErrorMessage("Instance named '" + instanceName
						+ "' already exists in the diagram");
			} else {
				okButton.setEnabled(true);
				setInfoMessage("");
			}
		}
	}

	@Override
	public Control createDialogArea(Composite parent) {
		area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 8;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		initDialog();
		createMessageArea(composite, nColumns);
		createInstanceDefinitionControl(composite, nColumns);

		return area;
	}

	protected void initDialog() {
		getShell().setText("Class Instance Editor");
		getShell().setMinimumSize(250, 200);
		// set the title string of the newTSMessageDialog to reflect the type of
		// class
		// being edited
		EObject mapEObj = ((Diagram) mapEditPart.getModel()).getElement();
		String mapBasePackage = ((InstanceMap) mapEObj).getBasePackage();
		String instanceClassName = "";
		if (!mapBasePackage.equals(artifact.getPackage())) {
			instanceClassName = artifact.getFullyQualifiedName();
		} else {
			instanceClassName = artifact.getName();
		}
		this.setTitleString("Instance of " + instanceClassName);
		// construct a list of the instance names that exist in the
		// instance diagram (if any) for use later (this list is used
		// to ensure that each instance has a unique name in a given
		// instance diagram)
		instanceNames.clear();
		instanceMap.clear();
		for (Object child : mapEditPart.getChildren()) {
			if (child instanceof ClassInstanceEditPart
					|| child instanceof AssociationInstanceEditPart) {
				IGraphicalEditPart graphicalEditPart = (IGraphicalEditPart) child;
				EObject eObj = ((View) graphicalEditPart.getModel())
						.getElement();
				Instance instance = (Instance) eObj;
				instanceNames.add(instance.getArtifactName());
				instanceMap.put(instance.getArtifactName(), instance);
			}
		}
	}

	private void createInstanceDefinitionControl(Composite composite,
			int nColumns) {
		Group box = new Group(composite, SWT.NULL);
		box.setText("Instance name");
		GridData bgd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		bgd.horizontalSpan = 8;
		box.setLayoutData(bgd);
		GridLayout bLayout = new GridLayout();
		bLayout.numColumns = 8;
		box.setLayout(bLayout);
		instanceNameField = new Text(box, SWT.BORDER);
		instanceNameField
				.setToolTipText("The name of the instance of this class.");
		instanceNameField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateOkButton(e.getSource());
			}
		});
		if (instance != null) {
			instanceName = instance.getArtifactName();
			instanceNameField.setText(instanceName);
		}
		bgd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		bgd.horizontalSpan = 8;
		instanceNameField.setLayoutData(bgd);

		entries.clear();
		// put together a list of all of the variables that can be set in this
		// artifact
		// (including the variables visible from the artifact's superclasses)
		IField[] fields = InstanceDiagramUtils.getInstanceVariables(artifact);
		// if an instance has been passed in via a constructor, get the list
		// of field names that exist in that instance
		Map<String, String> variableNameValMap = new HashMap<String, String>();
		if (fields != null && fields.length > 0) {
			if (instance == null) {
				for (IField field : fields) {
					String defValue = field.getDefaultValue();
					if (defValue != null)
						variableNameValMap.put(field.getName(), defValue);
				}
			} else {
				EList<?> variableList = instance.getVariables();
				for (Object obj : variableList) {
					Variable variable = (Variable) obj;
					variableNameValMap.put(variable.getName(),
							variable.getValue());
				}
			}
		}
		Set<String> variableNames = variableNameValMap.keySet();
		// if an instance has been passed in via a constructor, get the list
		// of associations that exist in that instance
		Map<String, Set<String>> associationNameValMap = new HashMap<String, Set<String>>();
		if (fields != null && fields.length > 0 && instance != null) {
			EList<?> associationList = instance.getAssociations();
			for (Object obj : associationList) {
				AssociationInstance association = (AssociationInstance) obj;
				ClassInstance destInstance = (ClassInstance) association
						.getZEnd();
				if (destInstance == null)
					continue;
				String referenceName = association.getReferenceName();
				String destInstanceName = destInstance.getArtifactName();
				Set<String> valSet = null;
				if (associationNameValMap.keySet().contains(referenceName)) {
					valSet = associationNameValMap.get(referenceName);
				} else {
					valSet = new HashSet<String>();
					associationNameValMap.put(referenceName, valSet);
				}
				valSet.add(destInstanceName);
			}
		}
		Set<String> associationNames = associationNameValMap.keySet();
		// now add the fields in the artifact to the entries list
		String artifactFQN = artifact.getFullyQualifiedName();
		EObject eObj = ((Diagram) mapEditPart.getModel()).getElement();
		String mapBasePackage = ((InstanceMap) eObj).getBasePackage();
		for (IField field : fields) {
			Entry entry = new Entry();
			entry.name = field.getName();
			entry.ordered = field.isOrdered();
			entry.unique = field.isUnique();
			IAbstractArtifact containingArt = field.getContainingArtifact();
			String containingArtFQN = containingArt.getFullyQualifiedName();
			if (!containingArtFQN.equals(artifactFQN)) {
				if (!mapBasePackage.equals(containingArt.getPackage()))
					entry.source = containingArtFQN;
				else
					entry.source = containingArt.getName();
			} else {
				entry.source = "";
			}
			entry.type = field.getType();
			if (variableNames.contains(entry.name)) {
				entry.enabled = true;
				String value = variableNameValMap.get(entry.name);
				// if here, the field is a primitive type, so the value is a
				// single string
				List<String> values = null;
				if (entry.type.getTypeMultiplicity().isArray()) {
					values = csvToList(value);
				} else {
					values = Arrays.asList(new String[] { value });
				}
				entry.values = values;
				selectionMap.put(
						entry.name,
						Arrays.asList(new Object[] { entry.type,
								entry.stringValue() }));
			} else if (associationNames.contains(entry.name)) {
				// if here, it's a non-primitive type (reference) type, so
				// the "value" is a list of the names of the instances of that
				// "type" in the
				entry.enabled = true;
				Set<String> stringVals = associationNameValMap.get(entry.name);
				String[] values = new String[stringVals.size()];
				associationNameValMap.get(entry.name).toArray(values);
				entry.values = Arrays.asList(values);
				selectionMap.put(
						entry.name,
						Arrays.asList(new Object[] { entry.type,
								entry.stringValue() }));
			} else {
				entry.values = new ArrayList<String>();
			}
			entries.add(entry);
		}
		box = new Group(composite, SWT.NULL);
		box.setText("Instance Fields");
		bgd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		bgd.horizontalSpan = 8;
		box.setLayoutData(bgd);
		bLayout = new GridLayout();
		bLayout.numColumns = 8;
		box.setLayout(bLayout);
		// create a TableViewer in this box (along with the
		// associated table)
		tableViewer = createTableViewer(box);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		for (int i = 0; i < columnNames.length; i++) {
			String columnName = columnNames[i];
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(columnName);
			if (i == (columnNames.length - 1)) {
				column.setWidth(200);
			}
		}
		tableViewer.setInput(entries);
		tableViewer.refresh(true);
		for (int i = 0; i < columnNames.length; i++) {
			if (i < (columnNames.length - 1))
				table.getColumn(i).pack();
		}
		table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		bgd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		bgd.horizontalSpan = 8;
		table.setLayoutData(bgd);
		table.pack();
		instanceNameField.setFocus();
		if (instanceName.length() > 0)
			instanceNameField.setSelection(instanceName.length());
	}

	private static List<String> csvToList(String input) {
		return Arrays.asList(input.split("[, ]+"));
	}

	private static String listAsCSV(List<String> list) {
		StringBuffer strBuff = new StringBuffer();
		int fieldNo = 0;
		for (String val : list) {
			strBuff.append(val);
			if (++fieldNo < list.size())
				strBuff.append(", ");
		}
		return strBuff.toString();
	}

	private class Entry {

		private boolean enabled = false;
		public String name;
		public String source;
		public IType type;
		public boolean ordered;
		public boolean unique;
		public List<String> values;

		public String stringValue() {
			return ClassInstanceEditDialog.listAsCSV(values);
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof Entry) {
				Entry ent = (Entry) other;
				// if have different numbers of values, then are not equal
				if (ent.values.size() != values.size())
					return false;
				// loop through all entries and compare...if all are equal, then
				// entries are equal
				for (int i = 0; i < ent.values.size(); i++)
					if (!values.get(i).equals(ent.values.get(i)))
						return false;
				return true;
			}
			return false;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public String toString() {
			return stringValue();
		}
	}

	private class EntryValuesCellModifier implements ICellModifier {

		private final TableViewer viewer;

		public EntryValuesCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		public boolean canModify(Object element, String property) {
			if (element instanceof Entry) {
				Entry entry = (Entry) element;
				CellEditor resultEditor = null;
				if (entry.type.getTypeMultiplicity().isArray()) {
					resultEditor = new ArrayValuesDialogCellEditor(table);
				} else if (!InstanceDiagramUtils.isPrimitive(artMgrSession,
						entry.type)) {
					resultEditor = new SingleValueDialogCellEditor(table);
				} else if (isEnumeration(entry.type)) {
					String[] vals = getComboBoxVals(entry.type);
					resultEditor = new MyEnumerationCellEditor(table, vals,
							SWT.READ_ONLY);
				} else if (isBooleanPrimitive(entry.type)) {
					String[] vals = booleanVals;
					resultEditor = new MyEnumerationCellEditor(table, vals,
							SWT.READ_ONLY);
				} else {
					resultEditor = textCellEditor;
				}
				cellEditors[columnNames.length - 1] = resultEditor;
			}
			if ("VALUE".equals(property) || "SELECTED".equals(property))
				return true;
			return false;
		}

		public Object getValue(Object element, String property) {
			if ("VALUE".equals(property)) {
				Entry entry = (Entry) element;
				CellEditor currEditor = cellEditors[columnNames.length - 1];
				if (currEditor instanceof SingleValueDialogCellEditor
						|| currEditor instanceof ArrayValuesDialogCellEditor) {
					return entry;
				} else {
					return entry.stringValue();
				}
			} else if ("SELECTED".equals(property)) {
				Entry entry = (Entry) element;
				return entry.isEnabled();
			}
			return null;
		}

		public void modify(Object item, String property, Object value) {
			// Find the index of the column
			int columnIndex = Arrays.asList(columnLabels).indexOf(property);

			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			Entry entry = (Entry) ssel.getFirstElement();

			switch (columnIndex) {
			case SET_COLUMN_IDX: // CHECKED Column
				boolean includeVal = ((Boolean) value).booleanValue();
				entry.setEnabled(includeVal);
				if (includeVal) {
					selectionMap.put(
							entry.name,
							Arrays.asList(new Object[] { entry.type,
									entry.stringValue() }));
				} else {
					if (selectionMap.keySet().contains(entry.name))
						selectionMap.remove(entry.name);
				}
				break;
			case FIELDVALUE_COLUMN_IDX: // VALUE Column
				if (entry.type.getTypeMultiplicity().isArray()) {
					if (value instanceof String[]) {
						entry.values = Arrays.asList((String[]) value);
					}
				} else if (value instanceof String) {
					String stringValue = (String) value;
					if (stringValue.length() > 0) {
						entry.values = Arrays.asList(stringValue);
					}
				}

				if (entry.values.size() > 0) {
					entry.enabled = true;
				} else {
					entry.enabled = false;
				}
				if (entry.isEnabled()) {
					// update entry in map if is selected
					selectionMap.put(
							entry.name,
							Arrays.asList(new Object[] { entry.type,
									entry.stringValue() }));
				} else {
					selectionMap.remove(entry.name);
				}
			default:
			}
			tableViewer.refresh(true);
		}

		private String[] getComboBoxVals(IType type) {
			String fqn = type.getFullyQualifiedName();
			IAbstractArtifact iArtifact = null;
			try {
				iArtifact = artMgrSession.getArtifactByFullyQualifiedName(fqn);
			} catch (TigerstripeException e) {
				return new String[0];
			}
			ArrayList<String> stringVals = new ArrayList<String>();
			do {
				List<String> lclStringVals = new ArrayList<String>();
				for (ILiteral literal : iArtifact.getLiterals())
					lclStringVals.add(literal.getName());
				stringVals.addAll(0, lclStringVals);
			} while ((iArtifact = iArtifact.getExtendedArtifact()) != null);
			stringVals.add(0, "");
			String[] items = new String[stringVals.size()];
			stringVals.toArray(items);
			return items;
		}

	}

	private class ArrayValuesDialogCellEditor extends DialogCellEditor {

		public ArrayValuesDialogCellEditor(Composite composite) {
			super(composite);
		}

		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			Entry entry = (Entry) getValue();
			ArrayValuesDialog dialog = new ArrayValuesDialog(
					cellEditorWindow.getShell(), "Edit values", entry.type,
					entry.ordered, entry.unique, entry.values);
			dialog.create();
			if (dialog.open() == Dialog.OK) {
				String[] result = dialog.getResult();
				return result;
			}
			return null;
		}
	}

	private class ArrayValuesDialog extends StatusDialog {
		private static final String DEFAULT_EMPTY_SIMPLE_VALUE = "<click here to edit>";
		private final IType type;
		private final boolean ordered;
		private final boolean unique;
		private final List<String> values;

		private Composite avdArea;

		private final String title;
		protected TableViewer viewer;

		private Button addButton;
		private Button removeButton;
		private Button upButton;
		private Button downButton;

		public ArrayValuesDialog(Shell shell, String title, IType type,
				boolean ordered, boolean unique, List<String> input) {
			super(shell);
			this.title = title;
			this.type = type;
			this.ordered = ordered;
			this.unique = unique;
			values = new ArrayList<String>();
			if (input != null) {
				values.addAll(input);
			}
		}

		@Override
		protected void configureShell(Shell shell) {
			super.configureShell(shell);
			if (title != null) {
				shell.setText(title);
			}
		}

		public String[] getResult() {
			if (isSimpleType()) {
				for (Iterator<String> it = values.iterator(); it.hasNext();) {
					String value = (String) it.next();
					if (DEFAULT_EMPTY_SIMPLE_VALUE.equals(value)) {
						it.remove();
					}
				}
			}
			return values.toArray(new String[values.size()]);
		}

		private void validateValues() {
			IStatus status = null;
			if (unique) {
				for (int i = 0; i < values.size(); i++) {
					String currValue = values.get(i);
					if (!currValue.equals(DEFAULT_EMPTY_SIMPLE_VALUE)) {
						for (int j = 0; j < values.size(); j++) {
							if (i != j && currValue.equals(values.get(j))) {
								status = new Status(IStatus.ERROR,
										InstanceDiagramEditorPlugin.ID,
										"Values must be unique.");
								break;
							}
						}
					}
				}
			}
			if (status == null) {
				status = Status.OK_STATUS;
			}
			updateStatus(status);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			avdArea = (Composite) super.createDialogArea(parent);

			Composite content = new Composite(avdArea, SWT.NONE);
			content.setLayout(new GridLayout(2, false));
			content.setLayoutData(new GridData(GridData.FILL_BOTH));

			Table table = new Table(content, SWT.BORDER | SWT.MULTI
					| SWT.FULL_SELECTION);
			viewer = new TableViewer(table);
			viewer.setLabelProvider(new LabelProvider());
			viewer.setColumnProperties(new String[] { "VALUES" });
			viewer.setContentProvider(new ArrayContentProvider());
			viewer.setInput(values);
			viewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					updateButtons();
				}
			});
			viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

			if (isSimpleType()) {
				viewer.setCellModifier(new ICellModifier() {

					public void modify(Object element, String property,
							Object value) {
						if (element instanceof Item) {
							int index = viewer.getTable().getSelectionIndex();
							values.set(index, (String) value);
							valuesChanged(index);
						}
					}

					public Object getValue(Object element, String property) {
						return element;
					}

					public boolean canModify(Object element, String property) {
						return true;
					}
				});
				viewer.setCellEditors(new CellEditor[] { new TextCellEditor(
						table) });
			}

			createButtons(content);

			updateButtons();
			validateValues();
			return avdArea;
		}

		@Override
		protected void okPressed() {
			if (avdArea != null) {
				avdArea.forceFocus();
			}
			super.okPressed();
		}

		private void createButtons(Composite parent) {
			Composite panel = new Composite(parent, SWT.NONE);
			RowLayout layout = new RowLayout(SWT.VERTICAL);
			layout.pack = false;
			panel.setLayout(layout);
			addButton = new Button(panel, SWT.PUSH);
			addButton.setText("Add");
			addButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean added = true;
					if (isSimpleType()) {
						values.add(DEFAULT_EMPTY_SIMPLE_VALUE);
					} else {
						added = addComplexValue();
					}
					valuesChanged(-1);
					if (added) {
						validateValues();
					}
				}
			});
			removeButton = new Button(panel, SWT.PUSH);
			removeButton.setText("Remove");
			removeButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int[] selections = getSelectionIndexes();
					List<Integer> indexes = new ArrayList<Integer>();
					for (int selection : selections) {
						indexes.add(selection);
					}
					Collections.sort(indexes);
					for (int i = 0; i < indexes.size(); i++) {
						values.remove(indexes.get(i) - i);
					}
					valuesChanged(-1);
				}
			});
			upButton = new Button(panel, SWT.PUSH);
			upButton.setText("Up");
			upButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = getSelectionIndexes()[0];
					String val = values.set(index - 1, values.get(index));
					values.set(index, val);
					valuesChanged(index - 1);
				}
			});
			downButton = new Button(panel, SWT.PUSH);
			downButton.setText("Down");
			downButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = getSelectionIndexes()[0];
					String val = values.set(index + 1, values.get(index));
					values.set(index, val);
					valuesChanged(index + 1);
				}
			});
			panel.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		}

		private void valuesChanged(int selectionIndex) {
			viewer.refresh();
			validateValues();
			if (selectionIndex > 0) {
				viewer.getTable().setSelection(selectionIndex);
			}
			updateButtons();
		}

		private int[] getSelectionIndexes() {
			return viewer.getTable().getSelectionIndices();
		}

		private void updateButtons() {
			int[] selections = getSelectionIndexes();
			addButton.setEnabled(true);
			removeButton.setEnabled(selections.length > 0);
			int size = values.size();
			if (ordered && selections.length == 1 && size > 0) {
				upButton.setEnabled(selections[0] != 0);
				downButton.setEnabled(selections[0] != size - 1);
			} else {
				upButton.setEnabled(false);
				downButton.setEnabled(false);
			}
		}

		private boolean isSimpleType() {
			return InstanceDiagramUtils.isPrimitive(artMgrSession, type);
		}

		private boolean addComplexValue() {
			String[] toExclude = null;
			if (unique) {
				toExclude = values.toArray(new String[values.size()]);
			}
			ReferenceSelectionDialog dialog = new ReferenceSelectionDialog(
					getShell(), toExclude, type);
			dialog.setTitle("Select references");
			dialog.create();
			if (dialog.open() == Dialog.OK) {
				Object[] results = dialog.getResult();
				if (results.length > 0) {
					String val = (String) results[0];
					if (dialog.isNewReference()) {
						addNewReferencedInstance(type, val);
					}
					values.add(val);
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Create the TableViewer
	 */
	private TableViewer createTableViewer(Composite box) {
		table = new Table(box, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		TableViewer localTableViewer = new TableViewer(table);
		localTableViewer.setLabelProvider(new MyLabelProvider());

		localTableViewer.setContentProvider(new ArrayContentProvider());
		localTableViewer.setColumnProperties(columnLabels);
		localTableViewer.setCellModifier(new EntryValuesCellModifier(
				localTableViewer));

		// Create the cell editors
		cellEditors = new CellEditor[columnNames.length];
		textCellEditor = new TextCellEditor(table);
		checkBoxCellEditor = new CheckboxCellEditor(table);
		for (int i = 0; i < columnNames.length; i++) {
			if (i == 0)
				cellEditors[i] = checkBoxCellEditor;
			else
				cellEditors[i] = textCellEditor;
		}
		// Assign the cell editors to the viewer
		localTableViewer.setCellEditors(cellEditors);
		// and return the table viewer we've built
		return localTableViewer;
	}

	private class MyEnumerationCellEditor extends ComboBoxCellEditor {

		List<String> itemsList;

		public MyEnumerationCellEditor(Composite parent, String[] items,
				int style) {
			super(parent, items, style);
			init(items);
		}

		private void init(String[] items) {
			// put together a list of items to use later when getting and
			// setting values
			itemsList = Arrays.asList(items);
		}

		@Override
		protected Object doGetValue() {
			Object selection = super.doGetValue();
			String val = itemsList.get(((Integer) selection).intValue());
			return val;
		}

		@Override
		protected void doSetValue(Object value) {
			int idx = itemsList.indexOf(value);
			Integer selection = Integer.valueOf((idx > 0 ? idx : 0));
			super.doSetValue(selection);
		}

	}

	private class SingleValueDialogCellEditor extends DialogCellEditor {

		public SingleValueDialogCellEditor(Composite composite) {
			super(composite);
		}

		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			Entry entry = (Entry) getValue();
			ReferenceSelectionDialog dialog = new ReferenceSelectionDialog(
					getShell(), null, entry.type);
			if (entry.values.size() > 0) {
				dialog.setInitialElementSelections(entry.values);
			}
			dialog.setTitle("Select reference");
			dialog.create();
			if (dialog.open() == Dialog.OK) {
				Object[] results = dialog.getResult();
				if (results.length > 0) {
					String val = (String) results[0];
					if (dialog.isNewReference()) {
						addNewReferencedInstance(entry.type, val);
					}
					return val;
				}
			}
			return null;
		}
	}

	private void addNewReferencedInstance(IType type, String name) {
		List<String> references = newReferenceInstances.get(type);
		if (references == null) {
			references = new ArrayList<String>();
			newReferenceInstances.put(type, references);
		}
		references.add(name);
	}

	private List<String> getNewReferencedInstancies(IType type) {
		List<String> result = new ArrayList<String>();
		Set<IType> keys = newReferenceInstances.keySet();
		for (IType key : keys) {
			if (key.getFullyQualifiedName()
					.equals(type.getFullyQualifiedName())) {
				result.addAll(newReferenceInstances.get(key));
			}
		}
		return result;
	}

	private class ReferenceSelectionDialog extends SelectionDialog {

		private CLabel messageLabel;
		private CheckboxTableViewer tableViewer;
		private final String[] inputElement;
		private String lclInstanceName;
		private Text instanceNameField;
		private final Button[] buttons = new Button[2];
		private Object[] prevCheckedVals = null;

		private final IType type;
		private boolean isNewReference = false;

		public ReferenceSelectionDialog(Shell parentShell,
				String[] excludeValues, IType type) {
			super(parentShell);
			this.type = type;
			this.inputElement = getInputElements(type, excludeValues);
		}

		private String[] getInputElements(IType type, String[] excludeValues) {
			List<String> possibleElements = getAllElements(type);

			if (excludeValues != null && excludeValues.length > 0) {
				List<String> excludeList = Arrays.asList(excludeValues);

				for (Iterator<String> it = possibleElements.iterator(); it
						.hasNext();) {
					if (excludeList.contains(it.next())) {
						it.remove();
					}
				}
			}

			return possibleElements
					.toArray(new String[possibleElements.size()]);
		}

		private List<String> getAllElements(IType type) {
			List<String> possibleEntries = new ArrayList<String>();
			String typeStr = type.getFullyQualifiedName();
			// for each of the class instances in the map...
			for (Object child : mapEditPart.getChildren()) {
				if (child instanceof ClassInstanceEditPart) {
					ClassInstanceEditPart editPart = (ClassInstanceEditPart) child;
					NodeImpl node = (NodeImpl) editPart.getModel();
					ClassInstance instance = (ClassInstance) node.getElement();
					String fqn = instance.getFullyQualifiedName();
					// get the iArtifact that goes with the class
					IAbstractArtifact iArtifact = null;
					try {
						iArtifact = artMgrSession
								.getArtifactByFullyQualifiedName(fqn);
					} catch (TigerstripeException e) {
						continue;
					}
					if (iArtifact == null)
						continue;
					// then check that iArtifact (and all of the super-types of
					// that iArtifact)
					// to determine whether or not the iArtifact is of the same
					// type as the
					// artifact represented by this field
					do {
						fqn = iArtifact.getFullyQualifiedName();
						if (typeStr.equals(fqn)) {
							possibleEntries.add(instance.getArtifactName());
						}
					} while ((iArtifact = iArtifact.getExtendedArtifact()) != null);
				}
			}

			List<String> refs = getNewReferencedInstancies(type);
			possibleEntries.addAll(refs);

			return possibleEntries;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			int numLayoutCols = 8;
			int nameLabelCols = 2;
			int nameValueCols = 6;
			GridLayout bLayout = new GridLayout();
			bLayout.numColumns = numLayoutCols;
			parent.setLayout(bLayout);
			// add a status label and a separator...
			messageLabel = new CLabel(parent, SWT.NONE);
			setFillLayout(messageLabel, numLayoutCols);
			// add a couple of radio buttons and a text entry box...
			Group box = new Group(parent, SWT.NULL);
			box.setText("Choose an instance");
			setFillLayout(box, numLayoutCols);
			bLayout = new GridLayout();
			bLayout.numColumns = numLayoutCols;
			box.setLayout(bLayout);
			SelectionListener selectionListener = new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					Object source = e.getSource();
					if (source instanceof Button) {
						if ((Button) source == buttons[0]) {
							instanceNameField.setEnabled(true);
							tableViewer.getTable().setEnabled(false);
							if (prevCheckedVals == null) {
								prevCheckedVals = tableViewer
										.getCheckedElements();
								tableViewer.setAllChecked(false);
							}
							lclUpdateOkButton(instanceNameField);
						} else {
							instanceNameField.setEnabled(false);
							tableViewer.getTable().setEnabled(true);
							if (prevCheckedVals != null) {
								tableViewer.setCheckedElements(prevCheckedVals);
								prevCheckedVals = null;
							}
							lclUpdateOkButton(source);
						}
					}
				}
			};
			// first radio button
			buttons[0] = new Button(box, SWT.RADIO);
			buttons[0].setText("Create a new instance");
			buttons[0].addSelectionListener(selectionListener);
			setFillLayout(buttons[0], numLayoutCols);
			// label and text box
			Label nameLabel = new Label(box, SWT.NULL);
			nameLabel.setText("Name:");
			GridData data = new GridData();
			data.horizontalSpan = nameLabelCols;
			nameLabel.setLayoutData(data);
			instanceNameField = new Text(box, SWT.BORDER);
			instanceNameField
					.setToolTipText("The name of the instance of this class.");
			instanceNameField.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					lclUpdateOkButton(e.getSource());
				}
			});
			setFillLayout(instanceNameField, nameValueCols);
			// second radio button
			buttons[1] = new Button(box, SWT.RADIO);
			buttons[1].setText("Select new instance from diagram");
			buttons[1].addSelectionListener(selectionListener);
			setFillLayout(buttons[1], numLayoutCols);
			// this block of code was taken from the ListSelectionDialog
			// createDialogArea() method
			// pretty much "as-is". The only real difference was that the code
			// that creates the
			// "Select All" and "Deselect All" buttons has been left off here
			Composite composite = (Composite) super.createDialogArea(box);
			initializeDialogUnits(composite);
			createMessageArea(composite);
			setFillLayout(composite, numLayoutCols);

			tableViewer = CheckboxTableViewer.newCheckList(composite,
					SWT.BORDER);
			data = new GridData(GridData.FILL_BOTH);
			data.horizontalIndent = 0;
			data.verticalIndent = 0;
			data.grabExcessHorizontalSpace = true;
			data.grabExcessVerticalSpace = true;
			tableViewer.getTable().setLayoutData(data);
			Composite tableComposite = tableViewer.getControl().getParent();
			data = (GridData) tableComposite.getLayoutData();
			data.horizontalIndent = 0;
			data.verticalIndent = 0;
			data.grabExcessVerticalSpace = true;
			data.grabExcessHorizontalSpace = true;
			data.heightHint = 100;
			GridLayout layout = (GridLayout) tableComposite.getLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.numColumns = numLayoutCols;
			layout.horizontalSpacing = 0;
			layout.verticalSpacing = 0;
			tableViewer.setLabelProvider(new LabelProvider());
			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setInput(inputElement);

			// initialize page
			if (!getInitialElementSelections().isEmpty()) {
				checkInitialSelections();
			}

			Dialog.applyDialogFont(composite);

			tableViewer.addCheckStateListener(new ICheckStateListener() {
				public void checkStateChanged(CheckStateChangedEvent event) {
					boolean newValue = event.getChecked();
					if (newValue == true)
						tableViewer.setAllChecked(false);
					tableViewer.setChecked(event.getElement(), newValue);
					lclUpdateOkButton(event.getSource());
				}
			});
			return box;
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			super.createButtonsForButtonBar(parent);
			if (inputElement.length == 0) {
				instanceNameField.setEnabled(true);
				tableViewer.getTable().setEnabled(false);
				buttons[0].setSelection(true);
				buttons[1].setEnabled(false);
				lclUpdateOkButton(instanceNameField);
			} else {
				instanceNameField.setEnabled(false);
				tableViewer.getTable().setEnabled(true);
				buttons[1].setSelection(true);
				lclSetInfoMessage("Select instance");
			}
		}

		private void checkInitialSelections() {
			Iterator<?> itemsToCheck = getInitialElementSelections().iterator();
			while (itemsToCheck.hasNext()) {
				tableViewer.setChecked(itemsToCheck.next(), true);
			}
		}

		protected void lclUpdateOkButton(Object eventSource) {
			Button okButton = this.getButton(IDialogConstants.OK_ID);
			if (okButton == null)
				return;
			if (eventSource instanceof Text) {
				lclInstanceName = ((Text) eventSource).getText();
				Matcher classNameMatcher = TigerstripeValidationUtils.classNamePattern
						.matcher(lclInstanceName);
				Matcher elementNameMatcher = TigerstripeValidationUtils.elementNamePattern
						.matcher(lclInstanceName);
				if (lclInstanceName == null || "".equals(lclInstanceName)) {
					okButton.setEnabled(false);
					lclSetInfoMessage("Enter an instance name");
				} else if (!classNameMatcher.matches()
						&& !elementNameMatcher.matches()) {
					okButton.setEnabled(false);
					lclSetErrorMessage("'" + lclInstanceName
							+ "' is not a legal instance name");
				} else if (instanceNames.contains(lclInstanceName)
						&& instanceMap.get(lclInstanceName) != instance
						|| getNewReferencedInstancies(type).contains(
								lclInstanceName)) {
					okButton.setEnabled(false);
					lclSetErrorMessage("Instance '" + lclInstanceName
							+ "' already exists");
				} else {
					okButton.setEnabled(true);
					lclSetInfoMessage("Create an instance");
				}
			} else if (eventSource instanceof Button) {
				lclSetInfoMessage("Select instance");
				okButton.setEnabled(true);
			} else if (eventSource instanceof CheckboxTableViewer) {
				okButton.setEnabled(true);
			}
		}

		protected void lclSetInfoMessage(String message) {
			messageLabel.setText(message);
			if (message != null && message.length() > 0) {
				Image newImage = JFaceResources
						.getImage(Dialog.DLG_IMG_MESSAGE_INFO);
				messageLabel.setImage(newImage);
				messageLabel.setVisible(true);
			} else {
				messageLabel.setVisible(false);
			}
		}

		protected void lclSetErrorMessage(String message) {
			messageLabel.setText(message);
			if (message != null && message.length() > 0) {
				Image newImage = JFaceResources
						.getImage(Dialog.DLG_IMG_MESSAGE_ERROR);
				messageLabel.setImage(newImage);
				messageLabel.setVisible(true);
			} else {
				messageLabel.setVisible(false);
			}
		}

		@Override
		protected void okPressed() {
			List<String> result = new ArrayList<String>();
			if (buttons[0].getSelection()) {
				result.add(instanceNameField.getText());
				isNewReference = true;
			}
			if (!buttons[0].getSelection()) {
				Object[] checkeds = tableViewer.getCheckedElements();
				for (Object checked : checkeds) {
					result.add((String) checked);
				}
			}
			setResult(result);
			super.okPressed();
		}

		public boolean isNewReference() {
			return isNewReference;
		}

		private void setFillLayout(Control control, int numColumns) {
			GridData bgd = new GridData(GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL);
			bgd.horizontalSpan = numColumns;
			control.setLayoutData(bgd);
		}

	}

	private boolean isEnumeration(IType type) {
		return (type.isEnum());
	}

	private boolean isBooleanPrimitive(IType type) {
		return (type.getFullyQualifiedName().equals("boolean"));
	}

	private class MyLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		private Image getImage(boolean isSelected) {
			String key = isSelected ? Images.CHECKED_ICON
					: Images.UNCHECKED_ICON;
			return Images.get(key);
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return (columnIndex == 0) ? // COMPLETED_COLUMN?
			getImage(((Entry) element).isEnabled())
					: null;
		}

		public String getColumnText(Object element, int index) {
			Entry entry = (Entry) element;
			switch (index) {
			case SET_COLUMN_IDX:
				return "";
			case FIELDNAME_COLUMN_IDX:
				return entry.name;
			case FIELDSOURCE_COLUMN_IDX:
				return entry.source;
			case FIELDTYPE_COLUMN_IDX:
				return entry.type.getName();
			case FIELDVALUE_COLUMN_IDX:
				return entry.stringValue();
			default:
				return "";
			}
		}

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		Button okButton = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		if ("".equals(instanceName)) {
			okButton.setEnabled(false);
			setInfoMessage("Enter an instance name");
		} else {
			setInfoMessage("");
		}
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	public HashMap<String, List<Object>> getSelection() {
		return selectionMap;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public Map<IType, List<String>> getNewReferenceInstances() {
		return newReferenceInstances;
	}
}
