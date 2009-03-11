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

import java.util.Collection;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AuditContribution;

public class SelectAuditDialog extends SelectContributionDialog {

	public SelectAuditDialog(Shell parentShell, ISDKProvider provider) {
		super(parentShell, provider);
		labelProvider = new AuditLabelProvider();
	}

	private class AuditLabelProvider extends ContributionLabelProvider  {

		@Override
		public String getText(Object element) {
			if (element instanceof AuditContribution){
				AuditContribution contribution = (AuditContribution) element ;
				if (! contribution.getName().equals("")){
					return contribution.getName();
				} else {
					return contribution.getAuditorClass();
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

		Collection<AuditContribution> result = this.provider.getAuditContributions();
		
		return result.toArray();
	}
	
}
