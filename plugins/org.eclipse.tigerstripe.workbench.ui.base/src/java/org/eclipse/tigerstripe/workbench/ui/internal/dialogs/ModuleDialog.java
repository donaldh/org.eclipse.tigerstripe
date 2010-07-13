/*******************************************************************************
 * Copyright (c) 2010 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial Contribution:
 *    Navid Mehregani (Cisco Systems, Inc.)
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModule;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModuleManager;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleRef;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;


public class ModuleDialog extends AbstractDialog {

	public ModuleDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	public ModuleDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}
	
	@Override
	protected void createContextMenu() {
		// Not used		
	}

	@Override
	protected String getDialogTitle() {
		return "Installed Tigerstripe Modules";
	}

	@Override
	protected String getItemDescription(int index) {
		InstalledModuleManager installedModuleManager = InstalledModuleManager.getInstance();
		InstalledModule[] moduleList = installedModuleManager.getModules();
		
		return moduleList[index].getModule().getProjectDetails().getDescription();
	
	}

	@Override
	public void populateTable() {
		InstalledModuleManager installedModuleManager = InstalledModuleManager.getInstance();
		InstalledModule[] moduleList = installedModuleManager.getModules();
		
		fListTable.removeAll();

		if (fDescriptionText != null)
			fDescriptionText.setText("");

		for (InstalledModule module : moduleList) {
			TableItem item = new TableItem(fListTable, SWT.NULL);
			
			String modelId = "";
			
			ModuleRef moduleRef = module.getModule();
			ProjectDetails projectDetail = moduleRef.getProjectDetails();
			
			try {
				ITigerstripeModuleProject installedModule = module.makeModuleProject();
				modelId = installedModule.getModelId();
				
			} catch (TigerstripeException e) {
				// Ignore
			}
			
			item.setText(new String[] { modelId, projectDetail.getVersion(),module.getPath().toString() });
		}
	}

	@Override
	protected void addColumns() {
		
		TableColumn providerColumn = new TableColumn(fListTable, SWT.NULL);
		providerColumn.setText("Model ID");
		providerColumn.setWidth(200);
		
		TableColumn versionColumn = new TableColumn(fListTable, SWT.NULL);
		versionColumn.setText("Version");
		versionColumn.setWidth(100);

		TableColumn pluginColumn = new TableColumn(fListTable, SWT.NULL);
		pluginColumn.setText("Installed Location");
		pluginColumn.setWidth(200);
		
	}
	
}