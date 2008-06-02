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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.UndoableEdit;

public class CheckButtonEditListener extends BaseEditListener {

	private URI uri;

	public CheckButtonEditListener(TigerstripeFormEditor editor,
			String feature, int type, URI uri) {
		super(editor, feature, type);
		this.uri = uri;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		String newValue = Boolean.toString(((Button) e.widget).getSelection());
		String oldValue = Boolean.toString(!((Button) e.widget).getSelection());

		UndoableEdit edit = makeEdit(getURI(e), getFeature(), getType(),
				oldValue, newValue, null);
		getEditor().getUndoManager().addEdit(edit);
	}

	protected URI getURI(SelectionEvent e) {
		return uri;
	}
}
