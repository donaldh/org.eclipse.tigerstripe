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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.eclipse.core.resources.IResource;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.Stereotype;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.NewTSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.ui.dialogs.ListSelectionDialog;

public class AssociationPropertiesEditDialog extends NewTSMessageDialog {

	private IArtifactManagerSession artifactMgrSession;

	private DiagramGraphicalViewer mapViewer;

	private AbstractArtifact source;

	private AbstractArtifact target;

	private IAbstractArtifact sourceArtifact;

	private IAbstractArtifact targetArtifact;

	private Text associationNameField;

	private Text associationStereotypesField;

	private List<Stereotype> newAssocStereotypes;

	private String origAssociationStereotypesStringVal;

	private String associationName;

	private Association association = null;

	private IAbstractArtifact iAssociation = null;

	private Text aEndRoleNameField;

	private Combo aEndMultiplicityCombo;

	private Combo aEndVisibilityCombo;

	private Combo aEndAggregationCombo;

	private Button aEndIsNavigableButton;

	private Button aEndIsUniqueButton;

	private Button aEndIsOrderedButton;

	private Text zEndRoleNameField;

	private Combo zEndMultiplicityCombo;

	private Combo zEndVisibilityCombo;

	private Combo zEndAggregationCombo;

	private Button zEndIsNavigableButton;

	private Button zEndIsUniqueButton;

	private Button zEndIsOrderedButton;

	private Set<String> elementNames = new HashSet<String>();

	private Map<String, IAbstractArtifact> elementNameMap = new HashMap<String, IAbstractArtifact>();

	private Map<String, Object> changedValuesMap = new HashMap<String, Object>();

