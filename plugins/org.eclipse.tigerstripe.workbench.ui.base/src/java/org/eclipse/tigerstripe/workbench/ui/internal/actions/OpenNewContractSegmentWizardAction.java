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
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.segment.NewContractSegmentWizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewContractSegmentWizardAction extends
		AbstractOpenWizardAction {

	/**
	 * Generic default constructor
	 */
	public OpenNewContractSegmentWizardAction() {
		initAction();
	}

	public OpenNewContractSegmentWizardAction(String label,
			Class[] acceptedTypes) {
		super(label, acceptedTypes, false);
		initAction();
	}

	protected void initAction() {
		setText("Contract Facet");
		setImageDescriptor(Images.getDescriptor(Images.CONTRACTSEGMENT_ICON));
	}

	@Override
	protected Wizard createWizard() {
		return new NewContractSegmentWizard();
	}

	@Override
	protected boolean shouldAcceptElement(Object obj) {
		return isOnBuildPath(obj) && !isInArchive(obj);
	}
}