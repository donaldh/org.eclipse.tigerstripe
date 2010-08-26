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
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener.IURIBaseProviderPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ConstantInfoEditComponent;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

public class ArtifactConstantDetailsPage implements IDetailsPage,
		IURIBaseProviderPage {

	private final ArtifactConstantsSection master;
	private final boolean isReadOnly;

	private TextEditListener nameEditListener;
	private StereotypeSectionManager stereotypeMgr;
	private IManagedForm form;
	private ConstantInfoEditComponent constantInfoEditComponent;

	public ArtifactConstantDetailsPage(ArtifactConstantsSection master,
			boolean isReadOnly) {
		super();
		this.master = master;
		this.isReadOnly = isReadOnly;
	}

	public URI getBaseURI() {
		return (URI) getLiteral().getAdapter(URI.class);
	}

	public ITigerstripeModelProject getProject() {
		try {
			if (getLiteral() != null)
				return getLiteral().getProject();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	public void createContents(Composite parent) {
		constantInfoEditComponent.createContents(parent);

		if (!isReadOnly) {
			TigerstripeFormEditor editor = (TigerstripeFormEditor) ((TigerstripeFormPage) getForm()
					.getContainer()).getEditor();

			nameEditListener = new TextEditListener(editor, "name",
					IModelChangeDelta.SET, this);
			constantInfoEditComponent.getNameText().addModifyListener(
					nameEditListener);
		}

		/*
		 * FIXME Just workaround to avoid appearing scrolls on details part.
		 */
		int height = constantInfoEditComponent.getSectionClient().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y;
		master.setMinimumHeight(height);

		form.getToolkit().paintBordersFor(parent);
	}

	// ============================================================
	private void setLiteral(ILiteral literal) {
		if (constantInfoEditComponent != null) {
			constantInfoEditComponent.setLiteral(literal);
		}
	}

	private ILiteral getLiteral() {
		if (constantInfoEditComponent != null) {
			return constantInfoEditComponent.getLiteral();
		} else {
			return null;
		}
	}

	// ============================================================

	public void initialize(IManagedForm form) {
		this.form = form;

		ConstantInfoEditComponent.Handler handler = new ConstantInfoEditComponent.Handler() {

			public void afterUpdate() {
				if (stereotypeMgr != null) {
					stereotypeMgr.refresh();
				}
			}

			public void stateModified() {
				master.markPageModified();
			}

			public void navigateToKeyPressed(KeyEvent e) {
				master.navigateToKeyPressed(e);
			}

			public void refresh() {
				if (master != null) {
					TableViewer viewer = master.getViewer();
					// viewer.refresh(getLabel());
					viewer.refresh();
				}
			}
		};

		constantInfoEditComponent = new ConstantInfoEditComponent(isReadOnly,
				form.getToolkit(), handler);
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
		constantInfoEditComponent.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		constantInfoEditComponent.update();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {

		if (part instanceof ArtifactConstantsSection) {
			if (nameEditListener != null)
				nameEditListener.reset();

			Table labelsTable = master.getViewer().getTable();

			ILiteral selected = (ILiteral) labelsTable.getSelection()[0]
					.getData();
			setLiteral(selected);

			if (stereotypeMgr == null) {
				stereotypeMgr = new StereotypeSectionManager(
						constantInfoEditComponent.getAddAnno(),
						constantInfoEditComponent.getEditAnno(),
						constantInfoEditComponent.getRemoveAnno(),
						constantInfoEditComponent.getAnnTable(), getLiteral(),
						master.getSection().getShell(), new PageModifyCallback(
								master.getPage()));
				stereotypeMgr.delegate();
			} else {
				stereotypeMgr.setArtifactComponent(getLiteral());
			}

			constantInfoEditComponent.update();
		}
	}

	public Table getAnnTable() {
		return constantInfoEditComponent.getAnnTable();
	}

	public IManagedForm getForm() {
		return form;
	}

}
