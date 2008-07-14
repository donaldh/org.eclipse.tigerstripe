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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.internal.annotation.AnnotationUtils;
import org.eclipse.ui.dialogs.ListSelectionDialog;

public class AnnotationPluginSelectionDialog extends ListSelectionDialog {

	public AnnotationPluginSelectionDialog(Shell parentShell,
			IStructuredContentProvider contentProvider,
			ILabelProvider labelProvider, String message) {
		super(parentShell, AnnotationUtils.getAnnotationPluginIds(),
				new ArrayContentProvider(), labelProvider, message);
	}

}
