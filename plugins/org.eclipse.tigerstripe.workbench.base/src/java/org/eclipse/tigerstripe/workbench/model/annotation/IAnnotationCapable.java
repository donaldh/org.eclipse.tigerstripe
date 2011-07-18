/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    JK Worrell - (Cisco Systems)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.model.annotation;

import java.util.List;

/**
 * Interface to be implemented by all components of the model that can be
 * annotated with Annotations.
 * 
 * @author jworrell
 *
 */
public interface IAnnotationCapable {
	/**
	 * Returns a list of all the annotations for this object
	 * 
	 * @return a list of all annotations defined againt this object
	 */
	public List<Object> getAnnotations();

	/**
	 * Returns a list of all the annotations for for the given scheme for this
	 * object
	 * 
	 * @param scheme -
	 *            the scheme to limit the return to
	 * @return the list of all annotations for the given scheme defined against
	 *         this object
	 */
//	public List<Object> getAnnotations(String scheme);

	/**
	 * Returns the first annotation defined against this object for the "tigerstripe"
	 * scheme and annotationType
	 * 
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return the first annotation for the given scheme and annotationType
	 * @deprecated Use getAnnotationsByID(annotationID) instead
	 */
	public Object getAnnotation(String annotationType);
	
	/**
	 * Returns the annotation with the given ID.  Null if annotation with the given ID does 
	 * not exist.
	 * 
	 * @param annotationID  -  ID of the annotation of interest
	 * @return  Annotation with with the given ID or null if no such annotation exists for 
	 * this object.
	 */
	public Object getAnnotationByID(String annotationID);
	

	/**
	 * Returns the list of annotations defined against this object for the "tigerstripe"
	 * scheme and annotationType
	 * 
	 * @param scheme -
	 *            the scheme to match, e.g. "tigerstripe"
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return the list of annotations for the given scheme and annotationType
	 */
	public List<Object> getAnnotations(String annotationType);

	/**
	 * Returns the list of annotations defined against this object for the "tigerstripe"
	 * scheme and annotationType
	 * 
	 * @param scheme -
	 *            the scheme to match, e.g. "tigerstripe"
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return the list of annotations for the given scheme and annotationType
	 */
	public List<Object> getAnnotations(Class<?> annotationType);

	/**
	 * Returns the first annotation defined against this object for the given
	 * scheme and annotationType
	 * 
	 * @param scheme -
	 *            the scheme to match, e.g. "tigerstripe"
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return the first annotation for the given scheme and annotationType
	 * @deprecated
	 */
//	public Object getAnnotation(String scheme, String annotationType);

	/**
	 * Returns the list of annotations defined against this object for the given
	 * scheme and annotationType
	 * 
	 * @param scheme -
	 *            the scheme to match, e.g. "tigerstripe"
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return the list of annotations for the given scheme and annotationType
	 * @deprecated
	 */
//	public List<Object> getAnnotations(String scheme, String annotationType);
	
	/**
	 * Returns true if there is any annotation defined against this object
	 * 
	 * @return true - if there is any annotation defined against this object
	 */
	public boolean hasAnnotations();

	/**
	 * Returns true if there is any annotation defined against this object for
	 * the given scheme
	 * 
	 * @param scheme -
	 *            the scheme to match, for example "tigerstripe"
	 * @return true - if there is any annotation defined against this object
	 */
//	public boolean hasAnnotations(String scheme);

	/**
	 * Returns true if there is any annotation defined against this object for
	 * the "tigerstripe" scheme and given annotationType
	 * 
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return true - if there is any annotation defined against this object
	 * @deprecated  Use hasAnnotationWithID(annotationID)
	 */
	public boolean hasAnnotations(String annotationType);
	
	/**
	 * Returns true if annotation with the given ID exists on this object; false otherwise
	 * 
	 * @param annotationID - ID of the annotation of interest
	 * @return true if annotation with the given ID exists on this object; false otherwise
	 */
	public boolean hasAnnotationWithID(String annotationID);

	/**
	 * Returns true if there is any annotation defined against this object for
	 * the "tigerstripe" scheme and given annotationType
	 * 
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return true - if there is any annotation defined against this object
	 */
	public boolean hasAnnotations(Class<?> annotationType);

	/**
	 * Returns true if there is any annotation defined against this object for
	 * the given scheme and given annotationType
	 * 
	 * @param scheme -
	 *            the scheme to match, for example "tigerstripe"
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return true - if there is any annotation defined against this object
	 * @deprecated
	 */
//	public boolean hasAnnotations(String scheme, String annotationType);
}
