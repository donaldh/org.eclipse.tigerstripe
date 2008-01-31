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
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactRemoveFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;

/**
 * A change request Factory
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IModelChangeRequestFactory {

	public final static String ARTIFACT_CREATE = IArtifactCreateRequest.class
			.getName();
	public final static String ARTIFACT_RENAME = IArtifactRenameRequest.class
			.getName();
	public final static String ARTIFACT_DELETE = IArtifactDeleteRequest.class
			.getName();

	public final static String ARTIFACT_SET_FEATURE = IArtifactSetFeatureRequest.class
			.getName();
	public final static String ARTIFACT_ADD_FEATURE = IArtifactAddFeatureRequest.class
			.getName();
	public final static String ARTIFACT_REMOVE_FEATURE = IArtifactRemoveFeatureRequest.class
			.getName();

	public final static String ATTRIBUTE_CREATE = IAttributeCreateRequest.class
			.getName();
	public final static String ATTRIBUTE_REMOVE = IAttributeRemoveRequest.class
			.getName();
	public final static String ATTRIBUTE_SET = IAttributeSetRequest.class
			.getName();

	public final static String LITERAL_CREATE = ILiteralCreateRequest.class
			.getName();
	public final static String LITERAL_REMOVE = ILiteralRemoveRequest.class
			.getName();
	public final static String LITERAL_SET = ILiteralSetRequest.class.getName();

	public final static String METHOD_CREATE = IMethodCreateRequest.class
			.getName();
	public final static String METHOD_SET = IMethodSetRequest.class.getName();
	public final static String METHOD_REMOVE = IMethodRemoveRequest.class
			.getName();

	public final static String ARTIFACTLINK_CREATE = IArtifactLinkCreateRequest.class
			.getName();

	public String[] getSupportedRequestTypes();

	/**
	 * Creates a blank request of the given type
	 * 
	 * @param requestType
	 * @return
	 * @throws TigerstripeException
	 */
	public IModelChangeRequest makeRequest(String requestType)
			throws TigerstripeException;
}
