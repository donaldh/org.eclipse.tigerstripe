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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.generator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.FileEditorInput;

public abstract class GeneratorDescriptorSectionPart extends
		TigerstripeSectionPart {

	public GeneratorDescriptorSectionPart(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit, int style) {
		super(page, parent, toolkit, style);
	}

	public ITigerstripeGeneratorProject getIPluggablePluginProject() {
		GeneratorDescriptorEditor editor = (GeneratorDescriptorEditor) getPage()
				.getEditor();
		return editor.getProjectHandle();
	}

	protected boolean projectHasErrors() {
		FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
		IFile projectDesc = input.getFile();
		IProject project = projectDesc.getProject();
		try {
			IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true,
					IResource.DEPTH_INFINITE);
			for (int i = 0; i < markers.length; i++) {
				if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(
						IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
					MessageBox dialog = new MessageBox(getSection().getShell(),
							SWT.ICON_ERROR | SWT.OK);
					dialog
							.setMessage("This project contains errors. \nPlease fix these errors before deploying it.");
					dialog.setText("Plugin Project Error");
					dialog.open();
					return true;
				}
			}
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
		return false;
	}

}
