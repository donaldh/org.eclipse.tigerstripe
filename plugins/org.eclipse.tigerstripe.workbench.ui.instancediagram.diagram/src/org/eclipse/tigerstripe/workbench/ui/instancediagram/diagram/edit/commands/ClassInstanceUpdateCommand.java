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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands.BaseGMFTransactionalCommand;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;

public class ClassInstanceUpdateCommand extends BaseGMFTransactionalCommand {

	private InstanceMapEditPart mapEditPart;
	private IArtifactManagerSession artMgrSession;
	private InstanceMap instanceMap;
	private ClassInstance instance;
	private String newInstanceName;
	private HashMap<String, List<Object>> newVariableMap;
	HashMap<String, String> additionalInstanceNames;
	HashMap<String, IType> additionalInstanceTypes;

	public ClassInstanceUpdateCommand(TransactionalEditingDomain domain,
			InstanceMapEditPart mapEditPart, ClassInstance instance,
			String newInstanceName,
			HashMap<String, List<Object>> newVariableMap,
			HashMap<String, String> additionalInstanceNames,
			HashMap<String, IType> additionalInstanceTypes) {
		super(domain, "Update instance " + instance.getArtifactName(), null);
		this.mapEditPart = mapEditPart;
		this.instance = instance;
		this.newInstanceName = newInstanceName;
		this.newVariableMap = newVariableMap;
		this.additionalInstanceNames = additionalInstanceNames;
		this.additionalInstanceTypes = additionalInstanceTypes;
		DiagramGraphicalViewer mapViewer = (DiagramGraphicalViewer) mapEditPart
				.getViewer();
		DiagramEditDomain diagramDomain = (DiagramEditDomain) mapViewer
				.getEditDomain();
		IResource res = (IResource) diagramDomain.getEditorPart()
				.getEditorInput().getAdapter(IResource.class);
		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getITigerstripeProjectFor(res.getProject());
		if (!(aProject instanceof ITigerstripeProject))
			throw new RuntimeException("non-Tigerstripe Project found");
		ITigerstripeProject project = (ITigerstripeProject) aProject;
		try {
			artMgrSession = project.getArtifactManagerSession();
		} catch (TigerstripeException e) {
			throw new RuntimeException("IArtifactManagerSession not found");
		}
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	protected CommandResult doExecuteWithResult(
			IProgressMonitor progressMonitor, IAdaptable info)
			throws ExecutionException {
		// first check to see if the name has change...if so, then set the
		// instance name to the new name
		if (!instance.getArtifactName().equals(newInstanceName)) {
			String oldArtifactName = instance.getArtifactName();
			instance.setArtifactName(newInstanceName);
			if (instance.isAssociationClassInstance()) {
				// if it's an association class instance, need to update the
				// names
				// of the ends to match too...
				EList associations = instance.getAssociations();
				for (Object obj : associations) {
					AssociationInstance association = (AssociationInstance) obj;
					if (association.getArtifactName().equals(
							oldArtifactName + "::aEnd"))
						association.setArtifactName(newInstanceName + "::aEnd");
					else if (association.getArtifactName().equals(
							oldArtifactName + "::zEnd"))
						association.setArtifactName(newInstanceName + "::zEnd");
				}
			}
		}
		// get a reference to the instance map (for use in adding new instances
		// to the map)
		instanceMap = (InstanceMap) ((View) mapEditPart.getModel())
				.getElement();
		// if we are adding any new additional class instances (from defining a
		// new class
		// instance while editing the referenced fields), then create those
		// instances now...
		if (additionalInstanceNames != null) {
			for (String fieldName : additionalInstanceNames.keySet()) {
				ClassInstance lclInstance = InstancediagramFactory.eINSTANCE
						.createClassInstance();
				lclInstance.setArtifactName(additionalInstanceNames
						.get(fieldName));
				IType type = additionalInstanceTypes.get(fieldName);
				lclInstance.setName(type.getName());
				String fqn = type.getFullyQualifiedName();
				int idx = fqn.lastIndexOf(".");
				if (idx > 0)
					lclInstance.setPackage(fqn.substring(0, idx));
				else
					lclInstance.setPackage("");
				instanceMap.getClassInstances().add(lclInstance);
			}
		}
		// next, put together a list of the variables/associations to add, the
		// variables/associations
		// to delete, and the variables/associations who's values need to be
		// changed
		EList variableList = instance.getVariables();
		EList associationList = instance.getAssociations();
		Map<String, Variable> existingVariables = new HashMap<String, Variable>();
		Map<String, List<AssociationInstance>> existingAssociations = new HashMap<String, List<AssociationInstance>>();
		for (Object obj : variableList) {
			Variable variable = (Variable) obj;
			existingVariables.put(variable.getName(), variable);
		}
		for (Object obj : associationList) {
			AssociationInstance association = (AssociationInstance) obj;
			// if the association in question is not a reference, then skip
			// further testing
			// to see if it should be removed
			if (!association.isReferenceType())
				continue;
			String refName = association.getReferenceName();
			if (existingAssociations.keySet().contains(refName)) {
				List<AssociationInstance> valList = existingAssociations
						.get(refName);
				valList.add(association);
			} else {
				List<AssociationInstance> valList = new ArrayList<AssociationInstance>();
				valList.add(association);
				existingAssociations.put(refName, valList);
			}
		}
		Set<String> newVarNames = newVariableMap.keySet();
		Set<String> existingVarNames = existingVariables.keySet();
		Set<String> existingAssocNames = existingAssociations.keySet();

		// lists used to remove variables and associations
		List<Variable> variablesToRemove = new ArrayList<Variable>();
		List<AssociationInstance> associationsToRemove = new ArrayList<AssociationInstance>();

		// first loop through the new names and modify existing variables
		// that match or add in new ones that don't exist yet
		for (String newName : newVarNames) {
			List<Object> varInfo = newVariableMap.get(newName);
			IType type = (IType) varInfo.get(0);
			String value = (String) varInfo.get(1);
			Variable variable = null;
			if (existingVarNames.contains(newName)) {
				// it's an existing variable, so change to the existing variable
				// to use the new values
				variable = existingVariables.get(newName);
				variable.setName(newName);
				variable.setType(type.getName());
				String oldValueStr = variable.getValue();
				variable.setValue(value);
				// don't need to do remove the references to the instances
				// named in the "oldValue" (if it isn't a primitive value)
				// here...removal of the referenced instance values from
				// the reference mapper is actually handled in the
				// VariableEditPart.handleNotificationEvent(Notification)
				// method instead of here
			} else {
				// if the value is blank, skip adding new variable
				if (value == null || "".equals(value))
					continue;
				// else create a new variable, initialize it's fields, and
				// add it to the list of variables in the diagram
				variable = InstancediagramFactory.eINSTANCE.createVariable();
				variable.setName(newName);
				variable.setType(type.getName());
				variable.setValue(value);
				variableList.add(variable);
				instance.getVariables().add(variable);
			}
			// don't need to do add the references to the instances
			// named in the new "value" (if it isn't a primitive value)
			// here...adding the referenced instance values to the
			// reference mapper is actually handled in the
			// VariableEditPart.handleNotificationEvent(Notification)
			// method instead of here
		}

		// now loop through the existing variable names and see which are not
		// included in the list of names in the new variable map...remove those
		// from the model and the diagram
		for (String oldName : existingVarNames) {
			if (!newVarNames.contains(oldName)) {
				variablesToRemove.add(existingVariables.get(oldName));
			}
		}
		if (variablesToRemove.size() != 0)
			instance.getVariables().removeAll(variablesToRemove);

		// finally loop through the existing association names and see which are
		// not
		// included in the list of names in the new association map...remove
		// those
		// from the model and the diagram
		for (String oldName : existingAssocNames) {
			if (!newVarNames.contains(oldName)) {
				associationsToRemove.addAll(existingAssociations.get(oldName));
			}
		}
		if (associationsToRemove.size() != 0) {
			instance.getAssociations().removeAll(associationsToRemove);
			instanceMap.getAssociationInstances().removeAll(
					associationsToRemove);
		}

		return CommandResult.newOKCommandResult();
	}
}
