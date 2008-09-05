/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.patterns;


import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.ModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.BaseArtifactElementRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.MethodCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ArtifactPattern extends Pattern implements IArtifactPattern  {

	protected String artifactType;
	protected String artifactName = "";
	protected String extendedArtifactName = "";
	
	
	public void  setTargetArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}

	public String getTargetArtifactType() {	
		return this.artifactType;
	}

	public void setArtifactname(String artifactName){
		this.artifactName = artifactName;
	}
	
	public String getArtifactName() {
		return this.artifactName;
	}

	public void setExtendedArtifactname(String artifactName){
		this.extendedArtifactName = artifactName;
	}
	
	public String getExtendedArtifactName() {
		return this.extendedArtifactName;
	}

	public void executeRequests(ITigerstripeModelProject project,
			String packageName, String artifactName, String extendedArtifact, boolean createAndSet) {
		executeRequests( project, packageName,  artifactName, extendedArtifact,
				"", "", createAndSet);
	}

	public void executeRequests(ITigerstripeModelProject project,
			String packageName, String artifactName, String extendedArtifact,
			String aEndType, String zEndType,boolean createAndSet) {
		
		IArtifactManagerSession mgrSession = null;
		IModelUpdater modelUpdater;
		try {
			mgrSession = project.getArtifactManagerSession();
			modelUpdater = mgrSession.getIModelUpdater();
		} catch (TigerstripeException t){
			// Complete failure
			BasePlugin.log(t);
			return;
		}

		boolean created = false;
		// We need to keep track of the method LABEL as it gets updated
		String lastMethodLabel = "";
		for (IModelChangeRequest request : requests){
			// Only the first one should be a create request.
			String  fqn = packageName+"."+artifactName;

			if (request instanceof IArtifactCreateRequest ){
				try {
					if (createAndSet){
						if (created){
							BasePlugin.logErrorMessage("Duplicate Create Request in Pattern execution");
							return;
						}
						ArtifactCreateRequest createRequest = (ArtifactCreateRequest) request;
						createRequest.setArtifactName(artifactName);
						createRequest.setArtifactPackage(packageName);
						if (request instanceof ArtifactLinkCreateRequest){
							ArtifactLinkCreateRequest linkRequest = (ArtifactLinkCreateRequest)  request;
							linkRequest.setAEndType(aEndType);
							linkRequest.setZEndType(zEndType);
						} 

						modelUpdater.handleChangeRequest(createRequest);
						created = true;

					}
					if ( extendedArtifact != null && !extendedArtifact.equals("") && !extendedArtifact.equals("$user")){
						IArtifactSetFeatureRequest setRequest =(IArtifactSetFeatureRequest) (new ModelChangeRequestFactory()).makeRequest(IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
						setRequest.setFeatureId(IArtifactSetFeatureRequest.EXTENDS_FEATURE);
						setRequest.setArtifactFQN(fqn);
						setRequest.setFeatureValue(extendedArtifact);
						modelUpdater.handleChangeRequest(setRequest);
					}
				} catch (TigerstripeException t){
					// Failed to create Artifact - we can just stop here.
					BasePlugin.log(t);
					return;
				}
			} else {
				try {
					

					if (request instanceof BaseArtifactElementRequest){
						BaseArtifactElementRequest elementRequest = (BaseArtifactElementRequest)  request;
						elementRequest.setArtifactFQN(fqn);
					} 

					if (request instanceof IMethodChangeRequest){
						IMethodChangeRequest methodChangeRequest = (IMethodChangeRequest)  request;
						methodChangeRequest.setMethodLabelBeforeChange(lastMethodLabel);
					}


					modelUpdater.handleChangeRequest(request);

					if (request instanceof MethodCreateRequest){
						MethodCreateRequest methodCreateRequest = (MethodCreateRequest)  request;
						lastMethodLabel = methodCreateRequest.getCreatedMethodLabel();

					} else if (request instanceof IMethodChangeRequest){
						IMethodChangeRequest methodChangeRequest = (IMethodChangeRequest)  request;
						lastMethodLabel = methodChangeRequest.getMethodLabelAfterChange();
					}

				} catch (TigerstripeException t){
					// Failed to set something - Roll back the create
					BasePlugin.log(t);
					try {
						ArtifactDeleteRequest deleteRequest = (ArtifactDeleteRequest) (new ModelChangeRequestFactory()).makeRequest(IModelChangeRequestFactory.ARTIFACT_DELETE);
						deleteRequest.setArtifactName(artifactName);
						deleteRequest.setArtifactPackage(packageName);

						// TODO should use updater to handle the request
						// deleteRequest.execute(mgrSession);
						modelUpdater.handleChangeRequest(deleteRequest);
					} catch (TigerstripeException t2){
						// We are in a very bad place..
						BasePlugin.log(t2);
						return;
					}
					return;
				}

			}
		}
	}
}
