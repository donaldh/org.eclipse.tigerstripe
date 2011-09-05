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
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class RefactoringUtil {
	public static IAbstractArtifact findElementInProjectByFqn(String newFqn,
			ITigerstripeModelProject project) throws TigerstripeException {
		IAbstractArtifact artifact = project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName(newFqn.toString());
		return artifact;
	}

	public static String genarateNewFqnFromNewFName(URI destUri, String newFName) {
		IPath path = new Path(newFName);
		String newFqn = extractFqnFromUri(destUri);
		newFqn += "." + path.removeFileExtension();
		return newFqn;
	}

	public static String extractFqnFromUri(URI uri) {
		if (uri == null)
			return null;
		return uri.lastSegment();
	}

	public static URI getUri(IModelComponent element) {
		try {
			return TigerstripeURIAdapterFactory.toURI(element);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return null;
		}
	}

	public static String extractFqn(IModelComponent element) {
		URI uri = getUri(element);
		if (uri == null)
			return null;
		return extractFqnFromUri(uri);
	}

	public static void collectAllUris(IModelComponent element, Set<URI> set) {
		try {
			URI uri = TigerstripeURIAdapterFactory.toURI(element);
			set.add(uri);
			for (IModelComponent child : element.getContainedModelComponents()) {
				collectAllUris(child, set);
			}
		} catch (TigerstripeException e) {
			// Do nothing
		}
	}
}
