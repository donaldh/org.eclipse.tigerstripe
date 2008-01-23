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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

public class AssociationInstanceEditDialog extends TSMessageDialog {

	private IArtifactManagerSession artifactMgrSession;
	private InstanceMapEditPart mapEditPart;
	private DiagramGraphicalViewer mapViewer;
	private ClassInstance source;
	private IAbstractArtifact sourceArtifact;
	private ClassInstance target;
	private IAbstractArtifact targetArtifact;

	private Map<String, IRelationship> reversedRelationships = new HashMap<String, IRelationship>();
	private Map<String, IRelationship> associationMap = new HashMap<String, IRelationship>();
	private Map<String, IRelationship> associationClassMap = new HashMap<String, IRelationship>();

	private final String[] buttonLabels = new String[] { "Associations",
			"Association Classes" };
	private Map<String, Map> typeMap = new HashMap<String, Map>();
	private Set<String> instanceNames = new HashSet<String>();

	private Text instanceNameField;
	private String selectedName;
	private String selectedType;
	private String instanceName;

	public AssociationInstanceEditDialog(Shell parent,
			InstanceMapEditPart mapEditPart, ClassInstance source,
			ClassInstance target) {
		super(parent);
		this.mapEditPart = mapEditPart;
		this.source = source;
		this.target = target;
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
		} catch (TigerstripeException e) {
			throw new RuntimeException("matching IAbstractArtifact not found");
		}
		typeMap.put(buttonLabels[0], associationMap);
		typeMap.put(buttonLabels[1], associationClassMap);
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

	// Bug 951
	public final static java.util.List<AssociationInstanceEditPart> filterAssociationInstanceEditParts(
			java.util.List list) {
		java.util.List<AssociationInstanceEditPart> result = new ArrayList<AssociationInstanceEditPart>();
		for (Object obj : list) {
			if (obj instanceof AssociationInstanceEditPart) {
				result.add((AssociationInstanceEditPart) obj);
			}
		}
		return result;
	}

	private boolean assocInstanceAlreadyDefined(IRelationship rel) {
		// Bug 951
		java.util.List<AssociationInstanceEditPart> existingConnections = filterAssociationInstanceEditParts(mapEditPart
				.getConnections());
		// loop through the existing connections
		for (AssociationInstanceEditPart editPart : existingConnections) {
			if (rel instanceof IAssociationClassArtifact) {
				IAssociationClassArtifact assocClassArt = (IAssociationClassArtifact) rel;
				// if here, then looking for a matching association class
				// instance
				AssociationInstance assocInstance = (AssociationInstance) ((View) editPart
						.getModel()).getElement();
				// if the names don't match, then this isn't the right "type" of
				// association class
				// to match with the instance we are trying to create
				// (regardless of the endpoints)
				if (!assocInstance.getName().equals(assocClassArt.getName()))
					continue;
				// if the aEnd of this instance equals the source of the
				// connection we are trying
				// to create, then might be a match
				if (assocInstance.getAEnd() == source) {
					ClassInstance otherClassInstance = (ClassInstance) assocInstance
							.getZEnd();
					// if the "other class instance" is an association class
					// instance, check
					// the "other connection" that (together with that class
					// instance and this
					// association instance) makes up the association class
					// instance
					if (otherClassInstance.isAssociationClassInstance()) {
						java.util.List<AssociationInstance> assocs = otherClassInstance
								.getAssociations();
						for (AssociationInstance assoc : assocs) {
							// if the "other connection's" zEnd matches the
							// target, then we
							// already have an association class between this
							// source and target
							if (assoc.getZEnd() == target)
								return true;
						}
					}
					// if the zEnd of this instance equals the target of the
					// connection we are trying
					// to create, then might be a match
				} else if (assocInstance.getZEnd() == target) {
					ClassInstance otherClassInstance = (ClassInstance) assocInstance
							.getAEnd();
					// if the "other class instance" is an association class
					// instance, check
					// the "other connection" that (together with that class
					// instance and this
					// association instance) makes up the association class
					// instance
					if (otherClassInstance.isAssociationClassInstance()) {
						java.util.List<AssociationInstance> assocs = otherClassInstance
								.getAssociations();
						for (AssociationInstance assoc : assocs) {
							// if the "other connection's" aEnd matches the
							// source, then we
							// already have an association class between this
							// source and target
							if (assoc.getAEnd() == source)
								return true;
						}
					}
				}
			} else if (rel instanceof IAssociationArtifact) {
				IAssociationArtifact assocArt = (IAssociationArtifact) rel;
				AssociationInstance assocInstance = (AssociationInstance) ((View) editPart
						.getModel()).getElement();
				// if the names don't match, then this isn't the right "type" of
				// association to
				// match with the instance we are trying to create (regardless
				// of the endpoints)
				if (!assocInstance.getName().equals(assocArt.getName()))
					continue;
				// otherwise, if the endpoints of this association instance
				// match with the source
				// and target we are trying to create, then an instance of this
				// association
				// already exists in the instance diagram
				if (assocInstance.getAEnd() == source
						&& assocInstance.getZEnd() == target
						||
						// Bug #910: Since instance diags only have one handle,
						// to check if an association
						// already exists we need to consider both "direction"
						assocInstance.getZEnd() == source
						&& assocInstance.getAEnd() == target)
					return true;
			}
		}
		// if here, then no matching instances were found, so return false
		return false;
	}

