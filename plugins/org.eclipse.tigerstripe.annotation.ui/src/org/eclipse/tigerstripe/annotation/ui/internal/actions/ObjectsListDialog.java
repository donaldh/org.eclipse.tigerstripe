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
package org.eclipse.tigerstripe.annotation.ui.internal.actions;

import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.core.ProviderContext;

/**
 * @author Yuri Strot
 *
 */
public class ObjectsListDialog extends Dialog {
	
	private Map<Object, ProviderContext> map;
	private TypesControl control;

	/**
	 * @param parentShell
	 */
	public ObjectsListDialog(Map<Object, ProviderContext> map, Shell parentShell) {
		super(parentShell);
		this.map = map;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(new GridLayout());
		control = new TypesControl(map, parent, SWT.NONE);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		return control;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#initializeBounds()
	 */
	@Override
	protected void initializeBounds() {
		getShell().setText("Annotation Conflict");
		getShell().setSize(400, 300);
	}
	
	public Object getSelected() {
		return control.getSelected();
	}

}
