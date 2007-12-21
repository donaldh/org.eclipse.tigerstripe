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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.gmf.synchronization.ClosedDiagramSynchronizerBase;
import org.eclipse.tigerstripe.workbench.ui.gmf.synchronization.IClosedDiagramSynchronizer;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;

/**
 * Closed Class Diagram Synchronizer
 * 
 * This is registered thru an extension defined in the .ui.base plugin so it
 * will be run against all Class diagrams that are closed.
 * 
 * @author eric
 * @since Bug 936
 */
public class ClosedClassDiagramSynchronizer extends
		ClosedDiagramSynchronizerBase implements IClosedDiagramSynchronizer {

	public final static String DIAGRAM_EXT = "wvd";

	private Map map;

	public ClosedClassDiagramSynchronizer() {
		super();
	}

	@Override
	public void artifactRenamed(String fromFQN, String toFQN)
			throws TigerstripeException {
		try {
			ClassDiagramSynchronizerUtils.handleQualifiedNamedElementRenamed(
					map, fromFQN, toFQN, getEditingDomain(),
					getDiagramEditDomain());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		saveDiagram();
	}

	@Override
	public void artifactChanged(IAbstractArtifact iArtifact)
			throws TigerstripeException {

		final IAbstractArtifact fArtifact = iArtifact;
		try {
			ClassDiagramSynchronizerUtils.handleQualifiedNamedElementChanged(
					getDiagram(), getDiagramEP(), map, fArtifact,
					getEditingDomain(), getDiagramEditDomain());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		saveDiagram();
	}

	@Override
	public void artifactRemoved(String targetFQN) throws TigerstripeException {

		TransactionalEditingDomain editingDomain = getEditingDomain();
		IDiagramEditDomain diagramEditDomain = getDiagramEditDomain();

		try {
			ClassDiagramSynchronizerUtils.handleQualifiedNamedElementRemoved(
					map, targetFQN, editingDomain, diagramEditDomain);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		saveDiagram();
	}

	@Override
	protected void initDiagramElement(Object diagramElement) {
		if (diagramElement instanceof Map) {
			map = (Map) diagramElement;
			map.setCorrespondingITigerstripeProject(getHandle()
					.getITigerstripeProject());
		}
	}

	@Override
	public String getSupportedDiagramExtension() {
		return DIAGRAM_EXT;
	}

}
