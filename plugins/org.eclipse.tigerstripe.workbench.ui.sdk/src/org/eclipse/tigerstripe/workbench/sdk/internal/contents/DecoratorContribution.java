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


public class DecoratorContribution {
	
	public DecoratorContribution(String contributor,String decoratorClass, boolean readOnly) {
		super();
		this.decoratorClass = decoratorClass;
		this.contributor = contributor;
		this.readOnly = readOnly;
	}
	private String decoratorClass;
	private String contributor;
	private boolean readOnly;
	
	public String getDecoratorClass() {
		return decoratorClass;
	}
	public void setDecoratorClass(String decoratorClass) {
		this.decoratorClass = decoratorClass;
	}
	public String getContributor() {
		return contributor;
	}
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	
}
