/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.patterns;

import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;

public class RelationPattern extends ArtifactPattern implements IRelationPattern {

	private String aEndType = "";
	private String zEndType = "";
	
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
	
	
}
