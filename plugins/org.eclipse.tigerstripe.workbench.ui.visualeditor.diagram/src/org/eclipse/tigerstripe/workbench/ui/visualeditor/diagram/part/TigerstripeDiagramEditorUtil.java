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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * @generated
 */
public class TigerstripeDiagramEditorUtil extends IDEEditorUtil {

	/**
	 * @generated
	 */
	public static final IFile createAndOpenDiagram(
			DiagramFileCreator diagramFileCreator, IPath containerPath,
			String fileName, InputStream initialContents, String kind,
			IWorkbenchWindow window, IProgressMonitor progressMonitor,
			boolean openEditor, boolean saveDiagram) {
		IFile diagramFile = TigerstripeDiagramEditorUtil.createNewDiagramFile(
				diagramFileCreator, containerPath, fileName, initialContents,
				kind, window.getShell(), progressMonitor);
		if (diagramFile != null && openEditor) {
			IDEEditorUtil.openDiagram(diagramFile, window, saveDiagram,
					progressMonitor);
		}
		return diagramFile;
	}

	/**
	 * <p>
	 * This method should be called within a workspace modify operation since it
	 * creates resources.
	 * </p>
	 * 
	 * @generated NOT
	 * @return the created file resource, or <code>null</code> if the file was
	 *         not created
	 */
	public static final IFile createNewDiagramFile(
			DiagramFileCreator diagramFileCreator, IPath containerFullPath,
			String fileName, InputStream initialContents, String kind,
			Shell shell, IProgressMonitor progressMonitor) {
		TransactionalEditingDomain editingDomain = WorkspaceEditingDomainFactory.INSTANCE
				.createEditingDomain();
		ResourceSet resourceSet = editingDomain.getResourceSet();
		progressMonitor.beginTask("Creating diagram and model files", 4); //$NON-NLS-1$
		final IProgressMonitor subProgressMonitor = new SubProgressMonitor(
				progressMonitor, 1);
		final IFile diagramFile = diagramFileCreator.createNewFile(
				containerFullPath, fileName, initialContents, shell,
				new IRunnableContext() {
					public void run(boolean fork, boolean cancelable,
							IRunnableWithProgress runnable)
							throws InvocationTargetException,
							InterruptedException {
						runnable.run(subProgressMonitor);
					}
				});
		final Resource diagramResource = resourceSet
				.createResource(URI.createPlatformResourceURI(diagramFile
						.getFullPath().toString()));
		List affectedFiles = new ArrayList();
		affectedFiles.add(diagramFile);

		IPath modelFileRelativePath = diagramFile.getFullPath()
				.removeFileExtension().addFileExtension("vwm"); //$NON-NLS-1$
		IFile modelFile = diagramFile.getParent().getFile(
				new Path(modelFileRelativePath.lastSegment()));
		final Resource modelResource = resourceSet.createResource(URI
				.createPlatformResourceURI(modelFile.getFullPath().toString()));
		affectedFiles.add(modelFile);

		final String kindParam = kind;
		AbstractTransactionalCommand command = new AbstractTransactionalCommand(
				editingDomain, "Creating diagram and model", affectedFiles) { //$NON-NLS-1$
			@Override
			protected CommandResult doExecuteWithResult(
					IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				Map model = createInitialModel();
				modelResource.getContents().add(createInitialRoot(model));
				Diagram diagram = ViewService
						.createDiagram(
								model,
								kindParam,
								TigerstripeDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
				if (diagram != null) {
					diagramResource.getContents().add(diagram);
					diagram.setName(diagramFile.getName());
					diagram.setElement(model);
				}
				try {
					java.util.Map options = new HashMap();
					options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
					modelResource.save(options);
					diagramResource.save(Collections.EMPTY_MAP);
				} catch (IOException e) {

					TigerstripeDiagramEditorPlugin.getInstance().logError(
							"Unable to store model and diagram resources", e); //$NON-NLS-1$
				}
				return CommandResult.newOKCommandResult();
			}
		};

		try {
			OperationHistoryFactory.getOperationHistory().execute(command,
					new SubProgressMonitor(progressMonitor, 1), null);
		} catch (ExecutionException e) {
			TigerstripeDiagramEditorPlugin.getInstance().logError(
					"Unable to create model and diagram", e); //$NON-NLS-1$
		}

		try {
			modelFile.setCharset(
					"UTF-8", new SubProgressMonitor(progressMonitor, 1)); //$NON-NLS-1$
		} catch (CoreException e) {
			TigerstripeDiagramEditorPlugin.getInstance().logError(
					"Unable to set charset for model file", e); //$NON-NLS-1$
		}
		try {
			diagramFile.setCharset(
					"UTF-8", new SubProgressMonitor(progressMonitor, 1)); //$NON-NLS-1$
		} catch (CoreException e) {
			TigerstripeDiagramEditorPlugin.getInstance().logError(
					"Unable to set charset for diagram file", e); //$NON-NLS-1$
		}

		return diagramFile;
	}

	/**
	 * Create a new instance of domain element associated with canvas. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static Map createInitialModel() {
		return VisualeditorFactory.eINSTANCE.createMap();
	}

	/**
	 * @generated
	 */
	private static EObject createInitialRoot(Map model) {
		return model;
	}
}
