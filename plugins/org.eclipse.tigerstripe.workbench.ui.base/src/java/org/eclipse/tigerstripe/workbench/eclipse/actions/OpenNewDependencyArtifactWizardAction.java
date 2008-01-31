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
package org.eclipse.tigerstripe.workbench.eclipse.actions;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.dependency.NewDependencyWizard;

/**
 * @author Eric Dillon
 * @since 1.2
 */
public class OpenNewDependencyArtifactWizardAction extends
		OpenNewArtifactWizardAction {

	@Override
	protected void initAction() {
		setText("Dependency");
		setImageDescriptor(TigerstripePluginImages.DESC_DEPENDENCY_NEW_ICON);
	}

	@Override
	protected Wizard createWizard() {
		return new NewDependencyWizard();
	}
}
