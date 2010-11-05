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

public interface IExternalContext {

	void save(EObject data);

	<T extends EObject> T load(Class<T> forClass);

	IDependencySubject getRootExternalModel();

	String getName();

}
