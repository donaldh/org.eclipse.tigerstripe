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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.association;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationEnd;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.EndSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.PageModifyCallback;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.StereotypeSectionManager;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AssociationSpecificsSection extends EndSection {

	private boolean silentUpdate = false;

	private class AssociationSpecificsSectionListener implements
			ModifyListener, SelectionListener, KeyListener {

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

	private String[] aggrStrs = null;

	private String[] mulStrs = null;

	private String[] chanStrs = null;

	private Text aEndNameText;

	private Text aEndCommentText;

	private Text aEndTypeText;

	private Button aEndTypeBrowseButton;

	private CCombo aEndAggregationCombo;

	private CCombo aEndMultiplicityCombo;

	private CCombo aEndChangeableCombo;

	private Button aEndIsNavigableButton;

	private Button aEndIsOrderedButton;

	private Button aEndIsUniqueButton;

	private Button aEndPublicButton;

	private Button aEndProtectedButton;

	private Button aEndPrivateButton;

	private Button aEndPackageButton;

	private Button aAddAnno;

	private Button aEditAnno;

	private Button aRemoveAnno;

	private Table aAnnTable;

	private StereotypeSectionManager aStereotypeManager;

	private Button zEndPublicButton;

	private Button zEndProtectedButton;

	private Button zEndPrivateButton;

	private Button zEndPackageButton;

	private Text zEndNameText;

	private Text zEndCommentText;

	private Text zEndTypeText;

	private Button zEndTypeBrowseButton;

	private CCombo zEndAggregationCombo;

	private CCombo zEndMultiplicityCombo;

	private CCombo zEndChangeableCombo;

	private Button zEndIsNavigableButton;

	private Button zEndIsOrderedButton;

	private Button zEndIsUniqueButton;

	private Button zAddAnno;

	private Button zEditAnno;

	private Button zRemoveAnno;

	private Table zAnnTable;

	private StereotypeSectionManager zStereotypeManager;

	public AssociationSpecificsSection(TigerstripeFormPage page,
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
		aggrStrs = new String[EAggregationEnum.values().length];
		int i = 0;
		for (EAggregationEnum val : EAggregationEnum.values()) {
			aggrStrs[i++] = val.getLabel();
		}

		mulStrs = new String[IModelComponent.EMultiplicity.values().length];
		i = 0;
		for (IModelComponent.EMultiplicity val : IModelComponent.EMultiplicity
				.values()) {
			mulStrs[i++] = val.getLabel();
		}

		chanStrs = new String[EChangeableEnum.values().length];
		i = 0;
		for (EChangeableEnum val : EChangeableEnum.values()) {
			chanStrs[i++] = val.getLabel();
		}

		AssociationSpecificsSectionListener listener = new AssociationSpecificsSectionListener();

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
			AssociationSpecificsSectionListener listener) {
		TableWrapLayout layout;
		TableWrapData td;

		Section aEndSection = TigerstripeLayoutFactory.createSection(getBody(),
				toolkit, Section.TITLE_BAR, "aEnd Details", null);
		aEndSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite aEndClient = toolkit.createComposite(aEndSection);
		layout = TigerstripeLayoutFactory.createFormTableWrapLayout(2, false);
		aEndClient.setLayout(layout);
		aEndClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		toolkit.createLabel(aEndClient, "Name:").setEnabled(!isReadonly());
		aEndNameText = toolkit.createText(aEndClient, "");
		aEndNameText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		aEndNameText.setEnabled(!isReadonly());
		aEndNameText.addModifyListener(listener);

		toolkit.createLabel(aEndClient, "Description:").setEnabled(
				!isReadonly());
		aEndCommentText = toolkit.createText(aEndClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 70;
		aEndCommentText.setLayoutData(td);
		aEndCommentText.setEnabled(!isReadonly());
		aEndCommentText.addModifyListener(listener);

		toolkit.createLabel(aEndClient, "Type:").setEnabled(!isReadonly());
		Composite c = toolkit.createComposite(aEndClient);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		layout = TigerstripeLayoutFactory.createFormPaneTableWrapLayout(2,
				false);
		c.setLayout(layout);
		aEndTypeText = toolkit.createText(c, "");
		aEndTypeText.setEnabled(!isReadonly());
		aEndTypeText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		aEndTypeText.addModifyListener(listener);
		aEndTypeText.addKeyListener(listener);
		aEndTypeBrowseButton = toolkit.createButton(c, "Browse", SWT.PUSH);
		aEndTypeBrowseButton.setEnabled(!isReadonly());
		aEndTypeBrowseButton.addSelectionListener(listener);

		toolkit.createLabel(aEndClient, "Multiplicity").setEnabled(
				!isReadonly());
		aEndMultiplicityCombo = new CCombo(aEndClient, SWT.SINGLE
				| SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
		aEndMultiplicityCombo.setItems(mulStrs);
		aEndMultiplicityCombo.setEnabled(!isReadonly());
		aEndMultiplicityCombo.addSelectionListener(listener);
		aEndMultiplicityCombo.setVisibleItemCount(IModelComponent.EMultiplicity
				.values().length);
		aEndMultiplicityCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		toolkit.adapt(this.aEndMultiplicityCombo, true, true);

		toolkit.createLabel(aEndClient, "Visibility: ").setEnabled(
				!isReadonly());
		c = toolkit.createComposite(aEndClient);
		c.setEnabled(!isReadonly());
		layout = TigerstripeLayoutFactory.createFormPaneTableWrapLayout(4,
				false);
		c.setLayout(layout);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		aEndPublicButton = toolkit.createButton(c, "Public", SWT.RADIO);
		aEndPublicButton.setEnabled(!isReadonly());
		aEndPublicButton.addSelectionListener(listener);
		aEndProtectedButton = toolkit.createButton(c, "Protected", SWT.RADIO);
		aEndProtectedButton.setEnabled(!isReadonly());
		aEndProtectedButton.addSelectionListener(listener);
		aEndPrivateButton = toolkit.createButton(c, "Private", SWT.RADIO);
		aEndPrivateButton.setEnabled(!isReadonly());
		aEndPrivateButton.addSelectionListener(listener);
		aEndPackageButton = toolkit.createButton(c, "Package", SWT.RADIO);
		aEndPackageButton.setEnabled(!isReadonly());
		aEndPackageButton.addSelectionListener(listener);

		toolkit.createLabel(aEndClient, "Changeable:")
				.setEnabled(!isReadonly());
		aEndChangeableCombo = new CCombo(aEndClient, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		aEndChangeableCombo.addSelectionListener(listener);
		aEndChangeableCombo.setItems(chanStrs);
		aEndChangeableCombo.setEnabled(!isReadonly());
		aEndChangeableCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		toolkit.adapt(this.aEndChangeableCombo, true, true);

		toolkit.createLabel(aEndClient, "Qualifiers:");
		c = toolkit.createComposite(aEndClient);
		c.setEnabled(!isReadonly());
		layout = TigerstripeLayoutFactory.createFormPaneTableWrapLayout(3,
				false);
		c.setLayout(layout);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		aEndIsNavigableButton = toolkit.createButton(c, "isNavigable",
				SWT.CHECK);
		aEndIsNavigableButton.setEnabled(!isReadonly());
		aEndIsNavigableButton.addSelectionListener(listener);
		aEndIsOrderedButton = toolkit.createButton(c, "isOrdered", SWT.CHECK);
		aEndIsOrderedButton.setEnabled(!isReadonly());
		aEndIsOrderedButton.addSelectionListener(listener);
		aEndIsUniqueButton = toolkit.createButton(c, "isUnique", SWT.CHECK);
		aEndIsUniqueButton.setEnabled(!isReadonly());
		aEndIsUniqueButton.addSelectionListener(listener);

		toolkit.createLabel(aEndClient, "Aggregation:").setEnabled(
				!isReadonly());
		aEndAggregationCombo = new CCombo(aEndClient, SWT.SINGLE
				| SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
		aEndAggregationCombo.setEnabled(!isReadonly());
		aEndAggregationCombo.addSelectionListener(listener);
		aEndAggregationCombo.setItems(aggrStrs);
		aEndAggregationCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		toolkit.adapt(this.aEndAggregationCombo, true, true);
		toolkit.paintBordersFor(aEndAggregationCombo);

		toolkit.createLabel(aEndClient, "Stereotypes:");

		c = toolkit.createComposite(aEndClient);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		GridLayout gridLayout = TigerstripeLayoutFactory.createClearGridLayout(
				2, false);
		gridLayout.horizontalSpacing = TigerstripeLayoutFactory.DEFAULT_HORIZONTAL_SPACING;
		c.setLayout(gridLayout);

		aAnnTable = toolkit.createTable(c, SWT.BORDER);
		aAnnTable.setLayoutData(new GridData(GridData.FILL_BOTH));

		c = toolkit.createComposite(c);
		c.setLayoutData(new GridData(GridData.FILL));
		c.setLayout(TigerstripeLayoutFactory.createButtonsGridLayout());

		aAddAnno = toolkit.createButton(c, "Add", SWT.PUSH);
		aAddAnno.setData("name", "Add_Stereo_Assoc_A");
		aAddAnno.setEnabled(!isReadonly());
		aAddAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));

		aEditAnno = toolkit.createButton(c, "Edit", SWT.PUSH);
		aEditAnno.setData("name", "Edit_Stereo_Assoc_A");
		aEditAnno.setEnabled(!isReadonly());
		aEditAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));

		aRemoveAnno = toolkit.createButton(c, "Remove", SWT.PUSH);
		aRemoveAnno.setData("name", "Remove_Stereo_Assoc_A");
		aRemoveAnno.setEnabled(!isReadonly());
		aRemoveAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));

		aStereotypeManager = new StereotypeSectionManager(aAddAnno, aEditAnno,
				aRemoveAnno, aAnnTable,
				((IAssociationArtifact) getIArtifact()).getAEnd(), getSection()
						.getShell(), new PageModifyCallback(getPage()));
		aStereotypeManager.delegate();

		aEndTypeText.setData("name", "aEndTypeText");
		aEndNameText.setData("name", "aEndNameText");

		aEndSection.setClient(aEndClient);
		getToolkit().paintBordersFor(aEndClient);
	}

	private void createZEndSection(FormToolkit toolkit,
			AssociationSpecificsSectionListener listener) {
		TableWrapLayout layout;
		TableWrapData td;

		Section zEndSection = TigerstripeLayoutFactory.createSection(getBody(),
				toolkit, Section.TITLE_BAR, "zEnd Details", null);
		zEndSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite zEndClient = toolkit.createComposite(zEndSection);
		layout = TigerstripeLayoutFactory.createFormTableWrapLayout(2, false);
		zEndClient.setLayout(layout);
		zEndClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		toolkit.createLabel(zEndClient, "Name:").setEnabled(!isReadonly());
		zEndNameText = toolkit.createText(zEndClient, "");
		zEndNameText.setEnabled(!isReadonly());
		zEndNameText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		zEndNameText.addModifyListener(listener);

		toolkit.createLabel(zEndClient, "Description:").setEnabled(
				!isReadonly());
		zEndCommentText = toolkit.createText(zEndClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 70;
		zEndCommentText.setLayoutData(td);
		zEndCommentText.setEnabled(!isReadonly());
		zEndCommentText.addModifyListener(listener);

		toolkit.createLabel(zEndClient, "Type").setEnabled(!isReadonly());
		Composite c = toolkit.createComposite(zEndClient);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		layout = TigerstripeLayoutFactory.createFormPaneTableWrapLayout(2,
				false);
		c.setLayout(layout);
		zEndTypeText = toolkit.createText(c, "");
		zEndTypeText.setEnabled(!isReadonly());
		zEndTypeText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		zEndTypeText.addModifyListener(listener);
		zEndTypeText.addKeyListener(listener);
		zEndTypeBrowseButton = toolkit.createButton(c, "Browse", SWT.PUSH);
		zEndTypeBrowseButton.setEnabled(!isReadonly());
		zEndTypeBrowseButton.addSelectionListener(listener);

		toolkit.createLabel(zEndClient, "Multiplicity").setEnabled(
				!isReadonly());
		zEndMultiplicityCombo = new CCombo(zEndClient, SWT.SINGLE
				| SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
		zEndMultiplicityCombo.setEnabled(!isReadonly());
		zEndMultiplicityCombo.addSelectionListener(listener);
		zEndMultiplicityCombo.setItems(mulStrs);
		zEndMultiplicityCombo.setVisibleItemCount(IModelComponent.EMultiplicity
				.values().length);
		zEndMultiplicityCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		toolkit.adapt(this.zEndMultiplicityCombo, true, true);

		toolkit.createLabel(zEndClient, "Visibility: ").setEnabled(
				!isReadonly());
		c = toolkit.createComposite(zEndClient);
		c.setEnabled(!isReadonly());
		layout = TigerstripeLayoutFactory.createFormPaneTableWrapLayout(4,
				false);
		c.setLayout(layout);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		zEndPublicButton = toolkit.createButton(c, "Public", SWT.RADIO);
		zEndPublicButton.setEnabled(!isReadonly());
		zEndPublicButton.addSelectionListener(listener);
		zEndProtectedButton = toolkit.createButton(c, "Protected", SWT.RADIO);
		zEndProtectedButton.setEnabled(!isReadonly());
		zEndProtectedButton.addSelectionListener(listener);
		zEndPrivateButton = toolkit.createButton(c, "Private", SWT.RADIO);
		zEndPrivateButton.setEnabled(!isReadonly());
		zEndPrivateButton.addSelectionListener(listener);
		zEndPackageButton = toolkit.createButton(c, "Package", SWT.RADIO);
		zEndPackageButton.setEnabled(!isReadonly());
		zEndPackageButton.addSelectionListener(listener);

		toolkit.createLabel(zEndClient, "Changeable").setEnabled(!isReadonly());
		zEndChangeableCombo = new CCombo(zEndClient, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		zEndChangeableCombo.setEnabled(!isReadonly());
		zEndChangeableCombo.addSelectionListener(listener);
		zEndChangeableCombo.setItems(chanStrs);
		zEndChangeableCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		toolkit.adapt(this.zEndChangeableCombo, true, true);

		toolkit.createLabel(zEndClient, "Qualifiers: ");
		c = toolkit.createComposite(zEndClient);
		c.setEnabled(!isReadonly());
		layout = TigerstripeLayoutFactory.createFormPaneTableWrapLayout(3,
				false);
		c.setLayout(layout);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		zEndIsNavigableButton = toolkit.createButton(c, "isNavigable",
				SWT.CHECK);
		zEndIsNavigableButton.setEnabled(!isReadonly());
		zEndIsNavigableButton.addSelectionListener(listener);
		zEndIsOrderedButton = toolkit.createButton(c, "isOrdered", SWT.CHECK);
		zEndIsOrderedButton.setEnabled(!isReadonly());
		zEndIsOrderedButton.addSelectionListener(listener);
		zEndIsUniqueButton = toolkit.createButton(c, "isUnique", SWT.CHECK);
		zEndIsUniqueButton.setEnabled(!isReadonly());
		zEndIsUniqueButton.addSelectionListener(listener);

		toolkit.createLabel(zEndClient, "Aggregation")
				.setEnabled(!isReadonly());
		zEndAggregationCombo = new CCombo(zEndClient, SWT.SINGLE
				| SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
		zEndAggregationCombo.setEnabled(!isReadonly());
		zEndAggregationCombo.addSelectionListener(listener);
		zEndAggregationCombo.setItems(aggrStrs);
		zEndAggregationCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		toolkit.adapt(this.zEndAggregationCombo, true, true);

		toolkit.createLabel(zEndClient, "Stereotypes:");
		c = toolkit.createComposite(zEndClient);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		GridLayout gridLayout = TigerstripeLayoutFactory.createClearGridLayout(
				2, false);
		gridLayout.horizontalSpacing = TigerstripeLayoutFactory.DEFAULT_HORIZONTAL_SPACING;
		c.setLayout(gridLayout);

		zAnnTable = toolkit.createTable(c, SWT.BORDER);
		zAnnTable.setLayoutData(new GridData(GridData.FILL_BOTH));

		c = toolkit.createComposite(c);
		c.setLayoutData(new GridData(GridData.FILL));
		c.setLayout(TigerstripeLayoutFactory.createButtonsGridLayout());

		zAddAnno = toolkit.createButton(c, "Add", SWT.PUSH);
		zAddAnno.setData("name", "Add_Stereo_Assoc_A");
		zAddAnno.setEnabled(!isReadonly());
		zAddAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));

		zEditAnno = toolkit.createButton(c, "Edit", SWT.PUSH);
		zEditAnno.setData("name", "Edit_Stereo_Assoc_A");
		zEditAnno.setEnabled(!isReadonly());
		zEditAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));

		zRemoveAnno = toolkit.createButton(c, "Remove", SWT.PUSH);
		zRemoveAnno.setData("name", "Remove_Stereo_Assoc_Z");
		zRemoveAnno.setEnabled(!isReadonly());
		zRemoveAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));

		zStereotypeManager = new StereotypeSectionManager(zAddAnno, zEditAnno,
				zRemoveAnno, zAnnTable,
				((IAssociationArtifact) getIArtifact()).getZEnd(), getSection()
						.getShell(), new PageModifyCallback(getPage()));
		zStereotypeManager.delegate();

		zEndTypeText.setData("name", "zEndTypeText");
		zEndNameText.setData("name", "zEndNameText");

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
	public void selectEndByName(String name) {
		if (name.equals(aEndNameText.getText())) {
			aEndTypeText.selectAll();
			aEndTypeText.setFocus();
		} else if (name.equals(zEndNameText.getText())) {
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
		IAssociationArtifact artifact = (IAssociationArtifact) getIArtifact();

		// Update aEnd
		IAssociationEnd aEnd = artifact.getAEnd();
		if (aEnd != null) {
			aEndNameText.setText(aEnd.getName());
			aEndCommentText.setText(aEnd.getComment());
			aEndTypeText.setText(aEnd.getType().getFullyQualifiedName());
			aEndAggregationCombo.select(indexIn(aggrStrs, aEnd.getAggregation()
					.getLabel()));
			aEndChangeableCombo.select(indexIn(chanStrs, aEnd.getChangeable()
					.getLabel()));
			aEndMultiplicityCombo.select(indexIn(mulStrs, aEnd.getMultiplicity()
					.getLabel()));
			aEndIsNavigableButton.setSelection(aEnd.isNavigable());
			
			if (aEnd.getMultiplicity().isArray()) {
				aEndIsOrderedButton.setEnabled(true);
				aEndIsOrderedButton.setSelection(aEnd.isOrdered());
				aEndIsUniqueButton.setEnabled(true);
				aEndIsUniqueButton.setSelection(aEnd.isUnique());
			} else {
				aEndIsOrderedButton.setEnabled(false);
				aEndIsOrderedButton.setSelection(false);
				aEnd.setOrdered(false);
				aEndIsUniqueButton.setEnabled(false);
				aEndIsUniqueButton.setSelection(true);
				aEnd.setUnique(true);
			}
			aStereotypeManager.setArtifactComponent(aEnd);
			aStereotypeManager.refresh();
			
			setAEndVisibility(aEnd.getVisibility());
		}

		// Update zEnd
		IAssociationEnd zEnd = artifact.getZEnd();
		if (zEnd != null) {
			zEndNameText.setText(zEnd.getName());
			zEndCommentText.setText(zEnd.getComment());
			zEndTypeText.setText(zEnd.getType().getFullyQualifiedName());
			zEndAggregationCombo.select(indexIn(aggrStrs, zEnd.getAggregation()
					.getLabel()));
			zEndChangeableCombo.select(indexIn(chanStrs, zEnd.getChangeable()
					.getLabel()));
			zEndMultiplicityCombo.select(indexIn(mulStrs, zEnd.getMultiplicity()
					.getLabel()));
			zEndIsNavigableButton.setSelection(zEnd.isNavigable());
			
			if (zEnd.getMultiplicity().isArray()) {
				zEndIsOrderedButton.setEnabled(true);
				zEndIsOrderedButton.setSelection(zEnd.isOrdered());
				zEndIsUniqueButton.setEnabled(true);
				zEndIsUniqueButton.setSelection(zEnd.isUnique());
			} else {
				zEndIsOrderedButton.setEnabled(false);
				zEndIsOrderedButton.setSelection(false);
				zEnd.setOrdered(false);
				zEndIsUniqueButton.setEnabled(false);
				zEndIsUniqueButton.setSelection(true);
				zEnd.setUnique(true);
			}
			zStereotypeManager.setArtifactComponent(zEnd);
			zStereotypeManager.refresh();
			
			setZEndVisibility(zEnd.getVisibility());
		}
		setSilentUpdate(false);
	}

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			IAssociationArtifact assoc = (IAssociationArtifact) getIArtifact();
			if (e.getSource() == aEndNameText) {
				IAssociationEnd aEnd = (IAssociationEnd) assoc.getAEnd();
				aEnd.setName(aEndNameText.getText().trim());
				markPageModified();
			} else if (e.getSource() == zEndNameText) {
				IAssociationEnd zEnd = (IAssociationEnd) assoc.getZEnd();
				zEnd.setName(zEndNameText.getText().trim());
				markPageModified();
			} else if (e.getSource() == aEndCommentText) {
				IAssociationEnd aEnd = (IAssociationEnd) assoc.getAEnd();
				aEnd.setComment(aEndCommentText.getText().trim());
				markPageModified();
			} else if (e.getSource() == zEndCommentText) {
				IAssociationEnd zEnd = (IAssociationEnd) assoc.getZEnd();
				zEnd.setComment(zEndCommentText.getText().trim());
				markPageModified();
			} else if (e.getSource() == aEndTypeText) {
				IType type = new Type(
						((IAbstractArtifactInternal) getIArtifact())
								.getArtifactManager());
				type.setFullyQualifiedName(aEndTypeText.getText().trim());
				IAssociationEnd end = (IAssociationEnd) assoc.getAEnd();
				end.setType(type);
				markPageModified();
			} else if (e.getSource() == zEndTypeText) {
				IType type = new Type(
						((IAbstractArtifactInternal) getIArtifact())
								.getArtifactManager());
				type.setFullyQualifiedName(zEndTypeText.getText().trim());
				IAssociationEnd end = (IAssociationEnd) assoc.getZEnd();
				end.setType(type);
				markPageModified();
			}
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			IAssociationArtifact artifact = (IAssociationArtifact) getIArtifact();
			IAssociationEnd aEnd = (IAssociationEnd) artifact.getAEnd();
			IAssociationEnd zEnd = (IAssociationEnd) artifact.getZEnd();

			if (e.getSource() == aEndAggregationCombo) {
				int i = aEndAggregationCombo.getSelectionIndex();
				if (i != -1) {
					String label = aggrStrs[i];
					EAggregationEnum ag = EAggregationEnum.parse(label);
					aEnd.setAggregation(ag);
					if (ag != EAggregationEnum.NONE
							&& zEnd.getAggregation() != EAggregationEnum.NONE) {
						zEnd.setAggregation(EAggregationEnum.NONE);
						zEndAggregationCombo.select(indexIn(aggrStrs, zEnd
								.getAggregation().getLabel()));
					}
					markPageModified();
				}
			} else if (e.getSource() == zEndAggregationCombo) {
				int i = zEndAggregationCombo.getSelectionIndex();
				if (i != -1) {
					String label = aggrStrs[i];
					EAggregationEnum ag = EAggregationEnum.parse(label);
					zEnd.setAggregation(ag);
					if (ag != EAggregationEnum.NONE
							&& aEnd.getAggregation() != EAggregationEnum.NONE) {
						aEnd.setAggregation(EAggregationEnum.NONE);
						aEndAggregationCombo.select(indexIn(aggrStrs, aEnd
								.getAggregation().getLabel()));
					}
					markPageModified();
				}
			} else if (e.getSource() == aEndMultiplicityCombo) {
				int i = aEndMultiplicityCombo.getSelectionIndex();
				if (i != -1) {
					String label = mulStrs[i];
					aEnd.setMultiplicity(IModelComponent.EMultiplicity
							.parse(label));
					markPageModified();
					updateForm();
				}
			} else if (e.getSource() == zEndMultiplicityCombo) {
				int i = zEndMultiplicityCombo.getSelectionIndex();
				if (i != -1) {
					String label = mulStrs[i];
					zEnd.setMultiplicity(IModelComponent.EMultiplicity
							.parse(label));
					markPageModified();
					updateForm();
				}
			} else if (e.getSource() == aEndChangeableCombo) {
				int i = aEndChangeableCombo.getSelectionIndex();
				if (i != -1) {
					String label = chanStrs[i];
					aEnd.setChangeable(EChangeableEnum.parse(label));
					markPageModified();
				}
			} else if (e.getSource() == zEndChangeableCombo) {
				int i = zEndChangeableCombo.getSelectionIndex();
				if (i != -1) {
					String label = chanStrs[i];
					zEnd.setChangeable(EChangeableEnum.parse(label));
					markPageModified();
				}
			} else if (e.getSource() == aEndIsNavigableButton) {
				boolean sel = aEndIsNavigableButton.getSelection();
				aEnd.setNavigable(sel);
				if (sel == false && !zEnd.isNavigable()) {
					zEndIsNavigableButton.setSelection(true);
					zEnd.setNavigable(true);
				}
				markPageModified();
			} else if (e.getSource() == zEndIsNavigableButton) {
				boolean sel = zEndIsNavigableButton.getSelection();
				zEnd.setNavigable(sel);
				if (sel == false && !aEnd.isNavigable()) {
					aEndIsNavigableButton.setSelection(true);
					aEnd.setNavigable(true);
				}
				markPageModified();
			} else if (e.getSource() == aEndIsOrderedButton) {
				aEnd.setOrdered(aEndIsOrderedButton.getSelection());
				markPageModified();
			} else if (e.getSource() == aEndIsUniqueButton) {
				aEnd.setUnique(aEndIsUniqueButton.getSelection());
				markPageModified();
			} else if (e.getSource() == zEndIsUniqueButton) {
				zEnd.setUnique(zEndIsUniqueButton.getSelection());
				markPageModified();
			} else if (e.getSource() == aEndPublicButton
					|| e.getSource() == aEndPrivateButton
					|| e.getSource() == aEndProtectedButton
					|| e.getSource() == aEndPackageButton) {
				aEnd.setVisibility(getAEndVisibility());
				markPageModified();
			} else if (e.getSource() == zEndIsOrderedButton) {
				zEnd.setOrdered(zEndIsOrderedButton.getSelection());
				markPageModified();
			} else if (e.getSource() == aEndTypeBrowseButton) {
				IType type = new Type(
						((IAbstractArtifactInternal) getIArtifact())
								.getArtifactManager());
				String typeStr = browseButtonPressed();
				if (typeStr != null) {
					type.setFullyQualifiedName(typeStr);
					IAssociationEnd end = (IAssociationEnd) artifact.getAEnd();
					end.setType(type);
					aEndTypeText.setText(typeStr);
					markPageModified();
				}
			} else if (e.getSource() == zEndTypeBrowseButton) {
				IType type = new Type(
						((IAbstractArtifactInternal) getIArtifact())
								.getArtifactManager());
				String typeStr = browseButtonPressed();
				if (typeStr != null) {
					type.setFullyQualifiedName(typeStr);
					IAssociationEnd end = (IAssociationEnd) artifact.getZEnd();
					end.setType(type);
					zEndTypeText.setText(typeStr);
					markPageModified();
				}
			} else if (e.getSource() == zEndPublicButton
					|| e.getSource() == zEndPrivateButton
					|| e.getSource() == zEndProtectedButton
					|| e.getSource() == zEndPackageButton) {
				zEnd.setVisibility(getZEndVisibility());
				markPageModified();
			}
		}
	}

	protected String browseButtonPressed() {
		BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
				getIArtifact().getTigerstripeProject(),
				AssociationEnd.getSuitableTypes());
		dialog.setIncludePrimitiveTypes(false);
		dialog.setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IAssociationArtifactImpl.class.getName()).getLabel(null)
				+ " End Type");
		dialog.setMessage("Select the type of the "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IAssociationArtifactImpl.class.getName())
						.getLabel(null) + " End.");

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

	private int indexIn(String[] labels, String targetLabel) {
		int i = 0;
		for (String label : labels) {
			if (label.equals(targetLabel))
				return i;
			i++;
		}
		return -1;
	}

	private EVisibility getAEndVisibility() {
		if (aEndPublicButton.getSelection())
			return EVisibility.PUBLIC;
		else if (aEndProtectedButton.getSelection())
			return EVisibility.PROTECTED;
		else if (aEndPrivateButton.getSelection())
			return EVisibility.PRIVATE;
		else
			return EVisibility.PACKAGE;
	}

	private EVisibility getZEndVisibility() {
		if (zEndPublicButton.getSelection())
			return EVisibility.PUBLIC;
		else if (zEndProtectedButton.getSelection())
			return EVisibility.PROTECTED;
		else if (zEndPrivateButton.getSelection())
			return EVisibility.PRIVATE;
		else
			return EVisibility.PACKAGE;
	}

	private void setAEndVisibility(EVisibility visibility) {
		aEndPublicButton.setSelection(visibility.equals(EVisibility.PUBLIC));
		aEndProtectedButton.setSelection(visibility
				.equals(EVisibility.PROTECTED));
		aEndPrivateButton.setSelection(visibility.equals(EVisibility.PRIVATE));
		aEndPackageButton.setSelection(visibility.equals(EVisibility.PACKAGE));
	}

	private void setZEndVisibility(EVisibility visibility) {
		zEndPublicButton.setSelection(visibility.equals(EVisibility.PUBLIC));
		zEndProtectedButton.setSelection(visibility
				.equals(EVisibility.PROTECTED));
		zEndPrivateButton.setSelection(visibility.equals(EVisibility.PRIVATE));
		zEndPackageButton.setSelection(visibility.equals(EVisibility.PACKAGE));
	}

}
