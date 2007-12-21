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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.action;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.eclipse.actions.OpenNewArtifactWizardAction;
import org.eclipse.tigerstripe.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeCreationWizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewTigerstripeDiagramWizardAction extends
		OpenNewArtifactWizardAction {

	@Override
	protected void initAction() {
		setText("new Class Diagram...");
		setImageDescriptor(TigerstripePluginImages.DESC_VISUALEDITOR_ICON);
	}

	@Override
	protected Wizard createWizard() {
		return new TigerstripeCreationWizard();
	}
}
