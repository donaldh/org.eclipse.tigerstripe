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

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationEnd;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.StereotypeSectionManager;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AssociationSpecificsSection extends ArtifactSectionPart {

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
		super(page, parent, toolkit, null, null, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.EXPANDED);
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

		Composite body = getBody();
		GridLayout layout = new GridLayout();
		layout.numColumns = 7;
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		body.setLayout(layout);

		GridData sgd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		sgd.horizontalSpan = 7;
		sgd.heightHint = 8;

		GridData gd = new GridData();
		gd.horizontalSpan = 7;
		Label l = toolkit.createLabel(body, "aEnd", SWT.BOLD);
		l.setFont(new Font(null, "arial", 8, SWT.BOLD));
		l.setLayoutData(gd);
		l = toolkit.createLabel(body, "Name:");
		l.setEnabled(!getIArtifact().isReadonly());
		aEndNameText = toolkit.createText(body, "");
		GridData tgd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		aEndNameText.setLayoutData(tgd);
		aEndNameText.setEnabled(!getIArtifact().isReadonly());
		aEndNameText.addModifyListener(listener);

		toolkit.createLabel(body, "");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");

		l = toolkit.createLabel(body, "Description:");
		aEndCommentText = toolkit.createText(body, "", SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		tgd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		tgd.heightHint = 40;
		aEndCommentText.setLayoutData(tgd);
		aEndCommentText.setEnabled(!getIArtifact().isReadonly());
		aEndCommentText.addModifyListener(listener);

		toolkit.createLabel(body, "");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");

		l = toolkit.createLabel(body, "Type");
		l.setEnabled(!getIArtifact().isReadonly());
		aEndTypeText = toolkit.createText(body, "");
		aEndTypeText.setEnabled(!getIArtifact().isReadonly());
		tgd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		aEndTypeText.setLayoutData(tgd);
		aEndTypeText.addModifyListener(listener);
		aEndTypeText.addKeyListener(listener);
		aEndTypeBrowseButton = toolkit.createButton(body, "Browse", SWT.PUSH);
		aEndTypeBrowseButton.setEnabled(!getIArtifact().isReadonly());
		aEndTypeBrowseButton.addSelectionListener(listener);
		toolkit.createLabel(body, "    ");

		l = toolkit.createLabel(body, "Multiplicity");
		l.setEnabled(!getIArtifact().isReadonly());
		aEndMultiplicityCombo = new CCombo(body, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		aEndMultiplicityCombo.setItems(mulStrs);
		aEndMultiplicityCombo.setEnabled(!getIArtifact().isReadonly());
		aEndMultiplicityCombo.addSelectionListener(listener);
		aEndMultiplicityCombo.setVisibleItemCount(IModelComponent.EMultiplicity
				.values().length);
		toolkit.adapt(this.aEndMultiplicityCombo, true, true);
		toolkit.createLabel(body, "    ");

		toolkit.createLabel(body, "Visibility: ");

		Composite visiComposite = toolkit.createComposite(body);
		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 4;
		visiComposite.setEnabled(!getIArtifact().isReadonly());
		visiComposite.setLayout(gLayout);
		GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
		gd1.horizontalSpan = 2;
		visiComposite.setLayoutData(gd1);
		aEndPublicButton = toolkit.createButton(visiComposite, "Public",
				SWT.RADIO);
		aEndPublicButton.setEnabled(!getIArtifact().isReadonly());
		aEndPublicButton.addSelectionListener(listener);
		aEndProtectedButton = toolkit.createButton(visiComposite, "Protected",
				SWT.RADIO);
		aEndProtectedButton.setEnabled(!getIArtifact().isReadonly());
		aEndProtectedButton.addSelectionListener(listener);
		aEndPrivateButton = toolkit.createButton(visiComposite, "Private",
				SWT.RADIO);
		aEndPrivateButton.setEnabled(!getIArtifact().isReadonly());
		aEndPrivateButton.addSelectionListener(listener);
		aEndPackageButton = toolkit.createButton(visiComposite, "Package",
				SWT.RADIO);
		aEndPackageButton.setEnabled(!getIArtifact().isReadonly());
		aEndPackageButton.addSelectionListener(listener);
		toolkit.createLabel(body, "    ");

		l = toolkit.createLabel(body, "Changeable");
		l.setEnabled(!getIArtifact().isReadonly());
		aEndChangeableCombo = new CCombo(body, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		aEndChangeableCombo.addSelectionListener(listener);
		aEndChangeableCombo.setItems(chanStrs);
		aEndChangeableCombo.setEnabled(!getIArtifact().isReadonly());
		toolkit.adapt(this.aEndChangeableCombo, true, true);
		toolkit.createLabel(body, "    ");

		toolkit.createLabel(body, "Qualifiers: ");

		Composite modifCompositeA = toolkit.createComposite(body);
		GridLayout gLayoutA = new GridLayout();
		gLayoutA.numColumns = 3;
		modifCompositeA.setEnabled(!getIArtifact().isReadonly());
		modifCompositeA.setLayout(gLayoutA);
		GridData gdA = new GridData(GridData.FILL_HORIZONTAL);
		gdA.horizontalSpan = 2;
		modifCompositeA.setLayoutData(gdA);

		aEndIsNavigableButton = toolkit.createButton(modifCompositeA,
				"isNavigable", SWT.CHECK);
		aEndIsNavigableButton.setEnabled(!getIArtifact().isReadonly());
		aEndIsNavigableButton.addSelectionListener(listener);
		aEndIsOrderedButton = toolkit.createButton(modifCompositeA,
				"isOrdered", SWT.CHECK);
		aEndIsOrderedButton.setEnabled(!getIArtifact().isReadonly());
		aEndIsOrderedButton.addSelectionListener(listener);
		aEndIsUniqueButton = toolkit.createButton(modifCompositeA, "isUnique",
				SWT.CHECK);
		aEndIsUniqueButton.setEnabled(!getIArtifact().isReadonly());
		aEndIsUniqueButton.addSelectionListener(listener);

		toolkit.createLabel(body, "    ");
		l = toolkit.createLabel(body, "Aggregation");
		l.setEnabled(!getIArtifact().isReadonly());
		aEndAggregationCombo = new CCombo(body, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		aEndAggregationCombo.setEnabled(!getIArtifact().isReadonly());
		aEndAggregationCombo.addSelectionListener(listener);
		aEndAggregationCombo.setItems(aggrStrs);
		toolkit.adapt(this.aEndAggregationCombo, true, true);
		toolkit.paintBordersFor(aEndAggregationCombo);

		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "Stereotypes:");

		aAnnTable = toolkit.createTable(body, SWT.BORDER);
		GridData aGd1 = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		aGd1.verticalSpan = 3;
		aGd1.widthHint = 200;
		aGd1.heightHint = 40;
		aAnnTable.setLayoutData(aGd1);

		aAddAnno = toolkit.createButton(body, "Add", SWT.PUSH);
		aAddAnno.setEnabled(!getIArtifact().isReadonly());
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");

		aEditAnno = toolkit.createButton(body, "Edit", SWT.PUSH);
		aEditAnno.setEnabled(!getIArtifact().isReadonly());
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");

		aRemoveAnno = toolkit.createButton(body, "Remove", SWT.PUSH);
		aRemoveAnno.setEnabled(!getIArtifact().isReadonly());

		aStereotypeManager = new StereotypeSectionManager(aAddAnno, aEditAnno,
				aRemoveAnno, aAnnTable, ((IAssociationArtifact) getIArtifact())
						.getAEnd(), getSection().getShell(),
				(ArtifactEditorBase) getPage().getEditor());
		aStereotypeManager.delegate();

		Composite separator = toolkit.createComposite(body);
		separator.setLayoutData(sgd);

		l = toolkit.createLabel(body, "zEnd", SWT.BOLD);
		l.setFont(new Font(null, "arial", 8, SWT.BOLD));
		l.setLayoutData(gd);
		l = toolkit.createLabel(body, "Name:");
		l.setEnabled(!getIArtifact().isReadonly());
		zEndNameText = toolkit.createText(body, "");
		tgd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		zEndNameText.setEnabled(!getIArtifact().isReadonly());
		zEndNameText.setLayoutData(tgd);
		zEndNameText.addModifyListener(listener);

		toolkit.createLabel(body, "");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");

		l = toolkit.createLabel(body, "Description:");
		zEndCommentText = toolkit.createText(body, "", SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		tgd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		tgd.heightHint = 40;
		zEndCommentText.setLayoutData(tgd);
		zEndCommentText.setEnabled(!getIArtifact().isReadonly());
		zEndCommentText.addModifyListener(listener);

		toolkit.createLabel(body, "");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");

		l = toolkit.createLabel(body, "Type");
		l.setEnabled(!getIArtifact().isReadonly());
		zEndTypeText = toolkit.createText(body, "");
		zEndTypeText.setEnabled(!getIArtifact().isReadonly());
		tgd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		zEndTypeText.setLayoutData(tgd);
		zEndTypeText.addModifyListener(listener);
		zEndTypeText.addKeyListener(listener);
		zEndTypeBrowseButton = toolkit.createButton(body, "Browse", SWT.PUSH);
		zEndTypeBrowseButton.setEnabled(!getIArtifact().isReadonly());
		zEndTypeBrowseButton.addSelectionListener(listener);
		toolkit.createLabel(body, "    ");

		l = toolkit.createLabel(body, "Multiplicity");
		l.setEnabled(!getIArtifact().isReadonly());
		zEndMultiplicityCombo = new CCombo(body, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		zEndMultiplicityCombo.setEnabled(!getIArtifact().isReadonly());
		zEndMultiplicityCombo.addSelectionListener(listener);
		zEndMultiplicityCombo.setItems(mulStrs);
		zEndMultiplicityCombo.setVisibleItemCount(IModelComponent.EMultiplicity
				.values().length);
		toolkit.adapt(this.zEndMultiplicityCombo, true, true);
		toolkit.createLabel(body, "    ");

		toolkit.createLabel(body, "Visibility: ");

		Composite visiCompositeZ = toolkit.createComposite(body);
		GridLayout gLayoutZ = new GridLayout();
		gLayoutZ.numColumns = 4;
		visiCompositeZ.setEnabled(!getIArtifact().isReadonly());
		visiCompositeZ.setLayout(gLayoutZ);
		GridData gdZ = new GridData(GridData.FILL_HORIZONTAL);
		gdZ.horizontalSpan = 2;
		visiCompositeZ.setLayoutData(gdZ);
		zEndPublicButton = toolkit.createButton(visiCompositeZ, "Public",
				SWT.RADIO);
		zEndPublicButton.setEnabled(!getIArtifact().isReadonly());
		zEndPublicButton.addSelectionListener(listener);
		zEndProtectedButton = toolkit.createButton(visiCompositeZ, "Protected",
				SWT.RADIO);
		zEndProtectedButton.setEnabled(!getIArtifact().isReadonly());
		zEndProtectedButton.addSelectionListener(listener);
		zEndPrivateButton = toolkit.createButton(visiCompositeZ, "Private",
				SWT.RADIO);
		zEndPrivateButton.setEnabled(!getIArtifact().isReadonly());
		zEndPrivateButton.addSelectionListener(listener);
		zEndPackageButton = toolkit.createButton(visiCompositeZ, "Package",
				SWT.RADIO);
		zEndPackageButton.setEnabled(!getIArtifact().isReadonly());
		zEndPackageButton.addSelectionListener(listener);
		toolkit.createLabel(body, "    ");

		l = toolkit.createLabel(body, "Changeable");
		l.setEnabled(!getIArtifact().isReadonly());
		zEndChangeableCombo = new CCombo(body, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		zEndChangeableCombo.setEnabled(!getIArtifact().isReadonly());
		zEndChangeableCombo.addSelectionListener(listener);
		zEndChangeableCombo.setItems(chanStrs);
		toolkit.adapt(this.zEndChangeableCombo, true, true);
		toolkit.createLabel(body, "    ");

		toolkit.createLabel(body, "Qualifiers: ");
		Composite modifCompositeZ = toolkit.createComposite(body);
		gLayoutZ = new GridLayout();
		gLayoutZ.numColumns = 3;
		modifCompositeZ.setEnabled(!getIArtifact().isReadonly());
		modifCompositeZ.setLayout(gLayoutZ);
		gdZ = new GridData(GridData.FILL_HORIZONTAL);
		gdZ.horizontalSpan = 2;
		modifCompositeZ.setLayoutData(gdZ);

		zEndIsNavigableButton = toolkit.createButton(modifCompositeZ,
				"isNavigable", SWT.CHECK);
		zEndIsNavigableButton.setEnabled(!getIArtifact().isReadonly());
		zEndIsNavigableButton.addSelectionListener(listener);
		zEndIsOrderedButton = toolkit.createButton(modifCompositeZ,
				"isOrdered", SWT.CHECK);
		zEndIsOrderedButton.setEnabled(!getIArtifact().isReadonly());
		zEndIsOrderedButton.addSelectionListener(listener);
		zEndIsUniqueButton = toolkit.createButton(modifCompositeZ, "isUnique",
				SWT.CHECK);
		zEndIsUniqueButton.setEnabled(!getIArtifact().isReadonly());
		zEndIsUniqueButton.addSelectionListener(listener);

		toolkit.createLabel(body, "    ");
		l = toolkit.createLabel(body, "Aggregation");
		l.setEnabled(!getIArtifact().isReadonly());
		zEndAggregationCombo = new CCombo(body, SWT.SINGLE | SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		zEndAggregationCombo.setEnabled(!getIArtifact().isReadonly());
		zEndAggregationCombo.addSelectionListener(listener);
		zEndAggregationCombo.setItems(aggrStrs);
		toolkit.adapt(this.zEndAggregationCombo, true, true);

		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "Stereotypes:");

		zAnnTable = toolkit.createTable(body, SWT.BORDER);
		aGd1 = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		aGd1.verticalSpan = 3;
		aGd1.widthHint = 200;
		aGd1.heightHint = 40;
		zAnnTable.setLayoutData(aGd1);

		zAddAnno = toolkit.createButton(body, "Add", SWT.PUSH);
		zAddAnno.setEnabled(!getIArtifact().isReadonly());
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");

		zEditAnno = toolkit.createButton(body, "Edit", SWT.PUSH);
		zEditAnno.setEnabled(!getIArtifact().isReadonly());
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");
		toolkit.createLabel(body, "    ");

		zRemoveAnno = toolkit.createButton(body, "Remove", SWT.PUSH);
		zRemoveAnno.setEnabled(!getIArtifact().isReadonly());

		zStereotypeManager = new StereotypeSectionManager(zAddAnno, zEditAnno,
				zRemoveAnno, zAnnTable, ((IAssociationArtifact) getIArtifact())
						.getZEnd(), getSection().getShell(),
				(ArtifactEditorBase) getPage().getEditor());
		zStereotypeManager.delegate();

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
	public void selectEndByName(String name) {
		if (name.equals(aEndNameText.getText())) {
			aEndTypeText.selectAll();
			aEndTypeText.setFocus();
		} else if (name.equals(zEndNameText.getText())) {
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
		aEndIsOrderedButton.setSelection(aEnd.isOrdered());
		aEndIsUniqueButton.setSelection(aEnd.isUnique());
		setAEndVisibility(aEnd.getVisibility());

		// Update zEnd
		IAssociationEnd zEnd = artifact.getZEnd();
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
		zEndIsOrderedButton.setSelection(zEnd.isOrdered());
		zEndIsUniqueButton.setSelection(zEnd.isUnique());

		setZEndVisibility(zEnd.getVisibility());

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
				IType type = new Type(((AssociationArtifact) getIArtifact())
						.getArtifactManager());
				type.setFullyQualifiedName(aEndTypeText.getText().trim());
				AssociationEnd end = (AssociationEnd) assoc.getAEnd();
				end.setType(type);
				markPageModified();
			} else if (e.getSource() == zEndTypeText) {
				IType type = new Type(((AssociationArtifact) getIArtifact())
						.getArtifactManager());
				type.setFullyQualifiedName(zEndTypeText.getText().trim());
				AssociationEnd end = (AssociationEnd) assoc.getZEnd();
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
				}
			} else if (e.getSource() == zEndMultiplicityCombo) {
				int i = zEndMultiplicityCombo.getSelectionIndex();
				if (i != -1) {
					String label = mulStrs[i];
					zEnd.setMultiplicity(IModelComponent.EMultiplicity
							.parse(label));
					markPageModified();
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
				IType type = new Type(((AssociationArtifact) getIArtifact())
						.getArtifactManager());
				String typeStr = browseButtonPressed();
				if (typeStr != null) {
					type.setFullyQualifiedName(typeStr);
					AssociationEnd end = (AssociationEnd) artifact.getAEnd();
					end.setType(type);
					aEndTypeText.setText(typeStr);
					markPageModified();
				}
			} else if (e.getSource() == zEndTypeBrowseButton) {
				IType type = new Type(((AssociationArtifact) getIArtifact())
						.getArtifactManager());
				String typeStr = browseButtonPressed();
				if (typeStr != null) {
					type.setFullyQualifiedName(typeStr);
					AssociationEnd end = (AssociationEnd) artifact.getZEnd();
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
				new IAbstractArtifact[0]);
		dialog.setIncludePrimitiveTypes(false);
		dialog.setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IAssociationArtifactImpl.class.getName()).getLabel()
				+ " End Type");
		dialog.setMessage("Select the type of the "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IAssociationArtifactImpl.class.getName()).getLabel()
				+ " End.");

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
