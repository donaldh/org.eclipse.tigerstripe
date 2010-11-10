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

import org.eclipse.emf.ecore.EObject;

/**
 * Defines the interface to an external system, for which we want to build
 * dependencies diagram
 */
public interface IExternalContext {

	/**
	 * Save current diagram visual state
	 * 
	 * @param data
	 *            the view state for the diagram
	 */
	void save(EObject data);

	/**
	 * Load last diagram visual state
	 * 
	 * @param forClass
	 *            the class which is define diagram state
	 * @return EObject which is the diagram state
	 */
	<T extends EObject> T load(Class<T> forClass);

	/**
	 * @return root dependency element for the diagram
	 */
	IDependencySubject getRootExternalModel();

	/**
	 * @return the name of the diagram
	 */
	String getName();

	/**
	 * Add listener to external system.
	 * 
	 * @see IExternalContextListener
	 */
	void addListener(IExternalContext listener);

	/**
	 * Remove listener to external system.
	 * 
	 * @see IExternalContextListener
	 */
	void removeListener(IExternalContext listener);
}
