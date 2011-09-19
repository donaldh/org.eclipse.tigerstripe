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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener.IURIBaseProviderPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.IModifyCallback;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.MethodInfoEditComponent;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactMethodDetailsPage implements IDetailsPage,
		IURIBaseProviderPage {

	private final ArtifactMethodsSection master;
	private final boolean isReadOnly;
	private final IModifyCallback stereotypeModifyCallback;
	private TextEditListener nameEditListener;
	private MethodInfoEditComponent methodInfoEditComponent;
	private StereotypeSectionManager stereotypeMgr;
	private IManagedForm form;

	public ArtifactMethodDetailsPage(final ArtifactMethodsSection master,
			boolean isReadOnly) {
		super();
		this.master = master;
		this.isReadOnly = isReadOnly;
		stereotypeModifyCallback = new PageModifyCallback(master.getPage());
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 300;
		parent.setLayoutData(td);

		methodInfoEditComponent.createContents(parent);

		if (!isReadOnly) {
			TigerstripeFormEditor editor = (TigerstripeFormEditor) ((TigerstripeFormPage) getForm()
					.getContainer()).getEditor();

			nameEditListener = new TextEditListener(editor, "name",
					IModelChangeDelta.SET, this);
			methodInfoEditComponent.getNameText().addModifyListener(
					nameEditListener);
		}

		int height = methodInfoEditComponent.getSectionClient().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y;
		/*
		 * FIXME Just workaround to avoid appearing scrolls on details part.
		 */
		master.setMinimumHeight(height);

		form.getToolkit().paintBordersFor(parent);
	}

	public URI getBaseURI() {
		return (URI) getMethod().getAdapter(URI.class);
	}

	public ITigerstripeModelProject getProject() {
		try {
			if (getMethod() != null)
				return getMethod().getProject();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	private void setIMethod(IMethod method) {
		if (methodInfoEditComponent != null) {
			IAbstractArtifact containingArtifact = method.getContainingArtifact();
			boolean inherited = containingArtifact != null && !containingArtifact.equals(master.getIArtifact());
			methodInfoEditComponent.setMethod(method, inherited);
		}
	}

	private IMethod getMethod() {
		if (methodInfoEditComponent != null) {
			return methodInfoEditComponent.getMethod();
		} else {
			return null;
		}
	}

	public void initialize(IManagedForm form) {
		this.form = form;

		MethodInfoEditComponent.Handler handler = new MethodInfoEditComponent.Handler() {

			public void afterUpdate() {
				if (stereotypeMgr != null) {
					stereotypeMgr.refresh();
				}
			}

			public void commit() {
				ArtifactMethodDetailsPage.this.commit(false);
			}

			public void navigateToKeyPressed(KeyEvent e) {
				master.navigateToKeyPressed(e);
			}

			public void refreshMethodLabel() {
				if (master != null) {
					TableViewer viewer = master.getViewer();
					viewer.refresh(getMethod());
				}
			}

			public void stateModified() {
				master.markPageModified();
			}

			public void stereotypeModify() {
				stereotypeModifyCallback.modify();
			}

		};

		methodInfoEditComponent = new MethodInfoEditComponent(isReadOnly,
				master.getContentProvider().enabledInstanceMethods(), master
						.getSection().getShell(), form.getToolkit(), handler,
				master.getPage().getSite());
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
		methodInfoEditComponent.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		methodInfoEditComponent.update();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof ArtifactMethodsSection) {
			if (nameEditListener != null)
				nameEditListener.reset();

			Table methodsTable = master.getViewer().getTable();

			IMethod selected = (IMethod) methodsTable.getSelection()[0]
					.getData();
			if (selected == null) {
				return;
			}
			setIMethod(selected);

			if (stereotypeMgr == null) {
				stereotypeMgr = new StereotypeSectionManager(
						methodInfoEditComponent.getAddAnno(),
						methodInfoEditComponent.getEditAnno(),
						methodInfoEditComponent.getRemoveAnno(),
						methodInfoEditComponent.getAnnTable(), getMethod(),
						master.getSection().getShell(),
						stereotypeModifyCallback);
				stereotypeMgr.delegate();
			} else {
				stereotypeMgr.setArtifactComponent(getMethod());
			}

			methodInfoEditComponent.update();
		}
	}

	public Table getAnnTable() {
		return methodInfoEditComponent.getAnnTable();
	}

	public IManagedForm getForm() {
		return form;
	}
}
