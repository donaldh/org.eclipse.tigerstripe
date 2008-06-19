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
package org.eclipse.tigerstripe.annotation.internal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPackage;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.espace.resources.core.EMFDatabase;
import org.eclipse.tigerstripe.espace.resources.core.IIdentifyManager;

/**
 * This class provide mechanism for loading, saving and caching <code>Annotation</code> objects.
 * 
 * @author Yuri Strot
 */
public class AnnotationStorage implements IIdentifyManager {
	
	protected Map<URI, List<Annotation>> annotations; 
	
	protected static Annotation[] EMPTY_ARRAY = new Annotation[0];
	
	protected ListenerList listeners = new ListenerList();
	protected EMFDatabase database = new EMFDatabase(this);
	
	protected Map<Annotation, ChangeRecorder> changes =
		new HashMap<Annotation, ChangeRecorder>();
	
	public AnnotationStorage() {
	}
	
	public void add(Annotation annotation) {
		addToList(annotation, annotation.getUri());
		trackChanges(annotation);
		database.write(annotation);
		fireAnnotationAdded(annotation);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIdentifyManager#getId(org.eclipse.emf.ecore.EObject)
	 */
	public int getId(EObject object) {
		if (object instanceof Annotation) {
			return ((Annotation)object).getId();
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIdentifyManager#setId(org.eclipse.emf.ecore.EObject, int)
	 */
	public void setId(EObject object, int id) {
		if (object instanceof Annotation) {
			((Annotation)object).setId(id);
		}
	}
	
	protected void trackChanges(Annotation annotation) {
		ChangeRecorder recorder = new ChangeRecorder(annotation);
		changes.put(annotation, recorder);
	}
	
	public void remove(Annotation annotation) {
		changes.remove(annotation);
		database.remove(annotation);
		removeFromList(annotation, annotation.getUri());
		fireAnnotationsRemoved( new Annotation[] { annotation } );
	}
	
	public EObject[] query(EClassifier classifier) {
		return database.query(classifier);
	}
	
	public void rebuildIndex() {
		database.rebuildIndex();
	}
	
	public List<Annotation> getAnnotations(URI uri) {
		List<Annotation> list = doGetAnnotations(uri);
		return list;
	}
	
	public Annotation getAnnotationById(int id) {
		EObject[] objects = database.get(AnnotationPackage.eINSTANCE.getAnnotation_Id(), id);
		List<Annotation> list = new ArrayList<Annotation>();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof Annotation) {
				Annotation annotation = (Annotation)objects[i];
				list.add(annotation);
				trackChanges(annotation);
				fireAnnotationLoaded(list.toArray(new Annotation[list.size()]));
				return annotation;
			}
		}
		return null;
	}
	
	protected List<Annotation> doGetAnnotations(URI uri) {
		EObject[] objects = database.get(AnnotationPackage.eINSTANCE.getAnnotation_Uri(), uri);
		List<Annotation> list = new ArrayList<Annotation>();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof Annotation) {
				Annotation annotation = (Annotation)objects[i];
				list.add(annotation);
				trackChanges(annotation);
			}
		}
		getAnnotationMap().put(uri, list);
		fireAnnotationLoaded(list.toArray(new Annotation[list.size()]));
		return list;
	}
	
	public List<Annotation> getPostfixAnnotations(URI uri) {
		EObject[] objects = database.getPostfixes(AnnotationPackage.eINSTANCE.getAnnotation_Uri(), uri);
		List<Annotation> list = new ArrayList<Annotation>();
		for (EObject object : objects)
			if (object instanceof Annotation)
				list.add((Annotation)object);
		return list;
	}
	
	public Annotation[] getAnnotations() {
		ArrayList<Annotation> allAnnotations = new ArrayList<Annotation>();
		Iterator<List<Annotation>> list = getAnnotationMap().values().iterator();
		while (list.hasNext()) {
			List<Annotation> elem = list.next();
	        allAnnotations.addAll(elem);
        }
		return allAnnotations.toArray(new Annotation[allAnnotations.size()]);
	}
	
	public void uriChanged(URI oldUri, URI newUri) {
		List<Annotation> oldList = doGetAnnotations(oldUri);
		List<Annotation> newList = doGetAnnotations(newUri);
		if (oldList.size() == 0)
			return;
		Iterator<Annotation> it = oldList.iterator();
		while (it.hasNext()) {
	        Annotation annotation = (Annotation) it.next();
	        annotation.setUri(newUri);
	        save(annotation);
        }
		newList.addAll(oldList);
		getAnnotationMap().remove(oldUri);
		fireAnnotationsChanged(oldList.toArray(new Annotation[oldList.size()]));
	}
	
	public void remove(URI uri) {
		Annotation[] annotations = doRemove(uri);
		if (annotations != null && annotations.length > 0)
			fireAnnotationsRemoved(annotations);
	}
	
	protected Annotation[] doRemove(URI uri) {
		List<Annotation> list = doGetAnnotations(uri);
		if (list.size() > 0) {
			Annotation[] array = list.toArray(new Annotation[list.size()]);
			for (int i = 0; i < array.length; i++)
				database.remove(array[i]);
			list.clear();
			return array;
		}
		return null;
	}
	
	public void addAnnotationListener(IAnnotationListener listener) {
		listeners.add(listener);
	}
	
	public void removeAnnotationListener(IAnnotationListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireAnnotationAdded(Annotation annotation) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener)objects[i];
				listener.annotationAdded(annotation);
			}
			catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}
	
	protected void fireAnnotationsRemoved(Annotation[] annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener)objects[i];
				listener.annotationsRemoved(annotations);
			}
			catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}
	
	protected void fireAnnotationsChanged(Annotation[] annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener)objects[i];
				listener.annotationsChanged(annotations);
			}
			catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}
	
	protected void fireAnnotationLoaded(Annotation[] annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener)objects[i];
				listener.annotationsLoaded(annotations);
			}
			catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}
	
	protected void addToList(Annotation annotation, URI uri) {
		doGetAnnotations(uri).add(annotation);
	}
	
	protected void removeFromList(Annotation annotation, URI uri) {
		doGetAnnotations(uri).remove(annotation);
	}
	
	public void save(Annotation annotation) {
		ChangeRecorder recorder = changes.get(annotation);
		ChangeDescription changes = recorder.summarize();
		boolean changed = changes.getObjectChanges().size() > 0;
		database.update(annotation, changes);
		if (changed)
			fireAnnotationsChanged(new Annotation[] { annotation });
	}
	
	public void revert(Annotation annotation) {
		ChangeRecorder recorder = changes.get(annotation);
		ChangeDescription changes = recorder.summarize();
		changes.apply();
	}
	
	protected Map<URI, List<Annotation>> getAnnotationMap() {
		if (annotations == null) {
			annotations = new HashMap<URI, List<Annotation>>();
		}
		return annotations;
	}

}
