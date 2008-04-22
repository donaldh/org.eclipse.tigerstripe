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

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.actions.MoveAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.action.LogicalNodeMoveAction;
import org.eclipse.ui.IWorkbenchSite;

public class TSMoveAction extends MoveAction {

	public TSMoveAction(CompilationUnitEditor editor) {
		super(editor);
	}

	public TSMoveAction(IWorkbenchSite site) {
		super(site);
	}

	@Override
	public void run(IStructuredSelection selection) {
		if (selection.getFirstElement() instanceof AbstractLogicalExplorerNode) {
			LogicalNodeMoveAction action = new LogicalNodeMoveAction("Move",
					getShell());
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
						.getFirstElement()).canBeMoved());
			}
		}
	}

}
