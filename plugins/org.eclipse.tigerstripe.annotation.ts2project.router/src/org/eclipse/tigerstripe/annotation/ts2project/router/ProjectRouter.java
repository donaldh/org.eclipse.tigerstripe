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

import static org.eclipse.tigerstripe.annotation.core.IAnnotationManager.ANNOTATION_FILE_EXTENSION;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Implementation of the {@link EObjectRouter} that able to save the annotations
 * resources to the workspace project. This router can be extended through
 * extension point
 * 'org.eclipse.tigerstripe.annotation.ts2project.explicitFileRouter'
 */
public class ProjectRouter implements EObjectRouter {

	private final static String BASE_DIR = ITigerstripeConstants.ANNOTATION_DIR;

	private final Map<String, IPath> explicitRoutersMap;

	public ProjectRouter() {
		explicitRoutersMap = buildExplicitRoutersMap();
	}

	public IResource route(Annotation annotation) {
		IResource path = getTarget(annotation);
		if (path == null) {
			return null;
		}
		return path;
	}

	protected Map<String, IPath> buildExplicitRoutersMap() {
		Map<String, IPath> explicitRoutersMap = new HashMap<String, IPath>();
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
		return explicitRoutersMap;
	}

	protected IResource getTarget(Annotation ann) {
		if (ann == null) {
			return null;
		}

		try {
			EObject content = ann.getContent();

			String nsURIStr = content.eClass().getEPackage().getNsURI();
			String epackage = content.eClass().getEPackage().getNsPrefix();
			String eclass = epackage + "." + content.eClass().getName();

			// Else revert to default algorithm
			Object annotable = AnnotationPlugin.getManager()
					.getAnnotatedObject(ann);

			IProject project = null;
			if (annotable instanceof IModelComponent) {
				IModelComponent comp = (IModelComponent) annotable;
				project = adapt(getTargetProject(comp), IProject.class);
			} else if (annotable instanceof IArgument) {
				IArgument argument = (IArgument) annotable;
				project = adapt(argument.getContainingMethod().getProject(),
						IProject.class);
			} else if (annotable instanceof IAbstractTigerstripeProject) {
				IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) annotable;
				project = adapt(tsProject, IProject.class);
			} else if (annotable instanceof IDiagram) {
				IDiagram diagram = (IDiagram) annotable;
				IFile dFile = diagram.getDiagramFile();
				project = adapt(dFile.getProject(), IProject.class);
			}

			// if project wasn't found and annotation is tigerstripe relative
			// try to extract project from uri
			if (project == null) {
				URI uri = ann.getUri();
				if (TigerstripeURIAdapterFactory.isRelated(uri)) {
					String projectName = uri.segment(0);
					project = ResourcesPlugin.getWorkspace().getRoot()
							.getProject(projectName);
				}
			}

			if (project == null) {
				return null;
			}

			// See if there's an explicit definition
			IPath explicitPath = explicitRoutersMap.get(eclass);
			if (explicitPath == null) {
				explicitPath = explicitRoutersMap.get(epackage);
			}
			if (explicitPath == null) {
				explicitPath = explicitRoutersMap.get(nsURIStr);
			}
			if (explicitPath != null) {
				return project.getFile(explicitPath);
			}

			IPath path = new Path(BASE_DIR);

			URI nsURI = URI.createURI(nsURIStr);
			String name = "";
			for (int i = 0; i < nsURI.segmentCount() - 1; i++) {
				String segment = nsURI.segment(i);
				if (!"".equals(name)) {
					name += ".";
				}
				name += segment;
			}

			if (name.length() == 0) {
				return null;
			}

			path = path.append(name);
			path = path.addFileExtension(ANNOTATION_FILE_EXTENSION);
			return project.getFile(path);

		} catch (TigerstripeException e) {
			// ignore here
		}
		return null;
	}

	private ITigerstripeModelProject getTargetProject(IModelComponent component)
			throws TigerstripeException {
		if (component instanceof IContextProjectAware) {
			return ((IContextProjectAware) component).getContextProject();
		} else {
			return component.getProject();
		}
	}
}
