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
package org.eclipse.tigerstripe.workbench.internal.core.model;

/**
 * RefComment are used as a work around to the fact that it is impossible to
 * store large texts into properties in Javadoc comments such as
 * 
 * @hhhh prop = "large comment"
 * 
 * So there are store in proper comments on a fake static label that can then
 * further be referenced from anywhere.
 * 
 * @author Eric Dillon
 * 
 */
public class RefComment {

	// The actual textual content
	private String content;

	// The reference id
	private String label;

	public RefComment(AbstractArtifact artifact) {
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
