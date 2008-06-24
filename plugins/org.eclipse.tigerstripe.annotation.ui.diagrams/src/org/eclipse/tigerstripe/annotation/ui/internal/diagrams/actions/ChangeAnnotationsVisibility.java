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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.actions;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.jface.action.Action;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.AnnotationStatus;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.DiagramRebuildUtils;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.AnnotationTree.EditPartNode;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Yuri Strot
 *
 */
public class ChangeAnnotationsVisibility extends Action {
	
	private EditPartNode[] nodes;
	private boolean show;
	
	public ChangeAnnotationsVisibility(EditPartNode[] nodes, boolean show) {
		this.nodes = nodes;
		this.show = show;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		IWorkbenchPart part = WorkbenchUtil.getPart();
		if (part instanceof DiagramEditor) {
			DiagramEditor editor = (DiagramEditor)part;
			for (EditPartNode node : nodes) {
				EditPart ePart = node.getPart();
				AnnotationStatus[] annotations = node.getAnnotations();
				if (show)
					DiagramRebuildUtils.showAnnotations(editor, 
							ePart, annotations);
				else
					DiagramRebuildUtils.hideAnnotations(editor, 
							ePart, annotations);
			}
		}
	}

}
