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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.VariableEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceVisualIDRegistry;

/**
 * @generated
 */
public class ClassInstanceVariableCompartmentCanonicalEditPolicy extends
		CanonicalEditPolicy {

	/**
	 * @generated
	 */
	@Override
	protected List getSemanticChildrenList() {
		List result = new LinkedList();
		EObject modelObject = ((View) getHost().getModel()).getElement();
		View viewObject = (View) getHost().getModel();
		EObject nextValue;
		int nodeVID;
		for (Iterator values = ((ClassInstance) modelObject).getVariables()
				.iterator(); values.hasNext();) {
			nextValue = (EObject) values.next();
			nodeVID = InstanceVisualIDRegistry.getNodeVisualID(viewObject,
					nextValue);
			if (VariableEditPart.VISUAL_ID == nodeVID) {
				result.add(nextValue);
			}
		}
		return result;
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected boolean shouldDeleteView(View view) {
		return view.isSetElement() && view.getElement() != null;
		// && view.getElement().eIsProxy();
	}

	/**
	 * @generated
	 */
	@Override
	protected String getDefaultFactoryHint() {
		return null;
	}

}
