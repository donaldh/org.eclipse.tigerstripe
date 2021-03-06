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
package org.eclipse.tigerstripe.workbench.internal.api.impl.updater;

import java.util.Arrays;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.AnnotationAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactCreateExistingRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactFQRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactRemoveFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.AttributeCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.AttributeRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.AttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.LiteralCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.LiteralRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.LiteralSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.MethodAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.MethodAnnotationsAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.MethodCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.MethodRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.MethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.StereotypeAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;

public class ModelChangeRequestFactory implements IModelChangeRequestFactory {

	private final static String[] supportedTypes = { ARTIFACT_CREATE, ARTIFACT_CREATE_EXISTING,

	ATTRIBUTE_CREATE, ATTRIBUTE_REMOVE, ATTRIBUTE_SET,

	LITERAL_CREATE, LITERAL_REMOVE, LITERAL_SET,

	ARTIFACT_SET_FEATURE, ARTIFACT_ADD_FEATURE, ARTIFACT_REMOVE_FEATURE,
			ARTIFACT_RENAME, ARTIFACT_FQRENAME, ARTIFACT_DELETE,

			METHOD_CREATE, METHOD_SET, METHOD_ADD_FEATURE, METHOD_REMOVE,

			ARTIFACTLINK_CREATE,
			
			STEREOTYPE_ADD, ANNOTATION_ADD,
			METHOD_ANNOTATION_ADD};

	private final static Class[] requestClasses = {
			ArtifactCreateRequest.class, ArtifactCreateExistingRequest.class,
			
			AttributeCreateRequest.class, AttributeRemoveRequest.class,
			AttributeSetRequest.class,

			LiteralCreateRequest.class, LiteralRemoveRequest.class,
			LiteralSetRequest.class,

			ArtifactSetFeatureRequest.class, ArtifactAddFeatureRequest.class,
			ArtifactRemoveFeatureRequest.class, ArtifactRenameRequest.class,
			ArtifactFQRenameRequest.class, ArtifactDeleteRequest.class,

			MethodCreateRequest.class, MethodSetRequest.class,
			MethodAddFeatureRequest.class, MethodRemoveRequest.class,

			ArtifactLinkCreateRequest.class,
			StereotypeAddFeatureRequest.class,
			AnnotationAddFeatureRequest.class,
			MethodAnnotationsAddFeatureRequest.class};

	private ModelUpdaterImpl modelUpdater;

	// Bug 242340
	// Adding a no argument version as the modelUpdater seems to 
	// be unncessary.
	public ModelChangeRequestFactory() {
	}
	
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
				
				// Bug 242340 - Add a null check
				if (modelUpdater != null){
					request.setTargetModelUpdater(modelUpdater);
				}
				
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

	public <T> T makeRequest(Class<T> requestClass)
			throws TigerstripeException {
		return requestClass.cast(makeRequest(requestClass.getName()));
	}

}
