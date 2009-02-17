/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class ExtensionSectionPart extends TigerstripeSectionPart {


	private IExtensionFormContentProvider contentProvider;

	public ExtensionSectionPart(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit,
			IExtensionFormContentProvider contentProvider, int style) {

		super(page, parent, toolkit,
				style != ExpandableComposite.NO_TITLE ? style
						| ExpandableComposite.TITLE_BAR
						: ExpandableComposite.NO_TITLE);

		this.contentProvider = contentProvider;
	}

	protected boolean isReadonly() {
		return false;
	}


	protected IExtensionFormContentProvider getContentProvider() {
		return contentProvider;
	}

	protected void setContentProvider(
			IExtensionFormContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}


	public void navigateToKeyPressed(KeyEvent e) {
		// TODO This will be really useful!
	}

}
