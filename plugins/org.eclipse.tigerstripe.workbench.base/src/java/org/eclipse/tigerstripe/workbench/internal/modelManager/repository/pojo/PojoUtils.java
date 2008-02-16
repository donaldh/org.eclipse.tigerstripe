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
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;

public class PojoUtils {

	/**
	 * Returns the normalized URI corresponding to the given FQN within this
	 * repository
	 * 
	 * @param fullyQualifiedName
	 * @throws TigerstripeException
	 *             if the argument doesn't contain a valid fully qualified name
	 * @return
	 */
	public static URI getURIforFQN(String fullyQualifiedName,
			PojoModelRepository repository) throws TigerstripeException {
		if (fullyQualifiedName == null || fullyQualifiedName.length() == 0)
			throw new TigerstripeException("Invalid fully qualified name: '"
					+ fullyQualifiedName + "'.");
		String pojoPath = Util.packageOf(fullyQualifiedName).replace('.', '/');
		String pojoName = Util.nameOf(fullyQualifiedName) + "."
				+ PojoModelRepository.POJO_EXTENSION;
		URI resourceURI = URI.createURI("pojo:///" + pojoPath + "/" + pojoName);
		return repository.normalize(resourceURI);
	}

}
