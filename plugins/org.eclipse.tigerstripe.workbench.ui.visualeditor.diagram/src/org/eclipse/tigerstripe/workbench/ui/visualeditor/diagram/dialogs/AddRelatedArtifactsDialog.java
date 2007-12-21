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

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IRelationship;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;

public class AddRelatedArtifactsDialog extends TSMessageDialog {

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

	private final static Boolean TRUE = new Boolean(true);

	private final static Boolean FALSE = new Boolean(false);

	public AddRelatedArtifactsDialog(Shell parentShell, Map map,
			IAbstractArtifact[] artifacts, BitSet creationMask) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.artifacts = artifacts;
		this.creationMask = creationMask;
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 2;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		initDialog();
		createInstanceDefinitionControl(composite, nColumns);

		return area;
	}

	protected void initDialog() {
		getShell().setText("Add Related Artifacts");
		getShell().setMinimumSize(250, 200);
	}

	private void createInstanceDefinitionControl(Composite composite,
			int nColumns) {
		Label label = new Label(composite, SWT.NULL);
		String artList = "";
		for (IAbstractArtifact artifact : artifacts) {
			if (artList.length() != 0) {
				artList += ", ";
			}
			artList += artifact.getName();
		}
		label.setText("Related to " + artList);

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
					if (((Button) obj).getSelection())
						buttonMapState.put(buttonMapKeys[idx], TRUE);
					else
						buttonMapState.put(buttonMapKeys[idx], FALSE);
				}
			});
			buttonMap.put(buttonMapKeys[idx], button);
			buttonMapState.put(buttonMapKeys[idx], FALSE);
			if (!creationMask.get(i)) {
				button.setEnabled(false);
			}
		}
		return box;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create an "OK" button
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		// create a "select all" button and register a listener for it
		Button selectAllButton = createButton(parent,
				IDialogConstants.SELECT_ALL_ID, "Select All", false);
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Button button : buttonMap.values()) {
					if (!button.isEnabled())
						continue;
					button.setSelection(true);
					int idx = buttonLabelsAsList.indexOf(button.getText());
					buttonMapState.put(buttonMapKeys[idx], TRUE);
				}
			}
		};
		selectAllButton.addSelectionListener(listener);
		// create a "deselect all" button and register a listener for it
		Button deSelectAllButton = createButton(parent,
				IDialogConstants.DESELECT_ALL_ID, "Deselect All", false);
		listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Button button : buttonMap.values()) {
					if (!button.isEnabled())
						continue;
					button.setSelection(false);
					int idx = buttonLabelsAsList.indexOf(button.getText());
					buttonMapState.put(buttonMapKeys[idx], FALSE);
				}
			}
		};
		deSelectAllButton.addSelectionListener(listener);
		// and create a "Cancel" button, making it the default
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, true);
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

}
