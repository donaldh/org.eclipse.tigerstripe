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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.actions;

import org.eclipse.tigerstripe.annotation.ui.internal.actions.PullDownContributionItem;

/**
 * @author Yuri Strot
 *
 */
public class HideAnnotationTypeContributionItem extends PullDownContributionItem {

	/**
	 * @param provider
	 */
	public HideAnnotationTypeContributionItem() {
		super(new ChangeAnnotationTypesVisibility(
				"Hide Annotation Types", false));
	}

}
