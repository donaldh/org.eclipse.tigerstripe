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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.wizards;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;

public class AddRelatedArtifactsPage extends WizardPage implements ArtifactsProvider {

	private IAbstractArtifact[] artifacts;

	private BitSet creationMask;

	private HashMap<String, IRelationship> selectionMap = new HashMap<String, IRelationship>();

	private HashMap<String, Button> buttonMap = new HashMap<String, Button>();

	private HashMap<String, Boolean> buttonMapState = new HashMap<String, Boolean>();

	private final String[] buttonMapKeys = new String[] {
			"showExtendedArtifact", "showExtendingArtifacts",
			"showAssociatedArtifacts", "showAssociatingArtifacts",
			"showDependentArtifacts", "showDependingArtifacts",
			"showImplementedArtifacts", "showImplementingArtifacts",
			"showReferencedArtifacts", "showReferencingArtifacts" };

	private final String[] buttonLabels = new String[] {
			"The artifact extended by this artifact",
			"Artifacts that extend this artifact",
			"Artifacts associated with this artifact",
			"Artifacts that this artifact is associated with",
			"Artifacts that this artifact depends on",
			"Artifacts that depend on this artifact",
			"Artifacts that define an interface that this artifact implements",
			"Artifacts that implement the interface defined by this artifact",
			"Artifacts that this artifact refers to",
			"Artifacts that refer to this artifact" };

	private final String[] pluralButtonLabels = new String[] {
			"The artifacts extended by these artifacts",
			"Artifacts that extend these artifacts",
			"Artifacts associated with these artifacts",
			"Artifacts that these artifacts are associated with",
			"Artifacts that these artifacts depend on",
			"Artifacts that depend on these artifacts",
			"Artifacts that define an interface that these artifacts implement",
			"Artifacts that implement the interface defined by these artifacts",
			"Artifacts that these artifacts refer to",
			"Artifacts that refer to these artifacts" };

	private final List<String> buttonLabelsAsList = Arrays.asList(buttonLabels);

	private final List<String> pluralButtonLabelsAsList = Arrays
			.asList(pluralButtonLabels);

	private final RelatedCollector collector;

	public AddRelatedArtifactsPage(IAbstractArtifact[] artifacts,
			BitSet creationMask, RelatedCollector collector) {
		super("Add Related Artifacts");
		this.artifacts = artifacts;
		this.creationMask = creationMask;
		this.collector = collector;
		setTitle("Select Kind of Relation");
		
		String artList = "";
		for (IAbstractArtifact artifact : artifacts) {
			if (artList.length() != 0) {
				artList += ", ";
			}
			artList += artifact.getName();
		}
		setDescription("Please Select Related to " + artList);
	}

	public void createControl(Composite parent) {
		Composite area = createMainArea(parent);
		area.setLayout(new FillLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 2;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);
		composite.getShell().setMinimumSize(250, 200);
		createInstanceDefinitionControl(composite, nColumns);
		createButtonsForButtonBar(composite);
		setControl(area);
	}

	protected Composite createMainArea(Composite parent) {
		// create a composite with standard margins and spacing
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		return composite;
	}

	private void createInstanceDefinitionControl(Composite composite,
			int nColumns) {

		// the group containing the buttons that control whether or not to show
		// artifacts
		// related by Extended/Extending relationships
		Group box = createCheckboxGroup(composite, nColumns,
				"by an Extends relationship", 0, 1);
		// the group containing the buttons that control whether or not to show
		// artifacts
		// related by Associated/Associating relationships
		box = createCheckboxGroup(composite, nColumns,
				"by an Association/Association Class", 2, 3);
		// the group containing the buttons that control whether or not to show
		// artifacts
		// related by Dependent/Depending relationships
		box = createCheckboxGroup(composite, nColumns,
				"by a Depends relationship", 4, 5);
		// the group containing the buttons that control whether or not to show
		// artifacts
		// related by Implemented/Implementing relationships
		box = createCheckboxGroup(composite, nColumns,
				"by an Implements relationship", 6, 7);
		// the group containing the buttons that control whether or not to show
		// artifacts
		// related by Implemented/Implementing relationships
		box = createCheckboxGroup(composite, nColumns,
				"by an Referenced/Referencing relationship", 8, 9);
	}

