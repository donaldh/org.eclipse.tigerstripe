/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class CompositeArtifactAuditor implements IArtifactAuditor {

	private final List<IArtifactAuditor> auditors;

	public CompositeArtifactAuditor(List<IArtifactAuditor> auditors) {
		this.auditors = auditors;
	}

	public void setDetails(IProject project, IAbstractArtifact artifact) {
		for (IArtifactAuditor auditor : auditors) {
			auditor.setDetails(project, artifact);
		}
	}

	public void run(IProgressMonitor monitor) {
		for (IArtifactAuditor auditor : auditors) {
			auditor.run(monitor);
			if (monitor.isCanceled()) {
				return;
			}
		}
	}

}
