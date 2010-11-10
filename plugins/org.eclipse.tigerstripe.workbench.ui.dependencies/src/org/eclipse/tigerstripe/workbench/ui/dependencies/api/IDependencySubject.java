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

import java.util.Set;
import java.util.SortedMap;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Base interface for constructing the dependency graph. Each instance if this
 * interface corresponding with a graph node in the external system. For
 * example, this interface can be corresponded with the Tigerstripe Module for a
 * Tigerstripe dependency diagram.
 */
public interface IDependencySubject extends IAdaptable {

	/**
	 * @return the unique identifier of the subject in the dependency graph
	 */
	String getId();

	/**
	 * @return the name is shown on the diagram and other GUI elements for this
	 *         subject
	 */
	String getName();

	/**
	 * @return set of dependencies for this subject. Based on this connection
	 *         will be constructed on the diagram
	 */
	Set<IDependencySubject> getDependencies();

	/**
	 * @return properties which will be shown in the diagram
	 */
	SortedMap<String, String> getProperties();

	/**
	 * @return short description which will be shown in the diagram
	 */
	String getDescription();

	/**
	 * @return type to which the subject belongs
	 */
	IDependencyType getType();
}
