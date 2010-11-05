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

public interface IDependencySubject extends IAdaptable {

	String getId();

	String getName();

	Set<IDependencySubject> getDependencies();

	SortedMap<String, String> getProperties();

	String getDescription();

	IDependencyType getType();
}
