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
package org.eclipse.tigerstripe.annotation.ui.util;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Yuri Strot
 *
 */
public class WorkbenchUtil {
	
	public static IWorkbenchWindow getWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
	
	public static Shell getShell() {
		IWorkbenchWindow window = getWindow();
		return window != null ? window.getShell() : null;
	}
	
	public static IWorkbenchPage getPage() {
		IWorkbenchWindow window = getWindow();
		return window != null ? window.getActivePage() : null;
	}
	
	public static IWorkbenchPart getPart() {
		IWorkbenchPage page = getPage();
		return page != null ? page.getActivePart() : null;
	}
	
	public static IEditorPart getEditor() {
		IWorkbenchPage page = getPage();
		return page != null ? page.getActiveEditor() : null;
	}
	
	public static IWorkbenchSite getSite() {
		IWorkbenchPart part = getPart();
		return part != null ? part.getSite() : null;
	}

}
