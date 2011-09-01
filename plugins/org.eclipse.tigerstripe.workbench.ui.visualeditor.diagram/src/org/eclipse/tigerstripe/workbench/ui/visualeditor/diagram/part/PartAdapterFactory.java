/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.TigerstripeShapeNodeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationAEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassAEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassZEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationZEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeAttributeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeLiteralEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeMethodEditPart;

public class PartAdapterFactory extends VisualeditorModelAdapterFactory {

	@Override
	protected IModelComponent adaptToModelComponent(Object adaptableObject) {
		if (adaptableObject instanceof TigerstripeShapeNodeEditPart) {
			TigerstripeShapeNodeEditPart part = (TigerstripeShapeNodeEditPart) adaptableObject;
			Node node = (Node) part.getModel();
			return super.adaptToModelComponent(node.getElement());
		} else if (adaptableObject instanceof ConnectionNodeEditPart) {
			ConnectionNodeEditPart part = (ConnectionNodeEditPart) adaptableObject;
			Edge edge = (Edge) part.getModel();
			if (edge.getElement() instanceof QualifiedNamedElement) {
				return super.adaptToModelComponent(edge.getElement());
			}
		} else if (adaptableObject instanceof ITextAwareEditPart) {
			ITextAwareEditPart tPart = (ITextAwareEditPart) adaptableObject;
			Node model = (Node) tPart.getModel();
			if (adaptableObject instanceof TigerstripeAttributeEditPart
					|| adaptableObject instanceof TigerstripeMethodEditPart
					|| adaptableObject instanceof TigerstripeLiteralEditPart) {
				return super.adaptToModelComponent(model.getElement());
			} else if (adaptableObject instanceof AssociationAEndNameEditPart
					|| adaptableObject instanceof AssociationClassAEndNameEditPart) {
				EditPart aEnd = (EditPart) adaptableObject;
				Edge edge = (Edge) ((AbstractEditPart) aEnd.getParent())
						.getModel();

				Association assoc = (Association) edge.getElement();
				if (assoc != null) {
					try {
						IAssociationArtifact artifact = (IAssociationArtifact) assoc
								.getCorrespondingIArtifact();
						if (artifact != null)
							return artifact.getAEnd();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						return null;
					}
				}
			} else if (adaptableObject instanceof AssociationZEndNameEditPart
					|| adaptableObject instanceof AssociationClassZEndNameEditPart) {
				EditPart zEnd = (EditPart) adaptableObject;
				Edge edge = (Edge) ((AbstractEditPart) zEnd.getParent())
						.getModel();

				Association assoc = (Association) edge.getElement();
				if (assoc != null) {
					try {
						IAssociationArtifact artifact = (IAssociationArtifact) assoc
								.getCorrespondingIArtifact();
						if (artifact != null)
							return artifact.getZEnd();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						return null;
					}
				}
			}
		}
		return null;
	}
}
