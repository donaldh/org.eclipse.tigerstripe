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
package org.eclipse.tigerstripe.releng.downloadsite.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.eclipse.tigerstripe.releng.downloadsite.ant.types.AntComponent;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Build;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Component;

/**
 * This task allows to add components to an existing Build descriptor
 * 
 * @author erdillon
 * 
 */
public class AddComponent extends BaseTask {

	private String file;
	private List<Component> components = new ArrayList<Component>();

	public void setFile(String file) {
		this.file = file;
	}

	public Component createComponent() {
		Component result = new AntComponent();
		this.components.add(result);
		return result;
	}

	@Override
	public void execute() throws BuildException {
		super.execute();

		Build build = null;
		try {
			build = DownloadSiteHelper.readBuildfile(new File(file));
		} catch (IOException e) {
			throw new BuildException(e);
		}

		build.getComponent().addAll(components);

		try {
			DownloadSiteHelper.save(build);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

}
