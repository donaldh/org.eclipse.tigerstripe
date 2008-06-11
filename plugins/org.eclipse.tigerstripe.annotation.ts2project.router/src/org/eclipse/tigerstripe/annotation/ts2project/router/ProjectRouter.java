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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

/**
 * 
 */
public class ProjectRouter implements EObjectRouter {

	private final static String ANN_EXT = "ann";
	private final static String BASE_DIR = "annotations";

	private Map<String, IPath> explicitRoutersMap = null;

	public URI route(EObject object) {

		buildExplicitRoutersMap();

		if (object instanceof Annotation) {
			Annotation annotation = (Annotation) object;
			IPath path = getTargetPath(annotation);
			if (path == null)
				return null;
			return URI.createFileURI(path.toString());
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
				String nsURI = element.getAttribute("nsURI");
				IPath path = new Path(element.getAttribute("path"));
				explicitRoutersMap.put(nsURI, path);
			}
		}
	}

	protected IPath getTargetPath(Annotation ann) {
		if (ann == null)
			return null;

		try {
			EObject content = ann.getContent();
			String nsURIStr = content.eClass().getEPackage().getNsURI();

			// Else revert to default algorithm
			Object annotable = AnnotationPlugin.getManager()
					.getAnnotatedObject(ann);

			if (annotable instanceof IModelComponent) {
				IModelComponent comp = (IModelComponent) annotable;
				IPath path = comp.getProject().getLocation();
				// See if there's an explicit definition
				IPath explicitPath = explicitRoutersMap.get(nsURIStr);
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
				path = path.addFileExtension(ANN_EXT);
				return path;
			}
		} catch (TigerstripeException e) {
			// ignore here
		}
		return null;
	}

}
