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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.ui.util.AsyncExecUtil;

/**
 * @author Yuri Strot
 *
 */
public class DiagramRebuldListener implements IAnnotationListener {
	
	private DiagramEditor editor;
	
	public DiagramRebuldListener(DiagramEditor editor) {
		this.editor = editor;
		initialize();
	}
	
	protected void initialize() {
		AnnotationPlugin.getManager().addAnnotationListener(this);
	}
	
	public void dispose() {
		AnnotationPlugin.getManager().removeAnnotationListener(this);
	}
	
	protected void update() {
		AsyncExecUtil.run(editor.getSite().getShell(), new Runnable() {
		
			public void run() {
				DiagramRebuildUtils.rebuld(editor);
			}
		
		});
	}

	public void annotationAdded(Annotation annotation) {
		update();
	}

	public void annotationRemoved(Annotation annotation) {
		update();
	}

	public void annotationChanged(Annotation annotation) {
		update();
	}

}
