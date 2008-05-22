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
package org.eclipse.tigerstripe.test_common;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.osgi.framework.BundleContext;

/**
 * THis is a convenient class for all resources store here for testing
 * 
 * @author erdillon
 * 
 */
public class ResourcesUtils {

	/**
	 * Copies the content of the resources/ dir into the project as a
	 * "resources/" dir
	 * 
	 * @param project
	 * @param context
	 * @param obj -
	 *            an object from the current classloader
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void copyAll(IProject project, BundleContext context,
			Object obj) throws Exception {
		IFolder resourcesFolder = project.getFolder("resources");
		resourcesFolder.create(true, true, null);

		for (Enumeration<URL> e = context.getBundle().findEntries("resources",
				"*", true); e.hasMoreElements();) {
			URL o = e.nextElement();
			String path = o.getPath();
			InputStream stream = obj.getClass().getResourceAsStream(path);
			IFile file = project.getFile(path);
			file.create(stream, true, null);
		}
	}
}
