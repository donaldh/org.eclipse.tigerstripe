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
package org.eclipse.tigerstripe.annotation.tsmodel;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.IAnnotationProvider;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.diagram.IDiagram;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IMethodElement;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author John Worrell
 * 
 */
public class TSModelAnnotationProvider implements IAnnotationProvider {

	public Object getObject(URI uri) {
		if (!TigerstripeURIAdapterFactory.isRelated(uri))
			return null;

		IAbstractTigerstripeProject project = TigerstripeURIAdapterFactory
				.uriToProject(uri);
		if (project != null)
			return project;
		IModelComponent component = TigerstripeURIAdapterFactory
				.uriToComponent(uri);
		if (component != null)
			return component;
		IMethodElement element = TigerstripeURIAdapterFactory
				.uriToArgument(uri);
		if (element != null) {
			return element;
		}
		IDiagram diagram = TigerstripeURIAdapterFactory.uriToDiagram(uri);
		return diagram;
	}

	public URI getUri(Object object) {
		try {
			if (object instanceof IModelComponent) {
				return TigerstripeURIAdapterFactory
						.toURI((IModelComponent) object);
			} else if (object instanceof IArgument) {
				return TigerstripeURIAdapterFactory.toURI((IArgument) object);
			} else if (object instanceof ITigerstripeModelProject) {
				return TigerstripeURIAdapterFactory
						.toURI((ITigerstripeModelProject) object);
			} else if (object instanceof IDiagram) {
				return TigerstripeURIAdapterFactory.toURI((IDiagram) object);
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return null;
	}
}
