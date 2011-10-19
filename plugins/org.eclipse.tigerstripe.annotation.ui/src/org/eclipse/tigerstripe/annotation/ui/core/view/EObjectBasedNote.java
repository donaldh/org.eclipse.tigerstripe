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
package org.eclipse.tigerstripe.annotation.ui.core.view;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;

/**
 * @author Alena Repina
 * 
 */
public abstract class EObjectBasedNote implements INote {
	private EContentAdapter adapter;
	private final ListenerList listeners = new ListenerList();

	public EObjectBasedNote() {
		adapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification msg) {
				super.notifyChanged(msg);
				if (msg.getEventType() == Notification.RESOLVE
						|| msg.getEventType() == Notification.REMOVING_ADAPTER)
					return;
				fireChange();
			}
		};
	}

	public void addChangeListener(INoteChangeListener listener) {
		if (listeners.size() == 0) {
			getContent().eAdapters().add(adapter);
		}
		listeners.add(listener);
	}

	public void removeChangeListener(INoteChangeListener listener) {
		listeners.remove(listener);
		if (listeners.size() == 0) {
			EObject content = getContent();
			if (content != null) {
				content.eAdapters().remove(adapter);
			}
		}
	}

	public INoteChangeListener[] getListeners() {
		Object[] objects = listeners.getListeners();
		INoteChangeListener[] result = new INoteChangeListener[objects.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = (INoteChangeListener) objects[i];
		}
		return result;
	}

	private void fireChange() {
		for (INoteChangeListener listener : getListeners()) {
			listener.changed();
		}
	}

	public boolean isValid() {
		return true;
	}
}
