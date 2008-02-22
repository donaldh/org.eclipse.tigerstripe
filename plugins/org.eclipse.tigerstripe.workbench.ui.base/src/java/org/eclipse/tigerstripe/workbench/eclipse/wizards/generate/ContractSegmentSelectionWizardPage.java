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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.generate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

public class ContractSegmentSelectionWizardPage extends WizardPage {

	private final static String PAGE_NAME = "Optional Contract Segments";

	private List<IContractSegment> segments;

	private List<IContractSegment> checkedSegments = new ArrayList<IContractSegment>();

	public ContractSegmentSelectionWizardPage(List<IContractSegment> segments,
			String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.segments = segments;

		setTitle("Generation Scope");
		setDescription("Reduce the scope of the generation to a set of Contract Segments.");
	}

	public ContractSegmentSelectionWizardPage(List<IContractSegment> segments) {
		super(PAGE_NAME);
		this.segments = segments;

		setTitle("Generation Scope");
		setDescription("Reduce the scope of the generation to a set of Contract Segments.");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		Group projectLocation = new Group(container, SWT.NULL);
		projectLocation.setText("Available Contract Segments");
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		projectLocation.setLayout(gridLayout);
		final GridData locationGridData = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		locationGridData.horizontalSpan = 2;
		locationGridData.verticalSpan = 4;
		projectLocation.setLayoutData(locationGridData);

		GridData data = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL);

		final CheckboxTreeViewer ctv = new CheckboxTreeViewer(projectLocation);
		ctv.getControl().setLayoutData(data);
		ctv.setContentProvider(new ITreeContentProvider() {
			public Object[] getChildren(Object parentElement) {
				return new Object[0];
			}

			public Object getParent(Object element) {
				return null;
			}

			public boolean hasChildren(Object element) {
				return false;
			}

			public Object[] getElements(Object inputElement) {
				List<IContractSegment> res = (List<IContractSegment>) inputElement;
				return res.toArray();
			}

			public void dispose() {

			}

			public void inputChanged(Viewer viewer, Object oldObject,
					Object newInput) {

			}
		});

		final LabelProvider labelProvider = new LabelProvider() {

			@Override
			public String getText(Object element) {
				IContractSegment segment = (IContractSegment) element;
				return segment.getName();
			}

			@Override
			public Image getImage(Object element) {
				return Images.get(Images.CONTRACTSEGMENT_ICON);
			}
		};
		ctv.setLabelProvider(labelProvider);
		ctv.setInput(segments);

		ctv.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent se) {
				IStructuredSelection iss = (IStructuredSelection) ctv
						.getSelection();
				if (iss.getFirstElement() != null) {
					IContractSegment selected = (IContractSegment) iss
							.getFirstElement();
					displaySegmentDescription(selected);
				}
			}
		});
		ctv.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent csce) {
				IContractSegment res = (IContractSegment) csce.getElement();
				if (csce.getChecked()) {
					if (!checkedSegments.contains(res)) {
						checkedSegments.add(res);
					}
				} else {
					checkedSegments.remove(res);
				}
			}
		});

		createSegmentValuesGroup(container);
		setControl(container);

	}

	private Label segmentDetailsNameLabel;

	private Text segmentDetailsNameText;

	private Label segmentDetailsVersionLabel;

	private Text segmentDetailsVersionText;

	private Label segmentDetailsDescLabel;

	private Text segmentDetailsDescText;

	public void createSegmentValuesGroup(Composite parent) {
		Group moduleGroup = new Group(parent, SWT.NULL);
		moduleGroup.setText("");
		final GridLayout natureGridLayout = new GridLayout();
		natureGridLayout.numColumns = 2;
		moduleGroup.setLayout(natureGridLayout);
		final GridData natureGridData = new GridData(GridData.FILL_HORIZONTAL);
		natureGridData.horizontalSpan = 2;
		moduleGroup.setLayoutData(natureGridData);

		segmentDetailsNameLabel = new Label(moduleGroup, SWT.NONE);
		segmentDetailsNameLabel.setLayoutData(new GridData(GridData.BEGINNING));
		segmentDetailsNameLabel.setText("Name :");

		segmentDetailsNameText = new Text(moduleGroup, SWT.BORDER);
		segmentDetailsNameText.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		segmentDetailsNameText.setText("");
		segmentDetailsNameText.setEditable(false);

		segmentDetailsVersionLabel = new Label(moduleGroup, SWT.NONE);
		segmentDetailsVersionLabel.setLayoutData(new GridData(
				GridData.BEGINNING));
		segmentDetailsVersionLabel.setText("Version :");

		segmentDetailsVersionText = new Text(moduleGroup, SWT.BORDER);
		segmentDetailsVersionText.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		segmentDetailsVersionText.setText("");
		segmentDetailsVersionText.setEditable(false);

		segmentDetailsDescLabel = new Label(moduleGroup, SWT.NONE);
		segmentDetailsDescLabel.setLayoutData(new GridData(GridData.BEGINNING));
		segmentDetailsDescLabel.setText("Description :");

		segmentDetailsDescText = new Text(moduleGroup, SWT.BORDER | SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 50;
		segmentDetailsDescText.setLayoutData(gd);
		segmentDetailsDescText.setText("");
		segmentDetailsDescText.setEditable(false);
	}

	private void displaySegmentDescription(IContractSegment segment) {
		segmentDetailsNameText.setText(segment.getName());
		segmentDetailsVersionText.setText(segment.getVersion());
		segmentDetailsDescText.setText(segment.getDescription());
	}

	public List<IContractSegment> getSelectedContractSegments() {
		return checkedSegments;
	}
}
