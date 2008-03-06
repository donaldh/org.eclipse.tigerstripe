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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
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
		AbstractLogicalExplorerNode {

	private IFile modelFile;

	private IFile diagramFile;

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
			IFile newModelFile = ResourcesPlugin.getWorkspace().getRoot().getFile(newModelPath);

			IPath newDiagramPath = targetLocation.getFullPath().append(newName)
					.addFileExtension(getDiagramExtension());
			IFile newDiagramFile = ResourcesPlugin.getWorkspace().getRoot().getFile(newDiagramPath);

			// fill in the content of the model file. Untouched.
			tryResolveModelFile();
			IFile oldModelFile = getModelFile();
			newModelFile.create(oldModelFile.getContents(), true, monitor);

			// fill in the content of the diagram file and make sure the
			// references are updated properly.
			newDiagramFile.create(getUpdatedContentsForDiagramFile(newName),
					true, monitor);
		}
	}

	@Override
	public void performMove(IContainer targetContainer, IProgressMonitor monitor)
			throws CoreException, TigerstripeException {
		if (!closeCorrespondingDiagram()) {
			IResource[] ress = getUnderlyingResources();
			for (IResource res : ress) {
				// Bug 937: looking if the basepackage needs to be updated
				if (res.equals(getModelFile())) {
					Object obj = JavaCore.create(targetContainer);
					if (obj instanceof IPackageFragment) {
						IPackageFragment frag = (IPackageFragment) obj;
						if (frag != null) {
							ResourceSet set = new ResourceSetImpl();
							Resource model = set
									.createResource(URI.createURI(res
											.getLocationURI().toString()));
							try {
								model.load(new HashMap());
								EObject map = model.getContents().get(0);
								int basePackageAttributeIndex = -1;
								String mapType = map.eClass().getName();
								// "3" can be found in VisualEditorPackageImpl
								// or 2 in InstanceDiagramPackageImpl
								if ("Map".equals(mapType))
									basePackageAttributeIndex = 3;
								else if ("InstanceMap".equals(mapType))
									basePackageAttributeIndex = 2;
								if (basePackageAttributeIndex != -1) {
									EAttribute attr = (EAttribute) map.eClass()
											.getEStructuralFeatures().get(
													basePackageAttributeIndex);
									map.eSet(attr, frag.getElementName());
									model.save(new HashMap());
								}
							} catch (IOException e) {
								EclipsePlugin.log(e);
							}
						}
					}
				}

				res.move(targetContainer.getFullPath().append(res.getName()),
						true, monitor);

			}
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
		return new StringBufferInputStream(sb.toString());
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

	public IFile getModelFile() {
		return this.modelFile;
	}

	public IFile getDiagramFile() {
		return this.diagramFile;
	}
}