	protected void initDialog() {
		getShell().setText("Instance Edit");
		getShell().setMinimumSize(250, 200);

		// put together a list of all of the relationships that can be built
		// with the source's
		// FQN (or the FQN of any of the source's parents) as the source of the
		// relationship
		Set<IRelationship> sourceRelationshipSet = InstanceDiagramUtils
				.getRelationshipSet(artifactMgrSession, sourceArtifact,
						targetArtifact);
		// and put together another list of all of the relationships that can be
		// built with
		// the source's FQN (or the FQN of any of the source's parents) as the
		// target of the
		// relationship
		Set<IRelationship> targetRelationshipSet = InstanceDiagramUtils
				.getRelationshipSet(artifactMgrSession, targetArtifact,
						sourceArtifact);
		if (sourceRelationshipSet.size() <= 0
				&& targetRelationshipSet.size() <= 0)
			throw new OperationCanceledException(
					"No matching IRelationships found between "
							+ source.getArtifactName() + ":"
							+ source.getFullyQualifiedName() + " and "
							+ target.getArtifactName() + ":"
							+ target.getFullyQualifiedName());
		associationMap.clear();
		associationClassMap.clear();
		reversedRelationships.clear();
		java.util.List<Set<IRelationship>> relationshipSets = new ArrayList<Set<IRelationship>>();
		if (sourceRelationshipSet.size() >= 0)
			relationshipSets.add(sourceRelationshipSet);
		if (targetRelationshipSet.size() >= 0)
			relationshipSets.add(targetRelationshipSet);
		for (Set<IRelationship> relationshipSet : relationshipSets) {
			for (IRelationship rel : relationshipSet) {
				// the order of the first two parts of this test are important,
				// since an AssociationClassArtifact is also an
				// AssociationArtifact
				if (rel instanceof IAssociationClassArtifact
						&& !assocInstanceAlreadyDefined(rel)) {
					IAssociationClassArtifact associationClass = (IAssociationClassArtifact) rel;
					if (!associationClass.isAbstract()) {
						associationClassMap.put(associationClass.getName(),
								associationClass);
						if (relationshipSet == targetRelationshipSet)
							reversedRelationships.put(associationClass
									.getName(), associationClass);
					}
				} else if (rel instanceof IAssociationArtifact
						&& !assocInstanceAlreadyDefined(rel)) {
					IAssociationArtifact association = (IAssociationArtifact) rel;
					if (!association.isAbstract()) {
						associationMap.put(association.getName(), association);
						if (relationshipSet == targetRelationshipSet)
							reversedRelationships.put(association.getName(),
									association);
					}
				}
			}
		}

		// load list of instance names from diagram...
		instanceNames.clear();
		for (Object child : mapEditPart.getChildren()) {
			if (child instanceof ClassInstanceEditPart
					|| child instanceof AssociationInstanceEditPart) {
				IGraphicalEditPart graphicalEditPart = (IGraphicalEditPart) child;
				EObject eObj = ((View) graphicalEditPart.getModel())
						.getElement();
				Instance instance = (Instance) eObj;
				instanceNames.add(instance.getArtifactName());
			}
		}

		// if both maps are empty, then there are no new association instances
		// that can
		// be created between the source and target
		if (associationClassMap.size() <= 0 && associationMap.size() <= 0)
			throw new OperationCanceledException(
					"No non-duplicate IRelationships found between "
							+ source.getArtifactName() + ":"
							+ source.getFullyQualifiedName() + " and "
							+ target.getArtifactName() + ":"
							+ target.getFullyQualifiedName());

	}

