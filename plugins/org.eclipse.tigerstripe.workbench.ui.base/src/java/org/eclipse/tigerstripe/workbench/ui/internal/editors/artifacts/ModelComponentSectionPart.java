/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class ModelComponentSectionPart extends ArtifactSectionPart {

	public static int MASTER_TABLE_COMPONENT_WIDTH = 250;

	public ModelComponentSectionPart(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider, int style) {
		super(page, parent, toolkit, labelProvider, contentProvider, style);
	}

	public abstract TableViewer getViewer();

	public abstract void updateMaster();

	protected abstract void createInternalContent();

	@Override
	protected void createContent() {
		createInternalContent();
		getViewer().getTable().addFocusListener(
				new SelectionProviderIntermediateFocusListener() {

					@Override
					public IWorkbenchSite getWorkbenchSite() {
						return getPage().getSite();
					}

					@Override
					public ISelectionProvider getSelectionProvider() {
						return getViewer();
					}
				});
	}
}
