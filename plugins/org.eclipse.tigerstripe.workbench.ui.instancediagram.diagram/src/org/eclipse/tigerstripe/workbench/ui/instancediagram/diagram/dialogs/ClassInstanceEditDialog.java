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
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.NewTSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;
import org.eclipse.ui.dialogs.SelectionDialog;

public class ClassInstanceEditDialog extends NewTSMessageDialog {

	private IAbstractArtifact artifact;
	private IArtifactManagerSession artMgrSession;
	private DiagramGraphicalViewer mapViewer;
	private InstanceMapEditPart mapEditPart;
	private ClassInstance instance = null;
	private String instanceName = "";
	private Text instanceNameField;
	private HashMap<String, List<Object>> selectionMap = new HashMap<String, List<Object>>();
	private Set<String> instanceNames = new HashSet<String>();
	private Map<String, Instance> instanceMap = new HashMap<String, Instance>();
	private List<Entry> entries = new ArrayList<Entry>();

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
	private final List columnNamesAsList = Arrays.asList(columnNames);
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
	// used when creating new reference instances...
	private HashMap<String, String> newReferenceInstanceNames = new HashMap<String, String>();
	private HashMap<String, IType> newReferenceInstanceTypes = new HashMap<String, IType>();
	private boolean isNewInstance = false;

	public ClassInstanceEditDialog(Shell parentShell,
			IAbstractArtifact artifact, InstanceMapEditPart mapEditPart) {
		super(parentShell, "", "Edit instance name/attributes");
		this.artifact = artifact;
		this.mapEditPart = mapEditPart;
		// and get the artMgrSession associated with this mapEditPart so that we
		// can use
		// it later
		mapViewer = (DiagramGraphicalViewer) mapEditPart.getViewer();
		DiagramEditDomain domain = (DiagramEditDomain) mapViewer
				.getEditDomain();
		IResource res = (IResource) domain.getEditorPart().getEditorInput()
				.getAdapter(IResource.class);
		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getITigerstripeProjectFor(res.getProject());
		if (!(aProject instanceof ITigerstripeProject))
			throw new RuntimeException("non-Tigerstripe Project found");
		ITigerstripeProject project = (ITigerstripeProject) aProject;
		try {
			artMgrSession = project.getArtifactManagerSession();
		} catch (TigerstripeException e) {
			throw new RuntimeException("IArtifactManagerSession not found");
		}
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		isNewInstance = true;
	}

