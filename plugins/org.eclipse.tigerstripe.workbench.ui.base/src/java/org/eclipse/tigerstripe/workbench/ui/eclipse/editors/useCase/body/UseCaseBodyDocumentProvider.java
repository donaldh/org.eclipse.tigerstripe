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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.useCase.body;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.update.internal.core.CoreExceptionWithRootCause;

public class UseCaseBodyDocumentProvider extends FileDocumentProvider {

	private IUseCase getUseCase(Object element) throws TigerstripeException {
		IUseCase result = null;
		if (element instanceof FileEditorInput) {
			IFile file = ((FileEditorInput) element).getFile();
			try {
				result = InternalTigerstripeCore.getIContractSession()
						.makeIUseCase(file.getLocationURI());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		return result;
	}

	@Override
	protected boolean setDocumentContent(IDocument document,
			IEditorInput editorInput, String encoding) throws CoreException {
		try {
			IUseCase useCase = getUseCase(editorInput);
			if (useCase != null)
				document.set(useCase.getBody());
		} catch (TigerstripeException e) {
			IStatus status = new Status(IStatus.WARNING,
					TigerstripePluginConstants.PLUGIN_ID, 222,
					"Couldn't parse use Case", e);
			throw new CoreExceptionWithRootCause(status);
		}

		return true;
	}

	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element,
			IDocument document, boolean overwrite) throws CoreException {
		// TODO Auto-generated method stub
		super.doSaveDocument(monitor, element, document, overwrite);
	}

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		return document;
	}

}
