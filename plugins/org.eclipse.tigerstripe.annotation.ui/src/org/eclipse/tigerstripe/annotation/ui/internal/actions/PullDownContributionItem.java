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

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.tigerstripe.annotation.ui.core.IExtendedMenuCreator;
import org.eclipse.tigerstripe.annotation.ui.core.IMenuProvider;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;

/**
 * @author Yuri Strot
 *
 */
public class PullDownContributionItem extends ContributionItem {
	
	private IMenuProvider provider;
	
	/**
	 * 
	 */
	public PullDownContributionItem(IMenuProvider provider) {
		this.provider = provider;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.Menu, int)
	 */
	@Override
	public void fill(Menu menu, int index) {
		IAction action = provider.getBaseAction();
		MenuItem item = new MenuItem(menu, SWT.CASCADE, index);
		item.setText(action.getText());
		
		IExtendedMenuCreator creator = provider.getMenu(WorkbenchUtil.getWindow(
				).getSelectionService().getSelection());
		if (creator != null) {
			Menu newMenu = creator.getMenu(item);
			item.setMenu(newMenu);
		}
		else {
			item.setEnabled(false);
		}
	}
//	
//	/* (non-Javadoc)
//	 * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
//	 */
//	protected List<Object> getContributionItems() {
//		List<Object> list = new ArrayList<Object>();
//		ActionContributionItem item = new ActionContributionItem(new CreateAnnotationAction());
//		ActionContributionItem item2 = new ActionContributionItem(new SimpleAction());
//		ActionContributionItem item3 = new ActionContributionItem(new SimpleAction2());
//		list.add(item);
//		list.add(item2);
//		list.add(item3);
//		return list;
//	}

}
