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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Yuri Strot
 *
 */
public class ViewLocationAdapter extends AdapterImpl implements ILocationAdapter, IPositionChangedListener {
	
	private View from;
	private Node to;
	
	private EditPartViewer viewer;
	
	public ViewLocationAdapter(View from, Node to, EditPartViewer viewer) {
		this.viewer = viewer;
		this.from = from;
		this.to = to;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.internal.diagrams.ILocationAdapter#addAdapters()
	 */
	public void addAdapters() {
		updateLocation();
		DiagramListeners.addListener(from, viewer, this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.internal.diagrams.ILocationAdapter#removeAdapters()
	 */
	public void removeAdapters() {
		DiagramListeners.removeListener(from, this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	@Override
	public void notifyChanged(Notification msg) {
		if (msg.getNotifier() == from) {
			if (msg.getOldValue() instanceof LayoutConstraint) {
				LayoutConstraint constraint = (LayoutConstraint)msg.getOldValue();
				constraint.eAdapters().remove(this);
			}
			if (msg.getNewValue() instanceof LayoutConstraint) {
				LayoutConstraint constraint = (LayoutConstraint)msg.getNewValue();
				constraint.eAdapters().add(this);
			}
		}
		else if (msg.getNotifier() instanceof LayoutConstraint) {
			updateLocation();
		}
	}
	
	protected void updateLocation() {
		Runnable runnable = new Runnable() {
			
			public void run() {
				Rectangle bounds = BoundsCalculationUtils.getBounds(from, viewer);
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
		
		};
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(from);
		if (domain != null) {
			DiagramRebuildUtils.modify(domain, runnable);
		}
		else {
			runnable.run();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.internal.diagrams.IPositionChangedListener#positionChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void positionChanged(Notification notification) {
		updateLocation();
	}

}
