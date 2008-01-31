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
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;

public class DiagramSynchronizerFactory {

	private static HashMap<String, IConfigurationElement> syncHash = null;

	private static ReentrantLock lock = new ReentrantLock();

	public static IClosedDiagramSynchronizer make(DiagramHandle handle)
			throws TigerstripeException {
		try {
			lock.lock();
			if (syncHash == null) {
				getRegisteredSynchronizers();
			}

			if (!handle.getDiagramResource().exists())
				throw new RemovedDiagramException("Diagram '"
						+ handle.getDiagramResource().getFullPath()
						+ "' has disappeared.");

			IConfigurationElement element = syncHash.get(handle
					.getDiagramResource().getFileExtension());
			if (element != null) {
				try {
					IClosedDiagramSynchronizer synchronizer = (IClosedDiagramSynchronizer) element
							.createExecutableExtension("synchronizerClass");
					synchronizer.setDiagramHandle(handle);
					return synchronizer;
				} catch (CoreException e) {
					throw new TigerstripeException(
							"Error while trying to instantiate synchronizer for "
									+ handle.getDiagramResource().getFullPath()
									+ ": " + e.getMessage(), e);
				}
			}
			throw new TigerstripeException(
					"Couldn't instantiate synchronizer for "
							+ handle.getDiagramResource().getFullPath());
		} finally {
			lock.unlock();
		}
	}

	private static void getRegisteredSynchronizers() {
		syncHash = new HashMap<String, IConfigurationElement>();
		IExtension[] extensions = EclipsePlugin.getDefault().getDescriptor()
				.getExtensionPoint("closedDiagramSynchronizer").getExtensions();

		for (IExtension extension : extensions) {
			IConfigurationElement[] configElements = extension
					.getConfigurationElements();
			for (IConfigurationElement configElement : configElements) {
				try {
					IClosedDiagramSynchronizer synchronizer = (IClosedDiagramSynchronizer) configElement
							.createExecutableExtension("synchronizerClass");
					syncHash.put(synchronizer.getSupportedDiagramExtension(),
							configElement);
				} catch (CoreException e) {
					TigerstripeRuntime.logErrorMessage(
							"CoreException detected", e);
				}
			}
		}

	}

}
