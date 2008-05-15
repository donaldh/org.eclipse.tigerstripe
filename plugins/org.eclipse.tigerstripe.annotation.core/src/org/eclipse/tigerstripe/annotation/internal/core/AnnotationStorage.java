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
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPackage;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.espace.resources.core.EMFDatabase;

/**
 * This class provide mechanism for loading, saving and caching <code>Annotation</code> objects.
 * 
 * @author Yuri Strot
 */
public class AnnotationStorage {
	
	protected Map<URI, List<Annotation>> annotations; 
	
	protected static Annotation[] EMPTY_ARRAY = new Annotation[0];
	
	protected ListenerList listeners = new ListenerList();
	private EMFDatabase database = new EMFDatabase();
	
	public AnnotationStorage() {
	}
	
	public void add(Annotation annotation) {
		addToList(annotation, annotation.getUri());
		database.write(annotation);
		fireAnnotationAdded(annotation);
	}
	
	public void remove(Annotation annotation) {
		removeFromList(annotation, annotation.getUri());
		database.remove(annotation);
		fireAnnotationsRemoved( new Annotation[] { annotation } );
	}
	
	public EObject[] query(EClassifier classifier) {
		return database.query(classifier);
	}
	
	public Annotation[] getAnnotations(URI uri) {
		List<Annotation> list = doGetAnnotations(uri);
		return list.toArray(new Annotation[list.size()]);
	}
	
	protected List<Annotation> doGetAnnotations(URI uri) {
		List<Annotation> list = getAnnotationMap().get(uri);
		if (list == null) {
			EObject[] objects = database.get(AnnotationPackage.eINSTANCE.getAnnotation_Uri(), uri);
			list = new ArrayList<Annotation>();
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] instanceof Annotation)
					list.add((Annotation)objects[i]);
			}
			getAnnotationMap().put(uri, list);
    		fireAnnotationLoaded(list.toArray(new Annotation[list.size()]));
		}
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
		if (oldList.size() == 0)
			return;
		Iterator<Annotation> it = oldList.iterator();
		while (it.hasNext()) {
	        Annotation annotation = (Annotation) it.next();
	        annotation.setUri(newUri);
	        database.write(annotation);
        }
		List<Annotation> newList = doGetAnnotations(newUri);
		newList.addAll(oldList);
		getAnnotationMap().remove(oldUri);
		fireAnnotationsChanged(oldList.toArray(new Annotation[oldList.size()]));
	}
	
	public void remove(URI uri) {
		List<Annotation> list = doGetAnnotations(uri);
		if (list.size() > 0) {
			Annotation[] array = list.toArray(new Annotation[list.size()]);
			for (int i = 0; i < array.length; i++)
				database.remove(array[i]);
			list.clear();
			fireAnnotationsRemoved(array);
		}
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
			IAnnotationListener listener = (IAnnotationListener)objects[i];
			listener.annotationAdded(annotation);
		}
	}
	
	protected void fireAnnotationsRemoved(Annotation[] annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			IAnnotationListener listener = (IAnnotationListener)objects[i];
			listener.annotationsRemoved(annotations);
		}
	}
	
	protected void fireAnnotationsChanged(Annotation[] annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			IAnnotationListener listener = (IAnnotationListener)objects[i];
			listener.annotationsChanged(annotations);
		}
	}
	
	protected void fireAnnotationLoaded(Annotation[] annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			IAnnotationListener listener = (IAnnotationListener)objects[i];
			listener.annotationsLoaded(annotations);
		}
	}
	
	protected void addToList(Annotation annotation, URI uri) {
		doGetAnnotations(uri).add(annotation);
	}
	
	protected void removeFromList(Annotation annotation, URI uri) {
		doGetAnnotations(uri).remove(annotation);
	}
	
	public void save(Annotation annotation) {
		database.write(annotation);
	}
	
	protected Map<URI, List<Annotation>> getAnnotationMap() {
		if (annotations == null) {
			annotations = new HashMap<URI, List<Annotation>>();
		}
		return annotations;
	}

}
