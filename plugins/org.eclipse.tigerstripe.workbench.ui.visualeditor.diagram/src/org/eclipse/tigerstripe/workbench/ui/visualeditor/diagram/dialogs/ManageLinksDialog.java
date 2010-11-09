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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.TSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;

public class ManageLinksDialog extends TSMessageDialog {

	private Set<IRelationship> possibleRelationships;
	private Set<String> associationsInMap;
	private Set<String> dependenciesInMap;
	private HashMap<String, IRelationship> selectionMap = new HashMap<String, IRelationship>();
	private List<Entry> entries = new ArrayList<Entry>();
    private static boolean showFullyQualifiedName = true;

	// Set the table column property names
	private final String SET_COLUMN = "";
	private final String RELATIONSHIP_NAME_COLUMN = "Association Name";
	private final String RELATIONSHIP_AEND_COLUMN = "A End";
	private final String RELATIONSHIP_ZEND_COLUMN = "Z End";
	// Set column names
	private final String[] columnNames = new String[] { SET_COLUMN,
			RELATIONSHIP_NAME_COLUMN, RELATIONSHIP_AEND_COLUMN,
			RELATIONSHIP_ZEND_COLUMN };
	private final String[] columnLabels = new String[] { "SELECTED", "NAME",
			"AEND", "ZEND" };
	private final String SHOW_QUALIFIED_NAMES_PREF = "ShowFullyQualifiedNamesInManageAssocationsView";
	private final List columnNamesAsList = Arrays.asList(columnNames);
	public static final Object ASSOCIATION_TYPE = new Object();
	public static final Object ASSOCIATION_CLASS_TYPE = new Object();
	public static final Object DEPENDENCY_TYPE = new Object();

	// and the table/table viewer references
	private TableViewer tableViewer;
	private TableSorter tableSorter;
	private Table table;
	private CellEditor[] cellEditors;
	private CellEditor textCellEditor;
	private CellEditor checkBoxCellEditor;

	public ManageLinksDialog(Shell parentShell,
			Set<IRelationship> possibleRelationships,
			Set<String> associationsInMap, Set<String> dependenciesInMap) {
		super(parentShell);
		this.possibleRelationships = possibleRelationships;
		this.associationsInMap = associationsInMap;
		this.dependenciesInMap = dependenciesInMap;
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 2;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		initDialog();
		createInstanceDefinitionControl(composite, nColumns);

		return area;
	}

	protected void initDialog() {
		getShell().setText("Manage Associations");
		getShell().setMinimumSize(450, 225);

        showFullyQualifiedName = TigerstripeDiagramEditorPlugin.getInstance().getPreferenceStore().getBoolean(SHOW_QUALIFIED_NAMES_PREF);
	}

