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
package org.eclipse.tigerstripe.workbench.ui.diagram.actions;

import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchPage;

public class DiagramContributionItemProvider extends
		AbstractContributionItemProvider {

	private static final String TIGERSTRIPE_FILL_COLOR_CUSTOM_CONTRIBUTION_ID = "tigerstripeFillColorContributionItem";

	@Override
	protected IContributionItem createCustomContributionItem(String customId,
			IWorkbenchPartDescriptor partDescriptor) {

		IWorkbenchPage workbenchPage = partDescriptor.getPartPage();

		if (customId.equals(TIGERSTRIPE_FILL_COLOR_CUSTOM_CONTRIBUTION_ID))
			return ColorPropertyContributionItem
					.createFillColorContributionItem(workbenchPage);

		return super.createCustomContributionItem(customId, partDescriptor);
	}
}
