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

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerMessages;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;

class GotoTypeAction extends Action {

	private TigerstripeExplorerPart fPackageExplorer;

	GotoTypeAction(TigerstripeExplorerPart part) {
		super();
		setText(TSExplorerMessages.GotoType_action_label);
		setDescription(TSExplorerMessages.GotoType_action_description);
		fPackageExplorer = part;
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IJavaHelpContextIds.GOTO_TYPE_ACTION);
	}

	@Override
	public void run() {
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		SelectionDialog dialog = null;
		try {
			dialog = JavaUI.createTypeDialog(shell, new ProgressMonitorDialog(
					shell), SearchEngine.createWorkspaceScope(),
					IJavaElementSearchConstants.CONSIDER_TYPES, false);
		} catch (JavaModelException e) {
			String title = getDialogTitle();
			String message = TSExplorerMessages.GotoType_error_message;
			ExceptionHandler.handle(e, title, message);
			return;
		}

		dialog.setTitle(getDialogTitle());
		dialog.setMessage(TSExplorerMessages.GotoType_dialog_message);
		if (dialog.open() == IDialogConstants.CANCEL_ID)
			return;

		Object[] types = dialog.getResult();
		if (types != null && types.length > 0) {
			gotoType((IType) types[0]);
		}
	}

	private void gotoType(IType type) {
		ICompilationUnit cu = (ICompilationUnit) type
				.getAncestor(IJavaElement.COMPILATION_UNIT);
		IJavaElement element = null;
		if (cu != null) {
			// element= JavaModelUtil.toOriginal(cu);
			// replaced by for 3.2
			if (cu.isWorkingCopy()) {
				element = cu.getPrimary();
			} else {
				element = cu;
			}
		} else {
			element = type.getAncestor(IJavaElement.CLASS_FILE);
		}
		if (element != null) {
			TigerstripeExplorerPart view = TigerstripeExplorerPart
					.openInActivePerspective();
			if (view != null) {
				view.selectReveal(new StructuredSelection(element));
				if (!element.equals(getSelectedElement(view))) {
					MessageDialog
							.openInformation(
									fPackageExplorer.getSite().getShell(),
									getDialogTitle(),
									Messages
											.format(
													TSExplorerMessages.PackageExplorer_element_not_present,
													element.getElementName()));
				}
			}
		}
	}

	private Object getSelectedElement(TigerstripeExplorerPart view) {
		return ((IStructuredSelection) view.getSite().getSelectionProvider()
				.getSelection()).getFirstElement();
	}

	private String getDialogTitle() {
		return TSExplorerMessages.GotoType_dialog_title;
	}
}
