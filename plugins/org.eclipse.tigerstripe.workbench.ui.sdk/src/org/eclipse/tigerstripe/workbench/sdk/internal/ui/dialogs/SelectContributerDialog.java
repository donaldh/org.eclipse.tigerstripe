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
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredList.FilterMatcher;

public class SelectContributerDialog extends Dialog{

	
	private Shell parentShell;
	private ISDKProvider provider;
	

	private String title;

	private String message;

	private int fWidth = 140;

	private int fHeight = 18;


	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public SelectContributerDialog(Shell parentShell,ISDKProvider provider) {
		super(parentShell);
		this.parentShell = parentShell;
		this.provider = provider;
	}


private class ContributerLabelProvider extends LabelProvider implements ILabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof IPluginModelBase){
			IPluginModelBase contributer = (IPluginModelBase) element ;
				return contributer.toString();

		}
		return super.getText(element);
	}
	
}

	public IPluginModelBase browseAvailableContribuers() throws TigerstripeException {

		final ContributerLabelProvider contributerProvider = new ContributerLabelProvider();
		ElementListSelectionDialog elsd = new ElementListSelectionDialog(
				parentShell, contributerProvider) {
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

		Object[] availablePatterns = getAvailablePatternsList();
		elsd.setElements(availablePatterns);

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				IPluginModelBase result = (IPluginModelBase) objects[0];
				
				return result;
			}
		}
		return null;
	}


	/**
	 * 
	 * @return
	 */
	private Object[] getAvailablePatternsList()
			throws TigerstripeException {

		return provider.getWriteableContributers().toArray();	
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
