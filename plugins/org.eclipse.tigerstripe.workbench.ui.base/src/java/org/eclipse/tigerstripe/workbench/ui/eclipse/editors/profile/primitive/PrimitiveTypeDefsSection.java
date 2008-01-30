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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.primitive;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.profile.primitiveType.PrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.ProfileEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.stereotypes.BaseStereotypeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.ColorUtils;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * 
 * @author Eric Dillon
 * 
 */
public class PrimitiveTypeDefsSection extends BaseStereotypeSectionPart
		implements IFormPart {

	public PrimitiveTypeDefsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.EXPANDED);
		setTitle("&Primitive Type Definitions");
		setDescription("Define the primitive types available within this profile.");
		getSection().marginWidth = 10;
		getSection().marginHeight = 5;
		getSection().clientVerticalSpacing = 4;

		createContent();
		updateMaster();
	}

	class MasterContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IWorkbenchProfile) {
				IWorkbenchProfile profile = (IWorkbenchProfile) inputElement;
				return profile.getPrimitiveTypeDefs(true).toArray();
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class PrimitiveTypeLabelProvider extends LabelProvider implements
			ITableLabelProvider, IColorProvider, ILabelProvider {

		private IWorkbenchProfile profile;

		public PrimitiveTypeLabelProvider(IWorkbenchProfile profile) {
			super();
			this.profile = profile;
		}

		public void updateProfile(IWorkbenchProfile profile) {
			this.profile = profile;
		}

		public String getColumnText(Object obj, int index) {
			if (obj instanceof IPrimitiveTypeDef) {
				IPrimitiveTypeDef field = (IPrimitiveTypeDef) obj;
				if (profile.getDefaultPrimitiveType() != null
						&& profile.getDefaultPrimitiveType().equals(obj))
					return field.getName() + " (default)";
				else
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

	@Override
	protected IStructuredContentProvider getRulesListContentProvider() {
		return new MasterContentProvider();
	}

	private int newFieldCount;

	/**
	 * Finds a new field name
	 */
	protected String findNewTypeName() {
		String result = "atype" + newFieldCount++;

		// make sure we're not creating a duplicate
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String name = ((IPrimitiveTypeDef) items[i].getData()).getName();
			if (result.equals(name))
				return findNewTypeName();
		}
		return result;
	}

	protected Button defaultAttributeButton;

	@Override
	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {

		FormToolkit toolkit = getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);

		Composite sectionClient = toolkit.createComposite(section);
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);

		Table t = toolkit.createTable(sectionClient, SWT.NULL);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 2;
		td.heightHint = 150;
		t.setLayoutData(td);

		addAttributeButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		addAttributeButton.setEnabled(ProfileEditor.isEditable());
		addAttributeButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (ProfileEditor.isEditable()) {
			addAttributeButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					addButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}

		removeAttributeButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		removeAttributeButton.setEnabled(ProfileEditor.isEditable());
		removeAttributeButton.setLayoutData(new TableWrapData());
		removeAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		if (this.getClass().getName().endsWith("PrimitiveTypeDefsSection")) {

			td.rowspan = 3;
			defaultAttributeButton = toolkit.createButton(sectionClient,
					"Set Default", SWT.PUSH);
			if (getDefaultAttributeButtonIsActive()) {
				defaultAttributeButton.setEnabled(true);
			} else {
				defaultAttributeButton.setEnabled(false);
			}
			defaultAttributeButton.setLayoutData(new TableWrapData());
			defaultAttributeButton
					.addSelectionListener(new SelectionListener() {
						public void widgetSelected(SelectionEvent event) {
							defaultButtonSelected(event);
						}

						public void widgetDefaultSelected(SelectionEvent event) {
							// empty
						}
					});

		}

		Label l = toolkit.createLabel(sectionClient, "", SWT.NULL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 100;
		l.setLayoutData(td);

		final IFormPart part = this;
		viewer = new TableViewer(t);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(part, event.getSelection());
				viewerSelectionChanged(event);
			}
		});

		try {
			viewer.setContentProvider(getRulesListContentProvider());
			viewer.setLabelProvider(new PrimitiveTypeLabelProvider(
					((ProfileEditor) getPage().getEditor()).getProfile()));
			viewer.setComparator(new ViewerComparator() {
			});

			viewer.setInput(((ProfileEditor) getPage().getEditor())
					.getProfile());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
	}

	protected boolean getDefaultAttributeButtonIsActive() {
		IWorkbenchProfile profile;
		try {
			profile = ((ProfileEditor) getPage().getEditor()).getProfile();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			return false;
		}
		if (ProfileEditor.isEditable()
				&& profile.getPrimitiveTypeDefs(true).size() > 0)
			return true;
		else
			return false;
	}

	@Override
	protected void addButtonSelected(SelectionEvent event) {

		try {
			ProfileEditor editor = (ProfileEditor) getPage().getEditor();
			IWorkbenchProfile profile = editor.getProfile();
			String name = findNewTypeName();

			PrimitiveTypeDef newPrimitiveTypeDef = new PrimitiveTypeDef();
			newPrimitiveTypeDef.setName(name);
			profile.addPrimitiveTypeDef(newPrimitiveTypeDef);

			getViewer().add(newPrimitiveTypeDef);
			getViewer().setSelection(
					new StructuredSelection(newPrimitiveTypeDef), true);
			markPageModified();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	@Override
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		Collection<IPrimitiveTypeDef> selectedFields = new ArrayList<IPrimitiveTypeDef>();

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields.add((IPrimitiveTypeDef) selectedItems[i].getData());
		}

		String message = "Do you really want to remove ";
		if (selectedFields.size() > 1) {
			message = message + "these " + selectedFields.size()
					+ " Primitive Type definitions ?";
		} else {
			message = message + "this Primitive Type definition?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove Primitive Type Definition", null, message,
				MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == Window.OK) {
			try {
				((ProfileEditor) getPage().getEditor()).getProfile()
						.removePrimitiveTypeDefs(selectedFields);
				viewer.remove(selectedFields);
				markPageModified();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		updateMaster();
	}

	@Override
	protected void defaultButtonSelected(SelectionEvent event) {

		TableItem[] selectedItems = viewer.getTable().getSelection();
		IPrimitiveTypeDef primitiveTypeDef = (IPrimitiveTypeDef) selectedItems[0]
				.getData();
		try {
			IWorkbenchProfile profile = ((ProfileEditor) getPage().getEditor())
					.getProfile();
			profile.setDefaultPrimitiveType(primitiveTypeDef);
			((PrimitiveTypeLabelProvider) viewer.getLabelProvider())
					.updateProfile(profile);
			refresh();
			markPageModified();
		} catch (TigerstripeException e) {
			MessageDialog
					.openWarning(
							getPage().getPartControl().getShell(),
							"Default Primitive Type was not set. Check log for error info.",
							e.getMessage());
			return;
		}

		MessageDialog.openInformation(getPage().getPartControl().getShell(),
				"Primitive Type",
				"This profile must be made active for the changes "
						+ "to take effect.");

		updateMaster();
	}

	@Override
	protected void updateMaster() {

		// Updates the state of the Remove Button
		if (ProfileEditor.isEditable() && viewer.getSelection() != null
				&& !viewer.getSelection().isEmpty()) {
			TableItem[] selectedItems = viewer.getTable().getSelection();
			IPrimitiveTypeDef[] selectedFields = new IPrimitiveTypeDef[selectedItems.length];

			for (int i = 0; i < selectedItems.length; i++) {
				selectedFields[i] = (IPrimitiveTypeDef) selectedItems[i]
						.getData();
			}

			removeAttributeButton.setEnabled(!selectedFields[0].isReserved());
		} else {
			removeAttributeButton.setEnabled(false);
		}

		// Updates the state of the Default Button
		if (getDefaultAttributeButtonIsActive()) {
			defaultAttributeButton.setEnabled(true);
		} else {
			defaultAttributeButton.setEnabled(false);
		}

	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(PrimitiveTypeDef.class,
				new PrimitiveTypeDefDetailsPage(this));
	}

	@Override
	protected String getTooltipText() {
		return "Define/Edit primitive types for this profile.";
	}

	@Override
	protected String getDescription() {
		return "Primitive Types:";
	}

}
