/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.IContribution;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredList.FilterMatcher;

public abstract class SelectContributionDialog extends Dialog{

	
	protected Shell parentShell;
	protected ISDKProvider provider;
	protected ContributionLabelProvider labelProvider;

	private String title;
	private String message;
	private int fWidth = 140;
	private int fHeight = 18;


	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public SelectContributionDialog(Shell parentShell,ISDKProvider provider) {
		super(parentShell);
		this.parentShell = parentShell;
		this.provider = provider;
		
	}

	protected abstract Object[] getAvailableContributionsList()
		throws TigerstripeException ;

	protected abstract class ContributionLabelProvider extends LabelProvider implements ILabelProvider{}; 

	public IContribution browseAvailableContributions() throws TigerstripeException {

		final ContributionLabelProvider provider = labelProvider;
		ElementListSelectionDialog elsd = new ElementListSelectionDialog(
				parentShell, provider) {
			@Override
			public void create() {
				super.create();
				fFilteredList.setFilterMatcher(new FilterMatcher() {
					private String searchPattern;

					public void setFilter(String pattern, boolean ignoreCase,
							boolean ignoreWildCards) {
						this.searchPattern = pattern;
					}

					public boolean match(Object element) {
						if (element instanceof String) {
							
							return ((String) element).contains(searchPattern);
						}
						return false;
					}
				});
			}
		};

		elsd.setTitle(getTitle());
		elsd.setMessage(getMessage());
		elsd.setSize(fWidth, fHeight);

		Object[] availablePatterns = getAvailableContributionsList();
		elsd.setElements(availablePatterns);

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				IContribution result = (IContribution) objects[0];
				
				return result;
			}
		}
		return null;
	}


	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