	public ClassInstanceEditDialog(Shell parentShell, ClassInstance instance,
			InstanceMapEditPart mapEditPart) {
		super(parentShell, "", "Edit instance name/attributes");
		this.instance = instance;
		this.mapEditPart = mapEditPart;
		// and get the IAbstractArtifact associated with this instance so that
		// we can use
		// it later (to display a list of the fields that can be set by the
		// user)
		String fullyQualifiedName = instance.getFullyQualifiedName();
		mapViewer = (DiagramGraphicalViewer) mapEditPart.getViewer();
		DiagramEditDomain domain = (DiagramEditDomain) mapViewer
				.getEditDomain();
		IResource res = (IResource) domain.getEditorPart().getEditorInput()
				.getAdapter(IResource.class);
		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getITigerstripeProjectFor(res.getProject());
		if (!(aProject instanceof ITigerstripeProject))
			throw new RuntimeException("non-Tigerstripe Project found");
		ITigerstripeProject project = (ITigerstripeProject) aProject;
		try {
			artMgrSession = project.getArtifactManagerSession();
			artifact = artMgrSession
					.getArtifactByFullyQualifiedName(fullyQualifiedName);
		} catch (TigerstripeException e) {
			throw new RuntimeException("matching IAbstractArtifact not found");
		}
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
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
		Composite area = (Composite) super.createDialogArea(parent);
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
			if (isNewInstance) {
				for (IField field : fields) {
					String defValue = field.getDefaultValue();
					if (defValue != null)
						variableNameValMap.put(field.getName(), defValue);
				}
			} else if (instance != null) {
				EList variableList = instance.getVariables();
				for (Object obj : variableList) {
					Variable variable = (Variable) obj;
					variableNameValMap.put(variable.getName(), variable
							.getValue());
				}
			}
		}
		Set<String> variableNames = variableNameValMap.keySet();
		// if an instance has been passed in via a constructor, get the list
		// of associations that exist in that instance
		Map<String, Set<String>> associationNameValMap = new HashMap<String, Set<String>>();
		if (fields != null && fields.length > 0 && instance != null) {
			EList associationList = instance.getAssociations();
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
					valSet = new HashSet();
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
			entry.type = field.getIType();
			if (variableNames.contains(entry.name)) {
				entry.enabled = true;
				String value = variableNameValMap.get(entry.name);
				// if here, the field is a primitive type, so the value is a
				// single string
				String[] values = new String[] { value };
				entry.values = Arrays.asList(values);
				selectionMap.put(entry.name, Arrays.asList(new Object[] {
						entry.type, entry.stringValue() }));
			} else if (associationNames.contains(entry.name)) {
				// if here, it's a non-primitive type (reference) type, so
				// the "value" is a list of the names of the instances of that
				// "type" in the
				entry.enabled = true;
				Set<String> stringVals = associationNameValMap.get(entry.name);
				String[] values = new String[stringVals.size()];
				associationNameValMap.get(entry.name).toArray(values);
				entry.values = Arrays.asList(values);
				selectionMap.put(entry.name, Arrays.asList(new Object[] {
						entry.type, entry.stringValue() }));
			} else {
				entry.values = new ArrayList<String>();
				entry.values.add("");
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

		public boolean enabled = false;
		public String name;
		public String source;
		public IType type;
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

	}

	private class EntryListCellModifier implements ICellModifier {

		private TableViewer viewer;

		public EntryListCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		public boolean canModify(Object element, String property) {
			if (element instanceof Entry) {
				Entry entry = (Entry) element;
				if (!InstanceDiagramUtils
						.isPrimitive(artMgrSession, entry.type)) {
					cellEditors[columnNames.length - 1] = new MyDialogCellEditor(
							table, entry.name, entry.type);
				} else if (isEnumeration(entry.type)) {
					String[] vals = getComboBoxVals(entry.type);
					cellEditors[columnNames.length - 1] = new MyEnumerationCellEditor(
							table, vals, SWT.READ_ONLY);
				} else if (isBooleanPrimitive(entry.type)) {
					String[] vals = booleanVals;
					cellEditors[columnNames.length - 1] = new MyEnumerationCellEditor(
							table, vals, SWT.READ_ONLY);
				} else {
					cellEditors[columnNames.length - 1] = textCellEditor;
				}
			}
			if ("VALUE".equals(property) || "SELECTED".equals(property))
				return true;
			return false;
		}

		public Object getValue(Object element, String property) {
			if ("VALUE".equals(property)) {
				Entry entry = (Entry) element;
				return entry.stringValue();
			} else if ("SELECTED".equals(property)) {
				Entry entry = (Entry) element;
				return entry.enabled;
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
					selectionMap.put(entry.name, Arrays.asList(new Object[] {
							entry.type, entry.stringValue() }));
				} else {
					if (selectionMap.keySet().contains(entry.name))
						selectionMap.remove(entry.name);
				}
				break;
			case FIELDVALUE_COLUMN_IDX: // VALUE Column
				// if field is not a primitive type, value is a comma-separated
				// list of strings
				String[] values = null;
				if (InstanceDiagramUtils.isPrimitive(artMgrSession, entry.type)) {
					values = new String[] { (String) value };
					if (((String) value).length() == 0)
						entry.enabled = false;
					else
						entry.enabled = true;
				} else {
					List<String> valueList = InstanceDiagramUtils
							.instanceReferencesAsList((String) value);
					values = (String[]) valueList.toArray();
					if (values.length == 1 && values[0].length() == 0)
						entry.enabled = false;
					else
						entry.enabled = true;
				}
				entry.values = Arrays.asList(values);
				if (entry.isEnabled()) {
					// update entry in map if is selected
					selectionMap.put(entry.name, Arrays.asList(new Object[] {
							entry.type, entry.stringValue() }));
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
				ILabel[] labels = iArtifact.getILabels();
				List<String> lclStringVals = new ArrayList<String>();
				for (ILabel label : labels)
					lclStringVals.add(label.getName());
				stringVals.addAll(0, lclStringVals);
			} while ((iArtifact = iArtifact.getExtendedArtifact()) != null);
			stringVals.add(0, "");
			String[] items = new String[stringVals.size()];
			stringVals.toArray(items);
			return items;
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
		localTableViewer.setCellModifier(new EntryListCellModifier(
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
		CCombo comboBox;

		public MyEnumerationCellEditor(Composite parent, String[] items,
				int style) {
			super(parent, items, style);
			init(items);
		}

		public MyEnumerationCellEditor(Composite parent, String[] items) {
			super(parent, items);
			init(items);
		}

		private void init(String[] items) {
			// put together a list of items to use later when getting and
			// setting values
			itemsList = Arrays.asList(items);
			// and then perform a bit of setup on the comboBox...first get it
			comboBox = (CCombo) this.getControl();
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

	private class MyDialogCellEditor extends DialogCellEditor {

		List<String> selectedEntries = new ArrayList<String>();
		List<String> possibleEntries = new ArrayList<String>();
		Control control;
		IType type;
		String fieldName;

		public MyDialogCellEditor(String fieldName, IType type) {
			super();
			init(fieldName, type);
		}

		public MyDialogCellEditor(Composite comp, String fieldName, IType type) {
			super(comp);
			init(fieldName, type);
		}

		public MyDialogCellEditor(Composite parent, int style,
				String fieldName, IType type) {
			super(parent, style);
			init(fieldName, type);
		}

		private void init(String fieldName, IType type) {
			this.fieldName = fieldName;
			this.type = type;
			// then add a cell editor listener that will refresh the labels when
			// the value is changed in the cell editor
		}

		private void reloadEntryList() {
			possibleEntries.clear();
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
						if (typeStr.equals(fqn))
							possibleEntries.add(instance.getArtifactName());
					} while ((iArtifact = iArtifact.getExtendedArtifact()) != null);
				}
			}
		}

		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			reloadEntryList();
			String[] entries = new String[possibleEntries.size()];
			for (int i = 0; i < possibleEntries.size(); i++)
				entries[i] = possibleEntries.get(i);
			SelectionDialog dialog = null;
			// depending on the multiplicity, either open a regular
			// ListSelectionDialog or open
			// a "SingleListSelectionDialog" (an inner class of the
			// ClassInstanceEditDialog class
			// that only allows for selection of a single element from the list
			// it presents)
			EMultiplicity typeMult = type.getTypeMultiplicity();
			if (InstanceDiagramUtils.getRelationshipMuliplicity(typeMult) == InstanceDiagramUtils.RELATIONSHIP_SINGLE)
				dialog = new TSListSelectionDialog(getShell(), entries,
						new ArrayContentProvider(), new LabelProvider(),
						"Select referenced instance", fieldName, true);
			else
				dialog = new TSListSelectionDialog(getShell(), entries,
						new ArrayContentProvider(), new LabelProvider(),
						"Select referenced instances", fieldName, false);
			dialog.setInitialElementSelections(selectedEntries);
			dialog.setTitle("Select references");
			dialog.create();
			boolean returnStatus = (dialog.open() == Dialog.OK);
			Object[] results = dialog.getResult();
			String resultString = "";
			if (returnStatus) {
				if (selectedEntries.size() > 0)
					selectedEntries = new ArrayList<String>();
				for (Object obj : results)
					selectedEntries.add((String) obj);
				newReferenceInstanceTypes.put(fieldName, type);
			}
			return ClassInstanceEditDialog.listAsCSV(selectedEntries);
		}

		@Override
		protected void updateContents(Object value) {
			if (value != null && value instanceof String && !"".equals(value))
				selectedEntries = Arrays
						.asList(((String) value).split("[, ]+"));
			super.updateContents(value);
		}

	}

	private class TSListSelectionDialog extends SelectionDialog {

		private CLabel messageLabel;
		private CheckboxTableViewer tableViewer;
		private String[] inputElement;
		private String lclInstanceName;
		private IStructuredContentProvider contentProvider;
		private ILabelProvider labelProvider;
		private boolean isSingleSelection;
		private String fieldName;
		private Text instanceNameField;
		private Button[] buttons = new Button[2];
		private final String[] buttonLabels = new String[] {
				"Create a new instance", "Select new instance(s) from diagram" };
		private Object[] prevCheckedVals = null;

		public TSListSelectionDialog(Shell parentShell, String[] inputElement,
				IStructuredContentProvider contentProvider,
				ILabelProvider labelProvider, String message, String fieldName,
				boolean isSingleSelection) {
			super(parentShell);
			this.inputElement = inputElement;
			this.labelProvider = labelProvider;
			this.contentProvider = contentProvider;
			this.isSingleSelection = isSingleSelection;
			this.fieldName = fieldName;
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
							if (isSingleSelection && prevCheckedVals == null) {
								prevCheckedVals = tableViewer
										.getCheckedElements();
								tableViewer.setAllChecked(false);
							}
							lclUpdateOkButton(instanceNameField);
						} else {
							instanceNameField.setEnabled(false);
							tableViewer.getTable().setEnabled(true);
							if (isSingleSelection && prevCheckedVals != null) {
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
			buttons[0].setText(buttonLabels[0]);
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
			String existingNewReferenceInstanceName = newReferenceInstanceNames
					.get(fieldName);
			if (existingNewReferenceInstanceName != null
					&& existingNewReferenceInstanceName.length() > 0)
				instanceNameField.setText(existingNewReferenceInstanceName);
			// second radio button
			buttons[1] = new Button(box, SWT.RADIO);
			buttons[1].setText(buttonLabels[1]);
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
			tableViewer.setLabelProvider(labelProvider);
			tableViewer.setContentProvider(contentProvider);
			initializeViewer();

			// initialize page
			if (!getInitialElementSelections().isEmpty()) {
				checkInitialSelections();
			}

			Dialog.applyDialogFont(composite);

			// here's our "new" code for this method (i.e. the code that is not
			// based on the
			// ListSelectionDialog class' createDialogArea() method). Now that
			// we've created
			// the dialog we want (without the select all and deselect all
			// buttons), add our
			// check state listener and return this composite as a Control
			if (isSingleSelection)
				tableViewer
						.addCheckStateListener(new SingleCheckStateListener());
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
				lclSetInfoMessage("Select instance(s)");
			}
		}

		private class SingleCheckStateListener implements ICheckStateListener {
			public void checkStateChanged(CheckStateChangedEvent event) {
				boolean newValue = event.getChecked();
				if (newValue == true)
					tableViewer.setAllChecked(false);
				tableViewer.setChecked(event.getElement(), newValue);
				lclUpdateOkButton(event.getSource());
			}
		}

		// the following are utility methods that were taken almost directly
		// from the
		// ListSelectionDialog class (some variable names have been changed,
		// other than that
		// it's pretty much a cut and paste for these three methods)
		private void initializeViewer() {
			tableViewer.setInput(inputElement);
		}

		private void checkInitialSelections() {
			Iterator itemsToCheck = getInitialElementSelections().iterator();
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
						&& instanceMap.get(lclInstanceName) != instance) {
					okButton.setEnabled(false);
					lclSetErrorMessage("Instance '" + lclInstanceName
							+ "' already exists");
				} else {
					okButton.setEnabled(true);
					lclSetInfoMessage("Create an instance");
				}
			} else if (eventSource instanceof Button) {
				lclSetInfoMessage("Select instance(s)");
				okButton.setEnabled(true);
			} else if (eventSource instanceof CheckboxTableViewer) {
				okButton.setEnabled(true);
			}
		}

		protected void lclSetMessage(String message) {
			messageLabel.setText(message);
			if (message != null && message.length() > 0) {
				messageLabel.setImage(null);
				messageLabel.setVisible(true);
			} else {
				messageLabel.setVisible(false);
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

		protected void lclSetWarningMessage(String message) {
			messageLabel.setText(message);
			if (message != null && message.length() > 0) {
				Image newImage = JFaceResources
						.getImage(Dialog.DLG_IMG_MESSAGE_WARNING);
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
			ArrayList list = new ArrayList();
			if (buttons[0].getSelection()) {
				// are adding a new instance, so get the name
				String strVal = instanceNameField.getText();
				newReferenceInstanceNames.put(fieldName, strVal);
				list.add(strVal);
			}
			if (!buttons[0].getSelection() || !isSingleSelection) {
				// are picking from list of existing elements, so get the list.
				Object[] children = contentProvider.getElements(inputElement);
				// from that list, build a list of selected children.
				if (children != null) {
					for (int i = 0; i < children.length; ++i) {
						Object element = children[i];
						if (tableViewer.getChecked(element)) {
							list.add(element);
						}
					}
				}
			}
			setResult(list);
			super.okPressed();
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
			String key = isSelected ? TigerstripePluginImages.CHECKED_ICON
					: TigerstripePluginImages.UNCHECKED_ICON;
			return TigerstripePluginImages.get(key);
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

	// @Override
	// protected void okPressed() {
	// for (String key : selectionMap.keySet()) {
	// TigerstripeRuntime.logTraceMessage(key + " -> " + selectionMap.get(key));
	// }
	// super.okPressed();
	// }

	public HashMap<String, List<Object>> getSelection() {
		return selectionMap;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public List getColumnNames() {
		return Collections.unmodifiableList(columnNamesAsList);
	}

	public HashMap<String, String> getNewReferenceInstanceNames() {
		return newReferenceInstanceNames;
	}

	public boolean areAddingNewReferenceInstances() {
		return newReferenceInstanceNames.keySet().size() > 0;
	}

	public HashMap<String, IType> getNewReferenceInstanceTypes() {
		return newReferenceInstanceTypes;
	}

}
