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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes;

/**
 * @generated
 */
public class DatatypeArtifactAttributeCompartmentItemSemanticEditPolicy extends
		TigerstripeBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	@Override
	protected Command getCreateCommand(CreateElementRequest req) {
		if (TigerstripeElementTypes.Attribute_2006 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getAbstractArtifact_Attributes());
			}
			return getMSLWrapper(new CreateAttribute_2006Command(req));
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated NOT
	 */
	private static class CreateAttribute_2006Command extends
			ConditionalCreateCommand {

		/**
		 * @generated
		 */
		public CreateAttribute_2006Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getDatatypeArtifact();
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

		@Override
		protected EObject doDefaultElementCreation() {
			Attribute attribute = (Attribute) super.doDefaultElementCreation();
			try {
				setAttributeDefaults(attribute);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			return attribute;
		}

	}

}
