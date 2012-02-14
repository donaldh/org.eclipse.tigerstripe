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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.segment;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
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
	protected ITigerstripeModelProject getContainingProject()
			throws TigerstripeException {
		IContractSegment facet = getFacet();
		if (facet != null) {
			IAbstractTigerstripeProject aProject = facet.getContainingProject();
			if (aProject instanceof ITigerstripeModelProject)
				return (ITigerstripeModelProject) aProject;
		}
		throw new TigerstripeException("No containing project");
	}
}