	private void createInstanceDefinitionControl(Composite composite,
			int nColumns) {
		Group box = new Group(composite, SWT.NULL);
		box.setText("Relationships");
		setFillLayout(box, 2, 10);
		GridLayout bLayout = new GridLayout();
		bLayout.numColumns = 2;
		box.setLayout(bLayout);
		HashMap<String, List> selectionMap = new HashMap<String, List>();
		MySelectionListener selectionListener = new MySelectionListener(
				selectionMap);
		IDoubleClickListener listDoubleClickListener = new MyListDoubleClickListener();
		ISelectionChangedListener listSelectionChangedListener = new MyListSelectionChangedListener();
		// first, add the radio buttons to the surrounding box
		Button button = new Button(box, SWT.RADIO);
		button.setText(buttonLabels[0]);
		button.addSelectionListener(selectionListener);
		setFillLayout(button, 1, 2);
		if (associationMap.keySet().size() <= 0) {
			button.setEnabled(false);
		}
		// create a radio button
		button = new Button(box, SWT.RADIO);
		button.setText(buttonLabels[1]);
		button.addSelectionListener(selectionListener);
		setFillLayout(button, 1, 2);
		if (associationClassMap.keySet().size() <= 0) {
			button.setEnabled(false);
		}
		// next, add the controls that are controlled by the radio buttons;
		// first
		// create a composite to contain the controls for defining associations
		Composite box2 = new Composite(box, SWT.NULL);
		setFillBothLayout(box2, 1, 8);
		bLayout = new GridLayout();
		bLayout.numColumns = 1;
		box2.setLayout(bLayout);
		GridData boxGridData2 = (GridData) box2.getLayoutData();
		boxGridData2.widthHint = 200;
		// and add some controls to that composite
		List list = new List(box2, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER);
		ListViewer viewer = new ListViewer(list);
		viewer.addSelectionChangedListener(listSelectionChangedListener);
		viewer.addDoubleClickListener(listDoubleClickListener);
		// disable the list control for now, will reset this when a radiobutton
		// value is set later...
		list.setEnabled(false);
		setFillBothLayout(list, 1, 8);
		selectionMap.put(buttonLabels[0], list);
		for (String name : associationMap.keySet()) {
			list.add(name);
		}
		// next, create a composite for the controls for creating
		// association classes
		Composite box3 = new Composite(box, SWT.NULL);
		setFillBothLayout(box3, 1, 8);
		bLayout = new GridLayout();
		bLayout.numColumns = 1;
		box3.setLayout(bLayout);
		GridData boxGridData3 = (GridData) box3.getLayoutData();
		boxGridData3.widthHint = 200;
		// in the inner group, place a label and text box for the
		// instance name and a list box for the available association classes
		Label instanceLabel = new Label(box3, SWT.NULL);
		instanceLabel.setText("Instance name");
		setFillLayout(instanceLabel, 1, 1);
		instanceNameField = new Text(box3, SWT.BORDER);
		// disable the instanceName control for now, will reset this when a
		// radiobutton
		// value is set later...
		instanceNameField.setEnabled(false);
		instanceNameField
				.setToolTipText("The name of the instance of this class.");
		instanceNameField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateOkButton(e.getSource());
			}
		});
		setFillLayout(instanceNameField, 1, 1);
		list = new List(box3, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER);
		viewer = new ListViewer(list);
		viewer.addSelectionChangedListener(listSelectionChangedListener);
		viewer.addDoubleClickListener(listDoubleClickListener);
		// disable the list control for now, will reset this when a radiobutton
		// value is set later...
		list.setEnabled(false);
		setFillBothLayout(list, 1, 6);
		selectionMap.put(buttonLabels[1], list);
		for (String name : associationClassMap.keySet()) {
			list.add(name);
		}
	}

	private void internalUpdateOkButton(Button okButton) {
		if (buttonLabels[1].equals(selectedType)) {
			// if here, then it's an association class, need to ensure that a
			// non-empty
			// instance name field has been entered and that a non-empty value
			// has been
			// chosen before we enable the OK button
			instanceName = instanceNameField.getText().trim();
			if (instanceName == null || "".equals(instanceName)
					|| selectedName == null || "".equals(selectedName)) {
				okButton.setEnabled(false);
			} else if (instanceNames.contains(instanceName)) {
				okButton.setEnabled(false);
			} else {
				okButton.setEnabled(true);
			}
		} else {
			// if here, then it's an association, so just need to make
			// sure that a non-empty value has been chosen before we
			// enable the OK button
			if (selectedName == null || "".equals(selectedName)) {
				okButton.setEnabled(false);
			} else {
				okButton.setEnabled(true);
			}
		}
	}

	protected void updateOkButton(Object eventSource) {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		if (okButton == null)
			return;
		if (eventSource == instanceNameField) {
			internalUpdateOkButton(okButton);
		}
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

	private class MyListSelectionChangedListener implements
			ISelectionChangedListener {
		private HashMap<List, String> selectedVals = new HashMap<List, String>();

		public void selectionChanged(SelectionChangedEvent e) {
			Object source = e.getSource();
			if (source instanceof ListViewer) {
				List list = ((ListViewer) source).getList();
				int selIdx = list.getSelectionIndex();
				if (selIdx >= 0) {
					String selVal = list.getItem(selIdx);
					if (selectedVals.keySet().contains(list)
							&& selectedVals.get(list).equals(selVal)) {
						selectedName = "";
						selectedVals.remove(list);
						list.deselectAll();
					} else if (selIdx >= 0) {
						selectedVals.put(list, selVal);
						selectedName = selVal;
					}
				}
				Button okButton = AssociationInstanceEditDialog.this
						.getButton(IDialogConstants.OK_ID);
				AssociationInstanceEditDialog.this
						.internalUpdateOkButton(okButton);
			}
		}
	}

	private class MyListDoubleClickListener implements IDoubleClickListener {
		public void doubleClick(DoubleClickEvent event) {
			// does nothing, since a double click is the same as two clicks (so
			// it should
			// unselect and then select or select and then unselect the item in
			// the list)
		}
	}

	private class MySelectionListener implements SelectionListener {
		HashMap<String, List> selectionMap;
		HashMap<String, String[]> prevSelectionMap = new HashMap<String, String[]>();

		public MySelectionListener(HashMap<String, List> selectionMap) {
			super();
			this.selectionMap = selectionMap;
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			Button source = (Button) e.getSource();
			selectedType = source.getText();
			List list = selectionMap.get(selectedType);
			boolean isSelection = source.getSelection();
			if (!isSelection) {
				// are deselecting this button, need to save the state of the
				// corresponding
				// list's selection so that we can restore it later if the user
				// reselects
				// this radio button
				String[] selVals = list.getSelection();
				if (selVals != null) {
					prevSelectionMap.put(selectedType, selVals);
					selectedName = "";
					list.deselectAll();
				}
				if (buttonLabels[1].equals(selectedType)) {
					instanceNameField.setEnabled(isSelection);
				}
			} else {
				// are selecting this button, so need to restore the previous
				// selection (if any)
				// and update the okButton's status to reflect the state of the
				// new list...
				String[] prevSelVals = prevSelectionMap.get(selectedType);
				if (prevSelVals != null && prevSelVals.length > 0) {
					list.setSelection(prevSelVals);
					selectedName = prevSelVals[0];
				} else {
					selectedName = "";
				}
				Button okButton = AssociationInstanceEditDialog.this
						.getButton(IDialogConstants.OK_ID);
				if (buttonLabels[1].equals(selectedType)) {
					instanceNameField.setEnabled(isSelection);
				}
				// check the length of the list...if there is only one element
				// in the list, then select it
				if (list.getItemCount() == 1) {
					selectedName = list.getItem(0);
					list.select(0);
				}
				internalUpdateOkButton(okButton);
			}
			list.setEnabled(isSelection);
		}
	}

	@Override
	public int open() {
		// if the user has set a preference to "auto create associations
		// where possible" (for now this test is set to always be true...
		// this test should actually be based on a preferences setting)
		if (true) {
			super.create();
			// and if there are no association classes that can be created
			// between the two endpoints and there is only one association
			// that can be created
			if (associationMap.size() == 1 && associationClassMap.size() == 0) {
				// set the selected type to "association", set the single
				// association's name as the selected name, and return the
				// same result that would be return if the user pressed the
				// OK button
				selectedType = buttonLabels[0];
				selectedName = (String) associationMap.keySet().toArray()[0];
				return IDialogConstants.OK_ID;
			}
		}
		// otherwise, open the dialog box so that the user can select
		// the association/association class they'd like to create
		return super.open();
	}

	public IRelationship getSelectedRelationship() {
		Map<String, IRelationship> selectedTypeMap = typeMap.get(selectedType);
		return selectedTypeMap.get(selectedName);
	}

	public String getInstanceName() {
		return instanceName;
	}

	public boolean isReversedInstance() {
		return reversedRelationships.keySet().contains(selectedName);
	}

}
