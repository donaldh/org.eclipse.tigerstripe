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

import org.eclipse.emf.common.util.URI;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

public abstract class TigerstripeShapeNodeEditPart extends ShapeNodeEditPart
		implements ITigerstripeChangeListener {

	public TigerstripeShapeNodeEditPart(View arg0) {
		super(arg0);
	}

	protected IModelComponent getModelComponent() {
		IModelComponent comp = (IModelComponent) getAdapter(IAbstractArtifact.class);
		return comp;
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

	private boolean shouldRefresh(IModelAnnotationChangeDelta[] deltas) {
		for (IModelAnnotationChangeDelta delta : deltas) {
			URI uri = delta.getAffectedModelComponentURI();
			IModelComponent component = getModelComponent();
			if (component != null) {
				try {
					URI thisURI = TigerstripeURIAdapterFactory.toURI(component);
					if (uri.equals(thisURI)) {
						return true;
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
		return false;
	}

	private boolean shouldRefresh(IModelChangeDelta[] deltas) {
		for (IModelChangeDelta delta : deltas) {
			if (delta.getType() == IModelChangeDelta.SET) {
				URI uri = delta.getAffectedModelComponentURI();
				IModelComponent component = getModelComponent();
				if (component != null) {
					try {
						URI thisURI = TigerstripeURIAdapterFactory
								.toURI(component);
						if (uri.equals(thisURI)) {
							return true;
						}
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
		return false;
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		if (shouldRefresh(delta)) {
			if (getPrimaryChildEditPart() != null)
				getPrimaryChildEditPart().refresh();
		}
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		if (shouldRefresh(delta)) {
			if (getPrimaryChildEditPart() != null)
				getPrimaryChildEditPart().refresh();
		}
	}

	public void projectAdded(IAbstractTigerstripeProject project) {
		// Ignoring here
	}

	public void projectDeleted(String projectName) {
		// Ignoring here
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
