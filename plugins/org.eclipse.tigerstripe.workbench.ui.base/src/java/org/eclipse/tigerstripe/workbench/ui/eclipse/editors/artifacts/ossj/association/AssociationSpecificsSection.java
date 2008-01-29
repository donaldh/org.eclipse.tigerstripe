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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.association;

import java.util.Arrays;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationEnd;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.ArtifactSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

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

	public AssociationSpecificsSection(TigerstripeFormPage page,
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

		aggrStrs = new String[EAggregationEnum.values().length];
		int i = 0;
		for (EAggregationEnum val : EAggregationEnum.values()) {
			aggrStrs[i++] = val.getLabel();
		}

		mulStrs = new String[IModelComponent.EMultiplicity.values().length];
		i = 0;
		for (IModelComponent.EMultiplicity val : IModelComponent.EMultiplicity.values()) {
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
		sgd.heightHint = 5;

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
					aEnd.setAggregation(EAggregationEnum.parse(label));
					markPageModified();
				}
			} else if (e.getSource() == zEndAggregationCombo) {
				int i = zEndAggregationCombo.getSelectionIndex();
				if (i != -1) {
					String label = aggrStrs[i];
					zEnd.setAggregation(EAggregationEnum.parse(label));
					markPageModified();
				}
			} else if (e.getSource() == aEndMultiplicityCombo) {
				int i = aEndMultiplicityCombo.getSelectionIndex();
				if (i != -1) {
					String label = mulStrs[i];
					aEnd.setMultiplicity(IModelComponent.EMultiplicity.parse(label));
					markPageModified();
				}
			} else if (e.getSource() == zEndMultiplicityCombo) {
				int i = zEndMultiplicityCombo.getSelectionIndex();
				if (i != -1) {
					String label = mulStrs[i];
					zEnd.setMultiplicity(IModelComponent.EMultiplicity.parse(label));
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
				aEnd.setNavigable(aEndIsNavigableButton.getSelection());
				markPageModified();
			} else if (e.getSource() == zEndIsNavigableButton) {
				zEnd.setNavigable(zEndIsNavigableButton.getSelection());
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
				getIArtifact().getTigerstripeProject(), new IAbstractArtifact[0]);
		dialog.setIncludePrimitiveTypes(false);
		dialog.setTitle("Association End Type");
		dialog.setMessage("Select the type of the Association End.");

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

	private int getAEndVisibility() {
		if (aEndPublicButton.getSelection())
			return IModelComponent.VISIBILITY_PUBLIC;
		else if (aEndProtectedButton.getSelection())
			return IModelComponent.VISIBILITY_PROTECTED;
		else if (aEndPrivateButton.getSelection())
			return IModelComponent.VISIBILITY_PRIVATE;
		else
			return IModelComponent.VISIBILITY_PACKAGE;
	}

	private int getZEndVisibility() {
		if (zEndPublicButton.getSelection())
			return IModelComponent.VISIBILITY_PUBLIC;
		else if (zEndProtectedButton.getSelection())
			return IModelComponent.VISIBILITY_PROTECTED;
		else if (zEndPrivateButton.getSelection())
			return IModelComponent.VISIBILITY_PRIVATE;
		else
			return IModelComponent.VISIBILITY_PACKAGE;
	}

	private void setAEndVisibility(int visibility) {
		aEndPublicButton
				.setSelection(visibility == IModelComponent.VISIBILITY_PUBLIC);
		aEndProtectedButton
				.setSelection(visibility == IModelComponent.VISIBILITY_PROTECTED);
		aEndPrivateButton
				.setSelection(visibility == IModelComponent.VISIBILITY_PRIVATE);
		aEndPackageButton
				.setSelection(visibility == IModelComponent.VISIBILITY_PACKAGE);
	}

	private void setZEndVisibility(int visibility) {
		zEndPublicButton
				.setSelection(visibility == IModelComponent.VISIBILITY_PUBLIC);
		zEndProtectedButton
				.setSelection(visibility == IModelComponent.VISIBILITY_PROTECTED);
		zEndPrivateButton
				.setSelection(visibility == IModelComponent.VISIBILITY_PRIVATE);
		zEndPackageButton
				.setSelection(visibility == IModelComponent.VISIBILITY_PACKAGE);
	}

}
