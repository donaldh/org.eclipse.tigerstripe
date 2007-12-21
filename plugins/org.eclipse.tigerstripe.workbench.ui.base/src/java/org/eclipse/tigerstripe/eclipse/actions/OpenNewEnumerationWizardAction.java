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
package org.eclipse.tigerstripe.eclipse.actions;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.wizards.NewEnumCreationWizard;
import org.eclipse.jface.wizard.Wizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewEnumerationWizardAction extends AbstractOpenWizardAction {

	/**
	 * Generic default constructor
	 */
	public OpenNewEnumerationWizardAction() {
		initAction();
	}

	public OpenNewEnumerationWizardAction(String label, Class[] acceptedTypes) {
		super(label, acceptedTypes, false);
		initAction();
	}

	protected void initAction() {
		setText("Enum");
		setImageDescriptor(JavaPluginImages
				.getDescriptor(JavaPluginImages.IMG_OBJS_ENUM));
	}

	@Override
	protected Wizard createWizard() {
		return new NewEnumCreationWizard();
	}

	@Override
	protected boolean shouldAcceptElement(Object obj) {
		return isOnBuildPath(obj) && !isInArchive(obj);
	}
}