	public AssociationPropertiesEditDialog(Shell parent,
			MapEditPart mapEditPart, Association association) {
		super(parent, "", "Edit Association Properties");
		this.association = association;
		this.source = association.getAEnd();
		this.target = association.getZEnd();
		String sourceFQN = source.getFullyQualifiedName();
		String targetFQN = target.getFullyQualifiedName();
		mapViewer = (DiagramGraphicalViewer) mapEditPart.getViewer();
		DiagramEditDomain domain = (DiagramEditDomain) mapViewer
				.getEditDomain();
		IResource res = (IResource) domain.getEditorPart().getEditorInput()
				.getAdapter(IResource.class);
		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getITigerstripeProjectFor(res.getProject());
		if (!(aProject instanceof ITigerstripeProject))
			throw new RuntimeException("non-Tigerstripe Project found");
		ITigerstripeProject project = (ITigerstripeProject) aProject;
		try {
			artifactMgrSession = project.getArtifactManagerSession();
			sourceArtifact = artifactMgrSession
					.getArtifactByFullyQualifiedName(sourceFQN);
			targetArtifact = artifactMgrSession
					.getArtifactByFullyQualifiedName(targetFQN);
			iAssociation = association.getCorrespondingIArtifact();
		} catch (TigerstripeException e) {
			throw new RuntimeException("matching IAbstractArtifact not found");
		}
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());
		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		initDialog();
		createMessageArea(composite, 8);
		createAssociationEditControl(composite);
		return area;
	}

	protected void initDialog() {
		if (association instanceof AssociationClass) {
			this.setTitleString("Edit Association Class Properties");
			getShell().setText("Association Class Property Editor");
		} else {
			this.setTitleString("Edit Association Properties");
			getShell().setText("Association Property Editor");
		}
		getShell().setMinimumSize(250, 200);
		elementNames.clear();
		elementNameMap.clear();
		ArtifactManager artMgr = ((ArtifactManagerSessionImpl) artifactMgrSession)
				.getArtifactManager();
		Collection artifacts = artMgr.getAllArtifacts(false,
				new TigerstripeNullProgressMonitor());
		for (Object artifact : artifacts) {
			if (artifact instanceof IAbstractArtifact) {
				IAbstractArtifact iAbstractArtifact = (IAbstractArtifact) artifact;
				elementNames.add(iAbstractArtifact.getFullyQualifiedName());
				elementNameMap.put(iAbstractArtifact.getFullyQualifiedName(),
						iAbstractArtifact);
			}
		}
	}

	protected void updateOkButton(Object eventSource) {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		if (okButton == null)
			return;
		if (eventSource instanceof Text) {
			associationName = ((Text) eventSource).getText();
			String associationFQN = association.getPackage() + "."
					+ associationName;
			Matcher classNameMatcher = TigerstripeValidationUtils.classNamePattern
					.matcher(associationName);
			Matcher elementNameMatcher = TigerstripeValidationUtils.elementNamePattern
					.matcher(associationName);
			if (associationName == null || "".equals(associationName)) {
				okButton.setEnabled(false);
				setInfoMessage("Enter an association name");
			} else if (!classNameMatcher.matches()
					&& !elementNameMatcher.matches()) {
				okButton.setEnabled(false);
				setErrorMessage("'" + associationName
						+ "' is not a legal association name");
			} else if (elementNames.contains(associationFQN)
					&& elementNameMap.get(associationFQN) != iAssociation) {
				okButton.setEnabled(false);
				setErrorMessage("Artifact named '" + associationName
						+ "' already exists in the project");
			} else {
				okButton.setEnabled(true);
				setInfoMessage("");
			}
		}
	}

	private void createAssociationEditControl(Composite composite) {
		int nameLabelCols = 2;
		int nameValueCols = 6;
		int totalNumCols = nameLabelCols + nameValueCols;
		int endControlPanelCols = totalNumCols / 2;
		int numRowsControlPanels = 10;
		// set the composite so that it has the right layout data...
		GridLayout layout = new GridLayout();
		layout.numColumns = totalNumCols;
		composite.setLayout(layout);
		// add the label, button, and text for the stereotype definition...
		Label annotationLabel = new Label(composite, SWT.NULL);
		annotationLabel.setText("Annotation Definition:");
		GridData bgd = new GridData();
		bgd.horizontalSpan = nameLabelCols;
		annotationLabel.setLayoutData(bgd);
		Button stereoTypeSelButton = new Button(composite, SWT.NULL);
		stereoTypeSelButton.setText("...");
		stereoTypeSelButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				Object source = e.getSource();
				if (source instanceof Button) {
					List<String> stereotypeVals = new ArrayList<String>();
					if (newAssocStereotypes != null) {
						for (IStereotype stereo : newAssocStereotypes) {
							stereotypeVals.add(stereo.getName());
						}
					} else {
						stereotypeVals.addAll(association.getStereotypes());
					}
					try {
						IAbstractArtifact iArtifact = association
								.getCorrespondingIArtifact();
						IStereotype[] availStereotypes = TigerstripeCore
								.getIWorkbenchProfileSession()
								.getActiveProfile()
								.getAvailableStereotypeForComponent(iArtifact);
						Arrays.sort(availStereotypes,
								new Comparator<IStereotype>() {
									public int compare(IStereotype o1,
											IStereotype o2) {
										return o1.getName().compareTo(
												o2.getName());
									}
								});
						List<IStereotype> availStereotypeList = Arrays
								.asList(availStereotypes);
						List<IStereotype> selectedStereotypeList = new ArrayList<IStereotype>();
						for (IStereotype availStereotype : availStereotypeList) {
							if (stereotypeVals.contains(availStereotype
									.getName())) {
								selectedStereotypeList.add(availStereotype);
							}
						}
						ListSelectionDialog stereotypeSelDialog = new ListSelectionDialog(
								getShell(), availStereotypes,
								new ArrayContentProvider(),
								new LabelProvider(), "Select Stereotypes");
						stereotypeSelDialog
								.setInitialElementSelections(selectedStereotypeList);
						stereotypeSelDialog.create();
						boolean returnStatus = (stereotypeSelDialog.open() == Window.OK);
						if (returnStatus) {
							Object[] results = stereotypeSelDialog.getResult();
							List tmpList = Arrays.asList(results);
							newAssocStereotypes = new ArrayList<Stereotype>();
							newAssocStereotypes.addAll(tmpList);
							associationStereotypesField
									.setText(getStereotypeLabel(tmpList));
						}
					} catch (TigerstripeException tsException) {
						TigerstripeRuntime.logInfoMessage(
								"TigerstripeException detected", tsException);
					}
				}
			}
		});
		associationStereotypesField = new Text(composite, SWT.BORDER | SWT.FLAT);
		List<String> assocStereotypes = association.getStereotypes();
		origAssociationStereotypesStringVal = getStereotypeLabel(assocStereotypes);
		associationStereotypesField
				.setText(origAssociationStereotypesStringVal);
		associationStereotypesField
				.setToolTipText("The annotations defined for the association.");
		associationStereotypesField.setEditable(false);
		setFillLayout(associationStereotypesField, nameValueCols - 1, 1);
		// and add the label and text for the name of the association
		Label assocNameLabel = new Label(composite, SWT.NULL);
		assocNameLabel.setText("Association Name:");
		bgd = new GridData();
		bgd.horizontalSpan = nameLabelCols;
		assocNameLabel.setLayoutData(bgd);
		associationNameField = new Text(composite, SWT.BORDER);
		associationNameField.setText(association.getName());
		associationNameField.setToolTipText("The name of the association.");
		associationNameField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateOkButton(e.getSource());
			}
		});
		setFillLayout(associationNameField, nameValueCols, 1);
		// now, define the controls for defining the aEnd properties...to do
		// this, first add a
		// new group to contain these controls
		Group aEndBox = new Group(composite, SWT.NULL);
		aEndBox.setText("aEnd Properties");
		GridLayout bLayout = new GridLayout();
		bLayout.numColumns = endControlPanelCols;
		aEndBox.setLayout(bLayout);
		setFillBothLayout(aEndBox, endControlPanelCols, numRowsControlPanels);
		// then define a text box (and a label for that text box) for editing
		// the role name
		// for the aEnd
		Label aEndRoleLabel = new Label(aEndBox, SWT.NULL);
		aEndRoleLabel.setText("Role Name:");
		bgd = new GridData();
		bgd.horizontalSpan = 1;
		aEndRoleLabel.setLayoutData(bgd);
		aEndRoleNameField = new Text(aEndBox, SWT.BORDER);
		aEndRoleNameField.setText(association.getAEndName());
		aEndRoleNameField.setToolTipText("The aEnd role name.");
		bgd = new GridData();
		bgd.horizontalSpan = endControlPanelCols - 1;
		bgd.verticalSpan = 1;
		bgd.widthHint = 200;
		aEndRoleNameField.setLayoutData(bgd);
		// add the control used to select the multiplicity
		String[] multLabels = IModelComponent.EMultiplicity.labels();
		AssocMultiplicity aEndMult = association.getAEndMultiplicity();
		Label aEndMultiplicityLabel = new Label(aEndBox, SWT.NULL);
		aEndMultiplicityLabel.setText("Multiplicity:");
		aEndMultiplicityCombo = new Combo(aEndBox, SWT.READ_ONLY);
		for (String multLabel : multLabels) {
			aEndMultiplicityCombo.add(multLabel);
		}
		aEndMultiplicityCombo.select(aEndMult.getValue());
		Label aEndMultiplicityFiller = new Label(aEndBox, SWT.NULL);
		this.setFillLayout(aEndMultiplicityFiller, 2, 1);
		// add the control used to select the visibility
		List<Visibility> visibilityValues = Visibility.VALUES;
		List<String> visibilityStrVals = new ArrayList<String>(visibilityValues
				.size());
		for (int i = 0; i < visibilityValues.size(); i++)
			visibilityStrVals.add(i, visibilityValues.get(i).getName()
					.toLowerCase());
		String[] visibilityStrValArray = new String[visibilityValues.size()];
		visibilityStrVals.toArray(visibilityStrValArray);
		Visibility aEndVisibility = association.getAEndVisibility();
		Label aEndVisibilityLabel = new Label(aEndBox, SWT.NULL);
		aEndVisibilityLabel.setText("Visibility:");
		aEndVisibilityCombo = new Combo(aEndBox, SWT.READ_ONLY);
		for (String visibilityStrVal : visibilityStrVals) {
			aEndVisibilityCombo.add(visibilityStrVal);
		}
		aEndVisibilityCombo.select(aEndVisibility.getValue());
		Label aEndVisibilityFiller = new Label(aEndBox, SWT.NULL);
		this.setFillLayout(aEndVisibilityFiller, 2, 1);
		// add the control used to select none/composition/aggregation
		List<AggregationEnum> aggregationValues = AggregationEnum.VALUES;
		List<String> aggregationStrVals = new ArrayList<String>(
				aggregationValues.size());
		for (int i = 0; i < aggregationValues.size(); i++)
			aggregationStrVals.add(i, aggregationValues.get(i).getName());
		String[] aggregationStrValArray = new String[aggregationValues.size()];
		aggregationStrVals.toArray(aggregationStrValArray);
		AggregationEnum aEndAggregation = association.getAEndAggregation();
		Label aEndAggregationLabel = new Label(aEndBox, SWT.NULL);
		aEndAggregationLabel.setText("Aggregation:");
		aEndAggregationCombo = new Combo(aEndBox, SWT.READ_ONLY);
		for (String aggregationStrVal : aggregationStrVals) {
			aEndAggregationCombo.add(aggregationStrVal);
		}
		aEndAggregationCombo.select(aEndAggregation.getValue());
		Label aEndAggregationFiller = new Label(aEndBox, SWT.NULL);
		this.setFillLayout(aEndAggregationFiller, 2, 1);
		// add the controls to chose if the end is navigable, ordered, and/or
		// unique
		boolean aEndIsNavigable = association.isAEndIsNavigable();
		aEndIsNavigableButton = new Button(aEndBox, SWT.CHECK);
		aEndIsNavigableButton.setText("isNavigable");
		aEndIsNavigableButton.setSelection(aEndIsNavigable);
		boolean aEndIsOrdered = association.isAEndIsOrdered();
		aEndIsOrderedButton = new Button(aEndBox, SWT.CHECK);
		aEndIsOrderedButton.setText("isOrdered");
		aEndIsOrderedButton.setSelection(aEndIsOrdered);
		boolean aEndIsUnique = association.isAEndIsUnique();
		aEndIsUniqueButton = new Button(aEndBox, SWT.CHECK);
		aEndIsUniqueButton.setText("isUnique");
		aEndIsUniqueButton.setSelection(aEndIsUnique);
		// now, define the controls for defining the zEnd properties...to do
		// this, first add a
		// new group to contain these controls
		Group zEndBox = new Group(composite, SWT.NULL);
		zEndBox.setText("zEnd Properties");
		bLayout = new GridLayout();
		bLayout.numColumns = endControlPanelCols;
		zEndBox.setLayout(bLayout);
		setFillBothLayout(zEndBox, endControlPanelCols, numRowsControlPanels);
		// then define a text box (and a label for that text box) for editing
		// the role name
		// for the zEnd
		Label zEndRoleLabel = new Label(zEndBox, SWT.NULL);
		zEndRoleLabel.setText("Role Name:");
		bgd = new GridData();
		bgd.horizontalSpan = 1;
		zEndRoleLabel.setLayoutData(bgd);
		zEndRoleNameField = new Text(zEndBox, SWT.BORDER);
		zEndRoleNameField.setText(association.getZEndName());
		zEndRoleNameField.setToolTipText("The zEnd role name.");
		bgd = new GridData();
		bgd.horizontalSpan = endControlPanelCols - 1;
		bgd.verticalSpan = 1;
		bgd.widthHint = 200;
		zEndRoleNameField.setLayoutData(bgd);
		// add the control used to select the multiplicity
		AssocMultiplicity zEndMult = association.getZEndMultiplicity();
		Label zEndMultiplicityLabel = new Label(zEndBox, SWT.NULL);
		zEndMultiplicityLabel.setText("Multiplicity:");
		zEndMultiplicityCombo = new Combo(zEndBox, SWT.READ_ONLY);
		for (String multLabel : multLabels) {
			zEndMultiplicityCombo.add(multLabel);
		}
		zEndMultiplicityCombo.select(zEndMult.getValue());
		Label zEndMultiplicityFiller = new Label(zEndBox, SWT.NULL);
		this.setFillLayout(zEndMultiplicityFiller, 2, 1);
		// add the control used to select the visibility
		Visibility zEndVisibility = association.getZEndVisibility();
		Label zEndVisibilityLabel = new Label(zEndBox, SWT.NULL);
		zEndVisibilityLabel.setText("Visibility:");
		zEndVisibilityCombo = new Combo(zEndBox, SWT.READ_ONLY);
		for (String visibilityStrVal : visibilityStrVals) {
			zEndVisibilityCombo.add(visibilityStrVal);
		}
		zEndVisibilityCombo.select(zEndVisibility.getValue());
		Label zEndVisibilityFiller = new Label(zEndBox, SWT.NULL);
		this.setFillLayout(zEndVisibilityFiller, 2, 1);
		// add the control used to select none/composition/aggregation
		AggregationEnum zEndAggregation = association.getZEndAggregation();
		Label zEndAggregationLabel = new Label(zEndBox, SWT.NULL);
		zEndAggregationLabel.setText("Aggregation:");
		zEndAggregationCombo = new Combo(zEndBox, SWT.READ_ONLY);
		for (String aggregationStrVal : aggregationStrVals) {
			zEndAggregationCombo.add(aggregationStrVal);
		}
		zEndAggregationCombo.select(zEndAggregation.getValue());
		Label zEndAggregationFiller = new Label(zEndBox, SWT.NULL);
		this.setFillLayout(zEndAggregationFiller, 2, 1);
		// add the controls to chose if the end is navigable, ordered, and/or
		// unique
		boolean zEndIsNavigable = association.isZEndIsNavigable();
		zEndIsNavigableButton = new Button(zEndBox, SWT.CHECK);
		zEndIsNavigableButton.setText("isNavigable");
		zEndIsNavigableButton.setSelection(zEndIsNavigable);
		boolean zEndIsOrdered = association.isZEndIsOrdered();
		zEndIsOrderedButton = new Button(zEndBox, SWT.CHECK);
		zEndIsOrderedButton.setText("isOrdered");
		zEndIsOrderedButton.setSelection(zEndIsOrdered);
		boolean zEndIsUnique = association.isZEndIsUnique();
		zEndIsUniqueButton = new Button(zEndBox, SWT.CHECK);
		zEndIsUniqueButton.setText("isUnique");
		zEndIsUniqueButton.setSelection(zEndIsUnique);
	}

	private String getStereotypeLabel(List stereotypeVals) {
		if (stereotypeVals.size() > 0) {
			StringBuffer buff = new StringBuffer();
			buff.append("<<");
			int count = 0;
			for (Object stereotypeVal : stereotypeVals) {
				buff.append(stereotypeVal.toString());
				if (++count < stereotypeVals.size())
					buff.append(", ");
			}
			buff.append(">>");
			return buff.toString();
		}
		return "";
	}

	private void setFillLayout(Control control, int nCols, int nRows) {
		GridData bgd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		bgd.horizontalSpan = nCols;
		bgd.verticalSpan = nRows;
		control.setLayoutData(bgd);
	}

	private void setFillBothLayout(Control control, int nCols, int nRows) {
		GridData bgd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		bgd.horizontalSpan = nCols;
		bgd.verticalSpan = nRows;
		control.setLayoutData(bgd);
	}

	// public String getAssociationName() {
	// return associationName;
	// }

	@Override
	protected void okPressed() {
		changedValuesMap.clear();
		String newAssociationName = associationNameField.getText();
		if (!newAssociationName.equals(association.getName()))
			changedValuesMap.put("associationName", newAssociationName);
		;
		String newAssociationStereotypes = associationStereotypesField
				.getText();
		if (!origAssociationStereotypesStringVal
				.equals(newAssociationStereotypes)) {
			List<Stereotype> newAssocStereotypeVals = new ArrayList();
			newAssocStereotypeVals.addAll(newAssocStereotypes);
			changedValuesMap.put("assocStereotypes", newAssocStereotypeVals);
		}
		// add any changed aEnd properites to the changedValuesMap
		String newAEndRoleName = aEndRoleNameField.getText();
		if (!newAEndRoleName.equals(association.getAEndName()))
			changedValuesMap.put("aEndName", newAEndRoleName);
		String newAEndMultiplicity = IModelComponent.EMultiplicity.at(
				aEndMultiplicityCombo.getSelectionIndex()).getLabel();
		if (!newAEndMultiplicity.equals(association.getAEndMultiplicity()
				.getLiteral()))
			changedValuesMap.put("aEndMultiplicity", newAEndMultiplicity);
		String newAEndVisibility = Visibility.get(
				aEndVisibilityCombo.getSelectionIndex()).getName();
		if (!newAEndVisibility.equals(association.getAEndVisibility()
				.getLiteral()))
			changedValuesMap.put("aEndVisibility", newAEndVisibility);
		String newAEndAggregation = AggregationEnum.get(
				aEndAggregationCombo.getSelectionIndex()).getName();
		if (!newAEndAggregation.equals(association.getAEndAggregation()
				.getLiteral()))
			changedValuesMap.put("aEndAggregation", newAEndAggregation);
		boolean aEndIsNavigable = aEndIsNavigableButton.getSelection();
		if (aEndIsNavigable != association.isAEndIsNavigable())
			changedValuesMap.put("aEndIsNavigable", Boolean
					.valueOf(aEndIsNavigable));
		boolean aEndIsUnique = aEndIsUniqueButton.getSelection();
		if (aEndIsUnique != association.isAEndIsUnique())
			changedValuesMap.put("aEndIsUnique", Boolean.valueOf(aEndIsUnique));
		boolean aEndIsOrdered = aEndIsOrderedButton.getSelection();
		if (aEndIsOrdered != association.isAEndIsOrdered())
			changedValuesMap.put("aEndIsOrdered", Boolean
					.valueOf(aEndIsOrdered));
		// and do the same for any changed zEnd properties...
		String newZEndRoleName = zEndRoleNameField.getText();
		if (!newZEndRoleName.equals(association.getZEndName()))
			changedValuesMap.put("zEndName", newZEndRoleName);
		String newZEndMultiplicity = IModelComponent.EMultiplicity.at(
				zEndMultiplicityCombo.getSelectionIndex()).getLabel();
		if (!newZEndMultiplicity.equals(association.getZEndMultiplicity()
				.getLiteral()))
			changedValuesMap.put("zEndMultiplicity", newZEndMultiplicity);
		String newZEndVisibility = Visibility.get(
				zEndVisibilityCombo.getSelectionIndex()).getName();
		if (!newZEndVisibility.equals(association.getZEndVisibility()
				.getLiteral()))
			changedValuesMap.put("zEndVisibility", newZEndVisibility);
		String newZEndAggregation = AggregationEnum.get(
				zEndAggregationCombo.getSelectionIndex()).getName();
		if (!newZEndAggregation.equals(association.getZEndAggregation()
				.getLiteral()))
			changedValuesMap.put("zEndAggregation", newZEndAggregation);
		boolean zEndIsNavigable = zEndIsNavigableButton.getSelection();
		if (zEndIsNavigable != association.isZEndIsNavigable())
			changedValuesMap.put("zEndIsNavigable", Boolean
					.valueOf(zEndIsNavigable));
		boolean zEndIsUnique = zEndIsUniqueButton.getSelection();
		if (zEndIsUnique != association.isZEndIsUnique())
			changedValuesMap.put("zEndIsUnique", Boolean.valueOf(zEndIsUnique));
		boolean zEndIsOrdered = zEndIsOrderedButton.getSelection();
		if (zEndIsOrdered != association.isZEndIsOrdered())
			changedValuesMap.put("zEndIsOrdered", Boolean
					.valueOf(zEndIsOrdered));
		super.okPressed();
	}

	public Map<String, Object> getChangedValuesMap() {
		return changedValuesMap;
	}

}
