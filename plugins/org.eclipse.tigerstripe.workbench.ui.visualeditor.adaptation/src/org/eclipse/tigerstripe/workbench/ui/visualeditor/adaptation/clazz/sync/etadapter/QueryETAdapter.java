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
import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class QueryETAdapter extends AbstractArtifactETAdapter {

	public QueryETAdapter(NamedQueryArtifact datatype,
			IModelUpdater modelUpdater, ClassDiagramSynchronizer synchronizer) {
		super(datatype, modelUpdater, synchronizer);
	}

	@Override
	protected void handleSetNotification(Notification arg0) {
		super.handleSetNotification(arg0);
		if (arg0.getFeature() instanceof EReference) {
			EReference ref = (EReference) arg0.getFeature();
			if (ref.getName().equals("returnedType")
					&& arg0.getNewValue() != null) {
				IArtifactSetFeatureRequest request = null;
				try {
					request = (IArtifactSetFeatureRequest) getModelUpdater()
							.getRequestFactory()
							.makeRequest(
									IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
					request
							.setFeatureId(IArtifactSetFeatureRequest.RETURNED_TYPE);
					request.setArtifactFQN(((QualifiedNamedElement) arg0
							.getNotifier()).getFullyQualifiedName());
					request.setFeatureValue(((QualifiedNamedElement) arg0
							.getNewValue()).getFullyQualifiedName());
					postChangeRequest(request);
				} catch (TigerstripeException e) {
					// Ignore
				}

			}
		}
	}

}
