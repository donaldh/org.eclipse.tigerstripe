/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.contents;


public class DisabledPatternContribution {

	public DisabledPatternContribution(String contributor,
			String disabledPatternName, boolean readOnly) {
		super();
		this.contributor = contributor;
		this.disabledPatternName = disabledPatternName;
		this.readOnly = readOnly;
	}
	private String contributor;
	private String disabledPatternName;
	private boolean readOnly;
	public String getContributor() {
		return contributor;
	}
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	public String getDisabledPatternName() {
		return disabledPatternName;
	}
	public void setDisabledPatternName(String disabledPatternName) {
		this.disabledPatternName = disabledPatternName;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	
}
