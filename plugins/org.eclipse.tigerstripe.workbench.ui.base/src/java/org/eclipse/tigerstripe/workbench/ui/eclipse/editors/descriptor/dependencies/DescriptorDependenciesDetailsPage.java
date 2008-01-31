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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.dependencies;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.core.module.InvalidModuleException;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class DescriptorDependenciesDetailsPage implements IDetailsPage {

	private IManagedForm form;

	private DescriptorDependenciesSection master;

	private IDependency dependency;

	public DescriptorDependenciesDetailsPage() {
		super();
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		createFieldInfo(parent);

		form.getToolkit().paintBordersFor(parent);
	}

	// ============================================================
	private void setDependency(IDependency dependency) {
		this.dependency = dependency;
	}

	private IDependency getDependency() {
		return dependency;
	}

	// ============================================================
	private Text moduleId;

	private Text projectVersion;

	private Text projectDescription;

	private Text projectName;

	private Text moduleBuilder;

	private Text packagedDate;

	private void createFieldInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 2;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Module ID: ");
		moduleId = toolkit.createText(sectionClient, "");
		moduleId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		moduleId.setEditable(false);
		moduleId.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Builder: ");
		moduleBuilder = toolkit.createText(sectionClient, "");
		moduleBuilder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		moduleBuilder.setEditable(false);
		moduleBuilder.setEnabled(false);

		label = toolkit.createLabel(sectionClient, "Packaged Date: ");
		packagedDate = toolkit.createText(sectionClient, "");
		packagedDate.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		packagedDate.setEditable(false);
		packagedDate.setEnabled(false);

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
		projectDescription = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI);
		projectDescription
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectDescription.setEditable(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 70;
		projectDescription.setLayoutData(gd);
		projectDescription.setEnabled(false);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
	}

	// ===================================================================

	public void initialize(IManagedForm form) {
		this.form = form;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	protected void pageModified() {
		master.markPageModified();
	}

	public boolean isDirty() {
		return master.isDirty();
	}

	public void commit(boolean onSave) {
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void setFocus() {
		moduleId.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof DescriptorDependenciesSection) {
			master = (DescriptorDependenciesSection) part;
			Table fieldsTable = master.getViewer().getTable();

			IDependency selected = (IDependency) fieldsTable.getSelection()[0]
					.getData();
			setDependency(selected);
			updateForm();
		}
	}

	private void updateForm() {
		IDependency dependency = getDependency();
		try {
			IModuleHeader header = dependency.parseIModuleHeader();
			IProjectDetails details = dependency.parseIProjectDetails();

			String id = header.getModuleID();
			moduleId.setText((id != null) ? id : "<unknown>");

			String builder = header.getBuild();
			moduleBuilder.setText((builder != null) ? builder : "<unknown>");

			Date date = header.getDate();
			String dateStr = "<unknown>";
			if (date != null) {
				DateFormat format = new SimpleDateFormat("yyyy.MM.dd-hh.mm");
				dateStr = format.format(date);
			}
			packagedDate.setText(dateStr);

			String name = details.getName();
			projectName.setText((name != null) ? name : "<unknown>");

			String version = details.getVersion();
			projectVersion.setText((version != null) ? version : "<unknown>");

			String desc = details.getDescription();
			projectDescription.setText((desc != null) ? desc : "<unknown>");
		} catch (InvalidModuleException e) {
			EclipsePlugin.log(e);
		}
	}
}
