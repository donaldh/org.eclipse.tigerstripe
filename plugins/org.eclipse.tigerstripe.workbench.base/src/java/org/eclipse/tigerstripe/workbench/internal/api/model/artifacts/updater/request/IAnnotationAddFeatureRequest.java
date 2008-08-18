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
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;

public interface IAnnotationAddFeatureRequest extends IModelChangeRequest {

	public static String ANNOTATION_FEATURE = "annotation";
	
	public void setArtifactFQN(String artifactFQN);
	
	public void setTarget(String target);

	public void setAnnotationClass(String annotationClass);
	
	public void setNamespaceURI(String namespaceURI);
	
	public void setFileURI(URI fileURI);
}
