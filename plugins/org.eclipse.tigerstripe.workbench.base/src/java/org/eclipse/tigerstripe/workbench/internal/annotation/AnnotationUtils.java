/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.osgi.framework.Bundle;

public class AnnotationUtils {

	private static final String ANNOTATION_TYPE_EXTPT = "org.eclipse.tigerstripe.annotation.core.annotationType";

	public static boolean isModelAnnotation(Annotation annotation) {
		URI uri = annotation.getUri();
		return TigerstripeURIAdapterFactory.isRelated(uri);
//		if (!TigerstripeURIAdapterFactory.isRelated(uri))
//			return false;
//
//		IModelComponent component = TigerstripeURIAdapterFactory
//				.uriToComponent(uri);
//
//		return component != null;
	}

	public static Annotation[] extractModelAnnotations(Annotation[] annotations) {
		ArrayList<Annotation> result = new ArrayList<Annotation>();
		for (Annotation annotation : annotations) {
			if (isModelAnnotation(annotation))
				result.add(annotation);
		}
		return result.toArray(new Annotation[result.size()]);
	}

	/**
	 * Returns an array with all pluginId of annotation defining plugins.
	 * 
	 * @return
	 */
	public static String[] getAnnotationPluginIds() {
		Set<String> result = new HashSet<String>();

		IExtensionPoint ext = Platform.getExtensionRegistry()
				.getExtensionPoint(ANNOTATION_TYPE_EXTPT);

		IExtension[] exts = ext.getExtensions();
		for (IExtension ex : exts) {
			result.add(ex.getNamespaceIdentifier());
		}
		return result.toArray(new String[result.size()]);
	}

	public static IPath getAnnotationPluginPath(String pluginId) {
		Bundle b = Platform.getBundle(pluginId);
		try {
			File bFile = FileLocator.getBundleFile(b);
			IPath pPath = (new Path(bFile.getAbsolutePath())).makeAbsolute();

			if (!"jar".equals(pPath.getFileExtension())) {
				pPath = pPath.append("bin");
			}
			return pPath;
		} catch (IOException e) {
			BasePlugin.log(e);
		}
		return new Path("unknown_location_for_" + pluginId);
	}
}