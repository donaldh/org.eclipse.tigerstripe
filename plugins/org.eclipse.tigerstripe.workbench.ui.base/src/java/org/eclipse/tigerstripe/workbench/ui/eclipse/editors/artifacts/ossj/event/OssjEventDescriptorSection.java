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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IField;
import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEventDescriptorEntry;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjEventSpecifics;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextField;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.EventArtifact;
import org.eclipse.tigerstripe.core.model.EventDescriptorEntry;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.ArtifactSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormLabelProvider;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class OssjEventDescriptorSection extends ArtifactSectionPart {

	private EventDescriptorContentProvider contentProvider;

	private TableViewer addEntriesViewer;

	private Button addAdditionalEntryButton;

	private Button editAdditionalEntryButton;

	private Button removeAdditionalEntryButton;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class EventDescriptorListener implements ICheckStateListener {

		public void checkStateChanged(CheckStateChangedEvent event) {
			handleCheckStateChanged(event);
		}
	}

	/**
	 * The model for a node in the TreeViewer
	 * 
	 * It corresponds to an attribute, depending on the type of the attribute it
	 * may have children too.
	 */
	public class TreeNode {

		private boolean selected = false;

		// the parent node
		private TreeNode parent;

		// the field corresponding to this node
		private IField field;

		// The potential children
		private List children;

		public void setParent(TreeNode parent) {
			this.parent = parent;
		}

		public TreeNode getParent() {
			return this.parent;
		}

		public void setField(IField field) {
			this.field = field;
		}

		public IField getField() {
			return this.field;
		}

		public boolean hasChildren() {
			IType type = getField().getIType();

			if (type.isDatatype() || type.isEntityType()) {
				if (children == null)
					refreshChildren();
				return (!children.isEmpty());
			}
			return false; // for now
		}

		public boolean isCheckable() {
			if (hasChildren())
				return false;
			else {
				IType type = getField().getIType();
				return type.isPrimitive()
						|| "java.lang.String".equals(type
								.getFullyQualifiedName())
						|| "String".equals(type.getFullyQualifiedName());
			}
		}

		public String getName() {
			return field.getName();
		}

		/**
		 * returns the fullname (taking into account the name of parent )
		 * 
		 * @return
		 */
		public String getFullName() {
			if (getParent() == null)
				return getName();
			else
				return getParent().getFullName() + "." + getName();
		}

		public String getComment() {
			return field.getComment();
		}

		public TreeNode[] getChildren() {

			if (children == null) {
				refreshChildren();
			}

			TreeNode[] nodes = new TreeNode[children.size()];
			return (TreeNode[]) children.toArray(nodes);
		}

		private void refreshChildren() {
			children = new ArrayList();
			try {
				IArtifactManagerSession session = getIArtifact().getIProject()
						.getArtifactManagerSession();

				AbstractArtifact artifact = session
						.getArtifactByFullyQualifiedName(getField().getIType()
								.getFullyQualifiedName());
				if (artifact != null
						&& artifact instanceof IManagedEntityArtifact
						&& getField().getRefBy() != IextField.REFBY_VALUE)
					return; // When a ManagedEntity not refBy value don't get
				// children
				if (artifact != null) {
					IField[] subFields = artifact.getIFields();
					for (int i = 0; i < subFields.length; i++) {
						TreeNode node = new TreeNode();
						node.setParent(this);
						node.setField(subFields[i]);
						children.add(node);
					}
				}
			} catch (TigerstripeException e) {
				// this means we couldn't resolve the type
				// just say there's no children then
			}
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public boolean isSelected() {
			return this.selected;
		}
	}

	/**
	 * A simple label provider for each node.
	 */
	public class EventDescriptorLabelProvider extends LabelProvider implements
			ILabelProvider {

		@Override
		public String getText(Object element) {
			TreeNode node = (TreeNode) element;

			IField field = node.getField();
			if (field.getRefBy() == IextField.REFBY_KEY)
				return node.getName() + " ("
						+ field.getIType().getFullyQualifiedName() + " by Key)";
			else if (field.getRefBy() == IextField.REFBY_KEYRESULT)
				return node.getName() + " ("
						+ field.getIType().getFullyQualifiedName()
						+ " by KeyResult)";

			return node.getName() + " ("
					+ field.getIType().getFullyQualifiedName() + ")";
		}
	}

	/**
	 * The content provider for the tree viewer
	 * 
	 * It is based on the current attributes for a specific event
	 * 
	 */
	public class EventDescriptorContentProvider extends ArrayContentProvider
			implements ITreeContentProvider {

		private List nodeList;

		/**
		 * 
		 */
		@Override
		public Object[] getElements(Object inputElement) {
			nodeList = new ArrayList();
			EventArtifact event = (EventArtifact) inputElement;
			IField[] fields = event.getIFields();

			for (int i = 0; i < fields.length; i++) {
				TreeNode node = new TreeNode();
				node.setParent(null);
				node.setField(fields[i]);
				nodeList.add(node);
			}

			TreeNode[] result = new TreeNode[nodeList.size()];
			return nodeList.toArray(result);
		}

		public List getNodes() {
			return nodeList;
		}

		public void clearSelection() {
			for (Iterator iter = nodeList.iterator(); iter.hasNext();) {
				TreeNode node = (TreeNode) iter.next();
				node.setSelected(false);
				viewer.setChecked(node, false);
			}
		}

		public Object[] getChildren(Object parentElement) {
			TreeNode node = (TreeNode) parentElement;
			return node.getChildren();
		}

		public Object getParent(Object element) {
			TreeNode node = (TreeNode) element;
			return node.getParent();
		}

		public boolean hasChildren(Object element) {
			TreeNode node = (TreeNode) element;
			return node.hasChildren();
		}
	}

	private boolean silentUpdate = false;

	// The main tree viewer
	private CheckboxTreeViewer viewer;

	public OssjEventDescriptorSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider, 0);

		setTitle("Event Descriptor");
		createContent();
	}

	@Override
	protected void createContent() {
		GridLayout layout = new GridLayout();
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 400;
		getSection().setLayoutData(td);

		getSection().clientVerticalSpacing = 5;

		Composite body = getBody();
		body.setLayout(layout);

		createSelectorTree(body, getToolkit());
		createAdditionalEventDescriptorEntryTable(body, getToolkit());
		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createSelectorTree(Composite parent, FormToolkit toolkit) {
		viewer = new CheckboxTreeViewer(parent, SWT.CHECK);
		viewer.setLabelProvider(new EventDescriptorLabelProvider());

		contentProvider = new EventDescriptorContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setInput(getIArtifact());
		viewer.addCheckStateListener(new EventDescriptorListener());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		gd.heightHint = 180;
		viewer.getTree().setEnabled(!isReadonly());
		viewer.getTree().setLayoutData(gd);
	}

	protected void createAdditionalEventDescriptorEntryTable(Composite parent,
			FormToolkit toolkit) {

		ExpandableComposite exComposite = toolkit.createExpandableComposite(
				parent, ExpandableComposite.TREE_NODE);
		exComposite.setText("Additional Event Selectors");
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL);
		exComposite.setLayoutData(gd);
		exComposite.setLayout(new FillLayout());
		exComposite.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				getManagedForm().reflow(true);
			}
		});

		Composite addComposite = toolkit.createComposite(exComposite);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		addComposite.setLayout(layout);
		exComposite.setClient(addComposite);

		addEntriesViewer = new TableViewer(addComposite, SWT.SINGLE
				| SWT.FULL_SELECTION);

		final Table table = addEntriesViewer.getTable();
		table.setEnabled(!isReadonly());
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setTopIndex(0);
		table.setSize(300, 50);
		table.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				tableRowSelected(e);
			}
		});
		GridData tableGD = new GridData();
		tableGD.verticalSpan = 3;
		tableGD.heightHint = 70;
		table.setLayoutData(tableGD);
		String[] columnNames = new String[] { "Label", "Type" };
		int[] columnWidths = new int[] { 160, 160 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT };

		for (int i = 0; i < columnAlignments.length; i++) {
			TableColumn tableColumn = new TableColumn(table,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}

		addEntriesViewer.setLabelProvider(new EventEntryLabelProvider());
		addEntriesViewer.setContentProvider(new EventEntryContentProvider());
		addEntriesViewer.setInput(getIArtifact());

		addAdditionalEntryButton = toolkit.createButton(addComposite, "Add",
				SWT.PUSH);
		addAdditionalEntryButton.setEnabled(!isReadonly());
		addAdditionalEntryButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL));
		addAdditionalEntryButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				addButtonSelected();
			}
		});

		editAdditionalEntryButton = toolkit.createButton(addComposite, "Edit",
				SWT.PUSH);
		editAdditionalEntryButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL));
		editAdditionalEntryButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				editButtonSelected();
			}
		});

		removeAdditionalEntryButton = toolkit.createButton(addComposite,
				"Remove", SWT.PUSH);
		removeAdditionalEntryButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
					}

					public void widgetSelected(SelectionEvent e) {
						removeButtonSelected();
					}
				});
	}

	private void tableRowSelected(SelectionEvent e) {
		updateCustomEntriesViewer();
	}

	private void addButtonSelected() {
		EventDescriptorEntry entry = new EventDescriptorEntry("label", "String");

		IOssjEventSpecifics specifics = (IOssjEventSpecifics) getIArtifact()
				.getIStandardSpecifics();

		EventDescriptorEntryEditDialog dialog = new EventDescriptorEntryEditDialog(
				getSection().getShell(), entry, Arrays.asList(specifics
						.getCustomEventDescriptorEntries()));

		if (dialog.open() == Window.OK) {
			ArrayList list = new ArrayList();
			list.addAll(Arrays.asList(specifics
					.getCustomEventDescriptorEntries()));
			list.add(entry);

			IEventDescriptorEntry[] entries = new IEventDescriptorEntry[list
					.size()];
			specifics
					.setCustomEventDescriptorEntries((IEventDescriptorEntry[]) list
							.toArray(entries));
			addEntriesViewer.refresh(true);
			markPageModified();
		}
		updateCustomEntriesViewer();
	}

	private void removeButtonSelected() {
		int index = addEntriesViewer.getTable().getSelectionIndex();
		IOssjEventSpecifics specifics = (IOssjEventSpecifics) getIArtifact()
				.getIStandardSpecifics();

		IEventDescriptorEntry entry = specifics
				.getCustomEventDescriptorEntries()[index];
		MessageBox dialog = new MessageBox(getSection().getShell(),
				SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		dialog.setText("Event Selector removal");
		dialog.setMessage("Do you really want to remove the '"
				+ entry.getLabel() + "' event selector?");
		if (dialog.open() == SWT.YES) {
			markPageModified();

			ArrayList list = new ArrayList();
			list.addAll(Arrays.asList(specifics
					.getCustomEventDescriptorEntries()));
			list.remove(entry);

			IEventDescriptorEntry[] entries = new IEventDescriptorEntry[list
					.size()];
			specifics
					.setCustomEventDescriptorEntries((IEventDescriptorEntry[]) list
							.toArray(entries));
			addEntriesViewer.refresh(true);
			addEntriesViewer.getTable().redraw();
		}
		updateCustomEntriesViewer();
	}

	private void editButtonSelected() {
		int index = addEntriesViewer.getTable().getSelectionIndex();
		IOssjEventSpecifics specifics = (IOssjEventSpecifics) getIArtifact()
				.getIStandardSpecifics();

		IEventDescriptorEntry entry = specifics
				.getCustomEventDescriptorEntries()[index];
		EventDescriptorEntry clone = new EventDescriptorEntry(
				(EventDescriptorEntry) entry);

		List subList = new ArrayList();
		subList.addAll(Arrays.asList(specifics
				.getCustomEventDescriptorEntries()));
		subList.remove(entry);

		EventDescriptorEntryEditDialog dialog = new EventDescriptorEntryEditDialog(
				getSection().getShell(), clone, subList);

		if (dialog.open() == Window.OK) {
			entry.setLabel(clone.getLabel());
			entry.setPrimitiveType(clone.getPrimitiveType());
			addEntriesViewer.refresh(true);
			markPageModified();
		}
		updateCustomEntriesViewer();
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

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void handleCheckStateChanged(CheckStateChangedEvent event) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.

			// If the node has children can't be checked.
			TreeNode node = (TreeNode) event.getElement();
			if (!node.isCheckable()) {
				viewer.setChecked(node, false);
			} else {
				IOssjEventSpecifics specifics = (IOssjEventSpecifics) getIArtifact()
						.getIStandardSpecifics();
				IEventDescriptorEntry[] entries = (IEventDescriptorEntry[]) specifics
						.getEventDescriptorEntries();
				EventDescriptorEntry selectedEntry = new EventDescriptorEntry(
						node.getFullName(), node.getField().getIType()
								.getFullyQualifiedName());
				selectedEntry.setCustom(false);
				ArrayList list = new ArrayList();
				list.addAll(Arrays.asList(entries));
				if (event.getChecked()) {
					// Add it to the list
					list.add(selectedEntry);
				} else {
					// remove it from the list
					list.remove(selectedEntry);
				}
				IEventDescriptorEntry[] newEntries = new IEventDescriptorEntry[list
						.size()];
				specifics
						.setEventDescriptorEntries((IEventDescriptorEntry[]) list
								.toArray(newEntries));
				markPageModified();
			}
		}
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		setSilentUpdate(true);
		viewer.refresh(true);

		IOssjEventSpecifics eventSpecifics = (IOssjEventSpecifics) getIArtifact()
				.getIStandardSpecifics();
		IEventDescriptorEntry[] entries = (IEventDescriptorEntry[]) eventSpecifics
				.getEventDescriptorEntries();

		// Un-select every node
		contentProvider.clearSelection();

		for (int i = 0; i < entries.length; i++) {
			TreeNode node = findNode(entries[i].getLabel());
			if (node != null) {
				node.setSelected(true);
				viewer.setChecked(node, true);
			}
		}

		updateCustomEntriesViewer();
		setSilentUpdate(false);
	}

	private void updateCustomEntriesViewer() {
		if (getIArtifact() != null)
			addEntriesViewer.setInput(getIArtifact());
		addEntriesViewer.refresh(true);
		addEntriesViewer.getTable().redraw();
		boolean tableSelected = addEntriesViewer.getTable().getSelectionIndex() != -1;
		removeAdditionalEntryButton.setEnabled(!isReadonly() && tableSelected);
		editAdditionalEntryButton.setEnabled(!isReadonly() && tableSelected);
	}

	/**
	 * Finds the node corresponding to the attr.attr1.attr notation
	 * 
	 * @param attr
	 * @return
	 */
	private TreeNode findNode(String attr) {
		for (Iterator iter = contentProvider.nodeList.iterator(); iter
				.hasNext();) {
			TreeNode node = (TreeNode) iter.next();
			TreeNode result = findNode(node, attr);
			if (result != null)
				return result;
		}
		return null;
	}

	private TreeNode findNode(TreeNode parentNode, String attr) {
		if (parentNode.getFullName().equals(attr))
			return parentNode;
		else {
			for (TreeNode childNode : parentNode.getChildren()) {
				TreeNode result = findNode(childNode, attr);
				if (result != null)
					return result;
			}
		}
		return null;
	}

}
