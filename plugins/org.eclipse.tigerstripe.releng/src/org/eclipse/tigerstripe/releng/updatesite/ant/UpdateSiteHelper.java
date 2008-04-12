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

import org.dom4j.DocumentException;
import org.eclipse.tigerstripe.releng.updatesite.elements.Site;

/**
 * Helper class with convenience methods to manipulate Build elements
 * 
 * @author erdillon
 * 
 */
public class UpdateSiteHelper {

	/**
	 * Reads the given site.xml into a Site element. The file is expected to
	 * contain a single top-level Site.
	 * 
	 * @param siteFile -
	 *            the absolute path to the site.xml file.
	 * @return
	 * @throws IOException
	 */
	public static Site readSite(File sitefile) throws DocumentException {
		Site result = Site.parse(sitefile);
		return result;
	}

}
