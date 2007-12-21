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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj;

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
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.profile.stereotype.UnresolvedStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForStereotypeDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.StereotypeInstanceEditDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.ColorUtils;

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
				return component.getStereotypeInstances();
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

	private ArtifactEditorBase editor;

	public StereotypeSectionManager(Button addButton, Button editButton,
			Button removeButton, Table stTable, IStereotypeCapable component,
			Shell shell, ArtifactEditorBase editor) {
		this.addButton = addButton;
		this.editButton = editButton;
		this.removeButton = removeButton;
		this.stTable = stTable;
		this.component = component;
		this.editor = editor;
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
	}

	/**
	 * Updates the state of the widgets
	 * 
	 */
	public void update() {
		if (viewer.getTable().getSelectionCount() > 0) {

			// Make sure the selected instance have been resolved. Or else can't
			// change them
			/** @see UnresolvedStereotypeInstance */
			boolean hasUnresolvedInstances = false;
			for (TableItem item : viewer.getTable().getSelection()) {
				Object obj = item.getData();
				if (obj instanceof UnresolvedStereotypeInstance) {
					hasUnresolvedInstances = true;
				}
			}
			if (!hasUnresolvedInstances) {
				removeButton.setEnabled(true);
				editButton.setEnabled(true);
			}
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
				for (IStereotype st : selected) {
					IStereotypeInstance instance = st.makeInstance();
					(component).addStereotypeInstance(instance);
					viewer.setInput(component);
					viewer.refresh(true);
					if (editor != null)
						editor.pageModified();
				}
			}
		} catch (TigerstripeException ee) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					ee);
		}
	}

	protected void editButtonSelected(SelectionEvent e) {
		TableItem[] selectedItems = this.viewer.getTable().getSelection();
		IStereotypeInstance instance = (IStereotypeInstance) selectedItems[0]
				.getData();

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
						.getCharacterizingIStereotype().getAttributes()) {
					try {
						if (attr.isArray()) {
							instance.setAttributeValues(attr, workingInstance
									.getAttributeValues(attr));
						} else {
							instance.setAttributeValue(attr, workingInstance
									.getAttributeValue(attr));
						}

					} catch (TigerstripeException ee) {
						// ignore
					}
				}
				if (editor != null)
					editor.pageModified();
			}
		} catch (CloneNotSupportedException ee) {
			// ignore
		}
	}

	protected void removeButtonSelected(SelectionEvent e) {
		TableItem[] selectedItems = this.viewer.getTable().getSelection();
		IStereotypeInstance[] selectedLabels = new IStereotypeInstance[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (IStereotypeInstance) selectedItems[i]
					.getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message + "these " + selectedLabels.length
					+ " annotations?";
		} else {
			message = message + "this annotation?";
		}

		MessageDialog msgDialog = new MessageDialog(this.shell,
				"Remove Annotation", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			viewer.remove(selectedLabels);
			(this.component).removeStereotypeInstances(selectedLabels);
			viewer.refresh(true);
			if (this.editor != null)
				this.editor.pageModified();
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
							"Undefined Annotation",
							"The annotation '"
									+ instance.getName()
									+ "' is not defined in the active profile.\n\nPlease active the correct profile to edit its details.");
			return;
		}

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
						.getCharacterizingIStereotype().getAttributes()) {
					try {
						if (attr.isArray()) {
							instance.setAttributeValues(attr, workingInstance
									.getAttributeValues(attr));
						} else {
							instance.setAttributeValue(attr, workingInstance
									.getAttributeValue(attr));
						}
					} catch (TigerstripeException ee) {
						// ignore
					}
				}
				if (editor != null)
					editor.pageModified();
			}
		} catch (CloneNotSupportedException ee) {
			// ignore
		}
	}
}
