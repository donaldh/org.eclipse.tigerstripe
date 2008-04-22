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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.convert.ConvertArtifactDialog;
import org.eclipse.ui.IWorkbenchSite;

public class ConvertArtifactAction extends SelectionDispatchAction {

	private IStructuredSelection currentSelection;

	/**
	 * Creates a new <code>RenameAction</code>. The action requires that the
	 * selection provided by the site's selection provider is of type <code>
	 * org.eclipse.jface.viewers.IStructuredSelection</code>.
	 * 
	 * @param site
	 *            the site providing context information for this action
	 */
	public ConvertArtifactAction(IWorkbenchSite site) {
		super(site);
		setText("Convert Artifact...");
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
		// IJavaHelpContextIds.RENAME_ACTION);
	}

	/**
	 * Note: This constructor is for internal use only. Clients should not call
	 * this constructor.
	 * 
	 * @param editor
	 *            the compilation unit editor
	 */
	public ConvertArtifactAction(CompilationUnitEditor editor) {
		this(editor.getEditorSite());
	}

	/*
	 * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection sel = event.getSelection();
		if (sel instanceof IStructuredSelection) {
			currentSelection = (IStructuredSelection) sel;
		}
		setEnabled(computeEnabledState());
	}

	/*
	 * @see SelectionDispatchAction#update(ISelection)
	 */
	@Override
	public void update(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			currentSelection = (IStructuredSelection) selection;
		}
		setEnabled(computeEnabledState());
	}

	private boolean computeEnabledState() {
		if (currentSelection == null
				|| currentSelection.getFirstElement() == null)
			return false;

		if (currentSelection.getFirstElement() instanceof ICompilationUnit)
			return true;
		else
			return false;
	}

	@Override
	public void run(IStructuredSelection selection) {
		if (selection instanceof IStructuredSelection) {
			currentSelection = selection;
		}
		ConvertArtifactDialog dialog = new ConvertArtifactDialog(getShell(),
				currentSelection);
		dialog.open();
	}

	@Override
	public void run(ITextSelection selection) {
	}
}
