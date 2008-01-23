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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;

public class EnumBasetypeUpdateCommand extends AbstractArtifactUpdateCommand {

	public EnumBasetypeUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
	}

	@Override
	public void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		IEnumArtifact iEnum = (IEnumArtifact) iArtifact;
		Enumeration eEnum = (Enumeration) eArtifact;

		if (!iEnum.getBaseTypeStr().equals(eEnum.getBaseType())) {
			eEnum.setBaseType(iEnum.getBaseTypeStr());
		}
	}

	public void redo() {
		// TODO Auto-generated method stub

	}

}
