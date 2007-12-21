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
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;

public class InstanceNamePackageParser extends InstanceStructuralFeaturesParser {

	public InstanceNamePackageParser(List features) {
		super(features);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {
		// first get the print string from the super-class
		String printString = super.getPrintString(adapter, flags);
		ClassInstance classInstance = (ClassInstance) adapter
				.getAdapter(ClassInstance.class);
		InstanceMap map = (InstanceMap) classInstance.eContainer();
		if (classInstance.getPackage().equals(map.getBasePackage())) {
			int pos = printString.indexOf(classInstance.getPackage());
			String firstPart = printString.substring(0, pos);
			String lastPart = printString.substring(pos
					+ classInstance.getPackage().length() + 1);
			printString = firstPart + lastPart;
		}
		return printString;
	}

}
