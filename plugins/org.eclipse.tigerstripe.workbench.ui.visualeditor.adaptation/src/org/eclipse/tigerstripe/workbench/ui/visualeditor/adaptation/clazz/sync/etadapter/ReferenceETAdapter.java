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
import org.eclipse.tigerstripe.api.artifacts.updater.request.IAttributeCreateRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class ReferenceETAdapter extends BaseETAdapter implements ETAdapter {

	private Reference reference;

	public ReferenceETAdapter(Reference reference, IModelUpdater modelUpdater,
			ClassDiagramSynchronizer synchronizer) {
		super(modelUpdater, synchronizer);
		this.reference = reference;
		initialize();
	}

	protected void initialize() {

		// register self
		reference.eAdapters().add(this);
	}

	@Override
	public void notifyChanged(Notification arg0) {
		super.notifyChanged(arg0);

		switch (arg0.getEventType()) {
		case Notification.SET:
			handleSetNotification(arg0);
		}
	}

	protected void handleSetNotification(Notification arg0) {
		// We're getting 2 successive "SET"s for the name and the type of this
		// attribute
		// We can only trigger a request to the Tigerstripe domain when we've
		// got both
		if (arg0.getFeature() instanceof EAttribute) {
			EAttribute attr = (EAttribute) arg0.getFeature();
			if (attr.getName().equals("name")) {
				// name has changed
				if (arg0.getOldStringValue() == null) {
					// it's a create, but can only propagate if the type is set
					// as well
					if (reference.getZEnd() != null) {
						// Yes! Create the attribute
						createAttribute(arg0.getNewStringValue(), reference
								.getZEnd().getFullyQualifiedName(), "FIXME",
								((AbstractArtifact) reference.eContainer())
										.getFullyQualifiedName(),
								getModelUpdater());
					}
				} else if (!arg0.getOldStringValue().equals(
						arg0.getNewStringValue())) {
					try {
						// simply setting new name
						IAttributeSetRequest request = (IAttributeSetRequest) getModelUpdater()
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ATTRIBUTE_SET);
						request.setArtifactFQN(((AbstractArtifact) reference
								.eContainer()).getFullyQualifiedName());
						request.setFeatureId(IAttributeSetRequest.NAME_FEATURE);
						request.setAttributeName(arg0.getOldStringValue());
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());

						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("typeMultiplicity")) {
				if (((AssocMultiplicity) arg0.getOldValue()) != ((AssocMultiplicity) arg0
						.getNewValue())) {
					try {
						AssocMultiplicity oldVal = (AssocMultiplicity) arg0
								.getOldValue();
						AssocMultiplicity newVal = (AssocMultiplicity) arg0
								.getNewValue();
						IAttributeSetRequest request = (IAttributeSetRequest) getModelUpdater()
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ATTRIBUTE_SET);
						request.setArtifactFQN(((AbstractArtifact) reference
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IAttributeSetRequest.MULTIPLICITY_FEATURE);
						request.setAttributeName(reference.getName());
						request.setOldValue(oldVal.getLiteral());
						request.setNewValue(newVal.getLiteral());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}

			}
		} else if (arg0.getFeature() instanceof EReference) {
			EReference ref = (EReference) arg0.getFeature();
			if (ref.getName().equals("zEnd")) {
				// type has changed
				if (arg0.getOldValue() == null) {
					// it's a create, but can only propagate if the name is set
					// as well
					if (reference.getName() != null
							&& reference.eContainer() != null) {
						// Yes! Create the attribute
						createAttribute(reference.getName(),
								((QualifiedNamedElement) arg0.getNewValue())
										.getFullyQualifiedName(), "FIXME",
								((AbstractArtifact) reference.eContainer())
										.getFullyQualifiedName(),
								getModelUpdater());
					}
				} else if (!arg0.getOldStringValue().equals(
						arg0.getNewStringValue())
						&& arg0.getNewStringValue() != null) {
					try {
						IAttributeSetRequest request = (IAttributeSetRequest) getModelUpdater()
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ATTRIBUTE_SET);
						request.setArtifactFQN(((AbstractArtifact) reference
								.eContainer()).getFullyQualifiedName());
						request.setFeatureId(IAttributeSetRequest.TYPE_FEATURE);
						request.setAttributeName(reference.getName());
						request.setOldValue(((QualifiedNamedElement) arg0
								.getOldValue()).getFullyQualifiedName());
						request.setNewValue(((QualifiedNamedElement) arg0
								.getNewValue()).getFullyQualifiedName());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
	}

	public static void createAttribute(String attributeName,
			String attributeType, String attributeMultiplicity,
			String parentArtifactFQN, IModelUpdater modelUpdater) {
		try {
			// Yes! Create the attribute
			IAttributeCreateRequest request = (IAttributeCreateRequest) modelUpdater
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ATTRIBUTE_CREATE);
			request.setArtifactFQN(parentArtifactFQN);
			request.setAttributeName(attributeName);
			request.setAttributeType(attributeType);
			request.setAttributeMultiplicity(attributeMultiplicity);

			modelUpdater.handleChangeRequest(request);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}

	}
}
