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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;

/**
 * Provides a set of convenience methods on a Map
 * 
 * @author Eric Dillon
 * 
 */
public class InstanceDiagramMapHelper {

	private final InstanceMap map;

	public InstanceDiagramMapHelper(InstanceMap map) {
		this.map = map;
	}

	public AssociationInstance findAssociationInstanceFor(String name,
			IAbstractArtifactInternal aEnd, IAbstractArtifactInternal zEnd) {
		List<AssociationInstance> associations = map.getAssociationInstances();
		for (AssociationInstance association : associations) {
			if (association.getName().equals(name)
					&& association.getAEnd() == aEnd
					&& association.getZEnd() == zEnd)
				return association;
		}
		return null;
	}

	public List<Instance> findElementFor(String fqn) {
		List<Instance> matchingElements = new ArrayList<Instance>();

		List<AssociationInstance> assocs = map.getAssociationInstances();
		for (AssociationInstance assoc : assocs) {
			if (assoc.getFullyQualifiedName() != null
					&& assoc.getFullyQualifiedName().equals(fqn)) {
				matchingElements.add(assoc);
			}
		}

		List<ClassInstance> classInstances = map.getClassInstances();
		for (ClassInstance classInstance : classInstances) {
			if (classInstance != null
					&& classInstance.getFullyQualifiedName() != null
					&& classInstance.getFullyQualifiedName().equals(fqn)) {
				matchingElements.add(classInstance);
			}
		}
		return matchingElements;
	}

	public ClassInstance findClassInstanceFor(String fqn) {
		ClassInstance eClassInstance = null;

		List<ClassInstance> classInstances = map.getClassInstances();
		for (ClassInstance classInstance : classInstances) {
			if (classInstance.getFullyQualifiedName() != null
					&& classInstance.getFullyQualifiedName().equals(fqn)) {
				eClassInstance = classInstance;
				break;
			}
		}

		return eClassInstance;
	}

	public ClassInstance findClassInstanceFor(ClassInstance iArtifact) {

		if (iArtifact != null)
			return findClassInstanceFor(iArtifact.getFullyQualifiedName());

		return null;
	}
}
