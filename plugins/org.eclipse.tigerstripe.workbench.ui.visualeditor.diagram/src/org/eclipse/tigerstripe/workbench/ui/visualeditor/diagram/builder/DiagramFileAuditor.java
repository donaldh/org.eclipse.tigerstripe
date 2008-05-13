/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.builder;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.builder.IFileExtensionBasedAuditor;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public abstract class DiagramFileAuditor implements IFileExtensionBasedAuditor {

	public abstract String getMatchingExtension();

	public void run(IProject project, List<IResource> resourcesToCheck,
			IProgressMonitor monitor) {
		if (resourcesToCheck == null || resourcesToCheck.size() == 0)
			return;

		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) project
				.getAdapter(ITigerstripeModelProject.class);
		if (tsProject != null) {
			monitor.beginTask("Checking diagram ."+getFileExtension() +" files", resourcesToCheck.size());
			for (IResource res : resourcesToCheck) {
				try {
					if (! (res instanceof IContainer) ){
						String resourceName = res.getName().substring(0,res.getName().length()- (getFileExtension().length()+1));
						IContainer parent = res.getParent();
						IResource matcher = parent.findMember(resourceName+"."+getMatchingExtension());
						
						if (matcher == null ){
							// The matching file does not exist!
							TigerstripeProjectAuditor.reportError("Project '"
									+ project.getName() + "' contains unmatched diagram file '"+res.getName()+"'.", res, 222);
						}

					}
				} catch (Exception e) {
					BasePlugin.log(e);
				}
			}
			
		}

	}
}
