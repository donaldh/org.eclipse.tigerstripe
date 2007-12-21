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

import java.util.List;

import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;

public class ClassInstanceHelper {

	private ClassInstance instance;

	public ClassInstanceHelper(ClassInstance instance) {
		this.instance = instance;
	}

	public AssociationInstance findAssociationByName(String name) {
		List<AssociationInstance> assocs = instance.getAssociations();
		for (AssociationInstance assoc : assocs) {
			if (assoc.getName() != null && assoc.getName().equals(name))
				return assoc;
		}
		return null;
	}
}
