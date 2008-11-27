/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John Worrell (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ts2project.router;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.espace.resources.core.EObjectRouter;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.diagram.IDiagram;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

/**
 * 
 */
public class ProjectRouter implements EObjectRouter {

	private final static String BASE_DIR = ITigerstripeConstants.ANNOTATION_DIR;

	private Map<String, IPath> explicitRoutersMap = null;

	public URI route(EObject object) {

		buildExplicitRoutersMap();

		if (object instanceof Annotation) {
			Annotation annotation = (Annotation) object;
			IPath path = getTargetPath(annotation);
			if (path == null)
				return null;
			return URI.createPlatformResourceURI(path.toString(), true);
		}
		return null;
	}

	protected void buildExplicitRoutersMap() {
		if (explicitRoutersMap == null) {
			explicitRoutersMap = new HashMap<String, IPath>();
			IConfigurationElement[] elements = Platform
					.getExtensionRegistry()
					.getConfigurationElementsFor(
							"org.eclipse.tigerstripe.annotation.ts2project.explicitFileRouter");
			for (IConfigurationElement element : elements) {
				String epackage = element.getAttribute("epackage");
				String key = epackage;
				if (epackage != null) {
					String eclass = element.getAttribute("eclass");
					if (eclass != null) {
						key += "." + eclass;
					}
				} else {
					String nsURI = element.getAttribute("nsURI");
					key = nsURI;
				}
				IPath path = new Path(element.getAttribute("path"));
				explicitRoutersMap.put(key, path);
			}
		}
	}

	protected IPath getTargetPath(Annotation ann) {
		if (ann == null)
			return null;

		try {
			EObject content = ann.getContent();
			String nsURIStr = content.eClass().getEPackage().getNsURI();
			String epackage = content.eClass().getEPackage().getNsPrefix();
			String eclass = epackage + "." + content.eClass().getName();

			// Else revert to default algorithm
			Object annotable = AnnotationPlugin.getManager()
					.getAnnotatedObject(ann);

			String projectName = null;
			if (annotable instanceof IModelComponent) {
				IModelComponent comp = (IModelComponent) annotable;
				projectName = comp.getProject().getName();
			} else if (annotable instanceof IAbstractTigerstripeProject) {
				IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) annotable;
				projectName = tsProject.getName();
			} else if(annotable instanceof IDiagram) {
				IDiagram diagram = (IDiagram)annotable;
				IFile dFile = diagram.getDiagramFile();
				projectName = dFile.getProject().getName();
			}

			// IProject iproject =
			// (IProject)tsProject.getAdapter(IProject.class);
			// IPath path = iproject.getFullPath();
//			IPath path = new Path(tsProject.getName());
			IPath path = new Path(projectName);

			// See if there's an explicit definition
			IPath explicitPath = explicitRoutersMap.get(eclass);
			if (explicitPath == null)
				explicitPath = explicitRoutersMap.get(epackage);
			if (explicitPath == null)
				explicitPath = explicitRoutersMap.get(nsURIStr);
			if (explicitPath != null)
				return path.append(explicitPath);

			path = path.append(BASE_DIR);

			URI nsURI = URI.createURI(nsURIStr);
			String name = "";
			for (int i = 0; i < nsURI.segmentCount() - 1; i++) {
				String segment = nsURI.segment(i);
				if (!"".equals(name))
					name += ".";
				name += segment;
			}
			path = path.append(name);
			path = path
					.addFileExtension(EObjectRouter.ANNOTATION_FILE_EXTENSION);
			return path;

		} catch (TigerstripeException e) {
			// ignore here
		}
		return null;
	}

}
