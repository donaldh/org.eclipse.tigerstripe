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
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AbstractArtifactExtendsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;

/**
 * @generated
 */
public class AbstractArtifactExtendsItemSemanticEditPolicy extends
		TigerstripeBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	@Override
	protected Command getDestroyReferenceCommand(DestroyReferenceRequest req) {
		return getMSLWrapper(new myDestroyReferenceCommand(req));
	}

	@Override
	protected Command getSetCommand(SetRequest req) {
		// TODO Auto-generated method stub
		return super.getSetCommand(req);
	}

	private class myDestroyReferenceCommand extends DestroyReferenceCommand {

		public myDestroyReferenceCommand(DestroyReferenceRequest request) {
			super(request);
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
				IAdaptable info) throws ExecutionException {
			EditPart hostPart = getHost();
			if (hostPart instanceof AbstractArtifactExtendsEditPart) {
				AbstractArtifactExtendsEditPart extendsPart = (AbstractArtifactExtendsEditPart) hostPart;
				Edge edge = (Edge) extendsPart.getModel();
				if (edge.getSource() != null) {
					AbstractArtifact sourceArtifact = (AbstractArtifact) edge
							.getSource().getElement();
					NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
							sourceArtifact);
					helper.setProperty(
							NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS,
							"true");
				}
			}
			return super.doExecuteWithResult(monitor, info);
		}

	}
}
