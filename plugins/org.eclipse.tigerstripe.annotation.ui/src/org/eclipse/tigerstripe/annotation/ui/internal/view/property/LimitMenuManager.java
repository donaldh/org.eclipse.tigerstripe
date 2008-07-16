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
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;

/**
 * @author Yuri Strot
 *
 */
public class LimitMenuManager extends MenuManager {
	
	private String[] groups;
	
	public LimitMenuManager(String[] groups) {
		this.groups = groups;
	}
	
	public LimitMenuManager(String group) {
		this(new String[] { group });
	}
	
	protected boolean ignoreMarker(AbstractGroupMarker marker) {
		String group = marker.getGroupName();
		if (group == null)
			return false;
		for (int i = 0; i < groups.length; i++) {
			if (groups[i].equals(group)) {
				return false;
			}
		}
		return true;
	}
	
	protected void hide(IContributionItem item) {
		if (item instanceof AbstractGroupMarker) {
			AbstractGroupMarker marker = (AbstractGroupMarker)item;
			if (ignoreMarker(marker)) {
				item.setVisible(false);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ContributionManager#add(org.eclipse.jface.action.IContributionItem)
	 */
	@Override
	public void add(IContributionItem item) {
		hide(item);
		super.add(item);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ContributionManager#insertAfter(java.lang.String, org.eclipse.jface.action.IContributionItem)
	 */
	@Override
	public void insertAfter(String ID, IContributionItem item) {
		try {
			hide(item);
			IContributionItem ci = find(ID);
			if (ci != null && !ci.isVisible())
				item.setVisible(false);
			super.insertAfter(ID, item);
		}
		catch (Exception e) {
			//ignore
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ContributionManager#insertBefore(java.lang.String, org.eclipse.jface.action.IContributionItem)
	 */
	@Override
	public void insertBefore(String ID, IContributionItem item) {
		try {
			hide(item);
			IContributionItem ci = find(ID);
			if (ci != null && !ci.isVisible())
				item.setVisible(false);
			super.insertBefore(ID, item);
		}
		catch (Exception e) {
			//ignore
		}
	}

}
