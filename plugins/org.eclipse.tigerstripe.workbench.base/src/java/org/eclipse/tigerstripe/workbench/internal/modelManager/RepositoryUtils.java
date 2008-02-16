/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager;

import org.eclipse.tigerstripe.workbench.model.IModelRepository;
import org.eclipse.tigerstripe.workbench.model.IModuleRepository;
import org.eclipse.tigerstripe.workbench.model.IReferencedProjectRepository;

public class RepositoryUtils {

	/**
	 * Returns true if the given repository is a local repository, i.e. it is
	 * not a IModuleRepository or a IReferencedProjectRepository.
	 * 
	 * @param repository
	 * @return
	 */
	public static boolean isLocal(IModelRepository repository) {
		return !(repository instanceof IModuleRepository || repository instanceof IReferencedProjectRepository);
	}
}
