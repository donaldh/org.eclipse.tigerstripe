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

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener.IURIBaseProviderPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.AttributeInfoEditComponent;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

public class ArtifactAttributeDetailsPage implements IDetailsPage,
		IURIBaseProviderPage {

	private final ArtifactAttributesSection master;
	private final boolean isReadOnly;

	private AttributeInfoEditComponent attributeInfoEditComponent;

	private StereotypeSectionManager stereotypeMgr;
	private IManagedForm form;
	private TextEditListener nameEditListener;

	public ArtifactAttributeDetailsPage(ArtifactAttributesSection master,
			boolean isReadOnly) {
		super();
		this.master = master;
		this.isReadOnly = isReadOnly;
	}

	public void createContents(Composite parent) {

		attributeInfoEditComponent.createContents(parent);

		if (!isReadOnly) {
			TigerstripeFormEditor editor = (TigerstripeFormEditor) ((TigerstripeFormPage) getForm()
					.getContainer()).getEditor();
			nameEditListener = new TextEditListener(editor, "name",
					IModelChangeDelta.SET, this);
			attributeInfoEditComponent.getNameText().addModifyListener(
					nameEditListener);
		}

		/*
		 * FIXME Just workaround to avoid appearing scrolls on details part.
		 */
		int height = attributeInfoEditComponent.getSectionClient().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y;
		master.setMinimumHeight(height);

		form.getToolkit().paintBordersFor(parent);
	}

	public void initialize(IManagedForm form) {
		this.form = form;

		AttributeInfoEditComponent.Handler handler = new AttributeInfoEditComponent.Handler() {

			public void afterUpdate() {
				if (stereotypeMgr != null) {
					stereotypeMgr.refresh();
				}
			}

			public void stateModified() {
				master.markPageModified();
			}

			public void refresh() {
				if (master != null) {
					final TableViewer viewer = master.getViewer();
					viewer.refresh(getField());
				}
			}

			public void navigateToKeyPressed(final KeyEvent e) {
				master.navigateToKeyPressed(e);
			}
		};

		attributeInfoEditComponent = new AttributeInfoEditComponent(isReadOnly,
				form.getToolkit(), master.getSection().getShell(), handler);
	}

	public void dispose() {
	}

	public boolean isDirty() {
		return master.isDirty();
	}

	public void commit(boolean onSave) {
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void setFocus() {
		attributeInfoEditComponent.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		attributeInfoEditComponent.update();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof ArtifactAttributesSection) {
			if (nameEditListener != null)
				nameEditListener.reset();

			Table fieldsTable = master.getViewer().getTable();

			IField selected = (IField) fieldsTable.getSelection()[0].getData();
			setIField(selected);

			if (stereotypeMgr == null) {
				stereotypeMgr = new StereotypeSectionManager(
						attributeInfoEditComponent.getAddAnno(),
						attributeInfoEditComponent.getEditAnno(),
						attributeInfoEditComponent.getRemoveAnno(),
						attributeInfoEditComponent.getAnnTable(), getField(),
						master.getSection().getShell(), new PageModifyCallback(
								master.getPage()));
				stereotypeMgr.delegate();
			} else {
				stereotypeMgr.setArtifactComponent(getField());
			}
			attributeInfoEditComponent.update();
		}
	}

	public Table getAnnTable() {
		return attributeInfoEditComponent.getAnnTable();
	}

	public IManagedForm getForm() {
		return form;
	}

	private void setIField(IField field) {
		if (attributeInfoEditComponent != null) {
			attributeInfoEditComponent.setIField(field);
			if (!master.getIArtifact().getFields().contains(field)) {
				attributeInfoEditComponent.setEnabled(false);
			}
		}
	}

	private IField getField() {
		if (attributeInfoEditComponent == null) {
			return null;
		} else {
			return attributeInfoEditComponent.getField();
		}
	}

	public URI getBaseURI() {
		return (URI) getField().getAdapter(URI.class);
	}

	public ITigerstripeModelProject getProject() {
		try {
			if (getField() != null)
				return getField().getProject();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}
}
