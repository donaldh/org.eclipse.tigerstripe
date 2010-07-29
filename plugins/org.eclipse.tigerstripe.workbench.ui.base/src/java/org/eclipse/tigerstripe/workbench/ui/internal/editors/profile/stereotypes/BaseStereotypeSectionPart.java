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
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.ProfileEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;

public abstract class BaseStereotypeSectionPart extends TigerstripeSectionPart {

	public BaseStereotypeSectionPart(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.NO_TITLE);
	}

	protected DetailsPart detailsPart;

	protected SashForm sashForm;

	class MasterLabelProvider extends LabelProvider implements
			ITableLabelProvider, IColorProvider, ILabelProvider {

		public String getColumnText(Object obj, int index) {
			if (obj instanceof IStereotype) {
				IStereotype field = (IStereotype) obj;
				return field.getName();
			} else
				return " ";
		}

		public Color getBackground(Object element) {
			return null;
		}

		public Color getForeground(Object element) {
			if (element instanceof IPrimitiveTypeDef) {
				IPrimitiveTypeDef field = (IPrimitiveTypeDef) element;
				if (field.isReserved())
					return ColorUtils.LIGHT_GREY;
			}
			return null;
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}

	}

	/**
	 * Creates the content of the master/details block inside the managed form.
	 * This method should be called as late as possible inside the parent part.
	 * 
	 * @param managedForm
	 *            the managed form to create the block in
	 */
	@Override
	public void createContent() {
		IManagedForm managedForm = getPage().getManagedForm();
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = getToolkit();

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		Composite body = getToolkit().createComposite(getSection());
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		layout.verticalSpacing = layout.horizontalSpacing = 0;
		body.setLayout(layout);
		sashForm = new SashForm(body, SWT.HORIZONTAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm.setToolTipText("Define/Edit attributes for this Artifact.");
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	protected abstract void registerPages(DetailsPart detailsPart);

	protected abstract String getTooltipText();

	/**
	 * Updates the current state of the master
	 * 
	 */
	protected void updateMaster() {

		// Updates the state of the Remove Button
		if (ProfileEditor.isEditable() && viewer.getSelection() != null
				&& !viewer.getSelection().isEmpty()) {
			removeAttributeButton.setEnabled(true);
		} else {
			removeAttributeButton.setEnabled(false);
		}
	}

	protected void createDetailsPart(final IManagedForm mform, Composite parent) {
		detailsPart = new DetailsPart(mform, parent, SWT.NULL);
		mform.addPart(detailsPart);
		registerPages(detailsPart);
	}

	// ====================================================================
	protected TableViewer viewer;

	protected Button addStereotypeButton;

	protected Button removeAttributeButton;

	public TableViewer getViewer() {
		return this.viewer;
	}

	protected abstract String getDescription();

	protected abstract IStructuredContentProvider getRulesListContentProvider();

	protected abstract void createMasterPart(final IManagedForm managedForm,
			Composite parent);

	/**
	 * Updates the master's side based on the selection on the list of
	 * attributes
	 * 
	 */
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		updateMaster();
	}

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected abstract void addButtonSelected(SelectionEvent event);

	/**
	 * Triggered when the set default button is pushed
	 * 
	 */
	protected void defaultButtonSelected(SelectionEvent event) {
		throw new UnsupportedOperationException(
				"This method is not supported! "
						+ "A subclass must override if functionality is required.");
	}

	public void markPageModified() {
		ProfileEditor editor = (ProfileEditor) getPage().getEditor();
		editor.pageModified();
	}

	private int newFieldCount;

	/**
	 * Finds a new field name
	 */
	protected String findNewStereotypeName() {
		String result = "aStereotype" + newFieldCount++;

		// make sure we're not creating a duplicate
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String name = ((IStereotype) items[i].getData()).getName();
			if (result.equals(name))
				return findNewStereotypeName();
		}
		return result;
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		Collection<IStereotype> selectedFields = new ArrayList<IStereotype>();

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields.add((IStereotype) selectedItems[i].getData());
		}

		String message = "Do you really want to remove ";
		if (selectedFields.size() > 1) {
			message = message + "these " + selectedFields.size()
					+ " stereotypes?";
		} else {
			message = message + "this stereotype?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove Stereotype", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			try {
				((ProfileEditor) getPage().getEditor()).getProfile()
						.removeStereotypes(selectedFields);
				viewer.remove(selectedFields);
				markPageModified();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		updateMaster();
	}

	/**
	 * Commits the part. Subclasses should call 'super' when overriding.
	 * 
	 * @param onSave
	 *            <code>true</code> if the request to commit has arrived as a
	 *            result of the 'save' action.
	 */
	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
		detailsPart.commit(onSave);
	}

	int viewerSel = -1;

	@Override
	public void refresh() {
		try {
			viewer.setInput(((ProfileEditor) getPage().getEditor())
					.getProfile());
			viewer.refresh();
			if (viewerSel != -1) {
				Object refreshedStereotype = viewer.getTable().getItem(
						viewerSel).getData();
				viewer.setSelection(
						new StructuredSelection(refreshedStereotype), true);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		updateMaster();
	}

}
