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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPackage;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.espace.resources.core.EMFDatabase;
import org.eclipse.tigerstripe.espace.resources.core.IDatabaseConfiguration;

/**
 * This class provide mechanism for loading, saving and caching <code>Annotation</code> objects.
 * 
 * @author Yuri Strot
 */
public class AnnotationStorage implements IDatabaseConfiguration {
	
	protected static Annotation[] EMPTY_ARRAY = new Annotation[0];
	
	private EMFDatabase database;
	private ReentrantLock databaseLock = new ReentrantLock();
	protected ListenerList listeners = new ListenerList();
	
	protected Map<Annotation, ChangeRecorder> changes =
		new ConcurrentHashMap<Annotation, ChangeRecorder>();
	
	public AnnotationStorage() {
	}
	
	protected EMFDatabase getDatabase() {
		databaseLock.lock();
		try {
			if (database == null)
				database = new EMFDatabase(this);
			return database;
		}
		finally {
			databaseLock.unlock();
		}
	}
	
	public void add(Annotation annotation) {
		getDatabase().write(annotation);
		trackChanges(annotation);
		fireAnnotationAdded(annotation);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIdentifyManager#getId(org.eclipse.emf.ecore.EObject)
	 */
	public String getId(EObject object) {
		if (object instanceof Annotation) {
			return ((Annotation)object).getId();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIdentifyManager#setId(org.eclipse.emf.ecore.EObject, int)
	 */
	public void setId(EObject object, String id) {
		if (object instanceof Annotation) {
			((Annotation)object).setId(id);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIdentifyManager#getPackages(org.eclipse.emf.ecore.EObject)
	 */
	public List<EPackage> getPackages(EObject object) {
		List<EPackage> packages = new ArrayList<EPackage>();
		if (object instanceof Annotation) {
			EObject content = ((Annotation)object).getContent();
			collectPackages(content, packages);
		}
		else {
			collectPackages(object, packages);
		}
		return packages;
	}
	
	protected void collectPackages(EObject object, List<EPackage> packages) {
		EPackage pack = object.eClass().getEPackage();
		if (!packages.contains(pack))
			packages.add(pack);
        for(EObject child : object.eContents()){
        	collectPackages(child, packages);
        }
	}
	
	protected void trackChanges(Annotation annotation) {
		ChangeRecorder recorder = new ChangeRecorder(annotation);
		changes.put(annotation, recorder);
	}
	
	public void remove(Annotation annotation) {
		changes.remove(annotation);
		getDatabase().remove(annotation);
		fireAnnotationsRemoved( new Annotation[] { annotation } );
	}
	
	public EObject[] query(EClassifier classifier) {
		return getDatabase().query(classifier);
	}
	
	public void rebuildIndex() {
		getDatabase().rebuildIndex();
	}
	
	public List<Annotation> getAnnotations(URI uri) {
		return doGetAnnotations(uri);
	}
	
	public Annotation getAnnotationById(String id) {
		EObject[] objects = getDatabase().get(AnnotationPackage.eINSTANCE.getAnnotation_Id(), id);
		List<Annotation> list = new ArrayList<Annotation>();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof Annotation) {
				Annotation annotation = (Annotation)objects[i];
				list.add(annotation);
				trackChanges(annotation);
				return annotation;
			}
		}
		return null;
	}
	
	protected List<Annotation> doGetAnnotations(URI uri) {
		EObject[] objects = getDatabase().get(AnnotationPackage.eINSTANCE.getAnnotation_Uri(), uri);
		List<Annotation> list = new ArrayList<Annotation>();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof Annotation) {
				Annotation annotation = (Annotation)objects[i];
				list.add(annotation);
				trackChanges(annotation);
			}
		}
		return list;
	}
	
	public List<Annotation> getPostfixAnnotations(URI uri) {
		EObject[] objects = getDatabase().getPostfixes(AnnotationPackage.eINSTANCE.getAnnotation_Uri(), uri);
		List<Annotation> list = new ArrayList<Annotation>();
		for (EObject object : objects)
			if (object instanceof Annotation)
				list.add((Annotation)object);
		return list;
	}
	
	public void uriChanged(URI oldUri, URI newUri) {
		List<Annotation> oldList = doGetAnnotations(oldUri);
		if (oldList.size() == 0)
			return;
		Iterator<Annotation> it = oldList.iterator();
		while (it.hasNext()) {
	        Annotation annotation = (Annotation) it.next();
	        annotation.setUri(newUri);
	        save(annotation);
        }
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
				getDatabase().remove(array[i]);
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
	
	public void save(Annotation annotation) {
		ChangeRecorder recorder = changes.get(annotation);
		ChangeDescription changes = recorder.summarize();
		getDatabase().update(annotation, changes);
		fireAnnotationsChanged(new Annotation[] { annotation });
	}
	
	public void revert(Annotation annotation) {
		ChangeRecorder recorder = changes.get(annotation);
		ChangeDescription changes = recorder.summarize();
		changes.apply();
	}

}
