/*******************************************************************************
 * Copyright (c) 2011 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.builder.generator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.plugins.ICopyRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class ModelRuleAuditor extends BasePluggableProjectAuditor {

	public ModelRuleAuditor(ITigerstripeM1GeneratorProject pProject,
			IProject project) {
		super(pProject, project);
	}

	public void audit(IRule rule, IProgressMonitor monitor) {
		super.audit(rule, monitor);

		if (!rule.isEnabled())
			return;

		String name = rule.getName();
		if (rule instanceof IRunnableRule){
			checkRunnableClass((IRunnableRule) rule);
		}
	}

}
