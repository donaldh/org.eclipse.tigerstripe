/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.metamodel.providers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.repository.manager.IKeyProvider;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;

public class TigerstripeKeyProvider implements IKeyProvider {

	public Object getKey(EObject obj) throws ModelCoreException {
		if (obj instanceof IAbstractArtifact) {
			IAbstractArtifact artifact = (IAbstractArtifact) obj;
			return artifact.getFullyQualifiedName();
		}
		return null;
	}

	public boolean selects(EObject obj) {
		return obj instanceof IAbstractArtifact;
	}

}
