/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.releng.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.tools.ant.BuildException;
import org.eclipse.ant.core.AntCorePlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.tigerstripe.releng.ant.builds.schema.Build;
import org.eclipse.tigerstripe.releng.ant.types.AntBuild;

/**
 * Creates a new build
 * 
 * @author erdillon
 * 
 */
public class NewBuild extends org.apache.tools.ant.Task {

	private String file;
	private Build build;

	public void setFile(String file) {
		this.file = file;
	}

	public Build createBuild() {
		this.build = new AntBuild();
		return this.build;
	}

	@Override
	public void execute() throws BuildException {
		File baseDir = getProject().getBaseDir();

		Resource res = new XMLResourceImpl(URI.createFileURI(baseDir
				.getAbsolutePath()
				+ File.separator + file));
		res.getContents().add(build);

		try {
			res.save(new HashMap<Object, Object>());
		} catch (IOException e) {
			AntCorePlugin.log(e);
		}
	}

}
