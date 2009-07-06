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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.ClosedDiagramSynchronizerBase;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.IClosedDiagramSynchronizer;

public class ClosedInstanceDiagramSynchronizer extends
		ClosedDiagramSynchronizerBase implements IClosedDiagramSynchronizer {

	public static final String DIAGRAM_EXT = "wod";

	private InstanceMap map;

	public ClosedInstanceDiagramSynchronizer() {
		super();
	}

	@Override
	public void artifactRenamed(String fromFQN, String toFQN)
			throws TigerstripeException {
		try {
			InstanceDiagramSynchronizerUtils.handleArtifactRenamed(map,
					fromFQN, toFQN, getEditingDomain(), getDiagramEditDomain());
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
			InstanceDiagramSynchronizerUtils.handleArtifactChanged(
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
			InstanceDiagramSynchronizerUtils.handleArtifactRemoved(map,
					targetFQN, editingDomain, diagramEditDomain);

			diagramEditDomain.getDiagramCommandStack().execute(
					new org.eclipse.gef.commands.Command() {

						@Override
						public boolean canExecute() {
							return true;
						}

						@Override
						public void execute() {
							DiagramEditPart diagPart = getDiagramEP();
							diagPart.refresh();
						}

					});

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		saveDiagram();
	}

	@Override
	protected void initDiagramElement(Object diagramElement) {
		if (diagramElement instanceof InstanceMap) {
			map = (InstanceMap) diagramElement;
			map.setCorrespondingITigerstripeProject(getHandle()
					.getITigerstripeProject());
		}
	}

	@Override
	public String getSupportedDiagramExtension() {
		return DIAGRAM_EXT;
	}
}
