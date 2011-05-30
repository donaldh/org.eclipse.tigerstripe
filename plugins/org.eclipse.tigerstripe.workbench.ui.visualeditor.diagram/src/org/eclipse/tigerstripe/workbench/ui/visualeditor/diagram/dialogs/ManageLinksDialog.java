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
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.TSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;

public class ManageLinksDialog extends TSMessageDialog {

	public static class LinksSet {
		private final String name;
		private final String[] columnNames;
		private final Collection<LinkEntry> entries;

		public LinksSet(String name, String[] columnNames,
				Collection<LinkEntry> entries) {
			this.name = name;
			this.columnNames = columnNames;
			this.entries = entries;
		}

		public String getName() {
			return name;
		}

		public String[] getColumnNames() {
			return columnNames;
		}

		public Collection<LinkEntry> getLinkEntries() {
			return entries;
		}

		public boolean hasLinks() {
			return entries != null && entries.size() > 0;
		}
	}

	public static abstract class LinkEntry {
		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public abstract boolean isExists();

		public abstract String getLabel(boolean fullNames, int column);
	}

	private final LinksSet[] linksSets;

	private static boolean showFullyQualifiedName = true;
	private final String SHOW_QUALIFIED_NAMES_PREF = "ShowFullyQualifiedNamesInManageAssocationsView";

	private LinkEntriesSorter tableSorter;

	public ManageLinksDialog(Shell parentShell, LinksSet[] linksSets) {
		super(parentShell);
		this.linksSets = linksSets;
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		composite.setLayout(new GridLayout());

		initDialog();

		createLinksManagementComponent(composite);

		return area;
	}

	protected void initDialog() {
		getShell().setText("Manage Artifact Links");
		getShell().setMinimumSize(450, 225);

		showFullyQualifiedName = TigerstripeDiagramEditorPlugin.getInstance()
				.getPreferenceStore().getBoolean(SHOW_QUALIFIED_NAMES_PREF);
	}

	private void createLinksManagementComponent(Composite composite) {
		Button showFQNButton = new Button(composite, SWT.CHECK);
		showFQNButton.setText("Show fully qualified names");
		showFQNButton.setSelection(showFullyQualifiedName);

		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

		final List<LinksTabInterface> linksTabs = new ArrayList<LinksTabInterface>(
				linksSets.length);
		for (LinksSet linkSet : linksSets) {
			linksTabs.add(createLinksTab(tabFolder, linkSet));
		}

		// activate first non empty tab
		for (int i = 0; i < linksSets.length; i++) {
			if (linksSets[i].hasLinks()) {
				tabFolder.setSelection(i);
				break;
			}
		}

		showFQNButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showFullyQualifiedName = !showFullyQualifiedName;
				IPreferenceStore prefs = TigerstripeDiagramEditorPlugin
						.getInstance().getPreferenceStore();
				prefs.setValue(SHOW_QUALIFIED_NAMES_PREF,
						showFullyQualifiedName);
				for (LinksTabInterface linksTab : linksTabs) {
					linksTab.refresh();
				}
			}
		});
	}

	private LinksTabInterface createLinksTab(TabFolder tabFolder,
			final LinksSet linkSet) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(linkSet.getName());

		Composite tabItemComposite = new Composite(tabFolder, SWT.NULL);
		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(tabItemComposite);
		tabItemComposite.setLayout(new GridLayout(2, false));
		tabItem.setControl(tabItemComposite);

		Composite tableComposite = new Composite(tabItemComposite, SWT.NULL);
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, SWT.DEFAULT));

		final TableViewer tableViewer = createLinksTableViewer(tableComposite,
				linkSet);
		tableViewer.setLabelProvider(new LinkEntryLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(linkSet.getLinkEntries());
		tableSorter = new LinkEntriesSorter();
		tableViewer.setSorter(tableSorter);
		tableViewer.getTable().pack();

		Composite buttonsComposite = new Composite(tabItemComposite, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL)
				.grab(true, false).applyTo(buttonsComposite);
		buttonsComposite.setLayout(new GridLayout());
		Button selectAllButton = createButton(buttonsComposite,
				IDialogConstants.SELECT_ALL_ID, "Select All", false);
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setEnablement(linkSet, true, tableViewer);
			}
		});
		Button deselectAllButton = createButton(buttonsComposite,
				IDialogConstants.DESELECT_ALL_ID, "Deselect All", false);
		deselectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setEnablement(linkSet, false, tableViewer);
			}
		});

		if (!linkSet.hasLinks()) {
			selectAllButton.setEnabled(false);
			deselectAllButton.setEnabled(false);
		}

		return new LinksTabInterface() {

			public void refresh() {
				tableViewer.refresh();
			}
		};
	}

	private void setEnablement(LinksSet linkSet, boolean enablement,
			TableViewer tableViewer) {
		for (LinkEntry entry : linkSet.getLinkEntries()) {
			entry.setEnabled(enablement);
		}
		tableViewer.refresh();
	}

	private TableViewer createLinksTableViewer(Composite tableComposite,
			LinksSet linkSet) {
		TableColumnLayout layout = new TableColumnLayout();
		tableComposite.setLayout(layout);

		final TableViewer tableViewer = new TableViewer(tableComposite,
				SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		String[] columnNames = linkSet.getColumnNames();
		for (int i = 0; i < columnNames.length + 1; i++) {
			final TableViewerColumn viewerColumn = new TableViewerColumn(
					tableViewer, SWT.LEFT);
			final TableColumn column = viewerColumn.getColumn();
			layout.setColumnData(column, new ColumnWeightData(1));
			if (i == 0) {
				column.setWidth(25);
				column.setResizable(false);
				viewerColumn.setEditingSupport(new EnabledColumnEditingSupport(
						tableViewer));
			} else {
				column.setText(columnNames[i - 1]);
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
		return tableViewer;
	}

	private interface LinksTabInterface {
		public void refresh();
	}

	private class EnabledColumnEditingSupport extends EditingSupport {

		public EnabledColumnEditingSupport(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected void setValue(Object element, Object value) {
			((LinkEntry) element).setEnabled((Boolean) value);
			getViewer().refresh();
		}

		@Override
		protected Object getValue(Object element) {
			return ((LinkEntry) element).isEnabled();
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new CheckboxCellEditor(
					((TableViewer) getViewer()).getTable());
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

	}

	private class LinkEntryLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return (columnIndex == 0) ? getImage(((LinkEntry) element)
					.isEnabled()) : null;
		}

		public String getColumnText(Object element, int index) {
			String result = null;
			if (index > 0) {
				result = ((LinkEntry) element).getLabel(showFullyQualifiedName,
						index - 1);
			}
			return result;
		}

		private Image getImage(boolean isSelected) {
			String key = isSelected ? Images.CHECKED_ICON
					: Images.UNCHECKED_ICON;
			return Images.get(key);
		}
	}

	private class LinkEntriesSorter extends ViewerSorter {
		private int column;
		private static final int DESCENDING = 1;

		private int direction = DESCENDING;

		public LinkEntriesSorter() {
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
			int result = 1;
			if (column > 0) {
				result = ((LinkEntry) o1).getLabel(showFullyQualifiedName,
						column - 1).compareTo(
						((LinkEntry) o2).getLabel(showFullyQualifiedName,
								column - 1));

				if (direction == DESCENDING) {
					result = -result;
				}
			}
			return result;
		}
	}
}
