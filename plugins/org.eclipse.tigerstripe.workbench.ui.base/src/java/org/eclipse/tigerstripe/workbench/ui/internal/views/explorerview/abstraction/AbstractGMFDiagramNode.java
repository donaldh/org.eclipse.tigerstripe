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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.diagram.IDiagram;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramRefactorHelper;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.HeadlessDiagramHandle;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Provides a representation of a logical node to represent a GMF diagram, i.e.
 * 2 files that need to be handled as a single logical entity.
 * 
 * The logical node is created using the diagram file as the key/trigger. The
 * model file is only set lazilly when needed.
 * 
 * @author Eric Dillon
 * 
 */
public abstract class AbstractGMFDiagramNode extends
		AbstractLogicalExplorerNode implements IDiagram {

	private IFile modelFile;

	private final IFile diagramFile;

	private boolean shouldCancel;

	protected AbstractGMFDiagramNode(IFile diagramFile) {
		this.diagramFile = diagramFile;
	}

	public void setModelFile(IFile modelFile) {
		this.modelFile = modelFile;
	}

	protected abstract String getModelExtension();

	protected abstract String getDiagramExtension();

	protected void tryResolveModelFile() {
		if (getModelFile() == null) {
			IFile diagFile = getDiagramFile();
			if (diagFile != null && diagFile.exists()) {
				IPath path = diagFile.getProjectRelativePath();
				path = path.removeFileExtension();
				path = path.addFileExtension(getModelExtension());
				IResource res = diagFile.getProject().findMember(path);
				if (res instanceof IFile) {
					setModelFile((IFile) res);
				}
			}
		}
	}

	@Override
	public void performCopy(String newName, IContainer targetLocation,
			IProgressMonitor monitor) throws CoreException,
			TigerstripeException {
		if (!closeCorrespondingDiagram()) {
			// Create both Resources
			IPath newModelPath = targetLocation.getFullPath().append(newName)
					.addFileExtension(getModelExtension());
			IFile newModelFile = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(newModelPath);

			IPath newDiagramPath = targetLocation.getFullPath().append(newName)
					.addFileExtension(getDiagramExtension());
			IFile newDiagramFile = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(newDiagramPath);

			// fill in the content of the model file. Untouched.
			tryResolveModelFile();
			IFile oldModelFile = getModelFile();
			newModelFile.create(oldModelFile.getContents(), true, monitor);

			// fill in the content of the diagram file and make sure the
			// references are updated properly.
			newDiagramFile.create(getUpdatedContentsForDiagramFile(newName),
					true, monitor);

			// Now need to potentially update the base package for the new
			// diagram
			DiagramRefactorHelper.updatePackage(newModelFile);
		}
	}

	/**
	 * Updates the base package for the given model file if it is hosted in a
	 * "package" within the workspace.
	 * 
	 * @param modelFile
	 */
	@Override
	public void performMove(IContainer targetContainer, IProgressMonitor monitor)
			throws CoreException, TigerstripeException {
		if (!closeCorrespondingDiagram()) {
			HeadlessDiagramHandle handle = DiagramRefactorHelper
					.getDiagramHandle(diagramFile);
			DiagramRefactorHelper.performDiagramMove(targetContainer, handle,
					monitor);
		}
	}

	@Override
	public boolean canBeMoved() {
		return true;
	}

	@Override
	public boolean canBeRenamed() {
		return true;
	}

	/**
	 * 
	 * @return true if operation should be canceled.
	 */
	protected boolean closeCorrespondingDiagram() {
		IWorkbenchWindow windows[] = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage pages[] = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IEditorReference[] refs = pages[j].getEditorReferences();
				for (IEditorReference ref : refs) {
					IEditorPart part = ref.getEditor(false);
					if (part != null
							&& part.getTitle().equals(
									getDiagramFile().getName())) {
						final IEditorPart fPart = part;
						Display display = Display.getDefault();
						shouldCancel = false;
						display.syncExec(new Runnable() {
							public void run() {
								shouldCancel = !PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow()
										.getActivePage().closeEditor(fPart,
												true);
							}
						});
					}
				}
			}
		}
		return shouldCancel;
	}

	@Override
	public void performRename(String newName, IProgressMonitor monitor)
			throws CoreException, TigerstripeException {

		if (!closeCorrespondingDiagram()) {

			// need to rename both files and change the corresponding reference
			IResource[] ress = getUnderlyingResources();
			diagramFile.setContents(getUpdatedContentsForDiagramFile(newName),
					true, true, monitor);

			// rename both now
			for (IResource res : ress) {
				IPath srcPath = res.getFullPath();
				IPath destPath = srcPath.removeLastSegments(1);
				destPath = destPath.append(newName);
				destPath = destPath.addFileExtension(res.getFileExtension());
				res.move(destPath, true, monitor);
			}
		}
	}

	protected InputStream getUpdatedContentsForDiagramFile(String newName)
			throws CoreException {

		IFile diagFile = getDiagramFile();

		IFile modelFile = getModelFile();
		String diagNamePattern = diagFile.getName();
		String newDiagNamePattern = newName + "." + diagFile.getFileExtension();
		String modelNamePattern = modelFile.getName();
		String newModelNamePattern = newName + "."
				+ modelFile.getFileExtension();

		// change ref in diagram file
		IFile diagramFile = (IFile) getKeyResource();
		InputStream contentStream = diagramFile.getContents();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				contentStream));
		String line;
		StringBuffer sb = new StringBuffer();
		try {
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll(diagNamePattern, newDiagNamePattern);
				line = line.replaceAll(modelNamePattern, newModelNamePattern);
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			EclipsePlugin.log(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	@Override
	public IResource[] getUnderlyingResources() {
		List<IResource> res = new ArrayList<IResource>();

		if (modelFile == null)
			tryResolveModelFile();

		if (modelFile != null)
			res.add(modelFile);
		if (diagramFile != null)
			res.add(diagramFile);
		return res.toArray(new IResource[res.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction
	 * .IGMFDiagramNode#getModelFile()
	 */
	public IFile getModelFile() {
		return this.modelFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction
	 * .IGMFDiagramNode#getDiagramFile()
	 */
	public IFile getDiagramFile() {
		return this.diagramFile;
	}

}
