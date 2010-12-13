/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.components.md.Details;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class ReferenceDetails extends Details {

	private final Composite externalContainer;

	private ModelReference reference;

	private Text moduleId;

	private Text projectDescription;

	private Text projectName;

	private Text projectVersion;

	private final FormToolkit toolkit;

	public ReferenceDetails(FormToolkit toolkit, Composite externalContainer) {
		this.toolkit = toolkit;
		this.externalContainer = externalContainer;
	}

	@Override
	protected void createContents(Composite parent) {
		createFieldInfo(parent);
		toolkit.paintBordersFor(parent);
	}

	private void createFieldInfo(Composite parent) {

		Section section = TigerstripeLayoutFactory.createSection(parent,
				toolkit, ExpandableComposite.TITLE_BAR | SWT.BORDER,
				"Reference Details", null);
		GridLayoutFactory.fillDefaults().applyTo(section);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(section);

		Composite sectionClient = toolkit.createComposite(section, SWT.NONE);

		GridLayoutFactory.fillDefaults().margins(3, 3).numColumns(2)
				.applyTo(sectionClient);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(sectionClient);

		Label label = toolkit.createLabel(sectionClient, "Module ID: ");
		moduleId = toolkit.createText(sectionClient, "");
		moduleId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		moduleId.setEditable(false);
		moduleId.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Project Name: ");
		projectName = toolkit.createText(sectionClient, "");
		projectName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectName.setEditable(false);
		projectName.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Version: ");
		projectVersion = toolkit.createText(sectionClient, "");
		projectVersion.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectVersion.setEditable(false);
		projectVersion.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Description: ");
		label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		projectDescription = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 100;
		projectDescription.setLayoutData(gd);
		projectDescription.setEditable(false);
		projectDescription.setEnabled(false);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
	}

	@Override
	protected Composite getExternalContainer() {
		return externalContainer;
	}

	public void switchTarget(Object target) {
		reference = (ModelReference) target;
		updateForm();
	}

	private void updateForm() {
		if (reference == null) {
			return;
		}

		String id = reference.getToModelId();
		moduleId.setText((id != null) ? id : "<unknown>");

		String name = reference.getResolvedModel().getName();
		projectName.setText((name != null) ? name : "<unknown>");

		try {
			IProjectDetails details = reference.getResolvedModel()
					.getProjectDetails();

			String version = details.getVersion();
			projectVersion.setText((version != null) ? version : "<unknown>");

			String desc = details.getDescription();
			projectDescription.setText((desc != null) ? desc : "<unknown>");
		} catch (TigerstripeException e) {
			final String errMsg = "Error while getting the data";
			projectVersion.setText(errMsg);
			projectDescription.setText(errMsg);
			EclipsePlugin.log(e);
		}

	}
}
