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
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.writer;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

public class ArtifactWriterFactory {

	public final static ArtifactWriterFactory INSTANCE = new ArtifactWriterFactory();

	@SuppressWarnings("unchecked")
	private Class[] artifactClasses = { IManagedEntityArtifactImpl.class };

	private AbstractArtifactWriter[] writers = { new ManagedEntityArtifactWriter() };

	private ArtifactWriterFactory() {
		// EMPTY
	}

	public <T extends IAbstractArtifact> AbstractArtifactWriter getWriter(
			Class<T> artifactClass) throws TigerstripeException {
		for (int index = 0; index < artifactClasses.length; index++) {
			if (artifactClasses[index] == artifactClass) {
				return writers[index];
			}
		}

		throw new TigerstripeException("No writer for "
				+ artifactClass.getCanonicalName());
	}
}
