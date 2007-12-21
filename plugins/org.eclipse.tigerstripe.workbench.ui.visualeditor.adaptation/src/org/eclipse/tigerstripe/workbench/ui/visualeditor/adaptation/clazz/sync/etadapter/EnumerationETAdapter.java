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

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class EnumerationETAdapter extends AbstractArtifactETAdapter {

	public EnumerationETAdapter(Enumeration datatype,
			IModelUpdater modelUpdater, ClassDiagramSynchronizer synchronizer) {
		super(datatype, modelUpdater, synchronizer);
	}

	@Override
	protected void initialize() {
		List<Literal> literals = getArtifact().getLiterals();

		for (Literal literal : literals) {
			try {
				LiteralETAdapter adapter = (LiteralETAdapter) ETAdapterFactory
						.makeETAdapterFor(literal, getModelUpdater(),
								getSynchronizer());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		super.initialize();
	}

	@Override
	protected void handleSetNotification(Notification arg0) {

		// First let's take care of the initial sets (name & package)
		// that would lead to creating the artifact in the TS domain
		// Before a create can be issued, we need name, package and both
		// end types

		super.handleSetNotification(arg0);

		if (arg0.getFeature() instanceof EAttribute) {
			EAttribute eAttr = (EAttribute) arg0.getFeature();
			if (eAttr.getName().equals("baseType")) {
				handleSetBaseType(arg0.getOldStringValue(), arg0
						.getNewStringValue());
			}
		}
	}

	protected void handleSetBaseType(String oldType, String newType) {
		IArtifactSetFeatureRequest request = null;
		try {
			request = (IArtifactSetFeatureRequest) getModelUpdater()
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
			request.setFeatureId(IArtifactSetFeatureRequest.BASE_TYPE);
			request.setArtifactFQN(getArtifact().getFullyQualifiedName());
			request.setFeatureValue(newType);
			postChangeRequest(request);
		} catch (TigerstripeException e) {
			// Ignore
		}
	}
}
