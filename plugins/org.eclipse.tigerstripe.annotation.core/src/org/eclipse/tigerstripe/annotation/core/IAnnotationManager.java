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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

/**
 * An annotation manager provide operations for annotation creation, removing, changing and others.
 * This class also providing access to annotation types, providers, refactoring support, etc.
 * Annotation manager works with adaptable objects which should be adapted to the <code>IAnnotable</code> class.
 * 
 * @see AnnotationType
 * @see IAnnotationProvider
 * @see IRefactoringSupport
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
	 * Check is it possible to add annotation to the annotable object
	 * with the following <code>EClass</code>
	 * 
	 * @param object annotable object
	 * @param eclass content <code>EClass</code>
	 * @return true, if it's possible and false if not
	 */
	public boolean isPossibleToAdd(Object object, EClass eclass);
	
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
	 * @param object annotated object
	 * @param deepest if false, this method return annotations only for passed object.
	 * If true, this method return annotations for passed object and for all delegated
	 * objects. For example, <code>IJavaElement</code> delegate to <code>IResource</code>,
	 * so <code>getAnnotations(IJavaElement, false)</code> return annotations only for
	 * <code>IJavaElement</code> and <code>getAnnotations(IJavaElement, true)</code>
	 * return annotations for <code>IJavaElement</code> and for corresponding
	 * <code>IResource</code>.
	 * @return all annotations of the annotable object or empty array, if there are no one annotation
	 */
	public Annotation[] getAnnotations(Object object, boolean deepest);
	
	/**
	 * Return all loaded annotations
	 * 
	 * @return all loaded annotations
	 */
	public Annotation[] getLoadedAnnotations();
	
	/**
	 * Return annotable object annotated with the passed annotation
	 *  
	 * @param annotation
	 * @return annotated object
	 */
	public Object getAnnotatedObject(Annotation annotation);
	
	/**
	 * Return all stored objects of the passed classifier
	 * 
	 * @param classifier classifier of the objects should be found
	 * @return Return all stored objects of the passed classifier
	 */
	public EObject[] query(EClassifier classifier);
	
	/**
	 * Save annotation to the corresponding storage
	 * 
	 * @param annotation annotation to save
	 */
	public void save(Annotation annotation);
	
	/**
	 * Revert annotation changes to the last state when it was saved
	 * 
	 * @param annotation annotation to revert
	 */
	public void revert(Annotation annotation);
	
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
	 * Return type by EMF package name and class name. To get package by EPackage and EClass
	 * you need to call:<br>
	 * <code>
	 *   getType(EPackage.getNsPrefix(), EClass.getName()); 
	 * </code> 
	 * 
	 * @param epackage EPackage namespace
	 * @param eclazz EClass name
	 * @return
	 */
	public AnnotationType getType(String epackage, String eclazz);
	
	/**
	 * Return annotation by id
	 * 
	 * @param id annotation id
	 * @return annotation by specified id or null, if there is no annotations with this id
	 */
	public Annotation getAnnotationById(String id);
	
	/**
	 * Add refactoring listener
	 * 
	 * @param listener
	 */
	public void addRefactoringListener(IRefactoringListener listener);
	
	/**
	 * Remove refactoring listener
	 * 
	 * @param listener
	 */
	public void removeRefactoringListener(IRefactoringListener listener);
	
	/**
	 * Return list of the provided targets
	 * 
	 * @return list of the provided targets
	 */
	public IProviderTarget[] getProviderTargets();
	
	/**
	 * @param object
	 * @return
	 */
	public TargetAnnotationType[] getAnnotationTargets(Object object); 
	
	/**
	 * TODO update documentation
	 * 
	 * @return
	 */
	public IRefactoringSupport getRefactoringSupport();

}
