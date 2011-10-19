package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;

public class AnnotationCapable implements IAnnotationCapable {

	private final Object object;

	public AnnotationCapable(Object object) {
		this.object = object;
	}

	public List<Object> getAnnotations() {
		IAnnotationManager mgr = AnnotationPlugin.getManager();
		List<Object> annotations = new LinkedList<Object>();
		Annotation[] all = mgr.doGetAnnotations(object, false);
		for (Annotation a : all) {
			if (TigerstripeURIAdapterFactory.isRelated(a.getUri())) {
				annotations.add(a.getContent());
			}
		}
		return Collections.unmodifiableList(annotations);
	}

	public List<Object> getAnnotations(Class<?> type) {
		IAnnotationManager mgr = AnnotationPlugin.getManager();
		List<Object> annotations = new LinkedList<Object>();
		Annotation[] all = mgr.doGetAnnotations(object, false);
		for (Annotation a : all) {
			if (TigerstripeURIAdapterFactory.isRelated(a.getUri())
					&& type.isInstance(a.getContent())) {
				annotations.add(a.getContent());
			}
		}
		return Collections.unmodifiableList(annotations);
	}

	public Object getAnnotation(String annotationSpecificationID) {
		List<Object> all = getAnnotations();
		for (Object obj : all) {
			if (isAnnotationMatch(annotationSpecificationID, obj))
				return obj;
		}

		return null;
	}

	public Object getAnnotationByID(String annotationID) {
		List<Object> all = getAnnotations();
		for (Object obj : all) {
			if (isAnnotationMatchWithID(annotationID, obj))
				return obj;
		}

		return null;
	}

	private boolean isAnnotationMatchWithID(String annotationID, Object obj) {
		if (obj != null) {
			String className = obj.getClass().getName();
			if (className != null) {
				String implementationClass = annotationID + "Impl";

				if (className.equals(annotationID)
						|| className.endsWith("." + annotationID)
						|| className.equals(implementationClass)
						|| className.endsWith("." + implementationClass))
					return true;
			}
		}
		return false;
	}

	public List<Object> getAnnotations(String annotationSpecificationID) {
		List<Object> annotations = new LinkedList<Object>(getAnnotations());
		for (Iterator<Object> i = annotations.iterator(); i.hasNext();) {
			if (!isAnnotationMatch(annotationSpecificationID, i.next()))
				i.remove();
		}

		return Collections.unmodifiableList(annotations);
	}

	public boolean hasAnnotations() {
		return !getAnnotations().isEmpty();
	}

	public boolean hasAnnotations(String annotationSpecificationID) {
		List<Object> annotations = getAnnotations();
		for (Iterator<Object> i = annotations.iterator(); i.hasNext();) {
			if (isAnnotationMatch(annotationSpecificationID, i.next()))
				return true;
		}
		return false;
	}

	public boolean hasAnnotationWithID(String annotationID) {
		List<Object> annotations = getAnnotations();
		for (Iterator<Object> i = annotations.iterator(); i.hasNext();) {
			if (isAnnotationMatchWithID(annotationID, i.next()))
				return true;
		}
		return false;
	}

	public boolean hasAnnotations(Class<?> annotationType) {
		return !getAnnotations(annotationType).isEmpty();
	}

	private boolean isAnnotationMatch(String annotationSpecificationID,
			Object obj) {
		Class<?>[] interfaces = obj.getClass().getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i].getName().endsWith(annotationSpecificationID))
				return true;
		}
		return false;
	}
}
