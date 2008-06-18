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
package org.eclipse.tigerstripe.annotation.ui.diagrams;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;

/**
 * @author Yuri Strot
 *
 */
public interface IDiagramListener {
	
	public void diagramOpened(DiagramEditor editor);
	
	public void diagramClosed(DiagramEditor editor);

}
