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
package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

public abstract class TigerstripeShapeNodeEditPart extends ShapeNodeEditPart
		implements ITigerstripeChangeListener {

	public TigerstripeShapeNodeEditPart(View arg0) {
		super(arg0);
	}

	@Override
	public void activate() {
		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
				this, ITigerstripeChangeListener.ALL);
		super.activate();
	}

	@Override
	public void deactivate() {
		TigerstripeWorkspaceNotifier.INSTANCE
				.removeTigerstripeChangeListener(this);
		super.deactivate();
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		EditPart part = getPrimaryChildEditPart();
		part.refresh();
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		refreshVisuals();
	}

	public void projectAdded(IAbstractTigerstripeProject project) {
		// TODO Auto-generated method stub

	}

	public void projectDeleted(String projectName) {
		// TODO Auto-generated method stub

	}

	/**
	 * A property for this artifact has been changed thru an action of the user
	 * 
	 * @param propertyKey
	 */
	public void artifactPropertyChanged(String propertyKey, String oldValue,
			String newValue) {
		handleArtifactPropertyChanged(propertyKey, oldValue, newValue);
	}

	protected abstract void handleArtifactPropertyChanged(String propertyKey,
			String oldValue, String newValue);
}
