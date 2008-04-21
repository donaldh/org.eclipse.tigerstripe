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
package org.eclipse.tigerstripe.annotation.resource;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.Annotable;
import org.eclipse.tigerstripe.annotation.core.IAnnotable;

/**
 * @author Yuri Strot
 *
 */
public class ResourceAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IAnnotable.class.equals(adapterType)) {
			if (adaptableObject instanceof IResource) {
				IResource resource = (IResource)adaptableObject;
				URI uri = ResourceURIConverter.toURI(resource);
				if (uri != null)
					return new Annotable(uri);
			}
		}
		return null;
    }

	public Class<?>[] getAdapterList() {
		return new Class[] { IAnnotable.class };
    }

}
