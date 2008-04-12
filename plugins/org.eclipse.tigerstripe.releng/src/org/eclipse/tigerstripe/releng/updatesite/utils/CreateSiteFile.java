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
package org.eclipse.tigerstripe.releng.updatesite.utils;

import java.io.IOException;

import org.eclipse.tigerstripe.releng.updatesite.elements.CategoryDef;
import org.eclipse.tigerstripe.releng.updatesite.elements.Site;

/**
 * Simple utility to create a new site file
 */
public class CreateSiteFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Site site = new Site();

		CategoryDef def = new CategoryDef();
		def.setLabel("New Category");
		def.setName("category.name");
		site.getCategoryDefs().add(def);

		try {
			site.saveAs(new java.io.File("site.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
