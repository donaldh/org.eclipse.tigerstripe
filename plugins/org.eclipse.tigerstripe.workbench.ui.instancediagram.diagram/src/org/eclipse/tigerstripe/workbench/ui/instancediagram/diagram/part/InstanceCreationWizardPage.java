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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * @generated
 */
public class InstanceCreationWizardPage extends EditorWizardPage {

	/**
	 * @generated
	 */
	public InstanceCreationWizardPage(IWorkbench workbench,
			IStructuredSelection selection) {
		super("CreationWizardPage", workbench, selection); //$NON-NLS-1$
		setTitle("Create Instance Diagram");
		setDescription("Create a new Instance diagram.");
	}

	/**
	 * @generated
	 */
	@Override
	public IFile createAndOpenDiagram(IPath containerPath, String fileName,
			InputStream initialContents, String kind, IWorkbenchWindow dWindow,
			IProgressMonitor progressMonitor, boolean saveDiagram) {
		return InstanceDiagramEditorUtil.createAndOpenDiagram(
				getDiagramFileCreator(), containerPath, fileName,
				initialContents, kind, dWindow, progressMonitor,
				isOpenNewlyCreatedDiagramEditor(), saveDiagram);
	}

	/**
	 * @generated
	 */
	@Override
	protected String getDefaultFileName() {
		return "default"; //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	@Override
	public DiagramFileCreator getDiagramFileCreator() {
		return InstanceDiagramFileCreator.getInstance();
	}

	/**
	 * @generated
	 */
	@Override
	protected String getDiagramKind() {
		return InstanceMapEditPart.MODEL_ID;
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected boolean validatePage() {
		if (super.validatePage()) {
			String fileName = getFileName();
			if (fileName == null)
				return false;
			// appending file extension to correctly process file names
			// including "." symbol
			IPath path = getContainerFullPath()
					.append(
							getDiagramFileCreator().appendExtensionToFileName(
									fileName));
			path = path.removeFileExtension().addFileExtension("owm"); //$NON-NLS-1$
			if (ResourcesPlugin.getWorkspace().getRoot().exists(path)) {
				setErrorMessage("Model File already exists: "
						+ path.lastSegment());
				return false;
			}
			return true;
		}
		return false;
	}

}
