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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;

public class AssociationInstanceParser extends InstanceStructuralFeaturesParser {

	public AssociationInstanceParser(List features) {
		super(features);
	}

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {
		// first get the print string from the super-class
		String tmpString = super.getPrintString(adapter, flags);
		// then get the underlying attribute from the wrapped adapter
		AssociationInstance association = (AssociationInstance) adapter
				.getAdapter(AssociationInstance.class);
		Instance aEnd = association.getAEnd();
		Instance zEnd = association.getZEnd();
		if (association.isReferenceType())
			return null;
		else if (aEnd instanceof ClassInstance
				&& ((ClassInstance) aEnd).isAssociationClassInstance())
			return null;
		else if (zEnd instanceof ClassInstance
				&& ((ClassInstance) zEnd).isAssociationClassInstance())
			return null;
		int colonPos = tmpString.indexOf(":");
		String printString = tmpString;
		if (colonPos >= 0)
			printString = tmpString.substring(colonPos + 1);
		return printString;
	}
}
