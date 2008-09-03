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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
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

	private Set<URI> registeredURIs = new HashSet<URI>();

	private ModuleAnnotationManager() {
	}

	/**
	 * Registers the annotations for the corresponding module URI.
	 * 
	 * @param tsModuleURI
	 */
	public void registerAnnotationsFor(URI tsModuleURI, Mode mode)
			throws IOException {
		if (!registeredURIs.contains(tsModuleURI)) {
			for (URI uri : extractResourcesFromModule(tsModuleURI)) {
//				AnnotationPlugin.getManager().addAnnotations(
//						new ResourceImpl(uri), mode);
				registeredURIs.add(tsModuleURI);
//				System.out.println("Registered: " + tsModuleURI);
			}
		}
	}

	public void unRegisterAnnotationsFor(URI tsModuleURI) throws IOException {
		if (registeredURIs.contains(tsModuleURI)) {
			for (URI uri : extractResourcesFromModule(tsModuleURI)) {
//				AnnotationPlugin.getManager().removeAnnotations(
//						new ResourceImpl(uri));
				registeredURIs.remove(tsModuleURI);
//				System.out.println("Un-Registered: " + tsModuleURI);
			}
		}
	}

	private URI[] extractResourcesFromModule(URI tsModuleURI)
			throws IOException {

		ArrayList<URI> result = new ArrayList<URI>();

		// Resources (Annotation files) can only be extracted if the module
		// is a workspace module (i.e. a module stored in a project within the
		// workspace)
		if (!tsModuleURI.isPlatformResource())
			return result.toArray(new URI[result.size()]);

		// We need a full path for the URI, a platform URI won't do
		String filePath = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().append(tsModuleURI.toPlatformString(false))
				.toOSString();

		JarFile file = new JarFile(filePath);
		for (Enumeration<JarEntry> entries = file.entries(); entries
				.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			org.eclipse.emf.common.util.URI foo = tsModuleURI
					.appendFragment(entry.getName());
			if (entry.getName().endsWith(
					EObjectRouter.ANNOTATION_FILE_EXTENSION)) {
				result.add(foo);
			}
		}

		return result.toArray(new URI[result.size()]);
	}
}
