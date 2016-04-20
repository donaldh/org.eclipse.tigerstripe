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
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UML22UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.osgi.framework.Bundle;

public class Utilities {

	public IWorkbenchProfile profile = TigerstripeCore
			.getWorkbenchProfileSession().getActiveProfile();

	public static ResourceSet resourceSet = new ResourceSetImpl();

	public static Map options;

	public static org.eclipse.uml2.uml.Package openModelFile(File file)
			throws InvocationTargetException {
		URI fileUri = URI.createFileURI(file.getAbsolutePath());
		return openModelURI(fileUri);
	}

	public static org.eclipse.uml2.uml.Package openModelURI(URI fileUri)
			throws InvocationTargetException {
		Resource resource;
		org.eclipse.uml2.uml.Package model = null;
		try {
			resource = resourceSet.createResource(fileUri);
			resource.load(options);
		} catch (Exception e) {
			throw new InvocationTargetException(e, "Cannot open model file :"
					+ fileUri);
		}
		if (resource.isLoaded()) {
			Iterator i = resource.getContents().iterator();
			while (i.hasNext()) {
				EObject eo = (EObject) i.next();
				if (eo instanceof org.eclipse.uml2.uml.Package) {
					model = (org.eclipse.uml2.uml.Package) eo;
					break;
				}
			} if (model == null){
			throw new InvocationTargetException(null, "No uml:Model found in file:"
					+ fileUri.toFileString());
			}
		} else
			throw new InvocationTargetException(null, "Can't find model :"
					+ fileUri.toFileString());
		return model;
	}

	public static Profile openProfileFile(File file)
			throws InvocationTargetException {
		URI fileUri = URI.createFileURI(file.getAbsolutePath());
		return openProfileURI(fileUri);
	}

	public static Profile openProfileURI(URI fileUri)
			throws InvocationTargetException {
		Resource resource;
		Profile profile = null;
		try {
			resource = resourceSet.createResource(fileUri);
			resource.load(options);
		} catch (Exception e) {
			throw new InvocationTargetException(e, "Cannot open profile file :"
					+ fileUri);
		}
		if (resource.isLoaded()) {
			Iterator i = resource.getContents().iterator();
			while (i.hasNext()) {
				EObject eo = (EObject) i.next();
				if (eo instanceof Profile) {
					profile = (Profile) eo;
					break;
				}
			}
		} else
			throw new InvocationTargetException(null, "Can't find profile :"
					+ fileUri.toFileString());
		return profile;
	}

	public static void setupPaths() {
		final Map extensionToFactoryMap = resourceSet
				.getResourceFactoryRegistry().getExtensionToFactoryMap();

		extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
				UML22UMLResource.Factory.INSTANCE);

		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI,
				UMLPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);

		Map uriMap = resourceSet.getURIConverter().getURIMap();
		// TODO - shouldn't need to hard code path & version surely?

		Bundle umlBundle = Platform.getBundle("org.eclipse.uml2.uml.resources");
		try {
			File f = FileLocator.getBundleFile(umlBundle);
			URL url = FileLocator.toFileURL(FileLocator.find(umlBundle, new Path("libraries"), null));
			uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), URI.createURI(url.toString()));
			url = FileLocator.toFileURL(FileLocator.find(umlBundle, new Path("metamodels"), null));
			uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), URI.createURI(url.toString()));
			url = FileLocator.toFileURL(FileLocator.find(umlBundle, new Path("profiles"), null));
			uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), URI.createURI(url.toString()));
		} catch (Exception e) {
			EclipsePlugin.log(e);
		}

//		IPath eclipseHome = JavaCore.getClasspathVariable("ECLIPSE_HOME");
//
//		String t = Platform.getBundle("org.eclipse.uml2.uml.resources")
//				.getLocation();
//		String uml2ResourcesRelLocation = t.substring(t.indexOf("@") + 1, t
//				.length());
//		URI uri = URI.createURI("jar:file:/" + eclipseHome + "/"
//				+ uml2ResourcesRelLocation + "!/");
//		uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri
//				.appendSegment("libraries").appendSegment(""));
//		uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri
//				.appendSegment("metamodels").appendSegment(""));
//		uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri
//				.appendSegment("profiles").appendSegment(""));

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"emx", UMLResource.Factory.INSTANCE);

	}

	public static void addPaths(File modelProfilesDir) {
		Map uriMap = resourceSet.getURIConverter().getURIMap();

		URI modelProfileUri = URI.createFileURI(modelProfilesDir
				.getAbsolutePath());

		uriMap.put(URI.createURI("pathmap://MODEL_PROFILE/"), modelProfileUri
				.appendSegment(""));
	}

	public static void tearDown() {
		for (Iterator i = resourceSet.getResources().iterator(); i.hasNext();) {
			Resource r = (Resource) i.next();
			r.unload();
		}
	}
}
