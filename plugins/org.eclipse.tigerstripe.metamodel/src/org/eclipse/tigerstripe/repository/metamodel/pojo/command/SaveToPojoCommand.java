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
package org.eclipse.tigerstripe.repository.metamodel.pojo.command;

import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.repository.internal.Activator;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;

/**
 * Saves the given artifact in its corresponding POJO for the given repository
 * 
 * @author erdillon
 * 
 */
public class SaveToPojoCommand extends PojoRepositoryCommand {

	public SaveToPojoCommand(IAbstractArtifact artifact,
			MultiFileArtifactRepository repository) {
		super(artifact, repository, "SaveToPojoCommand");
	}

	public void execute() {
		Resource eResource = getArtifact().eResource();
		if (eResource != null) {
			try {
				eResource.save(null);
			} catch (IOException e) {
				Activator.log(e);
			}
		}
	}

	public void redo() {
		// TODO Auto-generated method stub

	}

}
