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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement;
import org.eclipse.ui.PlatformUI;

/**
 * @generated
 */
public class BaseAttributeItemSemanticEditPolicy extends
		TigerstripeBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	@Override
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		return getMSLWrapper(new MyDestroyElementCommand(req));
	}

	protected class MyDestroyElementCommand extends DestroyElementCommand {

		public MyDestroyElementCommand(DestroyElementRequest request) {
			super(request);
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
				IAdaptable info) throws ExecutionException {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();

			EObject destructee = getElementToDestroy();
			String name = "";
			if (destructee instanceof NamedElement) {
				name = "(" + ((NamedElement) destructee).getName() + ") ";
			}

			boolean response = MessageDialog.openConfirm(shell,
					"Remove from model",
					"Do you really want to remove this element " + name
							+ "from the model?");
			if (response)
				return super.doExecuteWithResult(monitor, info);
			else
				return CommandResult.newCancelledCommandResult();
		}

		@Override
		protected EObject getElementToDestroy() {
			View view = (View) getHost().getModel();
			EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
			if (annotation != null)
				return view;
			return super.getElementToDestroy();
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
		return super.getCreateRelationshipCommand(req);
	}
}
