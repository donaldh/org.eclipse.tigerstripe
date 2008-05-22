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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.repository.internal.Activator;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;

public class RenamePojoCommand extends PojoRepositoryCommand {

	public RenamePojoCommand(IAbstractArtifact artifact,
			MultiFileArtifactRepository repository) {
		super(artifact, repository, "RenamePojoCommand");
	}

	public void execute() {
		Resource resource = getArtifact().eResource();
		ResourceSet rSet = resource.getResourceSet();
		URI oldURI = resource.getURI();
		rSet.getResources().remove(resource);
		try {
			// Look for references to this and force a save
			for( EObject eCross : getArtifact().getExtendingArtifacts()) {
				eCross.eResource().save(null);
			}

			// Remove file
			String s = oldURI.toPlatformString(true);
			IResource r = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(s);
			if (r instanceof IFile) {
				IFile file = (IFile) r;
				try {
					file.delete(true, null);
				} catch (CoreException e) {
					Activator.log(e);
				}
			}

			// create the new resource
			URI newURI = getRepository().normalizedURI(getArtifact());

			Resource newResource = rSet.createResource(newURI);
			newResource.getContents().add(getArtifact());
			newResource.save(null);
			

		} catch (ModelCoreException e) {
			Activator.log(e);// FIXME what's the right way to handle this?
		} catch (IOException e) {
			Activator.log(e);// FIXME what's the right way to handle this?
		}

	}

	public void redo() {
		// TODO Auto-generated method stub

	}

}
