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
package org.eclipse.tigerstripe.api.impl.updater;

import java.util.Arrays;

import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.updater.request.ArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.ArtifactCreateRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.ArtifactDeleteRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.ArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.ArtifactRemoveFeatureRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.ArtifactRenameRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.ArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.AttributeCreateRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.AttributeRemoveRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.AttributeSetRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.LabelCreateRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.LabelRemoveRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.LabelSetRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.MethodCreateRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.MethodRemoveRequest;
import org.eclipse.tigerstripe.api.impl.updater.request.MethodSetRequest;

public class ModelChangeRequestFactory implements IModelChangeRequestFactory {

	private final static String[] supportedTypes = { ARTIFACT_CREATE,

	ATTRIBUTE_CREATE, ATTRIBUTE_REMOVE, ATTRIBUTE_SET,

	LABEL_CREATE, LABEL_REMOVE, LABEL_SET,

	ARTIFACT_SET_FEATURE, ARTIFACT_ADD_FEATURE, ARTIFACT_REMOVE_FEATURE,
			ARTIFACT_RENAME, ARTIFACT_DELETE,

			METHOD_CREATE, METHOD_SET, METHOD_REMOVE,

			ARTIFACTLINK_CREATE };

	private final static Class[] requestClasses = {
			ArtifactCreateRequest.class,

			AttributeCreateRequest.class, AttributeRemoveRequest.class,
			AttributeSetRequest.class,

			LabelCreateRequest.class, LabelRemoveRequest.class,
			LabelSetRequest.class,

			ArtifactSetFeatureRequest.class, ArtifactAddFeatureRequest.class,
			ArtifactRemoveFeatureRequest.class, ArtifactRenameRequest.class,
			ArtifactDeleteRequest.class,

			MethodCreateRequest.class, MethodSetRequest.class,
			MethodRemoveRequest.class,

			ArtifactLinkCreateRequest.class };

	private ModelUpdaterImpl modelUpdater;

	public ModelChangeRequestFactory(ModelUpdaterImpl modelUpdater) {
		this.modelUpdater = modelUpdater;
	}

	public String[] getSupportedRequestTypes() {
		return supportedTypes;
	}

	public IModelChangeRequest makeRequest(String requestType)
			throws TigerstripeException {
		if (Arrays.asList(supportedTypes).contains(requestType)) {
			int index = Arrays.asList(supportedTypes).indexOf(requestType);
			Class requestClass = requestClasses[index];

			try {
				BaseModelChangeRequest request = (BaseModelChangeRequest) requestClass
						.newInstance();
				request.setTargetModelUpdater(modelUpdater);
				return request;
			} catch (InstantiationException e) {
				throw new TigerstripeException(
						"Couldn't instantiate request of type '" + requestType
								+ "': " + e.getMessage(), e);
			} catch (IllegalAccessException e) {
				throw new TigerstripeException(
						"Couldn't instantiate request of type '" + requestType
								+ "': " + e.getMessage(), e);
			}
		}
		throw new TigerstripeException("Unknown request type");
	}

}
