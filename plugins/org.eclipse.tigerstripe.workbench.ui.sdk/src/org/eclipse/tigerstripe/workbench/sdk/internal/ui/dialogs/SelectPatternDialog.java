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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.PatternFileContribution;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredList.FilterMatcher;

public class SelectPatternDialog extends Dialog{

	
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
	public SelectPatternDialog(Shell parentShell,ISDKProvider provider) {
		super(parentShell);
		this.parentShell = parentShell;
		this.provider = provider;
	}


private class PatternLabelProvider extends LabelProvider implements ILabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof PatternFileContribution){
			PatternFileContribution patternFileContribution = (PatternFileContribution) element ;
			IPattern pattern = provider.getPattern(patternFileContribution.getContributor(), patternFileContribution.getFileName());
			if (pattern != null){
				return pattern.getName();
			}
		}
		return super.getText(element);
	}
	
}

	public PatternFileContribution browseAvailablePatterns() throws TigerstripeException {

		final PatternLabelProvider labelProvider = new PatternLabelProvider();
		ElementListSelectionDialog elsd = new ElementListSelectionDialog(
				parentShell, labelProvider) {
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
				PatternFileContribution result = (PatternFileContribution) objects[0];
				
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

		Collection<PatternFileContribution> result = new ArrayList<PatternFileContribution>();
		
		Collection<PatternFileContribution> patterns = this.provider.getPatternFileContributions();
		
		for (PatternFileContribution p : patterns){
			IPattern pattern = provider.getPattern(p.getContributor(), p.getFileName());
			if (pattern != null && ! pattern.getName().equals("")){
				// Is it true tha we can only disable those that are not already disabled?
				// probas not - you can disable the thing in differetn tplugins to cover the
				// case where only same are installed
				result.add(p);
			}
		}
	

		return result.toArray();
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
