/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

public abstract class AbstractDiagramEditor extends FileDiagramEditor {
	
	public AbstractDiagramEditor(boolean hasFlyoutPalette) {
		super(hasFlyoutPalette);
	}

	@Override
	protected void performSaveAs(IProgressMonitor progressMonitor) {

		Shell shell = getSite().getShell();
		IEditorInput input = getEditorInput();

		SaveAsDialog dialog = new SaveAsDialog(shell);

		IFile original = (input instanceof IFileEditorInput) ? ((IFileEditorInput) input)
				.getFile() : null;
		if (original != null)
			dialog.setOriginalFile(original);

		dialog.create();

		IDocumentProvider provider = getDocumentProvider();
		if (provider == null) {
			// editor has been programmatically closed while the dialog was open
			return;
		}

		if (provider.isDeleted(input) && original != null) {
			String message = NLS.bind(
					EditorMessages.Editor_warning_save_delete,
					original.getName());
			dialog.setErrorMessage(null);
			dialog.setMessage(message, IMessageProvider.WARNING);
		}

		if (dialog.open() == Window.CANCEL) {
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return;
		}

		IPath filePath = dialog.getResult();
		if (filePath == null) {
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return;
		}

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		final IFile file = workspaceRoot.getFile(filePath);
		final IEditorInput newInput = new FileEditorInput(file);

		// Check if the editor is already open
		IEditorMatchingStrategy matchingStrategy = getEditorDescriptor()
				.getEditorMatchingStrategy();

		IEditorReference[] editorRefs = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (int i = 0; i < editorRefs.length; i++) {
			if (matchingStrategy.matches(editorRefs[i], newInput)) {
				MessageDialog
						.openWarning(
								shell,
								org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.EditorMessages.FileSaveAs_DialogTitle,
								org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.EditorMessages.FileSaveAs_DialogMessageText);
				return;
			}
		}

		boolean success = false;
		try {
			try {
				IFile modelFile = workspaceRoot.getFile(getModelPath(file
						.getFullPath()));
				final EObject model = getDiagram().getElement();
				ResourceSet resourceSet = model.eResource().getResourceSet();
				final Resource resource = resourceSet.createResource(URI
						.createPlatformResourceURI(modelFile.getFullPath()
								.toString(), true));

				new AbstractTransactionalCommand(getEditingDomain(),
						"Save Model", null) {

					@Override
					protected CommandResult doExecuteWithResult(
							IProgressMonitor monitor, IAdaptable info)
							throws ExecutionException {
						resource.getContents().add(model);
						try {
							resource.save(Collections.emptyMap());
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						getDiagram().setElement(model);
						return CommandResult.newOKCommandResult();
					}
				}.execute(progressMonitor, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			provider.aboutToChange(newInput);
			getDocumentProvider(newInput).saveDocument(progressMonitor,
					newInput,
					getDocumentProvider().getDocument(getEditorInput()), true);
			success = true;
		} catch (CoreException x) {
			IStatus status = x.getStatus();
			if (status == null || status.getSeverity() != IStatus.CANCEL)
				ErrorDialog.openError(shell,
						EditorMessages.Editor_error_saving_title2,
						EditorMessages.Editor_error_saving_message2,
						x.getStatus());
		} finally {
			provider.changed(newInput);
			if (success) {
				setInput(newInput);
				getGraphicalViewer().deselectAll();
			}
		}

		if (progressMonitor != null)
			progressMonitor.setCanceled(!success);
	}

	protected abstract IPath getModelPath(IPath diagramPath);

}
