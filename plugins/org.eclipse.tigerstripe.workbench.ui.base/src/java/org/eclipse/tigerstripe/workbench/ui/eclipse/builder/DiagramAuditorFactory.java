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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.gmf.synchronization.DiagramHandle;
import org.eclipse.ui.IFileEditorInput;

/**
 * A factory for Diagram Auditors that is based on the "diagramAuditor"
 * extension point
 * 
 * @author Eric Dillon
 * 
 */
public class DiagramAuditorFactory {

	/**
	 * Returns an array of all the file extensions for which a Diagram Auditor
	 * has been registered thru the extension point.
	 * 
	 * @return
	 */
	public static synchronized String[] getAuditableDiagramfileExtensions() {
		if (auditorHash == null) {
			getRegisteredAuditors();
		}
		return auditorHash.keySet().toArray(
				new String[auditorHash.keySet().size()]);
	}

	private static HashMap<String, IConfigurationElement> auditorHash = null;

	public static synchronized IDiagramAuditor make(DiagramEditor editor)
			throws TigerstripeException {
		if (auditorHash == null) {
			getRegisteredAuditors();
		}

		if (editor.getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) editor.getEditorInput();
			DiagramHandle handle = new DiagramHandle(input.getFile());
			return make(handle);
		}

		throw new TigerstripeException(
				"Couldn't instantiate diagram auditor for this diagram");
	}

	public static synchronized IDiagramAuditor make(DiagramHandle handle)
			throws TigerstripeException {
		if (auditorHash == null) {
			getRegisteredAuditors();
		}
		IConfigurationElement element = auditorHash.get(handle
				.getDiagramResource().getFileExtension());
		if (element != null) {
			try {
				IDiagramAuditor reader = (IDiagramAuditor) element
						.createExecutableExtension("auditorClass");
				return reader;
			} catch (CoreException e) {
				throw new TigerstripeException(
						"Error while trying to instantiate a diagram auditor for "
								+ handle.getDiagramResource().getFullPath()
								+ ": " + e.getMessage(), e);
			}
		}
		throw new TigerstripeException(
				"Couldn't instantiate diagram auditor for "
						+ handle.getDiagramResource().getFullPath());
	}

	private static void getRegisteredAuditors() {
		auditorHash = new HashMap<String, IConfigurationElement>();
		IExtension[] extensions = EclipsePlugin.getDefault().getDescriptor()
				.getExtensionPoint("diagramAuditor").getExtensions();

		for (IExtension extension : extensions) {
			IConfigurationElement[] configElements = extension
					.getConfigurationElements();
			for (IConfigurationElement configElement : configElements) {
				try {
					IDiagramAuditor auditor = (IDiagramAuditor) configElement
							.createExecutableExtension("auditorClass");
					auditorHash.put(auditor.getSupportedFileExtension(),
							configElement);
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

}
