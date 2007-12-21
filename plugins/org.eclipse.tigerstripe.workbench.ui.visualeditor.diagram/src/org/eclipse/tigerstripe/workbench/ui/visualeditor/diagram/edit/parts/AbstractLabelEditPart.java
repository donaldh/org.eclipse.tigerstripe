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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import org.eclipse.gmf.runtime.diagram.ui.editparts.LabelEditPart;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

/**
 * Commonality between all NamePackageEditPart to ensure the label is refreshed
 * when the isAbstract attribute is changed.
 * 
 * @author Eric Dillon
 * @since 2.1
 */
public abstract class AbstractLabelEditPart extends LabelEditPart {

	public AbstractLabelEditPart(View arg0) {
		super(arg0);
	}

	protected boolean isAbstractArtifact() {
		if (getParent().getModel() instanceof Node) {
			Node model = (Node) getParent().getModel();
			if (model.getElement() instanceof QualifiedNamedElement)
				return ((QualifiedNamedElement) model.getElement())
						.isIsAbstract();
		} else if (getParent().getModel() instanceof Edge) {
			Edge model = (Edge) getParent().getModel();
			if (model.getElement() instanceof QualifiedNamedElement)
				return ((QualifiedNamedElement) model.getElement())
						.isIsAbstract();
		}
		return false;
	}

}
