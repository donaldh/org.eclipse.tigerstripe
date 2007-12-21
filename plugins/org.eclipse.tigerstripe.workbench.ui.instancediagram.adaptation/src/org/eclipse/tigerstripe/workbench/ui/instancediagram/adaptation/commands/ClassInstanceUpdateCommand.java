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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IField;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.ClassInstanceImpl;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramReferenceMapper;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

public class ClassInstanceUpdateCommand extends AbstractInstanceUpdateCommand {

	private IArtifactManagerSession artMgrSession;

	public ClassInstanceUpdateCommand(Instance eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
		InstanceMap instanceMap = (InstanceMap) eArtifact.eContainer();
		instanceMap.getCorrespondingITigerstripeProject();
		ITigerstripeProject project = instanceMap
				.getCorrespondingITigerstripeProject();
		try {
			artMgrSession = project.getArtifactManagerSession();
		} catch (TigerstripeException e) {
			throw new RuntimeException("IArtifactManagerSession not found");
		}
	}

	@Override
	public void updateInstance(Instance instance,
			IAbstractArtifact relatedIArtifact) {
		if (!(instance instanceof ClassInstance))
			throw new IllegalArgumentException("input Instance argument "
					+ instance + "is not an ClassInstance");
		ClassInstance eArtifact = (ClassInstance) instance;
		// first, assemble a list of all of the associations that exist in the
		// eArtifact that
		// represent referenced fields (their reference name is not empty)
		EList associations = eArtifact.getAssociations();
		Map<String, AssociationInstance> assocMap = new HashMap<String, AssociationInstance>();
		for (Object obj : associations) {
			AssociationInstance assocInstance = (AssociationInstance) obj;
			if (assocInstance.getReferenceName() != null
					&& assocInstance.getReferenceName().length() > 0)
				assocMap.put(assocInstance.getReferenceName(), assocInstance);
		}
		// Now make sure we don't have anything on the eArtifact that is not on
		// the iArtifact that corresponds to this eArtifact (note that we cannot
		// simply use the input iArtifact here because it may be the iArtifact
		// for a superclass of this eArtifact...)
		List<Variable> variablesToRemove = new ArrayList<Variable>();
		try {
			IAbstractArtifact iArtifact = ((ClassInstanceImpl) eArtifact)
					.getCorrespondingIArtifact();
			for (Variable variable : (List<Variable>) eArtifact.getVariables()) {
				IField field = InstanceDiagramUtils.findIFieldByName(iArtifact,
						variable.getName());
				if (field == null) {
					variablesToRemove.add(variable);
				} else if (!InstanceDiagramUtils.isPrimitive(artMgrSession,
						field.getIType())) {
					String value = variable.getValue();
					List<String> values = InstanceDiagramUtils
							.instanceReferencesAsList(value);
					InstanceDiagramReferenceMapper.eINSTANCE
							.addVariableReferences(variable, values);
				}
			}
			// next, if it's an association class object, update the association
			// instances
			// that are linked to the class instance that matches this
			// association class instance
			if (eArtifact.isAssociationClassInstance()) {
				List<AssociationInstance> assocs = eArtifact.getAssociations();
				for (AssociationInstance assoc : assocs) {
					String assocFQN = assoc.getFullyQualifiedName();
					AssociationInstanceUpdateCommand assocUpdateCmd = new AssociationInstanceUpdateCommand(
							assoc, iArtifact, iArtifact.getIProject()
									.getArtifactManagerSession());
					assocUpdateCmd.updateInstance(assoc, iArtifact);
				}
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logInfoMessage("TigerstripeException detected",
					e);
		}

		if (variablesToRemove.size() != 0)
			eArtifact.getVariables().removeAll(variablesToRemove);
	}

	public void redo() {
		// TODO Auto-generated method stub

	}

}
