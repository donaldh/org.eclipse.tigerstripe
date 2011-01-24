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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.TSMessageDialog;

public class AssociationInstanceEditDialog extends TSMessageDialog {

	private IArtifactManagerSession artifactMgrSession;
	private final InstanceMapEditPart mapEditPart;
	private final DiagramGraphicalViewer mapViewer;
	private final ClassInstance source;
	private IAbstractArtifact sourceArtifact;
	private final ClassInstance target;
	private IAbstractArtifact targetArtifact;

	private final Map<String, IRelationship> reversedRelationships = new HashMap<String, IRelationship>();
	private final Map<String, IRelationship> associationMap = new HashMap<String, IRelationship>();
	private final Map<String, IRelationship> associationClassMap = new HashMap<String, IRelationship>();

	private final String[] buttonLabels = new String[] { "Associations",
			"Association Classes" };
	private final Map<String, Map> typeMap = new HashMap<String, Map>();
	private final Set<String> instanceNames = new HashSet<String>();

	private Text instanceNameField;
	private String selectedName;
	private String selectedType;
	private String instanceName;

	private Combo aEndCombo;
	private Combo zEndCombo;
	private String selectedAEnd;
	private String selectedZEnd;

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
		IAbstractTigerstripeProject aProject = (IAbstractTigerstripeProject) res
				.getProject().getAdapter(IAbstractTigerstripeProject.class);
		if (!(aProject instanceof ITigerstripeModelProject))
			throw new RuntimeException("non-Tigerstripe Project found");
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
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
					// if the aEnd of this instance equals the target of the
					// connection we are trying
					// to create, then might be a match
				} else if (assocInstance.getAEnd() == target) {
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
							// source, then we
							// already have an association class between this
							// source and target
							if (assoc.getZEnd() == source)
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
						if (relationshipSet == targetRelationshipSet
								&& !associationClass
										.getAEnd()
										.getType()
										.getFullyQualifiedName()
										.equals(associationClass.getZEnd()
												.getType()
												.getFullyQualifiedName())) // bug
																			// 288718)
							reversedRelationships.put(
									associationClass.getName(),
									associationClass);
					}
				} else if (rel instanceof IAssociationArtifact
						&& !assocInstanceAlreadyDefined(rel)) {
					IAssociationArtifact association = (IAssociationArtifact) rel;
					if (!association.isAbstract()) {
						associationMap.put(association.getName(), association);
						if (relationshipSet == targetRelationshipSet
								&& !association
										.getAEnd()
										.getType()
										.getFullyQualifiedName()
										.equals(association.getZEnd().getType()
												.getFullyQualifiedName())) // bug
																			// 288718
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
		Button assocButton = new Button(box, SWT.RADIO);
		assocButton.setText(buttonLabels[0]);
		assocButton.addSelectionListener(selectionListener);
		setFillLayout(assocButton, 1, 2);
		if (associationMap.keySet().size() <= 0) {
			assocButton.setEnabled(false);
		}
		// create a radio button
		Button assocClassButton = new Button(box, SWT.RADIO);
		assocClassButton.setText(buttonLabels[1]);
		assocClassButton.addSelectionListener(selectionListener);
		setFillLayout(assocClassButton, 1, 2);
		if (associationClassMap.keySet().size() <= 0) {
			assocClassButton.setEnabled(false);
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

		// bug 222176
		createAssociationEndsComposite(box2, viewer);

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

	private Composite createAssociationEndsComposite(Composite parent,
			ListViewer viewer) {
		final Composite endsComposite = new Composite(parent, SWT.NONE);
		endsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		endsComposite.setLayout(layout);

		Label aEndLabel = new Label(endsComposite, SWT.NONE);
		aEndLabel.setText("aEnd: ");
		final Text aEndName = new Text(endsComposite, SWT.NONE);
		aEndName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		aEndName.setEditable(false);
		aEndCombo = new Combo(endsComposite, SWT.NONE | SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		aEndCombo.setLayoutData(gridData);

		Label zEndLabel = new Label(endsComposite, SWT.NONE);
		zEndLabel.setText("zEnd: ");
		final Text zEndName = new Text(endsComposite, SWT.NONE);
		zEndName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		zEndName.setEditable(false);
		zEndCombo = new Combo(endsComposite, SWT.NONE | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		zEndCombo.setLayoutData(gridData);

		aEndCombo.addSelectionListener(new AssocEndComboSelectionAdapter());
		zEndCombo.addSelectionListener(new AssocEndComboSelectionAdapter());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSource() instanceof ListViewer) {
					List list = ((ListViewer) event.getSource()).getList();
					int selIdx = list.getSelectionIndex();
					aEndCombo.removeAll();
					zEndCombo.removeAll();
					if (selIdx >= 0) {
						String assocName = list.getItem(selIdx);
						IAssociationArtifact assoc = (IAssociationArtifact) associationMap
								.get(assocName);
						if (isEndsSelectable(assoc)) {
							aEndName.setText(assoc.getAEnd().getName());
							zEndName.setText(assoc.getZEnd().getName());

							String[] comboVals = new String[] {
									source.getInstanceName(),
									target.getInstanceName() };
							setEnabledRecursive(endsComposite, true);
							for (String comboVal : comboVals) {
								aEndCombo.add(comboVal);
								zEndCombo.add(comboVal);
							}
							aEndCombo.select(0);
							zEndCombo.select(1);
							assocEndsUpdated();
							return;
						}
					}
					aEndName.setText("");
					zEndName.setText("");
					setEnabledRecursive(endsComposite, false);
				}
			}
		});
		setEnabledRecursive(endsComposite, false);

		return endsComposite;
	}

	private class AssocEndComboSelectionAdapter extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Combo combo = ((Combo) e.getSource());
			if (combo == aEndCombo && !selectedAEnd.equals(combo.getText())) {
				zEndCombo.select(1 - combo.getSelectionIndex());
			} else if (combo == zEndCombo
					&& !selectedZEnd.equals(combo.getText())) {
				aEndCombo.select(1 - combo.getSelectionIndex());
			}
			assocEndsUpdated();
			super.widgetSelected(e);
		}
	}

	private void assocEndsUpdated() {
		selectedAEnd = aEndCombo.getText();
		selectedZEnd = zEndCombo.getText();
	}

	private void setEnabledRecursive(Control control, boolean enabled) {
		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			for (Control child : composite.getChildren()) {
				setEnabledRecursive(child, enabled);
			}
		}
		control.setEnabled(enabled);
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
		private final HashMap<List, String> selectedVals = new HashMap<List, String>();

		public void selectionChanged(SelectionChangedEvent e) {
			Object source = e.getSource();
			if (source instanceof ListViewer) {
				List list = ((ListViewer) source).getList();
				int selIdx = list.getSelectionIndex();
				if (selIdx >= 0) {
					String selVal = list.getItem(selIdx);
					selectedVals.put(list, selVal);
					selectedName = selVal;
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
					list.notifyListeners(SWT.Selection, new Event());
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
					list.notifyListeners(SWT.Selection, new Event());
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
					list.notifyListeners(SWT.Selection, new Event());
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
			// between the two endpoints and there is only one non self-related
			// association that can be created
			if (associationMap.size() == 1) {
				IAssociationArtifact assoc = (IAssociationArtifact) associationMap
						.values().iterator().next();
				if (!isEndsSelectable(assoc) && associationClassMap.size() == 0) {
					// set the selected type to "association", set the single
					// association's name as the selected name, and return the
					// same result that would be return if the user pressed the
					// OK button
					selectedType = buttonLabels[0];
					selectedName = (String) associationMap.keySet().toArray()[0];
					return IDialogConstants.OK_ID;
				}
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
		IRelationship rel = getSelectedRelationship();
		if (rel instanceof IAssociationArtifact) {
			IAssociationArtifact assoc = (IAssociationArtifact) rel;
			if (isEndsSelectable(assoc)
					&& !source.getInstanceName().equals(selectedAEnd)) {
				return true;
			}
		}
		return reversedRelationships.keySet().contains(selectedName);
	}

	private boolean isEndsSelectable(IAssociationArtifact assoc) {
		return assoc.getAEnd().getType().getFullyQualifiedName()
				.equals(assoc.getZEnd().getType().getFullyQualifiedName());
	}
}
