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
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

/**
 * @author John Worrell
 * 
 */
public class TSModelAnnotationProvider implements IAnnotationProvider {

	public Object getObject(URI uri) {
		return TSModelURIConverter.isRelated(uri) ? TSModelURIConverter
				.toComponent(uri) : null;
	}

	public URI getUri(Object object) {
		try {
			if (object instanceof IModelComponent)
				return TSModelURIConverter.toURI((IModelComponent) object);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return null;
	}

}
