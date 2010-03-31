/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.refactor.diagrams;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.HeadlessDiagramHandle;

/**
 * This helper contains basic logic to figure out where how to deal (basic
 * stuff) with Diagrams without having to load GMF or have any dependency on UI.
 * 
 * @author erdillon
 * 
 */
public class DiagramRefactorHelper {

	private static final String[] diagramFileExtensions = { "wvd", "wod" }; // FIXME:
	// use
	// extension

	private static final String[] modelFileExtensions = { "vwm", "owm" }; // FIXME:

	// use

	/**
	 * Returns a DiagramHandle for the given resource. The resource can either
	 * be the model file or the diagram file.
	 * 
	 * @param resource
	 * @return
	 */
	public static HeadlessDiagramHandle getDiagramHandle(IResource resource) {
		if (resource instanceof IFile) {
			IResource diagFile = null;
			IResource modelFile = null;
			for (int i = 0; i < diagramFileExtensions.length; i++) {
				String ext = diagramFileExtensions[i];
				if (ext.equals(resource.getFileExtension())) {
					diagFile = resource;
					IResource m = resource.getParent().findMember(
							resource.getName().replace(ext,
									modelFileExtensions[i]));
					if (m != null) {
						modelFile = m;
						break;
					}
				}
			}

			if (diagFile == null || modelFile == null) {
				for (int i = 0; i < modelFileExtensions.length; i++) {
					String ext = modelFileExtensions[i];
					if (ext.equals(resource.getFileExtension())) {
						modelFile = resource;
						IResource d = resource.getParent().findMember(
								resource.getName().replace(ext,
										diagramFileExtensions[i]));
						if (d != null) {
							diagFile = d;
							break;
						}
					}
				}
			}

			ITigerstripeModelProject tsProject = (ITigerstripeModelProject) resource
					.getProject().getAdapter(ITigerstripeModelProject.class);

			if (diagFile != null && modelFile != null && tsProject != null) {
				HeadlessDiagramHandle handle = new HeadlessDiagramHandle(
						diagFile, modelFile, tsProject);
				return handle;
			}
		}
		return null;
	}

	public static void applyDelta(DiagramChangeDelta diagramDelta,
			IProgressMonitor monitor) throws TigerstripeException {
		try {
			HeadlessDiagramHandle handle = diagramDelta.getAffDiagramHandle();
			IPath destPath = diagramDelta.getDestinationPath();
			IFolder f = ResourcesPlugin.getWorkspace().getRoot().getFolder(
					destPath);
			if (!f.exists()) {
				// this may happen when dragging diagrams across projects.
				// The recipient container may not have been created yet at this
				// stage
				createRecursive(f, monitor);
			}
			performDiagramMove(f, handle.getUnderlyingResources(), monitor);
			IResource movedModelResoource = f.findMember(handle
					.getModelResource().getName());
			updatePackage(movedModelResoource);
		} catch (CoreException e) {
			throw new TigerstripeException("While handling diagram move: "
					+ diagramDelta.getAffDiagramHandle().getDiagramResource()
							.getName(), e);
		}
	}

	protected static void createRecursive(IFolder f, IProgressMonitor monitor)
			throws CoreException {
		IContainer parent = f.getParent();
		if (!parent.exists()) {
			if (parent instanceof IFolder) {
				IFolder pf = (IFolder) parent;
				createRecursive(pf, monitor);
			}
		}
		f.create(true, true, monitor);
	}

	public static void performDiagramMove(IContainer targetContainer,
			IResource[] underlyingResources, IProgressMonitor monitor)
			throws CoreException {
		for (IResource res : underlyingResources) {
			try {
				IPath newPath = targetContainer.getFullPath().append(
						res.getName());
				res.move(newPath, true, monitor);
			} catch (Exception e) {
				BasePlugin.log(e);
			}
		}

	}

	/**
	 * When a diagram is moved and this diagram is inside a package, it is
	 * expected that the "base package" for that diagram be updated as well.
	 * 
	 * This method takes care of that by going through the model file of a
	 * diagram (class or instance) and updating the corresponding package
	 * string.
	 * 
	 * @param modelFile
	 */
	public static void updatePackage(IResource modelFile) {
		IContainer targetContainer = modelFile.getParent();
		Object obj = JavaCore.create(targetContainer);
		if (obj instanceof IPackageFragment) {
			IPackageFragment frag = (IPackageFragment) obj;
			if (frag != null) {
				ResourceSet set = new ResourceSetImpl();
				Resource model = set.createResource(URI.createURI(modelFile
						.getLocationURI().toString()));
				try {
					model.load(new HashMap<Object, Object>());
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
						model.save(new HashMap<Object, Object>());
					}
				} catch (IOException e) {
					BasePlugin.log(e);
				}
			}
		}
	}

}
