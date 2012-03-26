/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.stereotypes;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IMethodElement;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;

public class StereotypeCapableModelHelper {

	public static URI getUri(IStereotypeCapable capable) {
		try {
			if (capable instanceof IModelComponent) {
				return TigerstripeURIAdapterFactory.toURI((IModelComponent)capable);
			} else if (capable instanceof IArgument) {
				return TigerstripeURIAdapterFactory.toURI((IArgument)capable);
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return null;
	}
	
	public static IAbstractArtifactInternal getArtifact(IStereotypeCapable capable) {
		if (capable instanceof IAbstractArtifactInternal) {
			return (IAbstractArtifactInternal) capable;
		} else if (capable instanceof IModelComponent) {
			IModelComponent mc = (IModelComponent) capable;
			IModelComponent parent = mc.getContainingModelComponent();
			return getArtifact(parent);
		} else if (capable instanceof IMethodElement) {
			IMethodElement me = (IMethodElement) capable;
			IModelComponent parent = me.getContainingMethod();
			return getArtifact(parent);
		}	
		return null;
	}

	public static IStereotypeCapable getCapable(Object element) {
		if (element == null)
			return null;
		if (element instanceof IStereotypeCapable) {
			return (IStereotypeCapable) element;
		}
		if (element instanceof URI) {
			return getCapable((URI) element);
		}
		IStereotypeCapable capable = getCapable(IStereotypeCapable.class,
				element);
		if (capable == null) {
			capable = getCapable(IModelComponent.class, element);
		}
		return capable;
	}

	private static <T extends IStereotypeCapable> IStereotypeCapable getCapable(
			Class<T> clazz, Object element) {
		Object result = null;
		if (element instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) element;
			IStereotypeCapable capable = (IStereotypeCapable) adaptable
					.getAdapter(clazz);
			result = capable;
		}
		if (result == null) {
			result = Platform.getAdapterManager().getAdapter(element, clazz);
		}
		return result == null ? null : clazz.cast(result);
	}

	private static IStereotypeCapable getCapable(URI uri) {
		IStereotypeCapable capable = null;
			capable = TigerstripeURIAdapterFactory.uriToComponent(uri);
		if (capable == null) {
			capable = TigerstripeURIAdapterFactory.uriToArgument(uri);
		}
		return capable;
	}
}
