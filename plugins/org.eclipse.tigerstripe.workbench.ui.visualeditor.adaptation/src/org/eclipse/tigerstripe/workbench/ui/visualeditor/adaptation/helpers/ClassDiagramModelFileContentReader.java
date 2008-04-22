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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers;

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
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.DiagramHandle;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.IModelFileContentReader;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;

public class ClassDiagramModelFileContentReader implements
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
			if (obj instanceof Map) {
				Map map = (Map) obj;
				for (AbstractArtifact artifact : (List<AbstractArtifact>) map
						.getArtifacts()) {
					result.add(artifact.getFullyQualifiedName());
				}
				for (Association assoc : (List<Association>) map
						.getAssociations()) {
					result.add(assoc.getFullyQualifiedName());
				}
				for (Dependency dep : (List<Dependency>) map.getDependencies()) {
					result.add(dep.getFullyQualifiedName());
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
		return "vwm";
	}

}
