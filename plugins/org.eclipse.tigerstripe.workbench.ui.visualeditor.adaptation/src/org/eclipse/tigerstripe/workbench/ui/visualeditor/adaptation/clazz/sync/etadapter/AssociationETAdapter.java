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
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class AssociationETAdapter extends QualifiedNamedElementETAdapter
		implements ETAdapter {

	private Association association;

	public AssociationETAdapter(Association association,
			IModelUpdater modelUpdater, ClassDiagramSynchronizer synchronizer) {
		super(association, modelUpdater, synchronizer);
		this.association = association;

		initialize();
	}

	protected void initialize() {

		// register self as an adapter
		association.eAdapters().add(this);
	}

	@Override
	protected void handleSetNotification(Notification arg0) {

		// First let's take care of the initial sets (name & package)
		// that would lead to creating the artifact in the TS domain
		// Before a create can be issued, we need name, package and both
		// end types

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
		} else if (arg0.getFeature() instanceof EAttribute) {
			EAttribute attr = (EAttribute) arg0.getFeature();
			if (attr.getName().equals("aEndIsChangeable")) {
				featureId = IArtifactSetFeatureRequest.AENDISCHANGEABLE;
				newFeatureValue = arg0.getNewStringValue();
			} else if (attr.getName().equals("aEndName")) {
				featureId = IArtifactSetFeatureRequest.AENDName;
				newFeatureValue = arg0.getNewStringValue();
			} else if (attr.getName().equals("aEndIsNavigable")) {
				featureId = IArtifactSetFeatureRequest.AENDNAVIGABLE;
				newFeatureValue = Boolean.toString(arg0.getNewBooleanValue());
			} else if (attr.getName().equals("aEndIsOrdered")) {
				featureId = IArtifactSetFeatureRequest.AENDISORDERED;
				newFeatureValue = Boolean.toString(arg0.getNewBooleanValue());
			} else if (attr.getName().equals("aEndIsUnique")) {
				featureId = IArtifactSetFeatureRequest.AENDISUNIQUE;
				newFeatureValue = Boolean.toString(arg0.getNewBooleanValue());
			} else if (attr.getName().equals("aEndAggregation")) {
				featureId = IArtifactSetFeatureRequest.AENDAGGREGATION;
				newFeatureValue = arg0.getNewStringValue();
			} else if (attr.getName().equals("aEndMultiplicity")) {
				featureId = IArtifactSetFeatureRequest.AENDMULTIPLICITY;
				newFeatureValue = arg0.getNewStringValue();
			} else if (attr.getName().equals("aEndVisibility")) {
				featureId = IArtifactSetFeatureRequest.AENDVISIBILITY;
				newFeatureValue = arg0.getNewStringValue();
			} else if (attr.getName().equals("zEndName")) {
				featureId = IArtifactSetFeatureRequest.ZENDName;
				newFeatureValue = arg0.getNewStringValue();
			} else if (attr.getName().equals("zEndIsOrdered")) {
				featureId = IArtifactSetFeatureRequest.ZENDISORDERED;
				newFeatureValue = Boolean.toString(arg0.getNewBooleanValue());
			} else if (attr.getName().equals("zEndIsUnique")) {
				featureId = IArtifactSetFeatureRequest.ZENDISUNIQUE;
				newFeatureValue = Boolean.toString(arg0.getNewBooleanValue());
			} else if (attr.getName().equals("zEndIsNavigable")) {
				featureId = IArtifactSetFeatureRequest.ZENDNAVIGABLE;
				newFeatureValue = Boolean.toString(arg0.getNewBooleanValue());
			} else if (attr.getName().equals("zEndIsChangeable")) {
				featureId = IArtifactSetFeatureRequest.ZENDISCHANGEABLE;
				newFeatureValue = arg0.getNewStringValue();
			} else if (attr.getName().equals("zEndAggregation")) {
				featureId = IArtifactSetFeatureRequest.ZENDAGGREGATION;
				newFeatureValue = arg0.getNewStringValue();
			} else if (attr.getName().equals("zEndMultiplicity")) {
				featureId = IArtifactSetFeatureRequest.ZENDMULTIPLICITY;
				newFeatureValue = arg0.getNewStringValue();
			} else if (attr.getName().equals("zEndVisibility")) {
				featureId = IArtifactSetFeatureRequest.ZENDVISIBILITY;
				newFeatureValue = arg0.getNewStringValue();
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

	@Override
	protected void handleAddNotification(Notification arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleRemoveNotification(Notification arg0) {
		// TODO Auto-generated method stub

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

			Association assoc = (Association) element;
			request.setArtifactName(element.getName());
			request.setArtifactPackage(element.getPackage());

			// we now that at this stage both ends are set!
			request.setAEndType(assoc.getAEnd().getFullyQualifiedName());
			request.setAEndNavigability(assoc.isAEndIsNavigable());
			request.setAEndName("aEnd");
			request.setZEndType(assoc.getZEnd().getFullyQualifiedName());
			request.setZEndName("zEnd");
			request.setZEndNavigability(assoc.isZEndIsNavigable());
		} catch (TigerstripeException e) {
			// Ignore
		}
		return request;
	}

}
