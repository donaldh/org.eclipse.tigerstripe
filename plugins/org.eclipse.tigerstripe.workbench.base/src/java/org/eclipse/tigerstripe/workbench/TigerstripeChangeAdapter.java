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
package org.eclipse.tigerstripe.workbench;

import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

public class TigerstripeChangeAdapter implements ITigerstripeChangeListener {

	public void projectAdded(IAbstractTigerstripeProject project) {
	}

	public void projectDeleted(String projectName) {
	}

	public void descriptorChanged(IResource changedDescriptor) {
	}

	public void modelChanged(IModelChangeDelta[] delta) {
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
	}

	public void artifactResourceChanged(IResource changedArtifactResource) {
	}

	public void artifactResourceAdded(IResource addedArtifactResource) {
	}

	public void artifactResourceRemoved(IResource removedArtifactResource) {
	}
}
