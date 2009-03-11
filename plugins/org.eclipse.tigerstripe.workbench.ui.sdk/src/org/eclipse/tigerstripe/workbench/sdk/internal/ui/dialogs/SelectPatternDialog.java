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

import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.PatternFileContribution;

public class SelectPatternDialog extends SelectContributionDialog {

	public SelectPatternDialog(Shell parentShell, ISDKProvider provider) {
		super(parentShell, provider);
		labelProvider = new PatternLabelProvider();
	}

	private class PatternLabelProvider extends ContributionLabelProvider  {

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
	
	/**
	 * 
	 * @return
	 */
	protected Object[] getAvailableContributionsList()
			throws TigerstripeException {

		Collection<PatternFileContribution> result = new ArrayList<PatternFileContribution>();
		
		Collection<PatternFileContribution> patterns = this.provider.getPatternFileContributions();
		
		for (PatternFileContribution p : patterns){
			IPattern pattern = provider.getPattern(p.getContributor(), p.getFileName());
			if (pattern != null && ! pattern.getName().equals("")){
				// Is it true that we can only disable those that are not already disabled?
				// probs not - you can disable the thing in different plugins to cover the
				// case where only same are installed
				result.add(p);
			}
		}
	

		return result.toArray();
	}
	
}
