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

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * Define the type of elements in the diagram. For example, may correspond to
 * the type ITigerstripeModelProject or ITigerstripeModuleProject
 */
public interface IDependencyType {

	/**
	 * @return the unique identifier of the type
	 */
	String getId();

	/**
	 * @return the name for display on the GUI components. For example, in the
	 *         color dialog for the types
	 */
	String getName();

	/**
	 * @return background shape color on the diagram for this type
	 */
	Color getDefaultBackgroundColor();

	/**
	 * @return foreground shape color on the diagram for this type
	 */
	Color getDefaultForegroundColor();

	/**
	 * @return images for this type
	 */
	Image getImage();

	/**
	 * @return list of actions which will contribute to diagram menu
	 */
	List<IDependencyAction> getActions();
}
