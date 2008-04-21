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
package org.eclipse.tigerstripe.annotation.core;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * An annotation manager provide operations for annotation creation, removing, changing and others.
 * Annotation manager works with adaptable objects which should be adapted to the <code>IAnnotable</code> class.
 * 
 * @see AnnotationType
 * @see IAnnotationProvider
 * @author Yuri Strot
 */
public interface IAnnotationManager {
	
	/**
	 * Add annotation to the annotable object with the following content
	 * 
	 * @param object annotable object
	 * @param content annotation content
	 * @return created annotation or null, if passed object do not annotable
	 */
	public Annotation addAnnotation(Object object, EObject content);
	
	/**
	 * Remove annotation
	 * 
	 * @param annotation
	 */
	public void removeAnnotation(Annotation annotation);
	
	/**
	 * Remove all annotations for annotable object
	 * 
	 * @param object annotable object
	 */
	public void removeAnnotations(Object object);
	
	/**
	 * Return all annotations of the annotable object
	 * 
	 * @param object
	 * @return all annotations of the annotable object or empty array, if there are no one annotation
	 */
	public Annotation[] getAnnotations(Object object);
	
	/**
	 * Return all annotations
	 * 
	 * @return all annotations
	 */
	public Annotation[] getAnnotations();
	
	/**
	 * Return all annotation type that can be create
	 * 
	 * @return
	 */
	public AnnotationType[] getTypes();
	
	/**
	 * Return type of the specific annotation
	 * 
	 * @param annotation
	 * @return type of the passed annotation
	 */
	public AnnotationType getType(Annotation annotation);
	
	/**
	 * Notify that some URI changed and all corresponding annotations should be changed.
	 * Annotation should not be changed directly with a <code>Annotation.setUri(URI)</code> method
	 * 
	 * @param oldUri old URI value
	 * @param newUri new URI value
	 */
	public void setUri(URI oldUri, URI newUri);
	
	/**
	 * Return URI of the annotable object
	 *  
	 * @param object annotable object
	 * @return URI of the annotable object or null, if this object is not annotable
	 */
	public URI getUri(Object object);
	
	/**
	 * Return annotable object by URI
	 * 
	 * @param uri annotable object URI
	 * @return annotable object or null, if there is no object
	 */
	public Object getObject(URI uri);
	
	/**
	 * 
	 * 
	 * @param uri
	 * @return
	 */
	public String getProviderId(URI uri);
	
	/**
	 * Save annotation to the corresponding storage
	 * 
	 * @param annotation annotation to save
	 */
	public void save(Annotation annotation);
	
	/**
	 * Add annotation listener
	 * 
	 * @param listener
	 */
	public void addAnnotationListener(IAnnotationListener listener);
	
	/**
	 * Remove annotation listener
	 * 
	 * @param listener
	 */
	public void removeAnnotationListener(IAnnotationListener listener);

}
