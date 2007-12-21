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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.stereotypes;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.core.profile.stereotype.Stereotype;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.ProfileEditor;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * 
 * @author Eric Dillon
 * 
 */
public class StereotypesSection extends BaseStereotypeSectionPart implements
		IFormPart {

	public StereotypesSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.EXPANDED);
		setTitle("&Annotation Definitions");
		setDescription("Define the annotations available within this profile.");
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
				return profile.getStereotypes();
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

	@Override
	protected void addButtonSelected(SelectionEvent event) {

		try {
			ProfileEditor editor = (ProfileEditor) getPage().getEditor();
			IWorkbenchProfile profile = editor.getProfile();
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
		return "Define/Edit annotations for this profile.";
	}

	@Override
	protected String getDescription() {
		return "Annotations:";
	}
}
