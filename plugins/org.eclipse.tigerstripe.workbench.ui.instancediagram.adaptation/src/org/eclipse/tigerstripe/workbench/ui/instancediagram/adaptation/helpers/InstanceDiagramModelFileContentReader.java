/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.gmf.synchronization.DiagramHandle;
import org.eclipse.tigerstripe.workbench.ui.gmf.synchronization.IModelFileContentReader;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;

public class InstanceDiagramModelFileContentReader implements
		IModelFileContentReader {

	public Set<String> getFQNs(DiagramHandle handle)
			throws TigerstripeException {
		Set<String> result = new HashSet<String>();

		IResource modelFile = handle.getModelResource();
		ResourceSet resSet = new ResourceSetImpl();
		Resource modelResource = resSet.createResource(URI.createURI(modelFile
				.getLocationURI().toString()));
		try {
			java.util.Map loadingOptions = new HashMap(GMFResourceFactory
					.getDefaultLoadOptions());
			modelResource.load(loadingOptions);
			Object obj = modelResource.getContents().get(0);
			if (obj instanceof InstanceMap) {
				InstanceMap map = (InstanceMap) obj;
				for (Instance instance : (List<Instance>) map
						.getClassInstances()) {
					result.add(instance.getFullyQualifiedName());
				}
				for (Instance instance : (List<Instance>) map
						.getAssociationInstances()) {
					result.add(instance.getFullyQualifiedName());
				}
			}
		} catch (IOException e) {
			throw new TigerstripeException("Couldn't assess content of "
					+ handle.getDiagramResource().getFullPath()
					+ " for indexing: " + e.getMessage(), e);
		}
		return result;
	}

	public String getSupportedModelFileExtension() {
		return "owm";
	}

}
