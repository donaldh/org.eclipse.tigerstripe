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
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.api.model.artifacts.updater.request.IArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class DependencyETAdapter extends QualifiedNamedElementETAdapter
		implements ETAdapter {

	private Dependency dependency;

	public DependencyETAdapter(Dependency dependency,
			IModelUpdater modelUpdater, ClassDiagramSynchronizer synchronizer) {
		super(dependency, modelUpdater, synchronizer);
		this.dependency = dependency;

		initialize();
	}

	protected void initialize() {

		// register self as an adapter
		dependency.eAdapters().add(this);
	}

	@Override
	protected void handleRemoveNotification(Notification arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleSetNotification(Notification arg0) {
		super.handleSetNotification(arg0);

		// then look at simple sets of the specifics of an association
		String featureId = null;
		String newFeatureValue = null;
		if (arg0.getFeature() instanceof EReference) {
			EReference ref = (EReference) arg0.getFeature();
			if (ref.getName().equals("aEnd") && arg0.getNewValue() != null) {
				featureId = IArtifactSetFeatureRequest.AEND;
				newFeatureValue = ((QualifiedNamedElement) arg0.getNewValue())
						.getFullyQualifiedName();
			} else if (ref.getName().equals("zEnd")
					&& arg0.getNewValue() != null) {
				featureId = IArtifactSetFeatureRequest.ZEND;
				newFeatureValue = ((QualifiedNamedElement) arg0.getNewValue())
						.getFullyQualifiedName();
			}
		}
		if (featureId != null) {
			IArtifactSetFeatureRequest request = null;
			try {
				request = (IArtifactSetFeatureRequest) getModelUpdater()
						.getRequestFactory()
						.makeRequest(
								IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
				request.setFeatureId(featureId);
				request.setArtifactFQN(((QualifiedNamedElement) arg0
						.getNotifier()).getFullyQualifiedName());
				request.setFeatureValue(newFeatureValue);
				postChangeRequest(request);
			} catch (TigerstripeException e) {
				// log exceptiona and ignore
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}
	}

	/**
	 * Makes an IArtifactCreateRequest for the appropriate artifact type
	 * 
	 * @return
	 */
	@Override
	protected IArtifactCreateRequest makeArtifactCreateRequest(
			QualifiedNamedElement element) {
		IArtifactLinkCreateRequest request = null;
		try {
			request = (IArtifactLinkCreateRequest) getModelUpdater()
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACTLINK_CREATE);
			request.setArtifactType(ElementTypeMapper.mapToIArtifactType(
					element).getName());

			Dependency dep = (Dependency) element;
			request.setArtifactName(element.getName());
			request.setArtifactPackage(element.getPackage());

			// we now that at this stage both ends are set!
			request.setAEndType(dep.getAEnd().getFullyQualifiedName());
			request.setAEndName("aEnd");
			request.setZEndType(dep.getZEnd().getFullyQualifiedName());
			request.setZEndName("zEnd");
		} catch (TigerstripeException e) {
			// Ignore
		}
		return request;
	}

}
