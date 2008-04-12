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
package org.eclipse.tigerstripe.releng.updatesite.ant;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dom4j.DocumentException;
import org.eclipse.tigerstripe.releng.updatesite.elements.Feature;
import org.eclipse.tigerstripe.releng.updatesite.elements.Site;

/**
 * Add a feature to an existing Update site
 * 
 * @author erdillon
 * 
 */
public class AddFeature extends Task {

	private Feature feature;
	private String sitefile;

	public Feature createFeature() {
		this.feature = new Feature();
		return feature;
	}

	/**
	 * Set the "site" file to process
	 * 
	 * @param site
	 */
	public void setSitefile(String sitefile) {
		this.sitefile = sitefile;
	}

	@Override
	public void execute() throws BuildException {
		super.execute();

		try {
			Site site = UpdateSiteHelper.readSite(new File(sitefile));
			site.getFeatures().add(feature);

			site.saveAs(new File(sitefile));
		} catch (IOException e) {
			throw new BuildException(e);
		} catch (DocumentException e) {
			throw new BuildException(e);
		}
	}
}
