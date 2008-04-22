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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.action;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceCreationWizard;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenNewArtifactWizardAction;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewTigerstripeInstanceDiagramWizardAction extends
		OpenNewArtifactWizardAction {

	@Override
	protected void initAction() {
		setText("new Instance Diagram...");
		setImageDescriptor(Images.getDescriptor(Images.INSTANCEEDITOR_ICON));
	}

	@Override
	protected Wizard createWizard() {
		return new InstanceCreationWizard();
	}
}
