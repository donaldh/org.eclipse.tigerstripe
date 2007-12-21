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
package org.eclipse.tigerstripe.core.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Eric Dillon
 * 
 * The definition of a repository containing a set of artifacts to consider for
 * input to a project.
 */
public class ArtifactRepository {

	private File baseDirectory;
	private Collection includes;
	private Collection excludes;

	public ArtifactRepository(File baseDirectory) {
		this.baseDirectory = baseDirectory;
		this.includes = new ArrayList();
		this.excludes = new ArrayList();
	}

	public File getBaseDirectory() {
		return this.baseDirectory;
	}

	public String[] getIncludes() {
		String[] result = new String[this.includes.size()];
		result = (String[]) this.includes.toArray(result);
		return result;
	}

	public void setIncludes(Collection includes) {
		this.includes = includes;
	}

	public void setExcludes(Collection excludes) {
		this.excludes = excludes;
	}

	public String[] getExcludes() {
		String[] result = new String[this.excludes.size()];
		result = (String[]) this.excludes.toArray(result);
		return result;
	}

	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

}