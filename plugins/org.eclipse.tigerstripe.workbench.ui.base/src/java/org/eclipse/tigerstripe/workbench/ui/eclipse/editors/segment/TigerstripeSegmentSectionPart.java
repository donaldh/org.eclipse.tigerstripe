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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class TigerstripeSegmentSectionPart extends
		TigerstripeSectionPart {

	public TigerstripeSegmentSectionPart(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit, int style) {
		super(page, parent, toolkit, style);
	}

	protected IContractSegment getFacet() throws TigerstripeException {
		SegmentEditor editor = (SegmentEditor) getPage().getEditor();
		return editor.getSegment();
	}

	protected boolean isReadonly() {
		return false;
	}

	/**
	 * Return the containing project for the facet if any
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	protected ITigerstripeProject getContainingProject()
			throws TigerstripeException {
		IContractSegment facet = getFacet();
		if (facet != null) {
			IAbstractTigerstripeProject aProject = facet.getContainingProject();
			if (aProject instanceof ITigerstripeProject)
				return (ITigerstripeProject) aProject;
		}
		throw new TigerstripeException("No containing project");
	}

}
