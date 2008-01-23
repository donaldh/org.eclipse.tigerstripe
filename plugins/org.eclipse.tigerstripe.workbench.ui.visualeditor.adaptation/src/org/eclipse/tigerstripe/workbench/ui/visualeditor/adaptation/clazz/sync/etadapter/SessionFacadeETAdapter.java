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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.etadapter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class SessionFacadeETAdapter extends AbstractArtifactETAdapter {

	public SessionFacadeETAdapter(SessionFacadeArtifact datatype,
			IModelUpdater modelUpdater, ClassDiagramSynchronizer synchronizer) {
		super(datatype, modelUpdater, synchronizer);
	}

	@Override
	protected void handleAddNotification(Notification arg0) {
		super.handleAddNotification(arg0);

		String tsFeature = null;
		if (arg0.getFeature() instanceof EReference) {
			EReference ref = (EReference) arg0.getFeature();
			if (ref.getName().equals("emittedNotifications")) {
				tsFeature = IArtifactAddFeatureRequest.EMITTED_NOTIFICATIONS;
			} else if (ref.getName().equals("managedEntities")) {
				tsFeature = IArtifactAddFeatureRequest.MANAGED_ENTITIES;
			} else if (ref.getName().equals("namedQueries")) {
				tsFeature = IArtifactAddFeatureRequest.NAMED_QUERIES;
			} else if (ref.getName().equals("exposedProcedures")) {
				tsFeature = IArtifactAddFeatureRequest.EXPOSED_PROCEDURES;
			}
		}

		if (tsFeature != null) {
			try {
				IArtifactAddFeatureRequest request = (IArtifactAddFeatureRequest) getModelUpdater()
						.getRequestFactory()
						.makeRequest(
								IModelChangeRequestFactory.ARTIFACT_ADD_FEATURE);
				request.setArtifactFQN(((QualifiedNamedElement) arg0
						.getNotifier()).getFullyQualifiedName());
				request.setFeatureId(tsFeature);
				request.setFeatureValue(((QualifiedNamedElement) arg0
						.getNewValue()).getFullyQualifiedName());

				postChangeRequest(request);
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e); // FIXME
			}
		}
	}

}
