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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;

/**
 * @author Yuri Strot
 *
 */
public class CreateAnnotationGroupAction extends Action {
	
	private MenuCreator menu;
	
	public CreateAnnotationGroupAction(String text, List<TargetAnnotationType> list) {
		super(text);
		List<Object> actions = new ArrayList<Object>();
		for (TargetAnnotationType type : list) {
			actions.add(new CreateSpecificTypeAnnotationAction(type));
		}
		menu = new MenuCreator(actions);
		setMenuCreator(menu);
	}

}
