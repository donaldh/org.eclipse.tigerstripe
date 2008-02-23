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

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.wizards.NewAnnotationCreationWizard;
import org.eclipse.jface.wizard.Wizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewAnnotationWizardAction extends AbstractOpenWizardAction {

	/**
	 * Generic default constructor
	 */
	public OpenNewAnnotationWizardAction() {
		initAction();
	}

	public OpenNewAnnotationWizardAction(String label, Class[] acceptedTypes) {
		super(label, acceptedTypes, false);
		initAction();
	}

	protected void initAction() {
		setText("Annotation");
		setImageDescriptor(JavaPluginImages
				.getDescriptor(JavaPluginImages.IMG_OBJS_ANNOTATION));
	}

	@Override
	protected Wizard createWizard() {
		return new NewAnnotationCreationWizard() {
			// Bug 219769, we need to run the performFinish in the UI Thread
			@Override
			protected boolean canRunForked() {
				return false;
			}
		};
	}

	@Override
	protected boolean shouldAcceptElement(Object obj) {
		return isOnBuildPath(obj) && !isInArchive(obj);
	}
}