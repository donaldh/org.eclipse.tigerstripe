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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.dnd;

import java.lang.reflect.Field;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

public class SpecialUtils {
	public static StructuredViewer getView(ViewerDropAdapter obj) {
		Field f = getViewField();
		if (f != null) {
			try {
				return (StructuredViewer) f.get(obj);
			} catch (IllegalArgumentException e) {
				EclipsePlugin.log(e);
			} catch (IllegalAccessException e) {
				EclipsePlugin.log(e);
			}
		}
		return null;
	}

	public static void setView(ViewerDropAdapter obj, StructuredViewer viewer) {
		Field f = getViewField();
		try {
			f.set(obj, viewer);
		} catch (IllegalArgumentException e) {
			EclipsePlugin.log(e);
		} catch (IllegalAccessException e) {
			EclipsePlugin.log(e);
		}
	}

	public static Field getViewField() {
		Class<ViewerDropAdapter> cl = ViewerDropAdapter.class;
		Field fView = null;
		try {
			fView = cl.getDeclaredField("viewer");
			fView.setAccessible(true);
		} catch (SecurityException e) {
			EclipsePlugin.log(e);
		} catch (NoSuchFieldException e) {
			EclipsePlugin.log(e);
		}
		return fView;
	}
}
