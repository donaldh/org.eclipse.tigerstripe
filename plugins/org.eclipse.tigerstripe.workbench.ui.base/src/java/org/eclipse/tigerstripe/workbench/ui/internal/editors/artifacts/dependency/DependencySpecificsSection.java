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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.dependency;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.EndSection;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class DependencySpecificsSection extends EndSection {

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
		super(page, parent, toolkit, null, null, Section.NO_TITLE);
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

		TableWrapLayout layout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, true);
		layout.horizontalSpacing = 10;
		getBody().setLayout(layout);

		createAEndSection(toolkit, listener);
		createZEndSection(toolkit, listener);

		updateForm();

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createAEndSection(FormToolkit toolkit,
			DependencySpecificsSectionListener listener) {
		TableWrapLayout layout;

		Section aEndSection = TigerstripeLayoutFactory.createSection(getBody(),
				toolkit, Section.TITLE_BAR, "aEnd Details", null);
		aEndSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite aEndClient = toolkit.createComposite(aEndSection);
		layout = TigerstripeLayoutFactory.createFormTableWrapLayout(3, false);
		aEndClient.setLayout(layout);
		aEndClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		toolkit.createLabel(aEndClient, "Type:").setEnabled(
				!getIArtifact().isReadonly());
		aEndTypeText = toolkit.createText(aEndClient, "");
		aEndTypeText.setEnabled(!getIArtifact().isReadonly());
		aEndTypeText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		aEndTypeText.addModifyListener(listener);
		aEndTypeText.addKeyListener(listener);
		aEndTypeBrowseButton = toolkit.createButton(aEndClient, "Browse",
				SWT.PUSH);
		aEndTypeBrowseButton.setEnabled(!getIArtifact().isReadonly());
		aEndTypeBrowseButton.addSelectionListener(listener);

		aEndTypeText.setData("name", "aEndTypeText");

		aEndSection.setClient(aEndClient);
		getToolkit().paintBordersFor(aEndClient);
	}

	private void createZEndSection(FormToolkit toolkit,
			DependencySpecificsSectionListener listener) {
		TableWrapLayout layout;

		Section zEndSection = TigerstripeLayoutFactory.createSection(getBody(),
				toolkit, Section.TITLE_BAR, "zEnd Details", null);
		zEndSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite zEndClient = toolkit.createComposite(zEndSection);
		layout = TigerstripeLayoutFactory.createFormTableWrapLayout(3, false);
		zEndClient.setLayout(layout);
		zEndClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		toolkit.createLabel(zEndClient, "Type:").setEnabled(
				!getIArtifact().isReadonly());
		zEndTypeText = toolkit.createText(zEndClient, "");
		zEndTypeText.setEnabled(!getIArtifact().isReadonly());
		zEndTypeText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		zEndTypeText.addModifyListener(listener);
		zEndTypeText.addKeyListener(listener);
		zEndTypeBrowseButton = toolkit.createButton(zEndClient, "Browse",
				SWT.PUSH);
		zEndTypeBrowseButton.setEnabled(!getIArtifact().isReadonly());
		zEndTypeBrowseButton.addSelectionListener(listener);

		zEndTypeText.setData("name", "zEndTypeText");

		zEndSection.setClient(zEndClient);
		getToolkit().paintBordersFor(zEndClient);
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

	/*
	 * used to select the text in the "end type" widgets based on a match in the
	 * end - A or Z - used by the details hyperlinks ...
	 */
	@Override
	public void selectEndByEnd(String end) {
		if (end.equals("aEnd")) {
			aEndTypeText.selectAll();
			aEndTypeText.setFocus();
		} else if (end.equals("zEnd")) {
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
				getIArtifact().getTigerstripeProject(), DependencyArtifact
						.getSuitableTypes());
		dialog.setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IDependencyArtifactImpl.class.getName()).getLabel(null)
				+ " End Type");
		dialog.setMessage("Select the type of the "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IDependencyArtifactImpl.class.getName()).getLabel(null)
				+ " End.");
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
