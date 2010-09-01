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
package org.eclipse.tigerstripe.workbench.ui.explorer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.LogicalExplorerNodeFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.navigator.ILinkHelper;
import org.eclipse.ui.part.FileEditorInput;

public class TigerstripeLinkHelper implements ILinkHelper {

	public void activateEditor(IWorkbenchPage aPage,
			IStructuredSelection aSelection) {
		Object element = aSelection.getFirstElement();
		if (element instanceof AbstractLogicalExplorerNode) {
			IResource resource = ((AbstractLogicalExplorerNode) element)
					.getKeyResource();
			if (resource instanceof IFile) {
				IEditorInput fileInput = new FileEditorInput((IFile) resource);
				IEditorPart editor;
				if ((editor = aPage.findEditor(fileInput)) != null)
					aPage.bringToTop(editor);
			}
		}
	}

	public IStructuredSelection findSelection(IEditorInput input) {
		IResource resource = ResourceUtil.getResource(input);
		if (resource != null) {
			return new StructuredSelection(LogicalExplorerNodeFactory
					.getInstance().getNode(resource));
		} else {
			return null;
		}
	}

}
