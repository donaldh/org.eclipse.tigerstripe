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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.action;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;

public class SetZEndOrderAction extends SetAssociationEndOrderAction {

	@Override
	protected IAssociationEnd getEnd(IAssociationArtifact associationArtifact) {
		return associationArtifact.getZEnd();
	}

	@Override
	protected boolean isAssociationEndIsOrdered(AssociationInstance association) {
		return association.isZEndIsOrdered();
	}

	@Override
	protected Integer getEndOrderNumber(AssociationInstance association) {
		return association.getZEndOrderNumber();
	}

	@Override
	protected void setEndOrderNumber(AssociationInstance association,
			Integer orderNumber) {
		association.setZEndOrderNumber(orderNumber);
	}

	@Override
	protected Instance getEndInstance(AssociationInstance association) {
		return association.getZEnd();
	}
}
