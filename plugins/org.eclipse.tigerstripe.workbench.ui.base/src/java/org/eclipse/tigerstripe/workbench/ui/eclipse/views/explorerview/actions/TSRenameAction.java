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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions;

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.actions.RenameAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.action.LogicalNodeRenameAction;
import org.eclipse.ui.IWorkbenchSite;

public class TSRenameAction extends RenameAction {

	public TSRenameAction(IWorkbenchSite site) {
		super(site);
	}

	public TSRenameAction(CompilationUnitEditor editor) {
		super(editor);
	}

	@Override
	public void run(IStructuredSelection selection) {
		if (selection.getFirstElement() instanceof AbstractLogicalExplorerNode) {
			LogicalNodeRenameAction action = new LogicalNodeRenameAction(
					"Rename", getShell());
			action.selectionChanged(selection);
			action.run();
		} else
			super.run(selection);

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) event
					.getSelection();
			if (ssel.getFirstElement() instanceof AbstractLogicalExplorerNode) {
				setEnabled(((AbstractLogicalExplorerNode) ssel
						.getFirstElement()).canBeRenamed());
			}
		}
	}

}
