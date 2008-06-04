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
package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.association.NewAssociationWizard;

/**
 * @author Eric Dillon
 * @since 1.2
 */
public class OpenNewAssociationArtifactWizardAction extends
		OpenNewArtifactWizardAction {

	@Override
	protected void initAction() {
		setText(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IAssociationArtifactImpl.class.getName()).getLabel(null));
		setImageDescriptor(Images.getDescriptor(Images.ASSOCIATION_ICON_NEW));
	}

	@Override
	protected Wizard createWizard() {
		return new NewAssociationWizard();
	}
}
