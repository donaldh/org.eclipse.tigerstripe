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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.runtime;

import org.eclipse.tigerstripe.workbench.internal.AbstractContainedObject;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

/**
 * Captures the details of a Plugin classpath entry (ie a .jar file for example)
 * 
 * @author Eric Dillon
 * 
 */
public class PluginClasspathEntry extends AbstractContainedObject implements
		IPluginClasspathEntry, IContainedObject {

	private String relativePath;

	public void setRelativePath(String relativePath) {
		markDirty();
		this.relativePath = relativePath;
	}

	/**
	 * Gets the project relative path for this entry
	 * 
	 * @return
	 */
	public String getRelativePath() {
		return this.relativePath;
	}

	public boolean isPresent() {
		return true; // TODO
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IPluginClasspathEntry) {
			IPluginClasspathEntry other = (IPluginClasspathEntry) obj;
			if (other.getRelativePath() != null)
				return other.getRelativePath().equals(getRelativePath());
		}
		return false;
	}

}
