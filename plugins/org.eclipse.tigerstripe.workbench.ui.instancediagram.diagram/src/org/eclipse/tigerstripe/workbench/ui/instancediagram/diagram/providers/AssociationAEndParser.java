/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;

public class AssociationAEndParser extends AssociationEndParser {

	public AssociationAEndParser(EStructuralFeature feature) {
		super(feature);
	}

	@Override
	protected IAssociationEnd getEnd(IAssociationArtifact associationArtifact) {
		return associationArtifact.getAEnd();
	}

	@Override
	protected boolean isAssociationEndIsOrdered(AssociationInstance association) {
		return association.isAEndIsOrdered();
	}

	@Override
	protected String getEndOrder(AssociationInstance association) {
		return association.getAEndOrder();
	}
}
