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
package org.eclipse.tigerstripe.annotation.ui.core;

import org.eclipse.jface.action.Action;
import org.eclipse.tigerstripe.annotation.core.Annotation;

/**
 * @author Yuri Strot
 *
 */
public class TestActionManager {
	
	private static IChangableActionFactory factory;
	
	public static void setAction(IChangableActionFactory factory) {
		TestActionManager.factory = factory;
	}
	
	public static Action getAction(Annotation annotation, boolean show) {
		return factory.create(annotation, show);
	}

}
