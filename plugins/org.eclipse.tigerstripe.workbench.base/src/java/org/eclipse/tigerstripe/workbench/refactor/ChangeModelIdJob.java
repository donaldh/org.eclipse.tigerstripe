/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.refactor;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.annotation.ChangeIdLazyObject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ChangeModelIdJob extends Job {

	private final ITigerstripeModelProject project;
	private final String newModelId;
	private final boolean updateAnnotations;

	public ChangeModelIdJob(ITigerstripeModelProject project,
			String newModelId, boolean updateAnnotations) {
		super("Change Model ID");
		setRule(ResourcesPlugin.getWorkspace().getRoot());
		setUser(true);
		this.project = project;
		this.newModelId = newModelId;
		this.updateAnnotations = updateAnnotations;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			int totalWork = updateAnnotations ? 2 : 1;
			monitor.beginTask("Change model id to '" + newModelId + "'",
					totalWork);
			IProjectDetails details = project.getProjectDetails();
			ChangeIdLazyObject oldObject = new ChangeIdLazyObject(project);
			if (updateAnnotations) {
				AnnotationPlugin.getRefactoringNotifier().fireChanged(
						oldObject, null,
						IRefactoringChangesListener.ABOUT_TO_CHANGE);
				monitor.worked(1);
			}
			details.setModelId(newModelId);
			project.commit(new SubProgressMonitor(monitor, 1));
			ChangeIdLazyObject newObject = new ChangeIdLazyObject(project);
			if (updateAnnotations) {
				AnnotationPlugin.getRefactoringNotifier().fireChanged(
						oldObject, newObject,
						IRefactoringChangesListener.CHANGED);
				monitor.worked(1);
			}
			monitor.done();
		} catch (Exception e) {
			return new Status(IStatus.ERROR, BasePlugin.getPluginId(), 222,
					"Error during model id changing", e);
		}
		return Status.OK_STATUS;
	}
}