	private void createInstanceDefinitionControl(Composite composite,
			int nColumns) {

		entries.clear();
		// now add the values in the set of associations to the entries list
		for (IRelationship relationship : possibleRelationships) {
			Entry entry = new Entry();
			entry.setRelationship(relationship);
			// if there is already an association or dependency in the map
			// with this name, check the selection box for that association
			// or dependency and add it to the selection map
			if (associationsInMap.contains(entry.getFullyQualifiedRelationshipName())
					|| dependenciesInMap.contains(entry.getFullyQualifiedRelationshipName())) {
				entry.setEnabled(true);
				selectionMap.put(entry.getFullyQualifiedRelationshipName(), entry
						.getRelationship());
			}
			entries.add(entry);
		}
		Group box = new Group(composite, SWT.NULL);
		box.setText("Associations");
		GridData bgd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		bgd.horizontalSpan = 8;
		box.setLayoutData(bgd);
		//GridLayout bLayout = new GridLayout();
		//bLayout.numColumns = 2;
		//box.setLayout(bLayout);
        TableColumnLayout layout = new TableColumnLayout();
		box.setLayout(layout);
		// create a TableViewer in this box (along with the
		// associated table)
		tableViewer = createTableViewer(box);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		for (int i = 0; i < columnNames.length; i++) {
			final TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(columnNames[i]);
			layout.setColumnData(column, new ColumnWeightData(1));
			if (i == 0) {
				column.setWidth(25);
	            column.setResizable(false);
			}
			else {
	            column.setResizable(true);
	            column.setMoveable(true);
			}
			final int index = i;
			column.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    tableSorter.setColumn(index);
                    int dir = tableViewer.getTable().getSortDirection();
                    if (tableViewer.getTable().getSortColumn() == column) {
                        dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
                    } else {
                        dir = SWT.DOWN;
                    }
                    tableViewer.getTable().setSortDirection(dir);
                    tableViewer.getTable().setSortColumn(column);
                    tableViewer.refresh();
                }
            });
		}
		tableViewer.setInput(entries);
		tableSorter = new TableSorter();
		tableViewer.setSorter(tableSorter);
		table.pack();
		
		// create a 'Show fully qualified names' checkbox
		Button showFullyQualifiedNamesButton = new Button(composite, SWT.CHECK);
		showFullyQualifiedNamesButton.setText("Show fully qualified names");
		showFullyQualifiedNamesButton.setSelection(showFullyQualifiedName);
		SelectionListener listener = new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
                showFullyQualifiedName = !showFullyQualifiedName;
		        IPreferenceStore prefs = TigerstripeDiagramEditorPlugin.getInstance().getPreferenceStore();
		        prefs.setValue(SHOW_QUALIFIED_NAMES_PREF, showFullyQualifiedName);
		        tableViewer.refresh();
		    }
		};
		showFullyQualifiedNamesButton.addSelectionListener(listener);
	}

	private class Entry {

		private boolean enabled = false;
		private IRelationship relationship;
		private String relationshipName = "";
		private String fullyQualifiedRelationshipName = "";
		private IRelationshipEnd aEnd;
		private String aEndName = "";
		private String fullyQualifiedAEndName = "";
		private IRelationshipEnd zEnd;
		private String zEndName = "";
		private String fullyQualifiedZEndName = "";
		private Object relationshipType;

		@Override
		public boolean equals(Object other) {
			if (other instanceof Entry) {
				Entry ent = (Entry) other;
				if (ent.relationshipType != relationshipType)
					return false;
				else if (ent.relationshipType == ManageLinksDialog.ASSOCIATION_CLASS_TYPE
						|| ent.relationshipType == ManageLinksDialog.ASSOCIATION_TYPE) {
					String entName = ((IAssociationArtifact) ent
							.getRelationship()).getName();
					String thisName = ((IAssociationArtifact) relationship)
							.getName();
					if (!entName.equals(thisName))
						return false;
					else if (ent.getAEnd() != aEnd)
						return false;
					else if (ent.getZEnd() != zEnd)
						return false;
				} else if (ent.relationshipType == ManageLinksDialog.DEPENDENCY_TYPE) {
					String entName = ((IDependencyArtifact) ent
							.getRelationship()).getName();
					String thisName = ((IDependencyArtifact) relationship)
							.getName();
					if (!entName.equals(thisName))
						return false;
					else if (ent.getAEnd() != aEnd)
						return false;
					else if (ent.getZEnd() != zEnd)
						return false;
				}
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

		public IRelationship getRelationship() {
			return relationship;
		}

		public void setRelationship(IRelationship relationship) {
			this.relationship = relationship;
			if (relationship instanceof IAssociationArtifact) {
				IAssociationArtifact association = (IAssociationArtifact) relationship;
				relationshipName = association.getName();
				fullyQualifiedRelationshipName = association.getFullyQualifiedName();
				aEnd = association.getRelationshipAEnd();
				aEndName = aEnd.getType().getName();
				fullyQualifiedAEndName = aEnd.getType().getFullyQualifiedName();
				zEnd = association.getRelationshipZEnd();
				zEndName = zEnd.getType().getName();
				fullyQualifiedZEndName = zEnd.getType().getFullyQualifiedName();
			} else {
				IDependencyArtifact dependency = (IDependencyArtifact) relationship;
				relationshipName = dependency.getName();
				fullyQualifiedRelationshipName = dependency.getFullyQualifiedName();
				aEnd = dependency.getRelationshipAEnd();
				aEndName = aEnd.getType().getName();
				fullyQualifiedAEndName = aEnd.getType().getFullyQualifiedName();
				zEnd = dependency.getRelationshipZEnd();
				zEndName = zEnd.getType().getName();
				fullyQualifiedZEndName = zEnd.getType().getFullyQualifiedName();
			}
		}
		
		public String getRelationshipName() {
		    return relationshipName;
		}

		public String getFullyQualifiedRelationshipName() {
			return fullyQualifiedRelationshipName;
		}

		public Object getRelationshipType() {
			return relationshipType;
		}

		public IRelationshipEnd getAEnd() {
			return aEnd;
		}
		
		public String getAEndName() {
		    return aEndName;
		}

		public String getFullyQualifiedAEndName() {
			return fullyQualifiedAEndName;
		}

		public IRelationshipEnd getZEnd() {
			return zEnd;
		}
		
		public String getZEndName() {
		    return zEndName;
		}

		public String getFullyQualifiedZEndName() {
			return fullyQualifiedZEndName;
		}

	}

	private class EntryListCellModifier implements ICellModifier {

		public EntryListCellModifier(TableViewer viewer) {
		}

		public boolean canModify(Object element, String property) {
			if ("SELECTED".equals(property))
				return true;
			return false;
		}

		public Object getValue(Object element, String property) {
			if ("SELECTED".equals(property)) {
				Entry entry = (Entry) element;
				return entry.isEnabled();
			} else if ("NAME".equals(property)) {
				Entry entry = (Entry) element;
				return entry.getRelationship();
			} else if ("AEND".equals(property)) {
				Entry entry = (Entry) element;
				return entry.getAEnd();
			} else if ("ZEND".equals(property)) {
				Entry entry = (Entry) element;
				return entry.getZEnd();
			}
			return null;
		}

		public void modify(Object item, String property, Object value) {
			// Find the index of the column
			int columnIndex = Arrays.asList(columnLabels).indexOf(property);

			TableItem[] selectedItems = tableViewer.getTable().getSelection();
			TableItem tableItem = selectedItems[0];
			int selectedIndex = tableViewer.getTable().getSelectionIndex();
			Entry entry = (Entry) tableItem.getData();

			switch (columnIndex) {
			case 0: // CHECKED Column
				boolean includeVal = ((Boolean) value).booleanValue();
				entry.setEnabled(includeVal);
				if (includeVal) {
					selectionMap
							.put(entry.fullyQualifiedRelationshipName, entry.relationship);
				} else {
					if (selectionMap.keySet().contains(entry.fullyQualifiedRelationshipName))
						selectionMap.remove(entry.fullyQualifiedRelationshipName);
				}
				break;
			default:
			}
			tableViewer.refresh();
			tableViewer.getTable().setSelection(selectedIndex);
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
			case 0:
				return "";
			case 1:
			    if (showFullyQualifiedName)
			        return entry.getFullyQualifiedRelationshipName();
			    return entry.getRelationshipName();
			case 2:
                if (showFullyQualifiedName)
                    return entry.getFullyQualifiedAEndName();
                return entry.getAEndName();
			case 3:
                if (showFullyQualifiedName)
                    return entry.getFullyQualifiedZEndName();
                return entry.getZEndName();
			default:
				return "";
			}
		}
	}
	
	private class TableSorter extends ViewerSorter {
	    private int column;
	    private static final int DESCENDING = 1;

	    private int direction = DESCENDING;

	    public TableSorter() {
	        this.column = 0;
	    }

	    public void setColumn(int column) {
	        if (column == this.column) {
	            direction = 1 - direction;
	        } else {
	            this.column = column;
	            direction = DESCENDING;
	        }
	    }

	    @Override
	    public int compare(Viewer viewer, Object o1, Object o2) {
	        int newOrder = 0;
	        Entry e1 = (Entry)o1;
	        Entry e2 = (Entry)o2;
	        switch (column) {
	        case 1:
	            if (showFullyQualifiedName) {
                    newOrder = e1.getFullyQualifiedRelationshipName().compareTo(e2.getFullyQualifiedRelationshipName());
	            }
	            else {
                    newOrder = e1.getRelationshipName().compareTo(e2.getRelationshipName());
	            }
	            break;
	        case 2:
                if (showFullyQualifiedName) {
                    newOrder = e1.getFullyQualifiedAEndName().compareTo(e2.getFullyQualifiedAEndName());
                }
                else {
                    newOrder = e1.getAEndName().compareTo(e2.getAEndName());
                }
                break;
	            
	        case 3:
                if (showFullyQualifiedName) {
                    newOrder = e1.getFullyQualifiedZEndName().compareTo(e2.getFullyQualifiedZEndName());
                }
                else {
                    newOrder = e1.getZEndName().compareTo(e2.getZEndName());
                }
	            break;
	            default:
	                newOrder = 1;
	        }
	        // If descending order, flip the direction
	        if (direction == DESCENDING) {
	            newOrder = -newOrder;
	        }
	        return newOrder;
	    }
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create an "OK" button
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		// create a "select all" button and register a listener for it
		Button selectAllButton = createButton(parent,
				IDialogConstants.SELECT_ALL_ID, "Select All", false);
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Entry entry : entries) {
					entry.setEnabled(true);
					selectionMap.put(entry.getFullyQualifiedRelationshipName(), entry
							.getRelationship());
				}
				tableViewer.refresh();
			}
		};
		selectAllButton.addSelectionListener(listener);
		// create a "deselect all" button and register a listener for it
		Button deSelectAllButton = createButton(parent,
				IDialogConstants.DESELECT_ALL_ID, "Deselect All", false);
		listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Entry entry : entries) {
					entry.setEnabled(false);
					selectionMap.remove(entry.getFullyQualifiedRelationshipName());
				}
				tableViewer.refresh();
			}
		};
		deSelectAllButton.addSelectionListener(listener);
		// and create a "Cancel" button, making it the default
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, true);
	}

	// @Override
	// protected void okPressed() {
	// for (String key : selectionMap.keySet()) {
	// TigerstripeRuntime.logTraceMessage(key + " -> " + selectionMap.get(key));
	// }
	// super.okPressed();
	// }

	public HashMap<String, IRelationship> getSelection() {
		return selectionMap;
	}

	public List getColumnNames() {
		return Collections.unmodifiableList(columnNamesAsList);
	}

}
