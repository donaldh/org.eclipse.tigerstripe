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

import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.IModifyCallback;
import org.eclipse.ui.forms.editor.FormEditor;

public class PageModifyCallback implements IModifyCallback {

	private final TigerstripeFormPage page; 
	
	public PageModifyCallback(TigerstripeFormPage page) {
		this.page = page;
	}

	public void modify() {
		FormEditor editor = page.getEditor();
		if (editor instanceof ArtifactEditorBase) {
			((ArtifactEditorBase)editor).pageModified();
		}
	}

}
