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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPackage;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener2;
import org.eclipse.tigerstripe.espace.core.Mode;
import org.eclipse.tigerstripe.espace.resources.core.EMFDatabase;
import org.eclipse.tigerstripe.espace.resources.core.EMFDatabase.Listener;
import org.eclipse.tigerstripe.espace.resources.internal.core.ResourceChangeListenersLoader;

/**
 * This class provide mechanism for loading, saving and caching
 * <code>Annotation</code> objects.
 * 
 * @author Yuri Strot
 */
public class AnnotationStorage implements Listener, IResourceChangeListener {

	protected static Annotation[] EMPTY_ARRAY = new Annotation[0];

	private EMFDatabase database;
	private final ReentrantLock databaseLock = new ReentrantLock();
	private final ReentrantLock listenersLock = new ReentrantLock();
	protected ListenerList listeners = new ListenerList();

	protected Map<Annotation, ChangeRecorder> changes = new ConcurrentHashMap<Annotation, ChangeRecorder>();

	private List<IResourceChangeListener> workspaceListeners;
	
	public AnnotationStorage() {
		org.eclipse.core.resources.ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	public EMFDatabase getDatabase() {
		databaseLock.lock();
		try {
			if (database == null) {
				database = new EMFDatabase();
				database.addListener(this);
			}
			return database;
		} finally {
			databaseLock.unlock();
		}
	}

	private List<IResourceChangeListener> getWorkspaceListeners() {
		listenersLock.lock();
		try {
			if (workspaceListeners == null) {
				workspaceListeners = ResourceChangeListenersLoader.load();
				workspaceListeners.add(0, getDatabase());
			}
			return workspaceListeners;
		} finally {
			listenersLock.unlock();
		}
	}

	public void resourceChanged(IResourceChangeEvent event) {
		for (IResourceChangeListener listener : getWorkspaceListeners()) {
			listener.resourceChanged(event);
		}
	}

	public void add(Annotation annotation) {
		getDatabase().write(annotation);
		trackChanges(annotation);
		fireAnnotationAdded(annotation);
	}

	protected void trackChanges(Annotation annotation) {
		ChangeRecorder recorder = new ChangeRecorder(annotation);
		changes.put(annotation, recorder);
	}

	public void remove(Annotation annotation) {
		if (isReadOnly(annotation))
			return;
		changes.remove(annotation);
		getDatabase().remove(annotation);
		fireAnnotationsRemoved(new Annotation[] { annotation });
	}

	public EObject[] query(EClassifier classifier) {
		return getDatabase().query(classifier);
	}

	public void rebuildIndex() {
		getDatabase().rebuildIndex();
	}

	public List<Annotation> getAnnotations(URI uri) {
		List<Annotation> list = doGetAnnotations(uri);
		for (Iterator<Annotation> it = list.iterator(); it.hasNext();) {
			if (it.next().getContent() == null) {
				it.remove();
			}
		}
		return list;
	}

	public Annotation getAnnotationById(String id) {
		EObject[] objects = getDatabase().get(
				AnnotationPackage.eINSTANCE.getAnnotation_Id(), id);
		List<Annotation> list = new ArrayList<Annotation>();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof Annotation) {
				Annotation annotation = (Annotation) objects[i];
				list.add(annotation);
				trackChanges(annotation);
				return annotation;
			}
		}
		return null;
	}

	protected List<Annotation> doGetAnnotations(URI uri) {
		EObject[] objects = getDatabase().get(
				AnnotationPackage.eINSTANCE.getAnnotation_Uri(), uri);
		List<Annotation> list = new ArrayList<Annotation>();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof Annotation) {
				Annotation annotation = (Annotation) objects[i];
				list.add(annotation);
				trackChanges(annotation);
			}
		}
		return list;
	}

	public List<Annotation> getPostfixAnnotations(URI uri, boolean raw) {
		EObject[] objects;
		if (raw) {
			objects = getDatabase().getPostfixesRaw(
					AnnotationPackage.eINSTANCE.getAnnotation_Uri(), uri);
		} else {
			objects = getDatabase().getPostfixes(
					AnnotationPackage.eINSTANCE.getAnnotation_Uri(), uri);
		}
		List<Annotation> list = new ArrayList<Annotation>();
		for (EObject object : objects)
			if (object instanceof Annotation)
				list.add((Annotation) object);
		return list;
	}

	public List<Annotation> getPostfixAnnotations(URI uri) {
		return getPostfixAnnotations(uri, false);
	}
	
	public List<Annotation> getPostfixAnnotationsRaw(URI uri) {
		return getPostfixAnnotations(uri, true);
	}
	
	public void uriChanged(URI oldUri, URI newUri) {
		List<Annotation> oldList = doGetAnnotations(oldUri);
		if (oldList.size() == 0)
			return;
		Iterator<Annotation> it = oldList.iterator();
		while (it.hasNext()) {
			Annotation annotation = it.next();
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

	public boolean isReadOnly(Annotation annotation) {
		if (isDynamic(annotation))
			return true;
		return getDatabase().isReadOnly(annotation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IAnnotationManager#addResource
	 * (org.eclipse.emf.ecore.resource.Resource,
	 * org.eclipse.tigerstripe.espace.core.ReadWriteOption)
	 */
	public void addAnnotations(Resource resource, Mode option) {
		getDatabase().addResource(resource, option);
		fireAnnotationsAdded(resource, option);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IAnnotationManager#removeResource
	 * (org.eclipse.emf.ecore.resource.Resource)
	 */
	public void removeAnnotations(Resource resource) {
		getDatabase().removeResource(resource, true);
		fireAnnotationsRemoved(resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.core.IAnnotationManager#
	 * unregisterAnnotations(org.eclipse.emf.ecore.resource.Resource)
	 */
	public void unregisterAnnotations(Resource resource) {
		getDatabase().removeResource(resource, false);
		fireAnnotationsRemoved(resource);
	}

	protected boolean isDynamic(Annotation annotation) {
		return annotation.getContent() instanceof DynamicEObjectImpl;
	}

	protected Annotation[] doRemove(URI uri) {
		List<Annotation> list = doGetAnnotations(uri);
		if (list.size() > 0) {
			for (Iterator<Annotation> it = list.iterator(); it.hasNext();) {
				Annotation annotation = it.next();
				if (isReadOnly(annotation)) {
					it.remove();
					continue;
				}
				getDatabase().remove(annotation);
			}
			return list.toArray(new Annotation[list.size()]);
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
				IAnnotationListener listener = (IAnnotationListener) objects[i];
				listener.annotationAdded(annotation);
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

	protected void fireAnnotationsRemoved(Annotation[] annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener) objects[i];
				listener.annotationsRemoved(annotations);
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

	protected void fireAnnotationsChanged(Annotation[] annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener) objects[i];
				listener.annotationsChanged(annotations);
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

	protected void fireAnnotationsAdded(Resource resource, Mode option) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener) objects[i];
				if (listener instanceof IAnnotationListener2) {
					((IAnnotationListener2) listener).annotationsAdded(
							resource, option);
				}
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

	protected void fireAnnotationsRemoved(Resource resource) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener) objects[i];
				if (listener instanceof IAnnotationListener2) {
					((IAnnotationListener2) listener)
							.annotationsRemoved(resource);
				}
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

	public void save(Annotation annotation) {
		if (isReadOnly(annotation))
			return;
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

	public void dispose() {
		if (database != null) {
			database.dispose();
		}
		org.eclipse.core.resources.ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	public void added(Collection<EObject> data) {
		for (EObject ann : data) {
			fireAnnotationAdded((Annotation) ann);
		}
	}

	public void removed(Collection<EObject> data) {
		fireAnnotationsRemoved(toAnnArray(data));
	}

	public void updated(Collection<EObject> data) {
		fireAnnotationsChanged(toAnnArray(data));
	}
	
	private Annotation[] toAnnArray(Collection<EObject> data) {
		Object[] array = data.toArray();
		Annotation[] anns = new Annotation[array.length];
		for (int i = 0; i < array.length; ++i) {
			anns[i] = (Annotation) array[i];
		}
		return anns;
	}
	
	public Resource findAnnotationResource(IResource resource) {
		return database.findAnnotationResource(resource);
	}
}
