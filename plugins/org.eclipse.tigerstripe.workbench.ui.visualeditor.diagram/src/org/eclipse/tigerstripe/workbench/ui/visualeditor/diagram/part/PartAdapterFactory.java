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

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.tigerstripe.annotations.IAnnotable;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.gmf.TigerstripeShapeNodeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeAttributeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeLiteralEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeMethodEditPart;

public class PartAdapterFactory implements IAdapterFactory {

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IAnnotable.class) {
			return getAnnotable(adaptableObject);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] { IAnnotable.class };
	}

	protected Object getAnnotable(Object adaptableObject) {
		if (adaptableObject instanceof TigerstripeShapeNodeEditPart) {
			TigerstripeShapeNodeEditPart part = (TigerstripeShapeNodeEditPart) adaptableObject;
			Node node = (Node) part.getModel();
			AbstractArtifact art = (AbstractArtifact) node.getElement();
			try {
				return art.getCorrespondingIArtifact().getAdapter(
						IAnnotable.class);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		} else if (adaptableObject instanceof ITextAwareEditPart) {
			ITextAwareEditPart tPart = (ITextAwareEditPart) adaptableObject;
			Node model = (Node) tPart.getModel();
			if (adaptableObject instanceof TigerstripeAttributeEditPart) {
				Attribute attr = (Attribute) model.getElement();
				AbstractArtifact art = (AbstractArtifact) attr.eContainer();
				if (art != null) {
					try {
						IAbstractArtifact artifact = art
								.getCorrespondingIArtifact();
						for (IField field : artifact.getFields()) {
							if (field.getName().equals(attr.getName())) {
								return field.getAdapter(IAnnotable.class);
							}
						}
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						return null;
					}
				}
			} else if (adaptableObject instanceof TigerstripeMethodEditPart) {
				Method meth = (Method) model.getElement();
				AbstractArtifact art = (AbstractArtifact) meth.eContainer();
				if (art != null) {
					try {
						IAbstractArtifact artifact = art
								.getCorrespondingIArtifact();
						for (IMethod method : artifact.getMethods()) {
							if (method.getName().equals(meth.getName())) {
								return method.getAdapter(IAnnotable.class);
							}
						}
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						return null;
					}
				}
			} else if (adaptableObject instanceof TigerstripeLiteralEditPart) {
				Literal lit = (Literal) model.getElement();
				AbstractArtifact art = (AbstractArtifact) lit.eContainer();
				if (art != null) {
					try {
						IAbstractArtifact artifact = art
								.getCorrespondingIArtifact();
						for (ILiteral literal : artifact.getLiterals()) {
							if (literal.getName().equals(lit.getName())) {
								return literal.getAdapter(IAnnotable.class);
							}
						}
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
