/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.gmf.synchronization;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * This factory allows to get model file content readers based on a model file
 * extension.
 * 
 * This relies on the "modelFileContentReader" extension point defined in the
 * ui.base plugin to gather all the possibilities in the site.
 * 
 * @author Eric Dillon
 * 
 */
public class ModelFileContentReaderFactory {

	private static HashMap<String, IConfigurationElement> readerHash = null;

	public static synchronized IModelFileContentReader make(DiagramHandle handle)
			throws TigerstripeException {
		if (readerHash == null) {
			getRegisteredReaders();
		}
		IConfigurationElement element = readerHash.get(handle
				.getModelResource().getFileExtension());
		if (element != null) {
			try {
				IModelFileContentReader reader = (IModelFileContentReader) element
						.createExecutableExtension("readerClass");
				return reader;
			} catch (CoreException e) {
				throw new TigerstripeException(
						"Error while trying to instantiate model file reader for "
								+ handle.getDiagramResource().getFullPath()
								+ ": " + e.getMessage(), e);
			}
		}
		throw new TigerstripeException(
				"Couldn't instantiate model file reader for "
						+ handle.getDiagramResource().getFullPath());
	}

	private static void getRegisteredReaders() {
		readerHash = new HashMap<String, IConfigurationElement>();
		IExtension[] extensions = EclipsePlugin.getDefault().getDescriptor()
				.getExtensionPoint("modelFileContentReader").getExtensions();

		for (IExtension extension : extensions) {
			IConfigurationElement[] configElements = extension
					.getConfigurationElements();
			for (IConfigurationElement configElement : configElements) {
				try {
					IModelFileContentReader reader = (IModelFileContentReader) configElement
							.createExecutableExtension("readerClass");
					readerHash.put(reader.getSupportedModelFileExtension(),
							configElement);
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

}
