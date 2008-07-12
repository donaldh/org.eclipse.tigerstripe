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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.DiagramRebuildUtils;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.utils.ViewFigureUtils;

/**
 * @author Yuri Strot
 *
 */
public class LocationMapper implements ILocationChangedListener {
	
	public static EditPart getLocationPart(EditPart part) {
		return map.get(part);
	}
	
	public static void mapLocation(EditPart from, EditPart to) {
		if (getLocationPart(from) == null) {
			LocationMapper adapter = new LocationMapper(from, to);
			adapter.addAdapters();
			map.put(from, to);
		}
	}
	
	private static Map<EditPart, EditPart> map = new HashMap<EditPart, EditPart>();
	
	private LocationMapper(EditPart from, EditPart to) {
		this.viewer = from.getViewer();
		this.from = (View)from.getModel();
		this.to = (Node)to.getModel();
		
		to.addEditPartListener(new LocationDeactivateListener());
		from.addEditPartListener(new PartDeactivateListener());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.internal.diagrams.IPositionChangedListener#positionChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void locationChanged() {
		updateLocation();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.internal.diagrams.ILocationAdapter#addAdapters()
	 */
	void addAdapters() {
		updateLocation();
		LocationService.addListener(from, viewer, this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.internal.diagrams.ILocationAdapter#removeAdapters()
	 */
	void removeAdapters() {
		LocationService.removeListener(from);
	}
	
	protected void doUpdateBounds() {
		Rectangle bounds = ViewFigureUtils.getBounds(from, viewer);
		if (bounds != null) {
			Bounds toLC = (Bounds)to.getLayoutConstraint();
			if (toLC != null) {
				toLC.setHeight(bounds.height);
				toLC.setWidth(bounds.width);
				toLC.setX(bounds.x);
				toLC.setY(bounds.y);
			}
		}
	}
	
	protected void updateLocation() {
		Runnable runnable = new Runnable() {
			
			public void run() {
				doUpdateBounds();
			}
		
		};
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(from);
		if (domain != null) {
			DiagramRebuildUtils.modify(domain, runnable);
		}
		else {
			runnable.run();
		}
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
	
	private View from;
	private Node to;
	private EditPartViewer viewer;
}