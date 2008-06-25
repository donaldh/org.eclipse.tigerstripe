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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IDiagramListener;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationDiagramBuilder implements IDiagramListener {
	
	private Map<DiagramEditor, DiagramRebuldListener> map = 
		new HashMap<DiagramEditor, DiagramRebuldListener>();

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.IDiagramListener#diagramClosed(org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart)
	 */
	public void diagramClosed(DiagramEditor editor) {
		DiagramRebuldListener listener = map.remove(editor);
		if (listener != null)
			listener.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.IDiagramListener#diagramOpened(org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart)
	 */
	public void diagramOpened(DiagramEditor editor) {
		map.put(editor, new DiagramRebuldListener(editor));
		DiagramRebuildUtils.rebuld(editor);
	}

}
