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
package org.eclipse.tigerstripe.workbench.internal.core.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.internal.AbstractContainedObject;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;

/**
 * @author Eric Dillon
 * 
 * The definition of a repository containing a set of artifacts to consider for
 * input to a project.
 */
public class ArtifactRepository extends AbstractContainedObject implements IContainedObject{

	private File baseDirectory;
	private Collection<String> includes;
	private Collection<String> excludes;

	public ArtifactRepository(File baseDirectory) {
		this.baseDirectory = baseDirectory;
		this.includes = new ArrayList<String>();
		this.excludes = new ArrayList<String>();
	}

	public File getBaseDirectory() {
		return this.baseDirectory;
	}

	public String[] getIncludes() {
		String[] result = new String[this.includes.size()];
		result = (String[]) this.includes.toArray(result);
		return result;
	}

	public void setIncludes(Collection<String> includes) {
		markDirty();
		this.includes.clear();
		for(String include : includes ) {
			this.includes.add(include);
		}
	}

	public void setExcludes(Collection<String> excludes) {
		markDirty();
		this.excludes.clear();
		for( String exclude : excludes ) {
			this.excludes.add(exclude);
		}
	}

	public String[] getExcludes() {
		String[] result = new String[this.excludes.size()];
		result = (String[]) this.excludes.toArray(result);
		return result;
	}

	public void setBaseDirectory(File baseDirectory) {
		markDirty();
		this.baseDirectory = baseDirectory;
	}

}