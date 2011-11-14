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
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ReadOnlyArtifactEditorInput;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.ElementWrapper;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.LogicalExplorerNodeFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.navigator.ILinkHelper;
import org.eclipse.ui.part.FileEditorInput;

public class TigerstripeLinkHelper implements ILinkHelper {

	public void activateEditor(IWorkbenchPage aPage,
			IStructuredSelection aSelection) {

		try {
			Object element = aSelection.getFirstElement();
			if (element instanceof AbstractLogicalExplorerNode) {
				IResource resource = ((AbstractLogicalExplorerNode) element)
						.getKeyResource();
				if (resource instanceof IFile) {
					IEditorInput fileInput = new FileEditorInput(
							(IFile) resource);
					IEditorPart editor;
					if ((editor = aPage.findEditor(fileInput)) != null) {
						aPage.bringToTop(editor);
					}
				}
			} else if (element instanceof ElementWrapper) {

				for (IEditorReference ref : aPage.getEditorReferences()) {
					IEditorInput input = ref.getEditorInput();
					if (input instanceof ReadOnlyArtifactEditorInput) {
						IAbstractArtifact artifact = ((ReadOnlyArtifactEditorInput) input)
								.getArtifact();
						if (artifact instanceof IAbstractArtifact) {

							URI uri = TigerstripeURIAdapterFactory
									.toURI((IAbstractArtifact) artifact);

							if (uri != null
									&& uri.equals(((ElementWrapper) element)
											.getUri())) {
								IEditorPart editor = ref.getEditor(true);
								if (editor != null) {
									aPage.bringToTop(editor);
									return;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			BasePlugin.log(e);
		}
	}

	public IStructuredSelection findSelection(IEditorInput input) {

		// Check for artifact from installed module
		if (input instanceof ReadOnlyArtifactEditorInput) {
			IAbstractArtifact artifact = ((ReadOnlyArtifactEditorInput) input)
					.getArtifact();
			if (artifact instanceof IContextProjectAware) {
				IContextProjectAware ctxArt = (IContextProjectAware) artifact;
				return new StructuredSelection(new ElementWrapper(ctxArt));
			}
		}

		IResource resource = ResourceUtil.getResource(input);
		if (resource != null) {
			Object node = LogicalExplorerNodeFactory.getInstance().getNode(
					resource);
			if (node == null) {
				return null;
			}
			return new StructuredSelection(node);
		} else {
			return null;
		}
	}

}
