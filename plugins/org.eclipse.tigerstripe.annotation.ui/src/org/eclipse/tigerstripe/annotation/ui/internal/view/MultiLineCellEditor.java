/**
 * <copyright> 
 *
 * Copyright (c) 2000-2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *   xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *
 * </copyright>
 */
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Yuri Strot
 *
 */
public class MultiLineCellEditor extends ExtendedDialogCellEditor {
	
	private String name;
	
	public MultiLineCellEditor(Composite composite, EDataType type, String name) {
		super(composite, new DefaultLabelProvider());
		this.name = name;
		valueHandler = new EDataTypeValueHandler(type);
	}
	
    protected EDataTypeValueHandler valueHandler;
	
    @Override
    protected Object openDialogBox(Control cellEditorWindow)
    {
      InputDialog dialog = new MultiLineInputDialog
        (cellEditorWindow.getShell(),
         name,
         "",
         valueHandler.toString(getValue()),
         valueHandler);
      return dialog.open() == Window.OK ? valueHandler.toValue(dialog.getValue()) : null;
    }

    private static class MultiLineInputDialog extends InputDialog
    {
      public MultiLineInputDialog(Shell parentShell, String title, String message, String initialValue, IInputValidator validator)
      {
        super(parentShell, title, message, initialValue, validator);
        setShellStyle(getShellStyle() | SWT.RESIZE);
      }

      protected Text createText(Composite composite)
      {
        Text text = new Text(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
        data.heightHint = 5 * text.getLineHeight();
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.ENTRY_FIELD_WIDTH);
        text.setLayoutData(data);
        return text;
      }
    }

}
