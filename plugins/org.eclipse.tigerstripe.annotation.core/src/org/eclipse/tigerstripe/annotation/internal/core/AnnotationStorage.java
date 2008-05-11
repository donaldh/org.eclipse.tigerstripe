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
		loadAnnotations(annotation.getUri());
		addToList(annotation, annotation.getUri());
		database.write(annotation);
		fireAnnotationAdded(annotation);
	}
	
	public void remove(Annotation annotation) {
		removeFromList(annotation, annotation.getUri());
		database.remove(annotation);
		fireAnnotationsRemoved( new Annotation[] { annotation } );
	}
	
	public Annotation[] getAnnotations(URI uri) {
		loadAnnotations(uri);
		List<Annotation> list = getAnnotationMap().get(uri);
		return list.toArray(new Annotation[list.size()]);
	}
	
	protected void loadAnnotations(URI uri) {
		List<Annotation> list = getAnnotationMap().get(uri);
		if (list == null) {
			EObject[] objects = database.get(AnnotationPackage.eINSTANCE.getAnnotation_Uri(), uri);
			list = new ArrayList<Annotation>();
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] instanceof Annotation) {
		        	Annotation annotation = (Annotation)objects[i];
					list.add((Annotation)objects[i]);
		    		fireAnnotationLoaded(annotation);
				}
			}
			getAnnotationMap().put(uri, list);
		}
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
		List<Annotation> oldList = getAnnotationMap().get(oldUri);
		if (oldList == null)
			return;
		Iterator<Annotation> it = oldList.iterator();
		while (it.hasNext()) {
	        Annotation annotation = (Annotation) it.next();
	        annotation.setUri(newUri);
	        database.write(annotation);
        }
		fireAnnotationsChanged(oldList.toArray(new Annotation[oldList.size()]));
		List<Annotation> newList = getAnnotationMap().get(newUri);
		if (newList == null) {
			getAnnotationMap().put(newUri, oldList);
		}
		else {
			newList.addAll(oldList);
		}
		getAnnotationMap().remove(oldUri);
	}
	
	public void remove(URI uri) {
		List<Annotation> list = getAnnotationMap().get(uri);
		if (list != null) {
			Annotation[] array = list.toArray(new Annotation[list.size()]);
			getAnnotationMap().remove(uri);
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
	
	protected void fireAnnotationLoaded(Annotation annotation) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			IAnnotationListener listener = (IAnnotationListener)objects[i];
			listener.annotationLoaded(annotation);
		}
	}
	
	protected void addToList(Annotation annotation, URI uri) {
		List<Annotation> list = getAnnotationMap().get(uri);
		if (list == null) {
			list = new ArrayList<Annotation>();
			getAnnotationMap().put(uri, list);
		}
		list.add(annotation);
	}
	
	protected void removeFromList(Annotation annotation, URI uri) {
		List<Annotation> list = getAnnotationMap().get(uri);
		if (list != null) {
			list.remove(annotation);
			if (list.isEmpty())
				getAnnotationMap().remove(list);
		}
	}
	
	public void save(Annotation annotation) {
		database.write(annotation);
	}
	
	protected Map<URI, List<Annotation>> getAnnotationMap() {
		if (annotations == null) {
			annotations = new HashMap<URI, List<Annotation>>();
			//EObject[] objects = database.read();
//			for (int i = 0; i < objects.length; i++) {
//		        if (objects[i] instanceof Annotation) {
//		        	Annotation annotation = (Annotation)objects[i];
//		    		addToList(annotation, annotation.getUri());
//		    		fireAnnotationLoaded(annotation);
//		        }
//	        }
		}
		return annotations;
	}

}
