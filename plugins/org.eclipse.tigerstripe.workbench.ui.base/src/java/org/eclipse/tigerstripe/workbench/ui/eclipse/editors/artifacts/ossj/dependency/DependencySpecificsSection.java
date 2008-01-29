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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.dependency;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.ArtifactSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class DependencySpecificsSection extends ArtifactSectionPart {

	private boolean silentUpdate = false;

	private class DependencySpecificsSectionListener implements ModifyListener,
			SelectionListener, KeyListener {

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.F3) {
				navigateToKeyPressed(e);
			}
		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

	}

	private Text aEndTypeText;

	private Button aEndTypeBrowseButton;

	private Text zEndTypeText;

	private Button zEndTypeBrowseButton;

	public DependencySpecificsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit, null, null, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE | ExpandableComposite.COMPACT);
		setTitle("&Details");
		getSection().marginWidth = 10;
		getSection().marginHeight = 5;
		getSection().clientVerticalSpacing = 4;

		createContent();
	}

	@Override
	protected IAbstractArtifact getIArtifact() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		return editor.getIArtifact();
	}

	@Override
	protected void createContent() {

		DependencySpecificsSectionListener listener = new DependencySpecificsSectionListener();

		FormToolkit toolkit = getToolkit();

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		Composite body = getBody();
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		body.setLayout(layout);

		GridData sgd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		sgd.horizontalSpan = 3;
		sgd.heightHint = 5;

		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		Label l = toolkit.createLabel(body, "aEnd", SWT.BOLD);
		l.setFont(new Font(null, "arial", 8, SWT.BOLD));
		l.setLayoutData(gd);
		GridData tgd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);

		l = toolkit.createLabel(body, "Type");
		l.setEnabled(!getIArtifact().isReadonly());
		aEndTypeText = toolkit.createText(body, "");
		aEndTypeText.setEnabled(!getIArtifact().isReadonly());
		aEndTypeText.setLayoutData(tgd);
		aEndTypeText.addModifyListener(listener);
		aEndTypeText.addKeyListener(listener);
		aEndTypeBrowseButton = toolkit.createButton(body, "Browse", SWT.PUSH);
		aEndTypeBrowseButton.setEnabled(!getIArtifact().isReadonly());
		aEndTypeBrowseButton.addSelectionListener(listener);
		toolkit.createLabel(body, "    ");

		Composite separator = toolkit.createComposite(body);
		separator.setLayoutData(sgd);

		l = toolkit.createLabel(body, "zEnd", SWT.BOLD);
		l.setFont(new Font(null, "arial", 8, SWT.BOLD));
		l.setLayoutData(gd);

		l = toolkit.createLabel(body, "Type");
		l.setEnabled(!getIArtifact().isReadonly());
		zEndTypeText = toolkit.createText(body, "");
		zEndTypeText.setEnabled(!getIArtifact().isReadonly());
		zEndTypeText.setLayoutData(tgd);
		zEndTypeText.addModifyListener(listener);
		zEndTypeText.addKeyListener(listener);
		zEndTypeBrowseButton = toolkit.createButton(body, "Browse", SWT.PUSH);
		zEndTypeBrowseButton.setEnabled(!getIArtifact().isReadonly());
		zEndTypeBrowseButton.addSelectionListener(listener);
		toolkit.createLabel(body, "    ");

		updateForm();

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	@Override
	public void refresh() {
		updateForm();
	}

	/*
	 * used to select the text in the "end type" widgets based on a match in
	 * type (a string match on the fully-qualified name of the type for the
	 * end)...
	 */
	public void selectEndName(String name) {
		if ("aEnd".equals(name)) {
			aEndTypeText.selectAll();
			aEndTypeText.setFocus();
		} else if ("zEnd".equals(name)) {
			zEndTypeText.selectAll();
			zEndTypeText.setFocus();
		}
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	private void setSilentUpdate(boolean silentUpdate) {
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

	protected void updateForm() {
		setSilentUpdate(true);
		IDependencyArtifact artifact = (IDependencyArtifact) getIArtifact();

		// Update aEnd
		IType aEndType = artifact.getAEndType();
		aEndTypeText.setText(aEndType.getFullyQualifiedName());

		// Update zEnd
		IType zEndType = artifact.getZEndType();
		zEndTypeText.setText(zEndType.getFullyQualifiedName());

		setSilentUpdate(false);
	}

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			IDependencyArtifact dep = (IDependencyArtifact) getIArtifact();
			if (e.getSource() == aEndTypeText) {
				IType type = dep.makeType();
				type.setFullyQualifiedName(aEndTypeText.getText().trim());
				dep.setAEndType(type);
				markPageModified();
			} else if (e.getSource() == zEndTypeText) {
				IType type = dep.makeType();
				type.setFullyQualifiedName(zEndTypeText.getText().trim());
				dep.setZEndType(type);
				markPageModified();
			}
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			IDependencyArtifact dep = (IDependencyArtifact) getIArtifact();

			if (e.getSource() == aEndTypeBrowseButton) {
				IType type = dep.makeType();
				String typeStr = browseButtonPressed();
				if (typeStr != null) {
					type.setFullyQualifiedName(typeStr);
					dep.setAEndType(type);
					aEndTypeText.setText(typeStr);
					markPageModified();
				}
			} else if (e.getSource() == zEndTypeBrowseButton) {
				IType type = dep.makeType();
				String typeStr = browseButtonPressed();
				if (typeStr != null) {
					type.setFullyQualifiedName(typeStr);
					dep.setZEndType(type);
					zEndTypeText.setText(typeStr);
					markPageModified();
				}
			}
		}
	}

	protected String browseButtonPressed() {
		BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
				getIArtifact().getTigerstripeProject(), new IAbstractArtifact[0]);
		dialog.setTitle("Dependency End Type");
		dialog.setMessage("Select the type of the Dependency End.");
		dialog.setIncludePrimitiveTypes(false);

		try {
			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
					getSection().getShell(), Arrays.asList(new Object[0]));
			if (artifacts.length != 0)
				return artifacts[0].getFullyQualifiedName();
		} catch (TigerstripeException e) {

		}
		return null;
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

}
