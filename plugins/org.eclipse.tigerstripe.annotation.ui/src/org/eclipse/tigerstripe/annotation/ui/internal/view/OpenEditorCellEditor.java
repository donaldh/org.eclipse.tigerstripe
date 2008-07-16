/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tigerstripe.annotation.ui.internal.editor.PropertyEditorInput;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.PartInitException;

/**
 * @author Yuri Strot
 * 
 */
public class OpenEditorCellEditor extends ExtendedDialogCellEditor {

	private IProperty property;
	private String editorId;

	public OpenEditorCellEditor(Composite composite, IProperty property,
			String editorId) {
		super(composite, new DefaultLabelProvider());
		this.property = property;
		this.editorId = editorId;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		deactivate();
		PropertyEditorInput input = new PropertyEditorInput(property);
		try {
			WorkbenchUtil.getPage().openEditor(input, editorId);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

}
