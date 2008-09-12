/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.tigerstripe.annotation.ui.core.IExtendedMenuCreator;

/**
 * @author Yuri Strot
 *
 */
public class MenuCreator implements IExtendedMenuCreator {
	
	private MenuManager menuMgr;
	private List<Object> actionsList;
	
	public MenuCreator(List<Object> actionsList) {
		this.actionsList = actionsList;
	}
	
	public Menu getMenu(Menu parent) {
		Menu menu = new Menu(parent);
		for (Iterator<Object> iter = actionsList.iterator(); iter.hasNext();) {
			addActionToMenu(menu, iter.next());
		}
		return menu;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.IExtendedMenuCreator#getMenu(org.eclipse.swt.widgets.MenuItem)
	 */
	public Menu getMenu(MenuItem parent) {
		Menu menu = new Menu(parent);
		for (Iterator<Object> iter = actionsList.iterator(); iter.hasNext();) {
			addActionToMenu(menu, iter.next());
		}
		return menu;
	}

	public Menu getMenu(Control parent) {
		// Create the menu manager and add all the NewActions to it
		if (menuMgr == null) {
			// Lazily create the manager
			menuMgr = new MenuManager();
			menuMgr.createContextMenu(parent);
		}
		
		updateMenuManager(menuMgr);
		return menuMgr.getMenu();
	}

	protected void updateMenuManager(MenuManager manager) {
		manager.removeAll();
		for (Iterator<Object> iter = actionsList.iterator(); iter.hasNext();) {
			Object item = iter.next();
			if (item instanceof IAction) {
				manager.add((IAction)item);
			}
			else if (item instanceof IContributionItem) {
				manager.add((IContributionItem)item);
			}
		}
	}
	
	private void addActionToMenu(Menu parent, Object item) {
		if (item instanceof IAction) {
			IAction action = (IAction)item;
			if (action.isEnabled()) {
				ActionContributionItem cItem = new ActionContributionItem(action);
				cItem.fill(parent, -1);
			}
		}
		else if (item instanceof IContributionItem) {
			IContributionItem cItem = (IContributionItem)item;
			cItem.fill(parent, -1);
		}
	}

	public void dispose() {
		if (menuMgr != null) {
			menuMgr.dispose();
			menuMgr = null;
		}
	}

}
