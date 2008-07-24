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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.TigerstripeShapeNodeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;

/**
 * This class simply implements the getAdapter(..) method on
 * {@link TigerstripeShapeNodeEditPart} so the resolution to an
 * IAbstractArtifact can be handled here.
 * 
 * @author erdillon
 * 
 */
public abstract class AdaptableTigerstripeShapeNodeEditPart extends
		TigerstripeShapeNodeEditPart implements IAdaptable {

	public AdaptableTigerstripeShapeNodeEditPart(View arg0) {
		super(arg0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class key) {
		if (key.equals(IAbstractArtifact.class)) {
			Object model = this.getModel();
			if (model instanceof Node) {
				Node node = (Node) model;
				EObject element = node.getElement();
				if (element instanceof AbstractArtifact) {
					AbstractArtifact artifact = (AbstractArtifact) element;
					try {
						return artifact.getCorrespondingIArtifact();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}

		return super.getAdapter(key);
	}
}
