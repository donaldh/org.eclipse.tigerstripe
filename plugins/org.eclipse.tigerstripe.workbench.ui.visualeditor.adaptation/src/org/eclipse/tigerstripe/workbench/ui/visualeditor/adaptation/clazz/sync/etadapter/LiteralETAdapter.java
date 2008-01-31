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
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class LiteralETAdapter extends BaseETAdapter implements ETAdapter {

	private Literal literal;

	public LiteralETAdapter(Literal literal, IModelUpdater modelUpdater,
			ClassDiagramSynchronizer synchronizer) {
		super(modelUpdater, synchronizer);
		this.literal = literal;
		initialize();
	}

	protected void initialize() {

		// TigerstripeRuntime.logInfoMessage(" Creating LiteralETAdapter for " +
		// literal.getName() + " " + this);
		// register self
		literal.eAdapters().add(this);
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
		if (literal.eContainer() == null) {
			Exception e = new Exception("null eContainer found for literal "
					+ literal.getName());
			IStatus status = new Status(IStatus.WARNING, EclipsePlugin
					.getPluginId(), 222, "null eContainer found for literal "
					+ literal.getName(), e);
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
					// it's a create, but can only propagate if the value is set
					// as well
					if (literal.getValue() != null) {
						// Yes! Create the attribute
						createLiteral(arg0.getNewStringValue(), literal
								.getValue(), "FIXME",
								((AbstractArtifact) literal.eContainer())
										.getFullyQualifiedName(),
								getModelUpdater(), (AbstractArtifact) literal
										.eContainer());
					}
				} else if (!arg0.getOldStringValue().equals(
						arg0.getNewStringValue())) {
					try {
						ILiteralSetRequest request = (ILiteralSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.LITERAL_SET);
						request.setArtifactFQN(((AbstractArtifact) literal
								.eContainer()).getFullyQualifiedName());
						request.setFeatureId(ILiteralSetRequest.NAME_FEATURE);
						request.setLiteralName(arg0.getOldStringValue());
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());

						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("value")) {
				// type has changed
				if (arg0.getOldStringValue() == null) {
					// it's a create, but can only propagate if the name is set
					// as well
					if (literal.getName() != null) {
						// Yes! Create the attribute
						createLiteral(literal.getName(), arg0
								.getNewStringValue(), "FIXME",
								((AbstractArtifact) literal.eContainer())
										.getFullyQualifiedName(),
								getModelUpdater(), (AbstractArtifact) literal
										.eContainer());
					}
				} else if (!arg0.getOldStringValue().equals(
						arg0.getNewStringValue())) {
					try {
						ILiteralSetRequest request = (ILiteralSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.LITERAL_SET);
						request.setArtifactFQN(((AbstractArtifact) literal
								.eContainer()).getFullyQualifiedName());
						request.setFeatureId(ILiteralSetRequest.VALUE_FEATURE);
						request.setLiteralName(literal.getName());
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
	}

	public static void createLiteral(String literalName, String literalValue,
			String attributeMultiplicity, String parentArtifactFQN,
			IModelUpdater modelUpdater, AbstractArtifact artifact) {
		try {
			// Yes! Create the attribute
			ILiteralCreateRequest request = (ILiteralCreateRequest) modelUpdater
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.LITERAL_CREATE);
			request.setArtifactFQN(parentArtifactFQN);
			request.setLiteralName(literalName);
			request.setLiteralValue(literalValue);

			String type = null;
			if (artifact instanceof Enumeration) {
				Enumeration enume = (Enumeration) artifact;
				type = enume.getBaseType();
			} else {
				type = "int";
			}
			request.setLiteralType(type);
			modelUpdater.handleChangeRequest(request);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}

	}
}
