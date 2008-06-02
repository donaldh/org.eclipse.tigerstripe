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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.undo;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.UndoableEdit;

public class TextEditListener extends BaseEditListener {

	public interface IURIBaseProviderPage {
		public URI getBaseURI();

		public ITigerstripeModelProject getProject();
	}

	private URI oldURI;
	private IURIBaseProviderPage page;

	public TextEditListener(TigerstripeFormEditor editor, String feature,
			int type, IURIBaseProviderPage page) {
		super(editor, feature, type);
		oldURI = null;
		this.page = page;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		URI newURI = getURIBase();

		if (oldURI != null) {
			UndoableEdit edit = makeEdit(oldURI, getFeature(), getType(),
					oldURI, newURI, getProject());
			getEditor().getUndoManager().addEdit(edit);
		}
		oldURI = newURI;
	}

	protected URI getURIBase() {
		return page.getBaseURI();
	}

	protected ITigerstripeModelProject getProject() {
		return page.getProject();
	}

	public void reset() {
		oldURI = null;
	}
}
