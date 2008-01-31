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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class AttributeETAdapter extends BaseETAdapter implements ETAdapter {

	private Attribute attribute;

	public AttributeETAdapter(Attribute attribute, IModelUpdater modelUpdater,
			ClassDiagramSynchronizer synchronizer) {
		super(modelUpdater, synchronizer);
		this.attribute = attribute;
		initialize();
	}

	protected void initialize() {

		// TigerstripeRuntime.logInfoMessage("Adding ETAdapter to Attr: " +
		// attribute.getName()
		// + this);

		// register self
		attribute.eAdapters().add(this);
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
		if (attribute.eContainer() == null) {
			Exception e = new Exception("null eContainer found for attribute "
					+ attribute.getName());
			IStatus status = new Status(IStatus.WARNING, EclipsePlugin
					.getPluginId(), 222, "null eContainer found for attribute "
					+ attribute.getName(), e);
			EclipsePlugin.log(status);
			return;
		}
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
					if (attribute.getType() != null) {
						// Yes! Create the attribute
						createAttribute(arg0.getNewStringValue(), attribute
								.getType(), attribute.getMultiplicity()
								.getLiteral(), ((AbstractArtifact) attribute
								.eContainer()).getFullyQualifiedName(),
								getModelUpdater());
					}
				} else if (!arg0.getOldStringValue().equals(
						arg0.getNewStringValue())) {
					try {
						IAttributeSetRequest request = (IAttributeSetRequest) getModelUpdater()
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ATTRIBUTE_SET);
						request.setArtifactFQN(((AbstractArtifact) attribute
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
						request.setArtifactFQN(((AbstractArtifact) attribute
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IAttributeSetRequest.MULTIPLICITY_FEATURE);
						request.setAttributeName(attribute.getName());
						request.setOldValue(oldVal.getLiteral());
						request.setNewValue(newVal.getLiteral());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("type")) {
				// type has changed
				if (arg0.getOldStringValue() == null) {
					// it's a create, but can only propagate if the name is set
					// as well
					if (attribute.getName() != null) {
						// Yes! Create the attribute
						createAttribute(attribute.getName(), arg0
								.getNewStringValue(), attribute
								.getMultiplicity().getLiteral(),
								((AbstractArtifact) attribute.eContainer())
										.getFullyQualifiedName(),
								getModelUpdater());
					}
				} else if (!arg0.getOldStringValue().equals(
						arg0.getNewStringValue())) {
					try {
						IAttributeSetRequest request = (IAttributeSetRequest) getModelUpdater()
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ATTRIBUTE_SET);
						request.setArtifactFQN(((AbstractArtifact) attribute
								.eContainer()).getFullyQualifiedName());
						request.setFeatureId(IAttributeSetRequest.TYPE_FEATURE);
						request.setAttributeName(attribute.getName());
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("visibility")) {
				if (attribute.getName() != null) {
					try {
						IAttributeSetRequest request = (IAttributeSetRequest) getModelUpdater()
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ATTRIBUTE_SET);
						request.setArtifactFQN(((AbstractArtifact) attribute
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IAttributeSetRequest.VISIBILITY_FEATURE);
						request.setAttributeName(attribute.getName());
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("defaultValue")) {
				if (attribute.getName() != null) {
					try {
						IAttributeSetRequest request = (IAttributeSetRequest) getModelUpdater()
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ATTRIBUTE_SET);
						request.setArtifactFQN(((AbstractArtifact) attribute
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IAttributeSetRequest.DEFAULTVALUE_FEATURE);
						request.setAttributeName(attribute.getName());
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("isOrdered")) {
				if (attribute.getName() != null) {
					try {
						IAttributeSetRequest request = (IAttributeSetRequest) getModelUpdater()
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ATTRIBUTE_SET);
						request.setArtifactFQN(((AbstractArtifact) attribute
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IAttributeSetRequest.ISORDERED_FEATURE);
						request.setAttributeName(attribute.getName());
						request.setOldValue(String.valueOf(arg0
								.getOldBooleanValue()));
						request.setNewValue(String.valueOf(arg0
								.getNewBooleanValue()));
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("isUnique")) {
				if (attribute.getName() != null) {
					try {
						IAttributeSetRequest request = (IAttributeSetRequest) getModelUpdater()
								.getRequestFactory()
								.makeRequest(
										IModelChangeRequestFactory.ATTRIBUTE_SET);
						request.setArtifactFQN(((AbstractArtifact) attribute
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IAttributeSetRequest.ISUNIQUE_FEATURE);
						request.setAttributeName(attribute.getName());
						request.setOldValue(String.valueOf(arg0
								.getOldBooleanValue()));
						request.setNewValue(String.valueOf(arg0
								.getNewBooleanValue()));
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
