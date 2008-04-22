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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.ReadOnlyEditorInput;

public class ReadOnlyDescriptorEditorInput extends ReadOnlyEditorInput {

	private ITigerstripeModelProject tsProject;

	public ReadOnlyDescriptorEditorInput(JarEntryFile jarEntryFile) {
		super(jarEntryFile);
	}

	private void extractTSProject() {
		try {
			TigerstripeProject embeddedProject = parseEmbeddedProjectDescriptor();
			tsProject = new ReadOnlyTigerstripeProjectHandle(embeddedProject);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private TigerstripeProject parseEmbeddedProjectDescriptor()
			throws TigerstripeException {
		try {
			InputStream stream = getJarEntryFile().getContents();
			Reader reader = new InputStreamReader(stream);
			TigerstripeProject embeddedProject = new TigerstripeProject(null);
			embeddedProject.parse(reader);
			return embeddedProject;
		} catch (CoreException e) {
			EclipsePlugin.log(e);
			throw new TigerstripeException(
					"Error while parsing embedded descriptor: "
							+ e.getLocalizedMessage(), e);
		}
	}

	public String getToolTipText() {
		return "Read-only Descriptor from Module";
	}

	public ITigerstripeModelProject getTSProject() {
		if (tsProject == null) {
			extractTSProject();
		}

		return this.tsProject;
	}

}
