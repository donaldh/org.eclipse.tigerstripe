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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The connection collector refresh all corresponding views when annotation was created.
 * It needs when annotation view visibility set to "false" value.
 * 
 * @author Yuri Strot
 */
public class ConnectionCollector {
	
	private List<AnnotationConnectionEditPart> parts;
	private View view;
	
	public ConnectionCollector(View view) {
		this.view = view;
	}
	
	public void start() {
		parts = new ArrayList<AnnotationConnectionEditPart>();
	}
	
	public void finish() {
		parts = null;
	}
	
	public void addConnection(AnnotationConnectionEditPart part) {
		if (parts != null)
			parts.add(part);
	}
	
	public void removeConnection(AnnotationConnectionEditPart part) {
		if (parts != null)
			parts.remove(part);
	}
	
	public void refresh() {
		for (AnnotationConnectionEditPart part : parts) {
			Edge edge = (Edge)part.getNotationView();
			View source = edge.getSource();
			View target = edge.getTarget();
			updateView(part, source);
			updateView(part, target);
		}
	}
	
	protected void updateView(AnnotationConnectionEditPart connection, View st) {
		if (st != view) {
			EditPart part = (EditPart)connection.getViewer().getEditPartRegistry().get(st);
			if (part != null) {
				if (part != connection.getSource() && part != connection.getTarget()) {
					part.refresh();
				}
			}
		}
	}

}
