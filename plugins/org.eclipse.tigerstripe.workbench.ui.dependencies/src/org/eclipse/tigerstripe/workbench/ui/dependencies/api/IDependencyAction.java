/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.api;

/**
 * Determines the action on the basis of which will be built the diagram menu
 */
public interface IDependencyAction {

	/**
	 * @return enable or disable this action for subject
	 */
	boolean isEnabled(IDependencySubject subject);

	/**
	 * This method will be invoked when the action will be launched from the
	 * diagram menu
	 * 
	 * @param subject
	 *            in the context of which was invoked the action
	 */
	void run(IDependencySubject subject);

	/**
	 * @return the name of action which will be shown in the diagram menu
	 */
	String getName();
}
