/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.refactor;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A Model refactor request captures a request for a change in the model that
 * requires refactoring (ie. propagation of changes across multiple model
 * elements).
 * 
 * @author erdillon
 * 
 */
public class ModelRefactorRequest extends RefactorRequest {

	private ITigerstripeModelProject originalProject;
	private ITigerstripeModelProject destinationProject;
	private String originalFQN;
	private String destinationFQN;

	public void setOriginal(ITigerstripeModelProject originalProject,
			String originalFQN) {
		this.originalFQN = originalFQN;
		this.originalProject = originalProject;
	}

	public void setDestination(ITigerstripeModelProject destinationProject,
			String destinationFQN) {
		this.destinationFQN = destinationFQN;
		this.destinationProject = destinationProject;
	}

	public ITigerstripeModelProject getOriginalProject() {
		return this.originalProject;
	}

	public ITigerstripeModelProject getDestinationProject() {
		return this.destinationProject;
	}

	public String getOriginalFQN() {
		return this.originalFQN;
	}

	public String getDestinationFQN() {
		return this.destinationFQN;
	}

	/**
	 * Returns true if the refactor request is valid, ie. if the original
	 * artifact exists and if the destination doesn't
	 * 
	 * @return
	 */
	@Override
	public boolean isValid() {
		throw new UnsupportedOperationException();
	}

}
