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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.Stereotype;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.ProfileEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * 
 * @author Eric Dillon
 * 
 */
public class StereotypesSection extends BaseStereotypeSectionPart implements
		IFormPart {

	private Table table;

	public StereotypesSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit);
		createContent();
		updateMaster();
	}

	class MasterContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IWorkbenchProfile) {
				IWorkbenchProfile profile = (IWorkbenchProfile) inputElement;
				return profile.getStereotypes().toArray();
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	@Override
	protected IStructuredContentProvider getRulesListContentProvider() {
		return new MasterContentProvider();
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {
		FormToolkit toolkit = getToolkit();

		Section section = TigerstripeLayoutFactory.createSection(parent,
				toolkit, ExpandableComposite.TITLE_BAR,
				"&Stereotype Definitions",
				"Define the stereotypes available within this profile.");

		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(TigerstripeLayoutFactory.createFormGridLayout(
				2, false));

		table = toolkit.createTable(sectionClient, SWT.NULL);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite buttonClient = toolkit.createComposite(sectionClient);
		buttonClient.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING));
		buttonClient.setLayout(TigerstripeLayoutFactory
				.createButtonsGridLayout());

		addStereotypeButton = toolkit.createButton(buttonClient, "Add",
				SWT.PUSH);
		// support for testing
		addStereotypeButton.setData("name", "Add_Stereotype");
		addStereotypeButton.setEnabled(ProfileEditor.isEditable());
		addStereotypeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
		if (ProfileEditor.isEditable()) {
			addStereotypeButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					addButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}
		removeAttributeButton = toolkit.createButton(buttonClient, "Remove",
				SWT.PUSH);
		removeAttributeButton.setEnabled(ProfileEditor.isEditable());
		removeAttributeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_BEGINNING));
		removeAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		final IFormPart part = this;
		viewer = new TableViewer(table);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				viewerSel = viewer.getTable().getSelectionIndex();
				managedForm.fireSelectionChanged(part, event.getSelection());
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(getRulesListContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());
		viewer.setComparator(new ViewerComparator() {

		});

		try {
			viewer.setInput(((ProfileEditor) getPage().getEditor())
					.getProfile());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
	}

	/**
	 * FIXME Used only by ArtifactAttributeDetailsPage. Just workaround to avoid
	 * appearing scrolls on details part.
	 */
	void setMinimumHeight(int value) {
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = value;
		table.setLayoutData(gd);
		getManagedForm().reflow(true);
	}

	@Override
	protected void addButtonSelected(SelectionEvent event) {
		try {
			ProfileEditor editor = (ProfileEditor) getPage().getEditor();
			WorkbenchProfile profile = editor.getProfile();
			String name = findNewStereotypeName();

			Stereotype newStereotype = new Stereotype(profile);
			newStereotype.setName(name);
			profile.addStereotype(newStereotype);

			getViewer().add(newStereotype);
			getViewer().setSelection(new StructuredSelection(newStereotype),
					true);
			markPageModified();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	@Override
	protected void defaultButtonSelected(SelectionEvent event) {
		System.out.println("Default button: StereotypesSection");
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(Stereotype.class, new StereotypeDetailsPage(
				this));
	}

	@Override
	protected String getTooltipText() {
		return "Define/Edit stereotypes for this profile.";
	}

	@Override
	protected String getDescription() {
		return "Stereotypes:";
	}
	
	@Override
	public void markPageModified() {
		validate();
		super.markPageModified();
	}

	private final List<String> errors = new ArrayList<String>();
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public void validate() {
		errors.clear();
		WorkbenchProfile profile = getProfile();
		if (profile != null) {
			Set<String> names = new HashSet<String>();
			for (IStereotype stereotype : profile.getStereotypes()) {
				if (stereotype != null) {
					String name = stereotype.getName();
					if (name == null || name.trim().isEmpty()) {
						errors.add("Stereotype name is empty");
					} else if (!names.add(name)) {
						errors.add("Stereotype name is duplicated: " + name);
					}
				}
			}
		}
		updateErrorMessage();
	}

	private WorkbenchProfile getProfile() {
		try {
			return ((ProfileEditor) getPage().getEditor()).getProfile();
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return null;
		}
	}
	
	private void updateErrorMessage() {
		ScrolledForm form = getManagedForm().getForm();
		if (errors.isEmpty()) {
			form.setMessage("", IMessageProvider.NONE);	
		} else {
			form.setMessage(toErrorString(errors), IMessageProvider.ERROR);	
		}
	}

	private String toErrorString(List<String> errors) {
		if (errors.isEmpty()) {
			return "";
		}
		StringBuilder msg = new StringBuilder(errors.get(0));
		for (int i = 1;i < errors.size(); ++i) {
			msg.append("\n").append(errors.get(i));
		}
		return msg.toString();
	}

	private boolean duplicateName(String name) throws TigerstripeException {
		byte hits = 0;
		for (IStereotype stereotype : getProfile().getStereotypes()) {
			if (name.equals(stereotype.getName())) {
				++hits;
			}
			if (hits > 1) {
				return true;
			}
		}
		return false;
	}
	
}
