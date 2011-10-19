/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationManager;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

/**
 * @author Yuri Strot
 * 
 */
public class TSModuleResource extends XMIResourceImpl {

	protected ModuleURIHelper container;

	/**
	 * Constructor for TSModuleResource.
	 * 
	 * @param uri
	 */
	public TSModuleResource(URI uri) {
		super(uri);
		this.container = new ModuleURIHelper(uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#load(java.util.Map)
	 */
	@Override
	public void load(Map<?, ?> options) throws IOException {
		boolean loaded = load(container.getArchiveUri(), options);
		if (loaded) updateContent();
	}

	protected void updateContent() {
		EList<EObject> contents = getContents();

		String containerName = getContainerName();
		for (EObject obj : contents) {
			if (obj instanceof Annotation) {
				Annotation ann = (Annotation) obj;
				URI originalURI = ann.getUri();
				String[] segments = originalURI.segments();

				// The first segment must be changed from being the original
				// project name
				// to being the module ID
				// if (containerName != null) {
				// segments[0] = containerName
				// +
				// TigerstripeURIAdapterFactory.SCHEME_TS_CONTEXT_PROJECT_SEPARATOR
				// + container.getModuleID();
				// } else {
					segments[0] = container.getModuleID();
				// }
				URI newURI = URI.createHierarchicalURI(
						TigerstripeURIAdapterFactory.SCHEME_TS, null,
						null, segments, null, originalURI.fragment());
				ann.setUri(newURI);
			}
		}
	}

	private String getContainerName() {
		IPath path = container.getArchivePath();
		IFile file = ResourcesPlugin.getWorkspace().getRoot()
				.getFileForLocation(path);
		if (file != null) {
			IProject project = file.getProject();
			IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) project
					.getAdapter(IAbstractTigerstripeProject.class);
			if (tsProject != null) {
				return project.getName();
			}
		}
		return null;
	}

	protected boolean load(URI uri, Map options) throws IOException {
		if (!isLoaded) {
			URIConverter uriConverter = getURIConverter();
			Map<?, ?> response = options == null ? null : (Map<?, ?>) options
					.get(URIConverter.OPTION_RESPONSE);
			if (response == null) {
				response = new HashMap<Object, Object>();
			}

			if (options == null) {
				options = new HashMap<Object, Object>();
			} else {
				options = new HashMap<Object, Object>(options); 
			}
			
			options.putAll(AnnotationPlugin.getDomain().getResourceSet()
					.getLoadOptions());
			
			// If an input stream can't be created, ensure that the resource is
			// still considered loaded after the failure,
			// and do all the same processing we'd do if we actually were able
			// to create a valid input stream.
			//
			InputStream inputStream = uriConverter.createInputStream(uri,
					new OptionsMap(URIConverter.OPTION_RESPONSE, response,
							options));
			try {
				load(inputStream, options);
			} finally {
				inputStream.close();
				Long timeStamp = (Long) response
						.get(URIConverter.RESPONSE_TIME_STAMP_PROPERTY);
				if (timeStamp != null) {
					setTimeStamp(timeStamp);
				}
			}
			return true;
		}
		return false;
	}

}
