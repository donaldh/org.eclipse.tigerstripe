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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Yuri Strot
 *
 */
public class DiagramListeners {
	
	private static Map<View, List<IPositionChangedListener>> map = 
		new HashMap<View, List<IPositionChangedListener>>();
	
	private static Map<View, NListener> nListeners = 
		new HashMap<View, NListener>();
	
	private static Map<View, EditPartViewer> viewers = 
		new HashMap<View, EditPartViewer>();
	
	protected static void doAddListeners(View view, EditPartViewer viewer, IPositionChangedListener listener) {
		doAddListener(view, listener);
		viewers.put(view, viewer);
		View[] views = getNext(view);
		for (int i = 0; i < views.length; i++)
			doAddListeners(views[i], viewer, listener);
	}
	
	protected static View[] getNext(View view) {
		List<View> views = new ArrayList<View>(2);
		if (view instanceof Node) {
			if (view.eContainer() instanceof View) {
				views.add((View)view.eContainer());
			}
		}
		else if (view instanceof Edge) {
			Edge edge = (Edge)view;
			if (edge.getSource() != null)
				views.add(edge.getSource());
			if (edge.getSource() != null)
				views.add(edge.getTarget());
		}
		return views.toArray(new View[views.size()]);
	}
	
	protected static View[] getPrev(View view) {
		EList<?> children = view.getVisibleChildren();
		List<View> views = new ArrayList<View>(children.size() + 2);
		for (Object object : children) {
			views.add((View)object);
		}
		List<?> source = view.getSourceEdges();
		for (Object object : source) {
			views.add((Edge)object);
		}
		List<?> target = view.getTargetEdges();
		for (Object object : target) {
			views.add((Edge)object);
		}
		return views.toArray(new View[views.size()]);
	}
	
	protected static void doAddListener(View view, IPositionChangedListener listener) {
		List<IPositionChangedListener> listeners = map.get(view);
		if (listeners == null) {
			listeners = new ArrayList<IPositionChangedListener>();
			map.put(view, listeners);
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(view);
			if (domain != null) {
				NListener nListener = new NListener(view);
				DiagramEventBroker.getInstance(domain).addNotificationListener(
						view, nListener);
				nListeners.put(view, nListener);
			}
		}
		listeners.add(listener);
	}
	
	protected static void doRemoveListeners(View view, IPositionChangedListener listener) {
		doRemoveListener(view, listener);
		View[] views = getNext(view);
		for (int i = 0; i < views.length; i++)
			doRemoveListeners(views[i], listener);
	}
	
	protected static void doRemoveListener(View view, IPositionChangedListener listener) {
		List<IPositionChangedListener> listeners = map.get(view);
		if (listeners != null) {
			listeners.remove(listener);
			if (listeners.size() == 0) {
				map.remove(view);
				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(view);
				if (domain != null) {
					NListener nListener = nListeners.get(view);
					DiagramEventBroker.getInstance(domain).removeNotificationListener(
							view, nListener);
				}
			}
			listeners.add(listener);
		}
	}
	
	public static void addListener(View view, EditPartViewer viewer, IPositionChangedListener listener) {
		doAddListeners(view, viewer, listener);
	}
	
	public static void removeListener(View view, IPositionChangedListener listener) {
		doRemoveListeners(view, listener);
	}
	
	private static class NListener implements NotificationListener {
		
		private View view;
		
		public NListener(View view) {
			this.view = view;
		}
		
		/**
		 * @return the view
		 */
		public View getView() {
			return view;
		}
		
		protected View getContainerView(Notification notification) {
			Object notifier = notification.getNotifier();
			if (notifier instanceof EObject) {
				EObject eobject = (EObject)notifier;
				while (eobject != null) {
					if (eobject instanceof View)
						return (View)eobject;
					eobject = eobject.eContainer();
				}
			}
			return null;
		}
		
		protected void updateViews(View view, EditPartViewer viewer) {
			BoundsCalculationUtils.update(view, viewer);
			View[] prevs = getPrev(view);
			for (View prev : prevs) {
				updateViews(prev, viewer);
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener#notifyChanged(org.eclipse.emf.common.notify.Notification)
		 */
		public void notifyChanged(Notification notification) {
			View view = getContainerView(notification);
			EditPartViewer viewer = viewers.get(view);
			if (viewer != null)
				updateViews(view, viewer);
			List<IPositionChangedListener> listeners = map.get(getView());
			if (listeners != null) {
				for (IPositionChangedListener listener : listeners)
					listener.positionChanged(notification);
			}
		}
		
	}

}
