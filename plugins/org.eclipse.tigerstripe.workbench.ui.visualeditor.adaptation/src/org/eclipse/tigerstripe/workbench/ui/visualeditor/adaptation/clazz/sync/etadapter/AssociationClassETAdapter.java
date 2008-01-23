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
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class AssociationClassETAdapter extends AssociationETAdapter {

	public AssociationClassETAdapter(AssociationClass associationClass,
			IModelUpdater modelUpdater, ClassDiagramSynchronizer synchronizer) {
		super(associationClass, modelUpdater, synchronizer);
		// TigerstripeRuntime.logInfoMessage(" here= " +
		// associationClass.getAssociation());

		// FIXME: make sure we create a ETAdapter for the associated class.
	}

	@Override
	public void notifyChanged(Notification arg0) {
		super.notifyChanged(arg0);
	}

	@Override
	protected void handleSetNotification(Notification arg0) {

		// First let's take care of the initial sets (name & package)
		// that would lead to creating the artifact in the TS domain
		// Before a create can be issued, we need name, package and both
		// end types

		super.handleSetNotification(arg0);

		if (arg0.getFeature() instanceof EReference) {
			EReference eRef = (EReference) arg0.getFeature();
			if (eRef.getName().equals("association")) {
				handleSetAssociation((Association) arg0.getOldValue(),
						(Association) arg0.getNewValue(),
						(AssociationClass) arg0.getNotifier());
			}
		}
	}

	/**
	 * In the case of an associationClass, we need to wait for the association
	 * to be set after the package before we trigger the creation
	 * 
	 */
	@Override
	protected void handleSetPackage(String oldValue, String newValue,
			QualifiedNamedElement element) {
		// if (oldValue == null && assocClass.getAssociation() != null) {
		if (oldValue == null) {
			// this is the last part of a new element being created
			IArtifactCreateRequest request = makeArtifactCreateRequest(element);
			try {
				postChangeRequest(request);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		} else {
			// this is a refactor, where the package of the artifact is changed.
		}
	}

	/**
	 * In the case of an associationClass, we need to wait for the association
	 * to be set after the package before we trigger the creation
	 * 
	 */
	protected void handleSetAssociation(Association oldValue,
			Association newValue, AssociationClass assocClass) {
		if (oldValue == null) {
			// this is the last part of a new element being created
			IArtifactCreateRequest request = makeArtifactCreateRequest(assocClass);
			try {
				postChangeRequest(request);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		} else {
			// this is a refactor, where the package of the artifact is changed.
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

			AssociationClass assocClass = (AssociationClass) element;
			request.setArtifactName(element.getName());
			request.setArtifactPackage(element.getPackage());

			// Association assoc = assocClass.getAssociation();

			// we now that at this stage both ends are set!
			request.setAEndType(assocClass.getAEnd().getFullyQualifiedName());
			request.setAEndName("aEnd");
			request.setAEndNavigability(assocClass.isAEndIsNavigable());
			request.setZEndType(assocClass.getZEnd().getFullyQualifiedName());
			request.setZEndName("zEnd");
			request.setZEndNavigability(assocClass.isZEndIsNavigable());
		} catch (TigerstripeException e) {
			// Ignore
		}
		return request;
	}

}
