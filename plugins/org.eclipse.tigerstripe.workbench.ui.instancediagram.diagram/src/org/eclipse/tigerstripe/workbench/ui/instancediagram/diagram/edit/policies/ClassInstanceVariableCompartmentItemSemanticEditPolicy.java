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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers.InstanceElementTypes;

/**
 * @generated
 */
public class ClassInstanceVariableCompartmentItemSemanticEditPolicy extends
		InstanceBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	@Override
	protected Command getCreateCommand(CreateElementRequest req) {
		if (InstanceElementTypes.Variable_2001 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(InstancediagramPackage.eINSTANCE
						.getClassInstance_Variables());
			}
			return getMSLWrapper(new CreateVariable_2001Command(req));
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated
	 */
	private static class CreateVariable_2001Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateVariable_2001Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return InstancediagramPackage.eINSTANCE.getClassInstance();
		};

		/**
		 * @generated
		 */
		@Override
		protected EObject getElementToEdit() {
			EObject container = ((CreateElementRequest) getRequest())
					.getContainer();
			if (container instanceof View) {
				container = ((View) container).getElement();
			}
			return container;
		}

	}

}
