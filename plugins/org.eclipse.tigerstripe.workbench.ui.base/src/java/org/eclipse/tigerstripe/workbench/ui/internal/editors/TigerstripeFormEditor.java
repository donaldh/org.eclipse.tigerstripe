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
package org.eclipse.tigerstripe.workbench.ui.internal.editors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.FileEditorInput;

public abstract class TigerstripeFormEditor extends FormEditor implements
		IViewPartInputProvider, IResourceChangeListener {

	private EditorUndoManager undoManager;

	public TigerstripeFormEditor() {
		undoManager = createUndoManager();
	}

	public EditorUndoManager getUndoManager() {
		return this.undoManager;
	}

	protected EditorUndoManager createUndoManager() {
		return new EditorUndoManager(this);
	}

	@Override
	public void doSaveAs() {
		// empty, not allowed
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public Object getViewPartInput() {
		return getEditorInput().getAdapter(IJavaElement.class);
	}

	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();

		if (getEditorInput() instanceof IFileEditorInput) {
			FileEditorInput input = (FileEditorInput) getEditorInput();
			IResourceDelta selfDelta = lookforSelf(delta);

			if (selfDelta != null) {
				switch (selfDelta.getKind()) {
				case IResourceDelta.REMOVED:
					closeMyself();
					break;
				}
			}
		} else if (getEditorInput() instanceof ReadOnlyEditorInput) {
			TigerstripeRuntime
					.logTraceMessage(" Ignoring a change of input on a readonly input:"
							+ event);
		}
	}

	@Override
	protected void setInput(IEditorInput input) {
		if (input instanceof FileEditorInput) {
			FileEditorInput fileInput = (FileEditorInput) input;

			if (!fileInput.getFile().getProject().isOpen()) {
				closeMyself();
				dispose();
				return;
			}
		}

		super.setInput(input);
	}

	protected void closeMyself() {
		close(true);
		unRegisterForResourceChanges();
	}

	protected IResourceDelta lookforSelf(IResourceDelta delta) {
		IResourceDelta[] children = delta.getAffectedChildren();
		if (children.length == 0) {
			IResource res = (IResource) getEditorInput().getAdapter(
					IResource.class);
			if (res != null && delta.getFullPath() != null
					&& delta.getFullPath().equals(res.getFullPath()))
				return delta;
			else
				return null;
		} else {
			for (IResourceDelta child : children) {
				IResourceDelta result = lookforSelf(child);
				if (result != null)
					return result;
			}
			return null;
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		registerForResourceChanges();
	}

	@Override
	public void dispose() {
		unRegisterForResourceChanges();
		super.dispose();
	}

	/**
	 * Register this editor for resource changes so it can close itself when the
	 * resource is deleted.
	 * 
	 */
	private void registerForResourceChanges() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
				IResourceChangeEvent.POST_CHANGE);
	}

	/**
	 * Register this editor for resource changes so it can close itself when the
	 * resource is deleted.
	 * 
	 */
	private void unRegisterForResourceChanges() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	@Override
	public void setFocus() {
		super.setFocus();
		IFormPage page = getActivePageInstance();
		if ((page != null) && (page instanceof TigerstripeFormPage)) {
			((TigerstripeFormPage) page).updateFormSelection();
		}
	}
}
