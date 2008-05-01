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

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.tigerstripe.annotation.core.Annotable;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotable;
//import org.eclipse.tigerstripe.annotation.java.JavaURIConverter;

//import org.eclipse.tigerstripe.metamodel.IModelComponent;

// Deprecated packages
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;


/**
 * @author John Worrell
 *
 */
public class TSModelAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IAnnotable.class.equals(adapterType)) {
			IModelComponent modelComponent = null;
			if (adaptableObject instanceof IModelComponent) {
				modelComponent = (IModelComponent)adaptableObject;
			}
			else
			if (adaptableObject instanceof IJavaElement) {
				IJavaElement element = (IJavaElement)adaptableObject;
				modelComponent = TSModelURIConverter.toModelComponent(element);
			}
			if(modelComponent != null)
			{
				URI uri = null;
				try {
					uri = TSModelURIConverter.toURI(modelComponent);
				} catch (TigerstripeException e) {
					AnnotationPlugin.log(e);
					return null;
				}
				if (uri != null)
					return new Annotable(uri);
			}
		}
		return null;
    }

	/**
	 * Returns an array of the <code>Class</code> objects that identify the
	 * types adapted by this <code>IAdapterFactory</code> implementation. In
	 * this the single type is <code>IAnnotable</code>
	 */
	public Class<?>[] getAdapterList() {
		return new Class[] { IAnnotable.class };
    }

}