	private Group createCheckboxGroup(Composite composite, int nColumns,
			String groupTitle, int startButtonIdx, int endButtonIdx) {
		Group box = new Group(composite, SWT.NULL);
		box.setText(groupTitle);
		GridData bgd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		bgd.horizontalSpan = 8;
		box.setLayoutData(bgd);
		GridLayout bLayout = new GridLayout();
		bLayout.numColumns = 2;
		box.setLayout(bLayout);
		for (int i = startButtonIdx; i <= endButtonIdx; i++) {
			// get the appropriate label to use
			String checkBoxLabel = buttonLabels[i];
			// create the button
			Button button = new Button(box, SWT.CHECK);
			button.setText(checkBoxLabel);
			// set it's layout data
			bgd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL
					| GridData.GRAB_VERTICAL);
			bgd.horizontalSpan = 8;
			button.setLayoutData(bgd);
			// and add the button to the "button map" using the appropriate key
			// so that
			// we can find it again later...
			final int idx = buttonLabelsAsList.indexOf(button.getText());
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Object obj = e.getSource();
					if (((Button) obj).getSelection()) {
						buttonMapState.put(buttonMapKeys[idx], Boolean.TRUE);
					} else {
						buttonMapState.put(buttonMapKeys[idx], Boolean.FALSE);
					}
					getWizard().getContainer().updateButtons();
				}
			});
			buttonMap.put(buttonMapKeys[idx], button);
			buttonMapState.put(buttonMapKeys[idx], Boolean.FALSE);
			if (!creationMask.get(i)) {
				button.setEnabled(false);
			}
		}
		return box;
	}

	private boolean anySelected() {
		for (Boolean state : buttonMapState.values()) {
			if (state) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canFlipToNextPage() {
		return super.canFlipToNextPage() && anySelected();
	}
	
	@Override
	public boolean isPageComplete() {
		return super.isPageComplete() && anySelected();
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		parent = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).applyTo(parent);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(parent);
		// create a "select all" button and register a listener for it
		Button selectAllButton = new Button(parent, SWT.PUSH);
		selectAllButton.setText("Select All");
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Button button : buttonMap.values()) {
					if (!button.isEnabled())
						continue;
					button.setSelection(true);
					int idx = buttonLabelsAsList.indexOf(button.getText());
					buttonMapState.put(buttonMapKeys[idx], Boolean.TRUE);
					getWizard().getContainer().updateButtons();
				}
			}
		};
		selectAllButton.addSelectionListener(listener);
		setButtonLayoutData(selectAllButton);
		// create a "deselect all" button and register a listener for it
		Button deSelectAllButton = new Button(parent, SWT.PUSH);
		deSelectAllButton.setText("Deselect All");
		listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Button button : buttonMap.values()) {
					if (!button.isEnabled())
						continue;
					button.setSelection(false);
					int idx = buttonLabelsAsList.indexOf(button.getText());
					buttonMapState.put(buttonMapKeys[idx], Boolean.FALSE);
					getWizard().getContainer().updateButtons();
				}
			}
		};
		deSelectAllButton.addSelectionListener(listener);
		setButtonLayoutData(deSelectAllButton);
	}

	public boolean isExtendedSelected() {
		return buttonMapState.get(buttonMapKeys[0]).booleanValue();
	}

	public boolean isExtendingSelected() {
		return buttonMapState.get(buttonMapKeys[1]).booleanValue();
	}

	public boolean isAssociatedSelected() {
		return buttonMapState.get(buttonMapKeys[2]).booleanValue();
	}

	public boolean isAssociatingSelected() {
		return buttonMapState.get(buttonMapKeys[3]).booleanValue();
	}

	public boolean isDependentSelected() {
		return buttonMapState.get(buttonMapKeys[4]).booleanValue();
	}

	public boolean isDependingSelected() {
		return buttonMapState.get(buttonMapKeys[5]).booleanValue();
	}

	public boolean isImplementedSelected() {
		return buttonMapState.get(buttonMapKeys[6]).booleanValue();
	}

	public boolean isImplementingSelected() {
		return buttonMapState.get(buttonMapKeys[7]).booleanValue();
	}

	public boolean isReferencedSelected() {
		return buttonMapState.get(buttonMapKeys[8]).booleanValue();
	}

	public boolean isReferencingSelected() {
		return buttonMapState.get(buttonMapKeys[9]).booleanValue();
	}

	public IAbstractArtifact[] provide() {
		return collector.collect(this).toArray(new IAbstractArtifact[0]);
	}
}
