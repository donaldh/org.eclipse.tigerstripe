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
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.project.NewPatternBasedProjectWizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewPatternBasedProjectWizardAction extends AbstractOpenWizardAction {
	IPattern pattern = null;
	
	/**
	 * Generic default constructor
	 */
	public OpenNewPatternBasedProjectWizardAction(IPattern pattern) {
		this.pattern = pattern;
		initAction();
	}

	/**
	 * Initialize the action (set text, images, etc...)
	 */
	protected void initAction() {
		setText(this.pattern.getUILabel());
		setImageDescriptor(this.pattern.getImageDescriptor());
	}

	@Override
	protected Wizard createWizard() {
		return new NewPatternBasedProjectWizard(this.pattern);
	}

	@Override
	protected boolean shouldAcceptElement(Object obj) {
		return isOnBuildPath(obj) && !isInArchive(obj);
	}

	// #174
	// to avoid having to create a JavaProject before creating a TS project
	// in an empty workspace
	@Override
	protected boolean checkWorkspaceNotEmpty() {
		return true;
	}
}