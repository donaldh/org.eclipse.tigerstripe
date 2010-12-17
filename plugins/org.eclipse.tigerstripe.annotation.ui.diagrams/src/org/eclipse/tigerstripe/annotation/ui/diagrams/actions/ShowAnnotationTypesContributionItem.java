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
package org.eclipse.tigerstripe.annotation.ui.diagrams.actions;

import org.eclipse.tigerstripe.annotation.ui.internal.actions.PullDownContributionItem;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.actions.ChangeAnnotationTypesVisibility;

/**
 * @author Yuri Strot
 *
 */
public class ShowAnnotationTypesContributionItem extends PullDownContributionItem {

	/**
	 * @param provider
	 */
	public ShowAnnotationTypesContributionItem() {
		super(new ChangeAnnotationTypesVisibility(
				"Show Annotation Types", true));
	}

}
