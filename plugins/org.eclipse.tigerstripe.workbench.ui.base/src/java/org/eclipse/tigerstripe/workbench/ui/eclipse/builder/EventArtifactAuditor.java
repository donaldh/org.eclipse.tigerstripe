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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

public class EventArtifactAuditor extends AbstractArtifactAuditor implements
		IArtifactAuditor {

	public EventArtifactAuditor(IProject project, IAbstractArtifact artifact) {
		super(project, artifact);
		// TODO Auto-generated constructor stub
	}

}
