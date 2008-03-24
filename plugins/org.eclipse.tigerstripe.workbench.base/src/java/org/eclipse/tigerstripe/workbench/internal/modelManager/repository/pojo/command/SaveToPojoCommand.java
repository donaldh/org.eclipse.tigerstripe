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

import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.PojoModelRepository;

/**
 * Saves the given artifact in its corresponding POJO for the given repository
 * 
 * @author erdillon
 * 
 */
public class SaveToPojoCommand extends PojoRepositoryCommand {

	public SaveToPojoCommand(IAbstractArtifact artifact,
			PojoModelRepository repository) {
		super(artifact, repository, "SaveToPojoCommand");
	}

	public void execute() {
		Resource eResource = getArtifact().eResource();
		if (eResource != null) {
			try {
				eResource.save(null);
			} catch (IOException e) {
				BasePlugin.log(e); // FIXME
			}
		}
	}

	public void redo() {
		// TODO Auto-generated method stub

	}

}
