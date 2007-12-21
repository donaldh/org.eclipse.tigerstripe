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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramReferenceMapper;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

/**
 * @generated
 */
public class ClassInstanceCanonicalEditPolicy extends CanonicalEditPolicy {

	/**
	 * @generated
	 */
	@Override
	protected List getSemanticChildrenList() {
		List result = new LinkedList();
		return result;
	}

	/**
	 * @generated
	 */
	@Override
	protected boolean shouldDeleteView(View view) {
		return view.isSetElement() && view.getElement() != null
				&& view.getElement().eIsProxy();
	}

	/**
	 * @generated
	 */
	@Override
	protected String getDefaultFactoryHint() {
		return null;
	}

	@Override
	protected void handleNotificationEvent(Notification event) {
		Object feature = event.getFeature();
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		if (feature instanceof EAttribute
				&& ((EAttribute) feature).getName().equals("artifactName")
				&& oldValue != null && newValue != null) {
			// are renaming this instance, need to update any fields that might
			// reference
			// this instance so that they will use the new name
			ClassInstance classInstance = (ClassInstance) event.getNotifier();
			InstanceMap map = (InstanceMap) classInstance.eContainer();
			String oldName = (String) oldValue;
			String newName = (String) newValue;
			InstanceDiagramReferenceMapper.eINSTANCE.renameReferencedInstance(
					map, oldName, newName);
		} else if (oldValue instanceof Variable && newValue == null) {
			// are removing a variable from a class, need to update the
			// reference mapper
			// to remove this variable from the lists related to the instances
			// that it used
			// to point to
			ClassInstance classInstance = (ClassInstance) event.getNotifier();
			InstanceMap map = (InstanceMap) classInstance.eContainer();
			Variable oldVariable = (Variable) oldValue;
			String oldValueStr = oldVariable.getValue();
			List<String> oldValues = InstanceDiagramUtils
					.instanceReferencesAsList(oldValueStr);
			InstanceDiagramReferenceMapper.eINSTANCE.removeVariableReferences(
					map, oldVariable, oldValues);
		} else if (event.getEventType() == Notification.ADD
				&& newValue instanceof Variable && oldValue == null) {
			// get here during an "undo" operation that adds a variable back
			// into a class instance,
			// in that case need to add the values for that variable back into
			// the reference mapper...
			Variable variable = (Variable) newValue;
			String valueStr = variable.getValue();
			List<String> values = InstanceDiagramUtils
					.instanceReferencesAsList(valueStr);
			InstanceDiagramReferenceMapper.eINSTANCE.addVariableReferences(
					variable, values);
		}
		super.handleNotificationEvent(event);
	}

}
