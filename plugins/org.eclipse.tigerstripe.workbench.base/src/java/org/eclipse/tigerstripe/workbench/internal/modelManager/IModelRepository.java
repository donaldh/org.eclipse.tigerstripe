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

import java.util.Collection;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

public interface IModelRepository {

	public boolean isLocal();

	public IAbstractArtifact store(IAbstractArtifact workingCopy, boolean force)
			throws TigerstripeException;

	public Collection<IAbstractArtifact> getAllArtifacts();

	public IAbstractArtifact getArtifactByFullyQualifiedName(
			String fullyQualifiedName);

	public void refresh();
}
