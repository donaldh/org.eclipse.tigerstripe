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

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EntityOptionsDialog extends Dialog {

	private EntitySelectorWizardPage.EntityOption entityOption;
	private Text entityNameField;

	public EntityOptionsDialog(Shell parentShell,
			EntitySelectorWizardPage.EntityOption entityOption) {
		super(parentShell);

		this.entityOption = entityOption;
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