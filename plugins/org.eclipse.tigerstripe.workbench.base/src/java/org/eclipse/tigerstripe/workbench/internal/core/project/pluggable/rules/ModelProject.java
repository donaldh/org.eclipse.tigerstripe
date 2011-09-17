/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     Anton Salnik (xored software, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ModelProject {

	private final ITigerstripeModelProject project;

	private final ITigerstripeModelProject contextProject;

	private final int referenceLevel;

	public ModelProject(ITigerstripeModelProject project) {
		this(project, null, 0);
	}

	public ModelProject(ITigerstripeModelProject project,
			ITigerstripeModelProject contextProject, int referenceLevel) {
		this.project = project;
		this.contextProject = contextProject;
		this.referenceLevel = referenceLevel;
	}

	public ITigerstripeModelProject getProject() {
		return project;
	}

	public ITigerstripeModelProject getContextProject() {
		return contextProject;
	}

	public int getReferenceLevel() {
		return referenceLevel;
	}

	public boolean isReferenced() {
		return contextProject != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + ((project == null) ? 0 : project.hashCode());
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
		ModelProject other = (ModelProject) obj;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}
}
