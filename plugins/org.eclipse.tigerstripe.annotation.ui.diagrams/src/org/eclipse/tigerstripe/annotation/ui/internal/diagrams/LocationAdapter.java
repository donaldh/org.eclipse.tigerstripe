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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Yuri Strot
 *
 */
public class LocationAdapter {
	
	private ILocationAdapter parentAdapter;
	private static Map<EditPart, EditPart> map = new HashMap<EditPart, EditPart>();
	
	private LocationAdapter(EditPart from, EditPart to) {
		View fromView = (View)from.getModel();
		Node toView = (Node)to.getModel();
		parentAdapter = new ViewLocationAdapter(fromView, toView, from.getViewer());
		addAdapters();
		LocationDeactivateListener listener = new LocationDeactivateListener();
		map.put(from, to);
		to.addEditPartListener(listener);
		from.addEditPartListener(new PartDeactivateListener());
	}
	
	public static EditPart getAgentPart(EditPart part) {
		return map.get(part);
	}
	
	public static void addLocationAdapter(EditPart from, EditPart to) {
		if (getAgentPart(from) == null)
			new LocationAdapter(from, to);
	}
	
	protected void addAdapters() {
		parentAdapter.addAdapters();
	}
	
	protected void removeAdapters() {
		parentAdapter.removeAdapters();
	}
	
	private class PartDeactivateListener extends EditPartListener.Stub {
		
		public void partDeactivated(EditPart editpart) {
			EditPart locationPart = map.get(editpart);
			if (locationPart != null) {
				View view = (View)locationPart.getModel();
				DiagramRebuildUtils.deleteAnnotation(view);
				removeAdapters();
			}
		}
		
	}
	
	private class LocationDeactivateListener extends EditPartListener.Stub {
		
		public void partDeactivated(EditPart editpart) {
			List<EditPart> toRemove = new ArrayList<EditPart>();
			for (EditPart key : map.keySet()) {
				EditPart value = map.get(key);
				if (value == editpart)
					toRemove.add(key);
			}
			for (EditPart key : toRemove) {
				map.remove(key);
			}
			removeAdapters();
		}
		
	}
}