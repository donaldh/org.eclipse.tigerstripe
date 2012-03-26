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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * @generated
 */
public class TigerstripeCreationWizardPage extends EditorWizardPage {

	/**
	 * @generated
	 */
	public TigerstripeCreationWizardPage(IWorkbench workbench,
			IStructuredSelection selection) {
		super("CreationWizardPage", workbench, selection); //$NON-NLS-1$
		setTitle("Create Class Diagram");
		setDescription("Create a new Class diagram.");
	}

	/**
	 * @generated
	 */
	@Override
	public IFile createAndOpenDiagram(IPath containerPath, String fileName,
			InputStream initialContents, String kind, IWorkbenchWindow dWindow,
			IProgressMonitor progressMonitor, boolean saveDiagram) {
		return TigerstripeDiagramEditorUtil.createAndOpenDiagram(
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
		return TigerstripeDiagramFileCreator.getInstance();
	}

	/**
	 * @generated
	 */
	@Override
	protected String getDiagramKind() {
		return MapEditPart.MODEL_ID;
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
			IResource cresource = ResourcesPlugin.getWorkspace().getRoot().findMember(getContainerFullPath());
			
			if (cresource != null) {
				Object modelProject = cresource.getProject().getAdapter(ITigerstripeModelProject.class);
				if (modelProject == null) {
					setErrorMessage("Diagram can be created only for model project");
					return false;
				}
			}
			
			// Bug # 214587
			// Check for a simple file name
			String fname = path.removeFileExtension().lastSegment();
			String regex = "^[a-zA-Z][0-9a-zA-Z _-]*$";
			if (! fname.matches(regex)){
				return false;
			}
			path = path.removeFileExtension().addFileExtension("wvm"); //$NON-NLS-1$
			
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
