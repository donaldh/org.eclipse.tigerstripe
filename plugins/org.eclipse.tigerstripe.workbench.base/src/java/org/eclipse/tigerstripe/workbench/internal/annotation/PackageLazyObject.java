/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
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

import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class PackageLazyObject implements ITigerstripeLazyObject {

	private IModelComponent element;
	private ITigerstripeModelProject project;
	private String fqn;

	public PackageLazyObject(IModelComponent element) {
		this.element = element;
	}

	public PackageLazyObject(ITigerstripeModelProject project, String fqn) {
		this.project = project;
		this.fqn = fqn;
	}

	@SuppressWarnings("deprecation")
	public IResource getObject() {
		IModelComponent c = null;
		if (element != null) {
			c = element;
		} else {
			try {
				c = project.getArtifactManagerSession()
						.getArtifactByFullyQualifiedName(fqn, true);
			} catch (TigerstripeException e) {
				// Do nothing
			}
		}
		if (c == null) {
			return null;
		}
		IResource r = (IResource) c.getAdapter(IResource.class);
		if (r == null) {
			return null;
		}
		return r.getParent();
	}

	public boolean isPackage() {
		return true;
	}
}
