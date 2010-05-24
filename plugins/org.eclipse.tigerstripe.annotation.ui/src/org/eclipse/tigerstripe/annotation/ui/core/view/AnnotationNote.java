/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.Images;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;

public class AnnotationNote implements INote {

	public AnnotationNote(Annotation annotation) {
		this.annotation = annotation;
		adapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getEventType() == Notification.RESOLVE
						|| msg.getEventType() == Notification.REMOVING_ADAPTER)
					return;
				fireChange();
			}
		};
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void addChangeListener(INoteChangeListener listener) {
		if (listeners.size() == 0) {
			annotation.getContent().eAdapters().add(adapter);
		}
		listeners.add(listener);
	}

	public INoteChangeListener[] getListeners() {
		Object[] objects = listeners.getListeners();
		INoteChangeListener[] result = new INoteChangeListener[objects.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = (INoteChangeListener) objects[i];
		}
		return result;
	}

	public void removeChangeListener(INoteChangeListener listener) {
		listeners.remove(listener);
		if (listeners.size() == 0) {
			annotation.getContent().eAdapters().remove(adapter);
		}
	}

	public void remove() {
		AnnotationPlugin.getManager().removeAnnotation(annotation);
	}

	public void save() {
		AnnotationPlugin.getManager().save(annotation);
	}

	public void revert() {
		AnnotationPlugin.getManager().revert(annotation);
	}

	public String getDescription() {
		AnnotationType type = AnnotationPlugin.getManager().getType(annotation);
		if (type != null) {
			return type.getDescription();
		}
		return null;
	}

	public Image getImage() {
		Image image = DisplayAnnotationUtil.getImage(annotation);
		if (image == null) {
			image = Images.getImage(Images.ANNOTATION);
		}
		return image;
	}

	public String getLabel() {
		return DisplayAnnotationUtil.getText(annotation);
	}

	public boolean isReadOnly() {
		return AnnotationPlugin.getManager().isReadOnly(annotation);
	}

	public EObject getContent() {
		return annotation.getContent();
	}

	private void fireChange() {
		for (INoteChangeListener listener : getListeners()) {
			listener.changed();
		}
	}

	@Override
	public String toString() {
		return getLabel();
	}

	private Annotation annotation;
	private EContentAdapter adapter;
	private ListenerList listeners = new ListenerList();

}
