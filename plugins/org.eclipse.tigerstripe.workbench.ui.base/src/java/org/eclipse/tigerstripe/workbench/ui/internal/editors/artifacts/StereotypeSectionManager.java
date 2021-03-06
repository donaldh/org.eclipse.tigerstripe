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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.UnresolvedStereotypeInstance;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.internal.Ask;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForStereotypeDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.StereotypeInstanceEditDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.IModifyCallback;

/**
 * Helper class that manages the table and buttons associated with attaching
 * stereotypes instances to an Artifact Component.
 * 
 * Actual Sections delegate all the processing to an instance of this manager
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class StereotypeSectionManager {

	public void setArtifactComponent(IStereotypeCapable component) {
		this.component = component;
	}

	private class StereotypeLabelProvider extends LabelProvider implements
			IColorProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof IStereotypeInstance) {

				IStereotypeInstance instance = (IStereotypeInstance) element;
				if (instance instanceof UnresolvedStereotypeInstance)
					return instance.getName() + " (Undefined)";
				else
					return instance.getName();
			} else
				return "<unknown>";
		}

		public Color getBackground(Object element) {
			return null;
		}

		public Color getForeground(Object element) {
			if (element instanceof UnresolvedStereotypeInstance)
				return ColorUtils.LIGHT_GREY;
			else
				return ColorUtils.BLACK;
		}

	}

	private class StereotypeContentProvider implements
			IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IStereotypeCapable) {
				IStereotypeCapable component = (IStereotypeCapable) inputElement;
				return component.getStereotypeInstances().toArray();
			} else
				return new IStereotypeInstance[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private Button addButton;

	private Button editButton;

	private Button removeButton;

	private Table stTable;

	private TableViewer viewer;

	private IStereotypeCapable component;

	private Shell shell;

	private final IModifyCallback callback;

	public StereotypeSectionManager(Button addButton, Button editButton,
			Button removeButton, Table stTable, IStereotypeCapable component,
			Shell shell, IModifyCallback callback) {
		this.addButton = addButton;
		this.editButton = editButton;
		this.removeButton = removeButton;
		this.stTable = stTable;
		this.component = component;
		this.callback = callback == null ? IModifyCallback.EMPTY : callback;
	}

	/**
	 * This method creates all the listener on the various widgets to manage the
	 * stereotypes on the given component
	 * 
	 */
	public void delegate() {
		viewer = new TableViewer(stTable);
		addButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!stTable.isDisposed() && !stTable.isFocusControl()) {
					stTable.forceFocus();
				}
				addButtonSelected(e);
			}
		});

		editButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				editButtonSelected(e);
			}
		});

		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				removeButtonSelected(e);
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClicked(event);
			}
		});

		viewer.setLabelProvider(new StereotypeLabelProvider());
		viewer.setContentProvider(new StereotypeContentProvider());
		viewer.setInput(component);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				update();
			}
		});

		update();
	}

	public void refresh() {
		viewer.setInput(component);
		viewer.refresh(true);
		update();
	}

	/**
	 * Updates the state of the widgets
	 * 
	 */
	public void update() {
		int selectionCount = viewer.getTable().getSelectionCount();
		if (selectionCount > 0) {
			if (selectionCount == 1) {
				TableItem selectedItem = viewer.getTable().getSelection()[0];
				IStereotypeInstance instance = (IStereotypeInstance) selectedItem
						.getData();
				if (canEdit(instance)) {
					editButton.setEnabled(true);
				} else {
					editButton.setEnabled(false);
				}
			}
			removeButton.setEnabled(true);
		} else {
			removeButton.setEnabled(false);
			editButton.setEnabled(false);
		}
	}

	protected void addButtonSelected(SelectionEvent e) {
		BrowseForStereotypeDialog dialog = new BrowseForStereotypeDialog(
				component, component.getStereotypeInstances());

		try {
			IStereotype[] selected = dialog.browseAvailableStereotypes(shell);
			if (selected.length > 0) {
				IStereotypeInstance instance = null;
				for (IStereotype st : selected) {
					instance = st.makeInstance();
					(component).addStereotypeInstance(instance);
					viewer.setInput(component);
					viewer.refresh(true);
					callback.modify();
				}
				if (selected.length == 1 && instance != null
						&& canEdit(instance)) {
					doEdit(instance);
				}
			}
		} catch (TigerstripeException ee) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					ee);
		}
	}

	protected void editButtonSelected(SelectionEvent e) {
		TableItem[] selectedItems = this.viewer.getTable().getSelection();
		if (selectedItems.length > 0) {
			IStereotypeInstance instance = (IStereotypeInstance) selectedItems[0]
					.getData();
			doEdit(instance);
		}
	}

	private boolean canEdit(IStereotypeInstance instance) {
		// Make sure the selected instance have been resolved. Or else
		// can't
		// change them (JS - except to delete, bugzilla 254406)
		/** @see UnresolvedStereotypeInstance */
		if (!(instance instanceof UnresolvedStereotypeInstance)
				&& instance.getCharacterizingStereotype().getAttributes().length > 0) {
			return true;
		}
		return false;
	}

	private void doEdit(IStereotypeInstance instance) {
		try {
			IStereotypeInstance workingInstance = (IStereotypeInstance) instance
					.clone();

			StereotypeInstanceEditDialog dialog = new StereotypeInstanceEditDialog(
					this.shell, workingInstance);
			if (dialog.open() == Window.OK) {
				// copy the attr values of the working instance back into the
				// main
				// instance
				for (IStereotypeAttribute attr : instance
						.getCharacterizingStereotype().getAttributes()) {
					try {
						if (attr.isArray()) {
							instance.setAttributeValues(attr,
									workingInstance.getAttributeValues(attr));
						} else {
							instance.setAttributeValue(attr,
									workingInstance.getAttributeValue(attr));
						}

					} catch (TigerstripeException ee) {
						// ignore
					}
				}
				callback.modify();
			}
		} catch (CloneNotSupportedException ee) {
			// ignore
		}

	}

	protected void removeButtonSelected(SelectionEvent e) {
		TableItem[] selectedItems = this.viewer.getTable().getSelection();
		Collection<IStereotypeInstance> selectedLabels = new ArrayList<IStereotypeInstance>();

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels
					.add((IStereotypeInstance) selectedItems[i].getData());
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.size() > 1) {
			message = message + "these " + selectedLabels.size()
					+ " stereotypes?";
		} else {
			message = message + "this stereotype?";
		}

		if (Ask.aboutMemebersRemovingStereotypes(shell, "Remove Stereotype",
				message)) {
			viewer.remove(selectedLabels);
			(this.component).removeStereotypeInstances(selectedLabels);
			viewer.refresh(true);
			callback.modify();
		}
		update();
	}

	protected void doubleClicked(DoubleClickEvent e) {
		TableItem[] selectedItems = this.viewer.getTable().getSelection();
		IStereotypeInstance instance = (IStereotypeInstance) selectedItems[0]
				.getData();

		if (instance instanceof UnresolvedStereotypeInstance) {
			MessageDialog
					.openInformation(
							shell,
							"Undefined Stereotype",
							"The Stereotype '"
									+ instance.getName()
									+ "' is not defined in the active profile.\n\nPlease active the correct profile to edit its details.");
			return;
		}
		if (instance.getCharacterizingStereotype().getAttributes().length <= 0)
			return;

		try {
			IStereotypeInstance workingInstance = (IStereotypeInstance) instance
					.clone();

			StereotypeInstanceEditDialog dialog = new StereotypeInstanceEditDialog(
					this.shell, workingInstance);
			if (dialog.open() == Window.OK) {
				// copy the attr values of the working instance back into the
				// main
				// instance
				for (IStereotypeAttribute attr : instance
						.getCharacterizingStereotype().getAttributes()) {
					try {
						if (attr.isArray()) {
							instance.setAttributeValues(attr,
									workingInstance.getAttributeValues(attr));
						} else {
							instance.setAttributeValue(attr,
									workingInstance.getAttributeValue(attr));
						}
					} catch (TigerstripeException ee) {
						// ignore
					}
				}
				callback.modify();
			}
		} catch (CloneNotSupportedException ee) {
			// ignore
		}
	}
}
