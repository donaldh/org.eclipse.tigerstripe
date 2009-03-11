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

import org.eclipse.core.resources.IResource;

public class AnnotationUsage {

	public AnnotationUsage(IResource resource, IContribution contribution) {
		super();
		this.resource = resource;
		this.contribution = contribution;
	}
	private IResource resource;
	private IContribution contribution;
	
	
	public IResource getResource() {
		return resource;
	}
	public void setResource(IResource resource) {
		this.resource = resource;
	}
	public IContribution getContribution() {
		return contribution;
	}
	public void setContribution(IContribution contribution) {
		this.contribution = contribution;
	}
	
}
