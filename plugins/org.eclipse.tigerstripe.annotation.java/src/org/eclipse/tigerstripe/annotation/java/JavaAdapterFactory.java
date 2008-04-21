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
package org.eclipse.tigerstripe.annotation.java;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.tigerstripe.annotation.core.Annotable;
import org.eclipse.tigerstripe.annotation.core.IAnnotable;

/**
 * @author Yuri Strot
 *
 */
public class JavaAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IAnnotable.class.equals(adapterType)) {
			if (adaptableObject instanceof IJavaElement) {
				IJavaElement element = (IJavaElement)adaptableObject;
				URI uri = JavaURIConverter.toURI(element);
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
