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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;

public class InstanceDiagramReferenceMapper {

	public static final InstanceDiagramReferenceMapper eINSTANCE = new InstanceDiagramReferenceMapper();
	private final HashMap<InstanceMap, HashMap<String, List<Variable>>> variableReferencesByMap = new HashMap<InstanceMap, HashMap<String, List<Variable>>>();

	private InstanceDiagramReferenceMapper() {
		super();
	}

	public void addVariableReference(Variable variable, String instanceName) {
		addVariableReferences(variable, Collections.singletonList(instanceName));
	}

	public void removeVariableReference(InstanceMap map, Variable variable,
			String instanceName) {
		removeVariableReferences(map, variable, Collections
				.singletonList(instanceName));
	}

	public void addVariableReferences(Variable variable,
			List<String> instanceNames) {
		InstanceMap map = (InstanceMap) variable.eContainer().eContainer();
		HashMap<String, List<Variable>> variableReferences = variableReferencesByMap
				.get(map);
		if (variableReferences == null) {
			variableReferences = new HashMap<String, List<Variable>>();
			variableReferencesByMap.put(map, variableReferences);
		}
		for (String instanceName : instanceNames) {
			if (variableReferences.containsKey(instanceName)) {
				List<Variable> varList = variableReferences.get(instanceName);
				if (!varList.contains(variable)) {
					// maintain a unique set of all of the variables that
					// contain
					// references to this instanceName
					varList.add(variable);
				}
			} else {
				// didn't find a matching entry in the HashMap, so create one
				// and add this variable to that list, then add that list to the
				// HashMap for this instanceName
				List<Variable> varList = new ArrayList<Variable>();
				varList.add(variable);
				variableReferences.put(instanceName, varList);
			}
		}
	}

	public void removeVariableReferences(InstanceMap map, Variable variable,
			List<String> instanceNames) {
		HashMap<String, List<Variable>> variableReferences = variableReferencesByMap
				.get(map);
		if (variableReferences == null)
			return;
		for (String instanceName : instanceNames) {
			if (variableReferences.containsKey(instanceName)) {
				List<Variable> varList = variableReferences.get(instanceName);
				varList.remove(variable);
				if (varList.isEmpty())
					variableReferences.remove(instanceName);
			}
		}
	}

	public Map<String, List<Variable>> getVariableReferences(InstanceMap map) {
		HashMap<String, List<Variable>> variableReferences = variableReferencesByMap
				.get(map);
		if (variableReferences == null)
			return Collections.emptyMap();
		return Collections.unmodifiableMap(variableReferences);
	}

	public List<Variable> getDependentVariables(InstanceMap map,
			String instanceName) {
		HashMap<String, List<Variable>> variableReferences = variableReferencesByMap
				.get(map);
		if (variableReferences == null)
			return Collections.emptyList();
		List<Variable> varList = variableReferences.get(instanceName);
		if (varList == null)
			return Collections.emptyList();
		else
			return Collections.unmodifiableList(variableReferences
					.get(instanceName));
	}

	public void renameReferencedInstance(InstanceMap map, String oldName,
			String newName) {
		HashMap<String, List<Variable>> variableReferences = variableReferencesByMap
				.get(map);
		if (variableReferences == null)
			return;
		List<Variable> variableList = variableReferences.get(oldName);
		if (variableList == null)
			return;
		for (Variable variable : variableList) {
			String refVal = variable.getValue();
			String newRefVal = InstanceDiagramUtils.renameInstanceReference(
					refVal, oldName, newName);
			if (newRefVal != null && !refVal.equals(newRefVal))
				variable.setValue(newRefVal);
		}
	}

	public void removeAllVariableReferences(InstanceMap map, Variable variable) {
		HashMap<String, List<Variable>> variableReferences = variableReferencesByMap
				.get(map);
		if (variableReferences == null)
			return;
		Set<String> instanceNames = new HashSet<String>();
		instanceNames.addAll(variableReferences.keySet());
		for (String instanceName : instanceNames) {
			List<Variable> referencingVars = variableReferences
					.get(instanceName);
			// if the list contains a reference to this variable, remove it
			if (referencingVars != null && referencingVars.size() > 0
					&& referencingVars.contains(variable))
				referencingVars.remove(variable);
			// if the list is now zero-length, remove it from the map
			if (referencingVars.size() <= 0)
				variableReferences.remove(instanceName);
		}
	}

	public List<Variable> removeInstance(InstanceMap map, String instanceName) {
		HashMap<String, List<Variable>> variableReferences = variableReferencesByMap
				.get(map);
		if (variableReferences == null)
			return Collections.emptyList();
		return variableReferences.remove(instanceName);
	}

	public void clear(InstanceMap map) {
		HashMap<String, List<Variable>> variableReferences = variableReferencesByMap
				.get(map);
		if (variableReferences == null)
			return;
		variableReferences.clear();
	}
}
