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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class MapETAdapter extends BaseETAdapter implements ETAdapter {

	private Map map;

	public MapETAdapter(Map map, IModelUpdater modelUpdater,
			ClassDiagramSynchronizer synchronizer) {
		super(modelUpdater, synchronizer);
		this.map = map;
		initialize();
	}

	/**
	 * initialize this adapter by - creating adapter for any contained EObject
	 * within the map - registering itself as an adapter to the Map
	 * 
	 */
	protected void initialize() {

		List<AbstractArtifact> artifacts = map.getArtifacts();
		for (AbstractArtifact artifact : artifacts) {
			try {
				AbstractArtifactETAdapter adapter = (AbstractArtifactETAdapter) ETAdapterFactory
						.makeETAdapterFor(artifact, getModelUpdater(),
								getSynchronizer());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		List<Association> associations = map.getAssociations();
		for (Association association : associations) {
			try {
				AssociationETAdapter adapter = (AssociationETAdapter) ETAdapterFactory
						.makeETAdapterFor(association, getModelUpdater(),
								getSynchronizer());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		// and register self as an adapter to the map
		map.eAdapters().add(this);
	}

	@Override
	public void notifyChanged(Notification arg0) {
		super.notifyChanged(arg0);

		switch (arg0.getEventType()) {
		case Notification.ADD:
			handleAddNotification(arg0);
			break;
		}
	}

	protected void handleAddNotification(Notification notification) {
		// This corresponds to a new AbstractArtifact, Association or Dependency
		// being added in the Map (i.e. onto the Diagram).
		// However, when this new object is first created, all its attributes
		// are null. In particular, its name and package are null, which
		// prevents
		// from creating the corresponding Artifact in the Tigerstripe domain.
		// As result, we only create the adapter which will trigger the creation
		// once all "set" notifications have been received.
		// NOTE: because we explicitly set default values upon creation for name
		// and package in the MapItemSemanticEditPolicy, we know we will get
		// 2 set notifications per newly created elements, to cover name &
		// package
		// The adapter created below, simply needs to wait that both name and
		// packages are set to trigger the creation of the artifact in the
		// tigerstripe domain. (@see
		// AbstractArtifactETAdapter#handleSetNotification)

		try {
			ETAdapter adapter = ETAdapterFactory.makeETAdapterFor(
					(EObject) notification.getNewValue(), getModelUpdater(),
					getSynchronizer());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * Makes an IArtifactCreateRequest for the appropriate artifact type
	 * 
	 * @return
	 */
	protected IArtifactCreateRequest makeArtifactCreateRequest(
			QualifiedNamedElement element) {
		IArtifactCreateRequest request = null;
		try {
			request = (IArtifactCreateRequest) getModelUpdater()
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACT_CREATE);

			request.setArtifactName(element.getName());
			request.setArtifactPackage(element.getPackage());
			request.setArtifactType(ElementTypeMapper.mapToIArtifactType(
					element).getName());
		} catch (TigerstripeException e) {
			// Ignore
		}
		return request;
	}

}
