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

package org.eclipse.tigerstripe.workbench.internal.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author Yuri Strot
 * 
 */
public class ProjectInfo {

	private String modelId;
	private String[] references;
	private ITigerstripeModelProject project;
	private IProject iProject;
	private boolean noAccess;

	public ProjectInfo(IProject project) {
		this.iProject = project;
		noAccess = !project.isOpen();
		if (noAccess)
			return;
		IAbstractTigerstripeProject aProject = (IAbstractTigerstripeProject) project
				.getAdapter(IAbstractTigerstripeProject.class);
		if (aProject instanceof ITigerstripeModelProject) {
			this.project = (ITigerstripeModelProject) aProject;
			try {
				modelId = this.project.getModelId();
			} catch (Exception e) {
				// ignore any exceptions
			}
			try {
				ModelReference[] modelReferences = this.project
						.getModelReferences();
				references = new String[modelReferences.length];
				for (int i = 0; i < modelReferences.length; i++) {
					references[i] = modelReferences[i].getToModelId();
				}
			} catch (Exception e) {
				// ignore any exceptions
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((iProject == null) ? 0 : iProject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectInfo other = (ProjectInfo) obj;
		if (iProject == null) {
			if (other.iProject != null)
				return false;
		} else if (!iProject.equals(other.iProject))
			return false;
		return true;
	}

	public boolean needReferenceContainer() {
		if (project == null || iProject == null)
			return false;
		IJavaProject jProject = JavaCore.create(iProject);
		if (jProject == null)
			return false;
		return ReferencesListener.needReferenceContainer(jProject);
	}

	public String getModelId() {
		return modelId;
	}

	public String[] getReferences() {
		return references;
	}

	public IProject getIProject() {
		return iProject;
	}

	public ITigerstripeModelProject getProject() {
		return project;
	}

}
