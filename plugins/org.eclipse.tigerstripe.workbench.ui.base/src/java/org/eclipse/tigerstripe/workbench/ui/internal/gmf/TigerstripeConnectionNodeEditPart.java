/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
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

public abstract class TigerstripeConnectionNodeEditPart extends
		ConnectionNodeEditPart implements ITigerstripeChangeListener {

	public TigerstripeConnectionNodeEditPart(View view) {
		super(view);
	}

	protected IModelComponent getModelComponent() {
		IModelComponent comp = (IModelComponent) getAdapter(IAbstractArtifact.class);
		return comp;
	}

	@Override
	public void activate() {
		// Performance improvements - only register for the right sort of changes.
		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
				this, ITigerstripeChangeListener.MODEL | ITigerstripeChangeListener.ANNOTATION);
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
	
	public void descriptorChanged(IResource changedDescriptor) {
		// NOT USED HERE
	}

	public void artifactResourceChanged(IResource changedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

	public void artifactResourceAdded(IResource addedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

	public void artifactResourceRemoved(IResource removedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

}
