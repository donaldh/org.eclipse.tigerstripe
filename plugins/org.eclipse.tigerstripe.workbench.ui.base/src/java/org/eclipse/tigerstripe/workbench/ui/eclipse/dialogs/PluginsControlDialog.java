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
package org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs;

import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.ui.eclipse.actions.RegisterPluggableHousingAction;
import org.eclipse.tigerstripe.workbench.ui.eclipse.actions.UnRegisterPluggableHousingAction;

public class PluginsControlDialog extends Dialog {

	private Table pluginListTable;

	private Text descriptionText;

	public PluginsControlDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	public PluginsControlDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		pluginListTable = new Table(area, SWT.FULL_SELECTION | SWT.SINGLE
				| SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.heightHint = 150;
		gd.widthHint = 350;
		pluginListTable.setLayoutData(gd);

		pluginListTable.setHeaderVisible(true);
		pluginListTable.setLinesVisible(true);
		pluginListTable.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				int index = pluginListTable.getSelectionIndex();

				if (descriptionText != null) {
					PluginManager mgr = PluginManager.getManager();

					List<PluggableHousing> pluggableHousings = mgr
							.getRegisteredPluggableHousings();
					descriptionText.setText(pluggableHousings.get(index)
							.getBody().getDescription());
				}
			}
		});

		TableColumn nameColumn = new TableColumn(pluginListTable, SWT.NULL);
		nameColumn.setText("Name");
		nameColumn.setWidth(100);

		TableColumn versionColumn = new TableColumn(pluginListTable, SWT.NULL);
		versionColumn.setText("Version");
		versionColumn.setWidth(100);

		TableColumn providerColumn = new TableColumn(pluginListTable, SWT.NULL);
		providerColumn.setText("Provider");
		providerColumn.setWidth(150);

		populateTable();

		pluginListTable.pack();

		Group descriptionGroup = new Group(area, SWT.V_SCROLL);
		descriptionGroup.setText("Description");
		descriptionGroup.setLayout(new FillLayout());
		GridData gd2 = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd2.heightHint = 50;
		descriptionGroup.setLayoutData(gd2);

		descriptionText = new Text(descriptionGroup, SWT.NONE);
		descriptionText.setEditable(false);

		createContextMenu();
		getShell().setText("Deployed Tigerstripe Plugins");

		return area;
	}

	public void populateTable() {
		PluginManager mgr = PluginManager.getManager();
		List<PluggableHousing> pluggableHousings = mgr
				.getRegisteredPluggableHousings();

		pluginListTable.removeAll();

		if (descriptionText != null)
			descriptionText.setText("");

		for (PluggableHousing housing : pluggableHousings) {
			TableItem item = new TableItem(pluginListTable, SWT.NULL);
			item.setText(new String[] { housing.getPluginId(),
					housing.getVersion(), housing.getGroupId() });
		}
	}

	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager m) {
				fillContextMenu(m);
			}
		});

		Menu menu = menuMgr.createContextMenu(pluginListTable);
		pluginListTable.setMenu(menu);

	}

	private void fillContextMenu(IMenuManager menuMgr) {
		boolean isEmpty = pluginListTable.getSelection().length == 0;
		menuMgr.add(new RegisterPluggableHousingAction(this));

		if (!isEmpty) {
			menuMgr.add(new UnRegisterPluggableHousingAction(pluginListTable
					.getSelectionIndex(), this));
		}
	}
}
