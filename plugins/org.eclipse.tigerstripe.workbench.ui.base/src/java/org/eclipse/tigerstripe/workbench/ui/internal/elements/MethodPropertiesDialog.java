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
package org.eclipse.tigerstripe.workbench.ui.internal.elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MethodPropertiesDialog extends Dialog {

	private final String[] methodLabels;
	private final Properties[] initialProperties;

	private Properties[] returnProperties;
	private Map buttonMap;

	public Properties getReturnProperties(int index) {
		return returnProperties[index];
	}

	private String[] propertiesLabel = { "Simple", "Bulk\n(Atomic)",
			"Bulk\n(Best-Effort)", "By Template", "By Templates",
			"By Template\n(Best Effort)", "By Templates\n(Best Effort)" };

	private String[] propertiesKeys = { "simple", "bulkAtomic",
			"bulkBestEffort", "byTemplate", "byTemplates",
			"byTemplateBestEffort", "byTemplatesBestEffort" };

	private String[] propertiesDefaultValues = { "true", "false", "false",
			"false", "false", "false", "false" };

	private String targetLabel;
	private String targetText;

	public MethodPropertiesDialog(Shell parentShell, String[] methodLabels,
			Properties[] methodProperties, String targetLabel, String targetText) {
		super(parentShell);

		// Copy the incoming properties
		this.methodLabels = methodLabels;
		this.initialProperties = methodProperties;
		this.buttonMap = new HashMap();
		this.targetLabel = targetLabel;
		this.targetText = targetText;

		this.returnProperties = new Properties[this.initialProperties.length];
		for (int i = 0; i < this.initialProperties.length; i++) {
			if (this.initialProperties[i] == null) {
				this.returnProperties[i] = createDefaultProperties();
			} else {
				this.returnProperties[i] = (Properties) this.initialProperties[i]
						.clone();
			}
		}
	}

	private Properties createDefaultProperties() {
		Properties result = new Properties();

		for (int i = 0; i < this.propertiesKeys.length; i++) {
			result.setProperty(this.propertiesKeys[i],
					this.propertiesDefaultValues[i]);
		}
		return result;
	}

	private Text entityNameField;

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FormLayout());

		Composite container = new Composite(area, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;

		final Group optionsGroupTitle = new Group(container, SWT.NULL);
		optionsGroupTitle.setText(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IManagedEntityArtifactImpl.class.getName()).getLabel(null));
		final GridLayout optionsGridTitleLayout = new GridLayout();
		optionsGridTitleLayout.numColumns = 2;
		optionsGroupTitle.setLayout(optionsGridTitleLayout);
		optionsGroupTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Label entityName = new Label(optionsGroupTitle, SWT.NONE);
		entityName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		entityName.setText(this.targetLabel);

		entityNameField = new Text(optionsGroupTitle, SWT.BORDER);
		entityNameField.setEditable(false);
		entityNameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		entityNameField.setText(this.targetText);

		final Group crudOptionsGroup = new Group(container, SWT.NULL);
		final GridLayout crudOptionsGridLayout = new GridLayout();
		crudOptionsGridLayout.numColumns = this.propertiesLabel.length + 1;
		crudOptionsGridLayout.horizontalSpacing = 10;
		crudOptionsGroup.setLayout(crudOptionsGridLayout);
		crudOptionsGroup.setText("Operations");

		GridData buttonData = new GridData();
		buttonData.horizontalAlignment = SWT.CENTER;
		buttonData.verticalAlignment = SWT.CENTER;
		buttonData.widthHint = 70;

		final Label crudOpNameLabel = new Label(crudOptionsGroup, SWT.RIGHT);
		crudOpNameLabel.setText("Op. Name");

		for (int i = 0; i < this.propertiesLabel.length; i++) {
			final Label opLabel = new Label(crudOptionsGroup, SWT.CENTER);
			opLabel.setText(this.propertiesLabel[i]);
			opLabel.setLayoutData(buttonData);
		}

		for (int i = 0; i < this.methodLabels.length; i++) {
			final Label opName = new Label(crudOptionsGroup, SWT.RIGHT);
			opName.setText(this.methodLabels[i]);

			for (int j = 0; j < this.propertiesLabel.length; j++) {
				final Button button = new Button(crudOptionsGroup, SWT.CHECK);
				button.setText("");
				GridData data = new GridData();
				data.horizontalAlignment = SWT.CENTER + SWT.FILL;
				data.verticalAlignment = SWT.CENTER;
				data.horizontalIndent = 30;
				button.setLayoutData(data);
				button.setSelection("true".equals(this.returnProperties[i]
						.getProperty(this.propertiesKeys[j])));
				buttonMap.put(button, new int[] { i, j });
				button.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						buttonSelected(e);
					}
				});
			}
		}

		initContent();

		return area;
	}

	private void buttonSelected(SelectionEvent e) {
		Button targetButton = (Button) e.widget;

		int coord[] = (int[]) this.buttonMap.get(targetButton);
		this.returnProperties[coord[0]].setProperty(propertiesKeys[coord[1]],
				String.valueOf(targetButton.getSelection()));
	}

	private void initContent() {

	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IManagedEntityArtifactImpl.class.getName()).getLabel(null)
				+ " Options");
	}
}