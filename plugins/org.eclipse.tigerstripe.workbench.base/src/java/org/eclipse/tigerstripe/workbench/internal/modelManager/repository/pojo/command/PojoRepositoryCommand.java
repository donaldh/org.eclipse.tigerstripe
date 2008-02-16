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
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.command;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.PojoModelRepository;

public abstract class PojoRepositoryCommand extends AbstractCommand {

	private IAbstractArtifact artifact;
	private PojoModelRepository repository;

	protected PojoRepositoryCommand(IAbstractArtifact artifact,
			PojoModelRepository repository, String name) {
		super(name);
		this.artifact = artifact;
		this.repository = repository;
	}

	protected PojoModelRepository getRepository() {
		return this.repository;
	}

	protected IAbstractArtifact getArtifact() {
		return this.artifact;
	}

	@Override
	public boolean canExecute() {
		return repository != null && !repository.isReadonly()
				&& artifact != null && artifact.getFullyQualifiedName() != null
				&& artifact.getFullyQualifiedName().length() != 0;
	}

}
