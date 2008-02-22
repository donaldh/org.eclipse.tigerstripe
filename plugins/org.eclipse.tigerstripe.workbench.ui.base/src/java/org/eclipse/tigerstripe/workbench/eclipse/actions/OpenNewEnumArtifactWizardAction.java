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
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.enums.NewEnumWizard;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewEnumArtifactWizardAction extends
		OpenNewArtifactWizardAction {

	@Override
	protected void initAction() {
		setText(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IEnumArtifactImpl.class.getName()).getLabel());
		setImageDescriptor(Images.getDescriptor(Images.ENUM_ICON_NEW));
	}

	@Override
	protected Wizard createWizard() {
		return new NewEnumWizard();
	}
}
