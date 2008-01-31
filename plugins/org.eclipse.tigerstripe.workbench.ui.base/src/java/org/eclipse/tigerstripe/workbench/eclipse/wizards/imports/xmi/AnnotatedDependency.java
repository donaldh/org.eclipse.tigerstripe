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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.imports.xmi;

import java.util.Properties;

public class AnnotatedDependency extends AnnotatedElement {

	private String aEndType;
	private String zEndType;

	public AnnotatedDependency(String packageName, String name) {
		super(packageName, name);
	}

	public String getAEndType() {
		return aEndType;
	}

	public void setAEndType(String endType) {
		aEndType = endType;
	}

	public String getZEndType() {
		return zEndType;
	}

	public void setZEndType(String endType) {
		zEndType = endType;
	}

	@Override
	public Properties mergeProperties(Properties prop) {
		Properties result = super.mergeProperties(prop);

		result.put("aEndType", getAEndType());
		result.put("zEndType", getZEndType());

		return result;
	}
}
