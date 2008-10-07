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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.patternBased.NewNodePatternBasedWizard;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.patternBased.NewRelationPatternBasedWizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewPatternBasedArtifactWizardAction extends
		AbstractOpenWizardAction {

	IPattern pattern = null;
	ITigerstripeModelProject modelProject = null;
	
	/**
	 * This constructor takes in a pattern that is used to build the
	 * ui parts.
	 */
	public OpenNewPatternBasedArtifactWizardAction(IPattern pattern) {
		// TODO: Implement Help context here
		// WorkbenchHelp.setHelp(this,
		// IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
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

	/**
	 * Returns the wizard to be called
	 */
	@Override
	protected Wizard createWizard(){
		if (this.pattern instanceof IRelationPattern){
			return new NewRelationPatternBasedWizard(this.pattern);
		} else  {// 	if (this.pattern instanceof INodePattern){
			return new NewNodePatternBasedWizard(this.pattern);
			
		}
	}

	@Override
	protected boolean shouldAcceptElement(Object obj) {
		return isOnBuildPath(obj) && !isInArchive(obj);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);

		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getTSProjectInFocus();
		action.setEnabled((aProject instanceof ITigerstripeModelProject && aProject
				.exists())
				&& EclipsePlugin.getProjectInFocus().isOpen());
	}

}