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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.PojoModelRepository;
import org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.PojoUtils;

public class RenamePojoCommand extends PojoRepositoryCommand {

	public RenamePojoCommand(IAbstractArtifact artifact,
			PojoModelRepository repository) {
		super(artifact, repository, "RenamePojoCommand");
	}

	@Override
	public void execute() {
		Resource resource = getArtifact().eResource();
		ResourceSet rSet = resource.getResourceSet();
		URI oldURI = resource.getURI();
		rSet.getResources().remove(resource);
		try {
			// Remove file
			String s = oldURI.toPlatformString(true);
			IResource r = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(s);
			if (r instanceof IFile) {
				IFile file = (IFile) r;
				try {
					file.delete(true, null);
				} catch (CoreException e) {
					BasePlugin.log(e);
				}
			}

			// create the new resource
			URI newURI = PojoUtils.getURIforFQN(getArtifact()
					.getFullyQualifiedName(), getRepository());

			Resource newResource = rSet.createResource(newURI);
			newResource.getContents().add(getArtifact());
			newResource.save(null);
			
		} catch (TigerstripeException e) {
			BasePlugin.log(e);// FIXME what's the right way to handle this?
		} catch (IOException e) {
			BasePlugin.log(e);// FIXME what's the right way to handle this?
		}

	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub

	}

}
