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
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.espace.core.Mode;
import org.eclipse.tigerstripe.espace.resources.core.EObjectRouter;

/**
 * This class is a singleton to manage the Annotations files embedded in TS
 * modules. It avoids duplicate registrations and adjusts the URIs on the fly.
 * 
 * @author erdillon
 * 
 */
public class ModuleAnnotationManager {

	public final static ModuleAnnotationManager INSTANCE = new ModuleAnnotationManager();

	private HashMap<URI, Resource[]> registeredURIs = new HashMap<URI, Resource[]>();

	private ModuleAnnotationManager() {
	}

	/**
	 * Registers the annotations for the corresponding module URI.
	 * 
	 * @param tsModuleURI
	 */
	public void registerAnnotationsFor(URI tsModuleURI, String moduleID,
			Mode mode) throws IOException {
		if (!registeredURIs.containsKey(tsModuleURI)) {
			Resource[] containedResources = extractResourcesFromModule(
					tsModuleURI, moduleID);
			if (containedResources.length != 0) {
				for (Resource res : containedResources) {
					AnnotationPlugin.getManager().addAnnotations(res,
							Mode.READ_ONLY);
				}
				registeredURIs.put(tsModuleURI, containedResources);
			}
		}
	}

	public void unRegisterAnnotationsFor(URI tsModuleURI) throws IOException {
		if (registeredURIs.containsKey(tsModuleURI)) {
			Resource[] registeredResources = registeredURIs.get(tsModuleURI);
			for (Resource res : registeredResources) {
				AnnotationPlugin.getManager().removeAnnotations(res);
			}
			registeredURIs.remove(tsModuleURI);
		}
	}

	private Resource[] extractResourcesFromModule(URI tsModuleURI,
			String moduleID) throws IOException {

		ArrayList<Resource> result = new ArrayList<Resource>();

		// Resources (Annotation files) can only be extracted if the module
		// is a workspace module (i.e. a module stored in a project within the
		// workspace)
		if (!tsModuleURI.isPlatformResource())
			return result.toArray(new Resource[result.size()]);

		// We need a full path for the URI, a platform URI won't do
		String filePath = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().append(tsModuleURI.toPlatformString(true))
				.toOSString();

		File file = new File(filePath);
		if (file.exists()) {
			JarFile jarFile = new JarFile(file);
			for (Enumeration<JarEntry> entries = jarFile.entries(); entries
					.hasMoreElements();) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().endsWith(
						EObjectRouter.ANNOTATION_FILE_EXTENSION)) {
					ResourceSet set = new ResourceSetImpl();

					// create archive URI
					String uriString = "tsmodule:/" + moduleID + "/" + filePath
							+ "!/" + entry.getName();
					URI rr = URI.createURI(uriString);

					Resource res = set.createResource(rr);
					result.add(res);
				}
			}
		}
		return result.toArray(new Resource[result.size()]);
	}
}
