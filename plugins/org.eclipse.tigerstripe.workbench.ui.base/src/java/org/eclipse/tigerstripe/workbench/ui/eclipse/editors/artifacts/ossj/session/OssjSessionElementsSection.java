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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.session;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.ArtifactSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormLabelProvider;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public abstract class OssjSessionElementsSection extends ArtifactSectionPart {

	protected abstract IStructuredContentProvider getElementsContentProvider();

	protected abstract ILabelProvider getElementsLabelProvider();

	private boolean silentUpdate = false;

	protected TableViewer viewer;

	protected TableViewer getViewer() {
		return viewer;
	}

	public OssjSessionElementsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider, 0);

		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 3;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		getSection().clientVerticalSpacing = 5;

		Composite body = getBody();
		body.setLayout(layout);
		createInterfaceName(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	protected Button addElementButton;

	protected Button removeElementButton;

	protected void createInterfaceName(Composite parent, FormToolkit toolkit) {

		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		parent.setLayout(twlayout);

		Table t = toolkit.createTable(parent, SWT.NULL);
		t.setEnabled(!isReadonly());
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 2;
		td.heightHint = 140;
		t.setLayoutData(td);

		addElementButton = toolkit.createButton(parent, "Add", SWT.PUSH);
		addElementButton.setEnabled(!isReadonly());
		addElementButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		addElementButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				try {
					addButtonSelected(event);
				} catch (TigerstripeException e) {
					Status status = new Status(IStatus.WARNING,
							TigerstripePluginConstants.PLUGIN_ID, 111,
							"Unexpected exception.", e);
					EclipsePlugin.log(status);
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});
		removeElementButton = toolkit.createButton(parent, "Remove", SWT.PUSH);
		removeElementButton.setLayoutData(new TableWrapData());
		removeElementButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});
		removeElementButton.setEnabled(false);

		viewer = new TableViewer(t);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(getElementsContentProvider());
		viewer.setLabelProvider(getElementsLabelProvider());
		viewer.setInput(((ArtifactEditorBase) getPage().getEditor())
				.getIArtifact());

	}

	protected IAbstractArtifact getIArtifactFromEditor() {
		ArtifactEditorBase base = (ArtifactEditorBase) getPage().getEditor();
		return base.getIArtifact();
	}

	protected abstract void removeButtonSelected(SelectionEvent event);

	/**
	 * Updates the master's side based on the selection on the list of
	 * attributes
	 * 
	 */
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		updateForm();
	}

	protected abstract void addButtonSelected(SelectionEvent event)
			throws TigerstripeException;

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	protected void setSilentUpdate(boolean silentUpdate) {
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

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.

			markPageModified();
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

		viewer.setInput(getIArtifact());
		viewer.refresh(true);
		if (viewer.getSelection().isEmpty()) {
			removeElementButton.setEnabled(false);
		} else {
			removeElementButton.setEnabled(!isReadonly());
		}

		setSilentUpdate(false);
	}
}
