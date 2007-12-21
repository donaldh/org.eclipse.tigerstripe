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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.core.model.Method;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EntityOptionsCompleteDialog extends Dialog {

	private List methodOptionsList;

	public List getMethodOptions() {
		return this.methodOptionsList;
	}

	public class methodOptions {

		private String name;
		private Method method;

		private Button simpleButton;
		private Button bulkAtomicButton;
		private Button bulkBestEffortButton;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public boolean isCustomMethod() {
			return this.method != null;
		}

		public Method getMethod() {
			return this.method;
		}

		public void setSimpleButton(Button simpleButton) {
			this.simpleButton = simpleButton;
		}

		public void setBulkAtomicButton(Button bulkAtomicButton) {
			this.bulkAtomicButton = bulkAtomicButton;
		}

		public void setBulkBestEffortButton(Button bulkBestEffortButton) {
			this.bulkBestEffortButton = bulkBestEffortButton;
		}

		public boolean getSimple() {
			return simpleButton.getSelection();
		}

		public boolean getBulkAtomic() {
			return bulkAtomicButton.getSelection();
		}

		public boolean getBulkBestEffort() {
			return bulkBestEffortButton.getSelection();
		}

	}

	private EntitySelectorWizardPage.EntityOption entityOption;
	private Text entityNameField;

	public EntityOptionsCompleteDialog(Shell parentShell,
			EntitySelectorWizardPage.EntityOption entityOption) {
		super(parentShell);

		this.entityOption = entityOption;
		this.methodOptionsList = new ArrayList();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FormLayout());

		Composite container = new Composite(area, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;

		final Group optionsGroupTitle = new Group(container, SWT.NULL);
		optionsGroupTitle.setText("Managed Entity");
		final GridLayout optionsGridTitleLayout = new GridLayout();
		optionsGridTitleLayout.numColumns = 2;
		optionsGroupTitle.setLayout(optionsGridTitleLayout);
		optionsGroupTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Label entityName = new Label(optionsGroupTitle, SWT.NONE);
		entityName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		entityName.setText("Entity Name");

		entityNameField = new Text(optionsGroupTitle, SWT.BORDER);
		entityNameField.setEditable(false);
		entityNameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Group crudOptionsGroup = new Group(container, SWT.NULL);
		final GridLayout crudOptionsGridLayout = new GridLayout();
		crudOptionsGridLayout.numColumns = 4;
		crudOptionsGridLayout.horizontalSpacing = 10;
		crudOptionsGroup.setLayout(crudOptionsGridLayout);
		crudOptionsGroup.setText("CRUD Operations");

		GridData buttonData = new GridData();
		buttonData.horizontalAlignment = SWT.CENTER;
		buttonData.verticalAlignment = SWT.CENTER;
		buttonData.widthHint = 80;

		final Label crudOpNameLabel = new Label(crudOptionsGroup, SWT.RIGHT);
		crudOpNameLabel.setText("Op. Name");

		final Label crudOpSimpleLabel = new Label(crudOptionsGroup, SWT.CENTER);
		crudOpSimpleLabel.setText("Simple");
		crudOpSimpleLabel.setLayoutData(buttonData);

		final Label crudOpBulkALabel = new Label(crudOptionsGroup, SWT.CENTER);
		crudOpBulkALabel.setText("Bulk Atomic");
		crudOpBulkALabel.setLayoutData(buttonData);

		final Label crudOpBulkBELabel = new Label(crudOptionsGroup, SWT.CENTER);
		crudOpBulkBELabel.setText("Bulk Best-Effort");
		crudOpBulkBELabel.setLayoutData(buttonData);

		String[] opNames = { "Create", "Read", "Update", "Delete" };

		for (int i = 0; i < opNames.length; i++) {
			final Label opName = new Label(crudOptionsGroup, SWT.RIGHT);
			opName.setText(opNames[i]);

			final Button simpleButton = new Button(crudOptionsGroup, SWT.CHECK);
			simpleButton.setText("");
			simpleButton.setLayoutData(new GridData(SWT.CENTER));

			final Button bulkAButton = new Button(crudOptionsGroup, SWT.CHECK);
			bulkAButton.setLayoutData(buttonData);

			final Button bulkBEButton = new Button(crudOptionsGroup, SWT.CHECK);
			bulkBEButton.setLayoutData(buttonData);

			methodOptions methodOp = new methodOptions();
			methodOp.setSimpleButton(simpleButton);
			methodOp.setBulkAtomicButton(bulkAButton);
			methodOp.setBulkBestEffortButton(bulkBEButton);
			methodOp.setName(opNames[i]);
			this.methodOptionsList.add(methodOp);
		}

		List methods = (List) this.entityOption.managedEntityArtifact
				.getMethods();

		if (methods.size() > 0) {
			final Group customOptionsGroup = new Group(container, SWT.NULL);
			final GridLayout customOptionsGridLayout = new GridLayout();
			customOptionsGridLayout.numColumns = 4;
			customOptionsGridLayout.horizontalSpacing = 10;
			customOptionsGroup.setLayout(customOptionsGridLayout);
			customOptionsGroup.setText("Custom Operations");

			final Label customOpNameLabel = new Label(customOptionsGroup,
					SWT.RIGHT);
			customOpNameLabel.setText("Op. Name");

			final Label customOpSimpleLabel = new Label(customOptionsGroup,
					SWT.CENTER);
			customOpSimpleLabel.setText("Simple");
			customOpSimpleLabel.setLayoutData(buttonData);

			final Label customOpBulkALabel = new Label(customOptionsGroup,
					SWT.CENTER);
			customOpBulkALabel.setText("Bulk Atomic");
			customOpBulkALabel.setLayoutData(buttonData);

			final Label customOpBulkBELabel = new Label(customOptionsGroup,
					SWT.CENTER);
			customOpBulkBELabel.setText("Bulk Best-Effort");
			customOpBulkBELabel.setLayoutData(buttonData);

			for (Iterator methIter = methods.iterator(); methIter.hasNext();) {
				Method method = (Method) methIter.next();
				String name = method.getName();

				final Label opName = new Label(customOptionsGroup, SWT.RIGHT);
				opName.setText(name);

				final Button simpleButton = new Button(customOptionsGroup,
						SWT.CHECK);
				simpleButton.setText("");
				simpleButton.setLayoutData(new GridData(SWT.CENTER));

				final Button bulkAButton = new Button(customOptionsGroup,
						SWT.CHECK);
				bulkAButton.setLayoutData(buttonData);

				final Button bulkBEButton = new Button(customOptionsGroup,
						SWT.CHECK);
				bulkBEButton.setLayoutData(buttonData);

				methodOptions methodOp = new methodOptions();
				methodOp.setSimpleButton(simpleButton);
				methodOp.setBulkAtomicButton(bulkAButton);
				methodOp.setBulkBestEffortButton(bulkBEButton);
				methodOp.setMethod(method);
				this.methodOptionsList.add(methodOp);
			}
		}

		final Group optionsGroup = new Group(container, SWT.NULL);
		final GridLayout optionsGridLayout = new GridLayout();
		optionsGridLayout.numColumns = 1;
		optionsGroup.setLayout(optionsGridLayout);
		optionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		optionsGroup.setText("Options");

		Composite text = new Composite(optionsGroup, SWT.NULL);
		GridLayout textLayout = new GridLayout();
		text.setLayout(textLayout);
		textLayout.numColumns = 1;

		final Label selectExplanation = new Label(text, SWT.NULL);
		selectExplanation
				.setText("Select the appropriate Entity Options below:");
		selectExplanation.setLayoutData(new GridData(GridData.BEGINNING));

		Composite options = new Composite(optionsGroup, SWT.NULL);
		GridLayout optionsLayout = new GridLayout();
		options.setLayout(optionsLayout);
		optionsLayout.numColumns = 2;

		createOptionsCheckBoxes(options);

		initContent();

		return area;
	}

	private void createOptionsCheckBoxes(Composite parent) {
		String[] labels = this.entityOption.getLabels();

		for (int i = 0; i < labels.length; i++) {
			final String label = labels[i];
			final Button button = new Button(parent, SWT.CHECK);
			button.setText(label);
			button.setSelection("true".equals(entityOption.getProperties()
					.getProperty(label)));
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					entityOption.getProperties().setProperty(label,
							String.valueOf(button.getSelection()));
				}
			});
		}
	}

	private void initContent() {
		entityNameField.setText(this.entityOption.getFullyQualifiedName());
	}

	protected void configureSehll(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Managed Entity Options");
	}
}