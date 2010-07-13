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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.RegisterPluggableHousingAction;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.UnRegisterPluggableHousingAction;

public class PluginsControlDialog extends AbstractDialog {

	public PluginsControlDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	public PluginsControlDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	@Override
	public void populateTable() {
		PluginManager mgr = PluginManager.getManager();
		List<PluggableHousing> pluggableHousings = mgr.getRegisteredPluggableHousings();

		fListTable.removeAll();

		if (fDescriptionText != null)
			fDescriptionText.setText("");

		for (PluggableHousing housing : pluggableHousings) {
			TableItem item = new TableItem(fListTable, SWT.NULL);
			item.setText(new String[] { housing.getPluginId(),
					housing.getVersion(), housing.isDeployed()?"Deploy":"Contrib" ,housing.getGroupId(), housing.getPluginNature().name() });
		}
	}

	@Override
	protected void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager m) {
				fillContextMenu(m);
			}
		});

		Menu menu = menuMgr.createContextMenu(fListTable);
		fListTable.setMenu(menu);

	}

	private void fillContextMenu(IMenuManager menuMgr) {
		boolean isEmpty = fListTable.getSelection().length == 0;
		menuMgr.add(new RegisterPluggableHousingAction(this));

		if (!isEmpty) {
			
			menuMgr.add(new UnRegisterPluggableHousingAction(fListTable
					.getSelectionIndex(), this));
		}
	}
	
	@Override
	protected String getDialogTitle() {
		return "Deployed Tigerstripe Generators";
	}
	
	@Override
	protected String getItemDescription(int index) {
		PluginManager mgr = PluginManager.getManager();

		List<PluggableHousing> pluggableHousings = mgr.getRegisteredPluggableHousings();
		
		return pluggableHousings.get(index).getBody().getDescription();
	}

	@Override
	protected void addColumns() {
		TableColumn nameColumn = new TableColumn(fListTable, SWT.NULL);
		nameColumn.setText("Name");
		nameColumn.setWidth(100);

		TableColumn versionColumn = new TableColumn(fListTable, SWT.NULL);
		versionColumn.setText("Version");
		versionColumn.setWidth(70);
		
		TableColumn typeColumn = new TableColumn(fListTable, SWT.NULL);
		typeColumn.setText("Type");
		typeColumn.setWidth(50);
		
		TableColumn providerColumn = new TableColumn(fListTable, SWT.NULL);
		providerColumn.setText("Provider");
		providerColumn.setWidth(150);

		TableColumn natureColumn = new TableColumn(fListTable, SWT.NULL);
		natureColumn.setText("Nature");
		natureColumn.setWidth(20);
	}
}
