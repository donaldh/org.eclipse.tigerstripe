/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.annotation;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TigerstripeLazyObject implements ITigerstripeLazyObject {
	private IModelComponent element;
	private ITigerstripeModelProject project;
	private String fqn;

	public TigerstripeLazyObject(IModelComponent element) {
		this.element = element;
	}

	public TigerstripeLazyObject(ITigerstripeModelProject project, String fqn) {
		this.project = project;
		this.fqn = fqn;
	}

	public IModelComponent getObject() {
		if (element != null) {
			return element;
		}
		try {
			IAbstractArtifact artifact = project.getArtifactManagerSession()
					.getArtifactByFullyQualifiedName(fqn, true);
			return artifact;
		} catch (TigerstripeException e) {
			return null;
		}
	}

	public boolean isPackage() {
		return false;
	}
}
