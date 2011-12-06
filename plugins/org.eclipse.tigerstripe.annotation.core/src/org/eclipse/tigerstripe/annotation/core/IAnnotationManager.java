/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.core;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.internal.core.ProviderManager;

/**
 * An annotation manager provide operations for annotation creation, removing,
 * changing and others. This class also providing access to annotation types,
 * providers, refactoring support, etc. Annotation manager works with adaptable
 * objects which should be adapted to the <code>IAnnotable</code> class.
 * 
 * @see AnnotationType
 * @see IAnnotationProvider
 * @author Yuri Strot
 */
public interface IAnnotationManager {

	String ANNOTATION_FILE_EXTENSION = "ann";

	/**
	 * Add annotation to the annotable object with the following content
	 * 
	 * @param object
	 *            annotable object
	 * @param content
	 *            annotation content
	 * @return created annotation or null, if passed object do not annotable
	 * @throws AnnotationException
	 *             if there is no annotation type for the specified content or
	 *             if there is illegal content for the specified object
	 */
	Annotation addAnnotation(Object object, EObject content)
			throws AnnotationException;

	/**
	 * Check is it possible to add annotation to the annotable object with the
	 * following <code>EClass</code>
	 * 
	 * @param object
	 *            annotable object
	 * @param eclass
	 *            content <code>EClass</code>
	 * @return true, if it's possible and false if not
	 */
	boolean isPossibleToAdd(Object object, EClass eclass);

	/**
	 * Check is it annotable object or not. Object annotable if and only if
	 * registered <code>IAnnotationProvider</code> which map this object to
	 * <code>URI</code>
	 * 
	 * @param object
	 *            candidate for annotable object
	 * @return true, if it's annotable object and false otherwise
	 */
	boolean isAnnotable(Object object);

	/**
	 * Remove annotation
	 * 
	 * @param annotation
	 */
	void removeAnnotation(Annotation annotation);

	/**
	 * Remove all annotations for annotable object
	 * 
	 * @param object
	 *            annotable object
	 */
	void removeAnnotations(Object object);

	/**
	 * Return all annotations of the annotable object
	 * 
	 * @param object
	 *            annotated object
	 * @param deepest
	 *            if false, this method return annotations only for passed
	 *            object. If true, this method return annotations for passed
	 *            object and for all delegated objects. For example,
	 *            <code>IJavaElement</code> delegate to <code>IResource</code>,
	 *            so <code>getAnnotations(IJavaElement, false)</code> return
	 *            annotations only for <code>IJavaElement</code> and
	 *            <code>getAnnotations(IJavaElement, true)</code> return
	 *            annotations for <code>IJavaElement</code> and for
	 *            corresponding <code>IResource</code>.
	 * @return all annotations of the annotable object or empty array, if there
	 *         are no one annotation
	 */
	Annotation[] getAnnotations(Object object, boolean deepest);

	/**
	 * Obtain annotations without transaction
	 */
	Annotation[] doGetAnnotations(Object object, boolean deepest);

	
	/**
	 * Return annotable object annotated with the passed annotation
	 * 
	 * @param annotation
	 * @return annotated object
	 */
	Object getAnnotatedObject(Annotation annotation);

	/**
	 * Return annotable object annotated with the specified uri
	 * 
	 * @param uri
	 * @return annotated object
	 */
	Object getAnnotatedObject(URI uri);

	/**
	 * Save annotation to the corresponding storage
	 * 
	 * @param annotation
	 *            annotation to save
	 */
	void save(Annotation annotation);

	/**
	 * Add annotation listener
	 * 
	 * @see IAnnotationListener2
	 * @param listener
	 */
	void addAnnotationListener(IAnnotationListener listener);

	/**
	 * Remove annotation listener
	 * 
	 * @param listener
	 */
	void removeAnnotationListener(IAnnotationListener listener);

	/**
	 * Return all annotation type that can be create
	 * 
	 * @return
	 */
	AnnotationType[] getTypes();

	/**
	 * Return type of the specific annotation
	 * 
	 * @param annotation
	 * @return type of the passed annotation
	 */
	AnnotationType getType(Annotation annotation);

	/**
	 * Return type by EMF package name and class name. To get package by
	 * EPackage and EClass you need to call:<br>
	 * <code>
	 *   getType(EPackage.getNsPrefix(), EClass.getName()); 
	 * </code>
	 * 
	 * @param epackage
	 *            EPackage namespace
	 * @param eclazz
	 *            EClass name
	 * @return
	 */
	AnnotationType getType(String epackage, String eclazz);

	/**
	 * Return annotation by id
	 * 
	 * @param id
	 *            annotation id
	 * @return annotation by specified id or null, if there is no annotations
	 *         with this id
	 */
	Annotation getAnnotationById(String id);

	/**
	 * Return annotations which start with the specified URI
	 * 
	 * @param uri
	 * @return list of the annotations started from the uri
	 */
	List<Annotation> getPostfixAnnotations(URI uri);

	/**
	 * Add refactoring listener
	 * 
	 * @param listener
	 */
	void addRefactoringListener(IRefactoringListener listener);

	/**
	 * Remove refactoring listener
	 * 
	 * @param listener
	 */
	void removeRefactoringListener(IRefactoringListener listener);

	/**
	 * @param object
	 * @return
	 */
	TargetAnnotationType[] getAnnotationTargets(Object object);

	/**
	 * Add resource to the Annotation Framework storage
	 * 
	 * @param resource
	 * @param option
	 */
	void addAnnotations(Resource resource);

	/**
	 * Remove resource from the Annotation Framework storage. If this resource
	 * have some unsaved changes this changes should be CANCELED (for example,
	 * because resource was deleted).
	 * 
	 * @param resource
	 */
	void removeAnnotations(Resource resource);

	/**
	 * @return true, if annotation can't be modified and false otherwise
	 */
	boolean isReadOnly(Annotation annotation);

	/**
	 * Return EMF package label or null if none
	 * 
	 * @param pckg
	 *            package
	 * @return package label
	 */
	String getPackageLabel(EPackage pckg);

	/**
	 * Notify framework about object with specified URI has been deleted
	 * 
	 * @param uri
	 *            URI of the deleted object
	 */
	void deleted(URI uri, boolean affectChildren);

	/**
	 * Notify framework about object's URI change
	 * 
	 * @param oldUri
	 *            old object URI
	 * @param newUri
	 *            new object URI
	 */
	void changed(URI oldUri, URI newUri, boolean affectChildren);

	/**
	 * Notify framework about object with fromUri was copied to object with
	 * toUri
	 * 
	 * @param fromUri
	 * @param toUri
	 */
	void copied(URI fromUri, URI toUri, boolean affectChildren);

	/**
	 * Notify framework about object has been deleted
	 * 
	 * @param object
	 */
	void fireDeleted(ILazyObject object);

	/**
	 * Notify framework about object has been changed. Kind of notification can
	 * take a value of {@link IRefactoringChangesListener.ABOUT_TO_CHANGE}
	 * before changes and {@link IRefactoringChangesListener.CHANGED} after
	 * that.
	 * 
	 * @param oldObject
	 *            represents object before changes
	 * @param newObject
	 *            represents object after changes
	 * @param kind
	 *            kind of notification
	 */
	void fireChanged(ILazyObject oldObject, ILazyObject newObject, int kind);

	/**
	 * Notify framework about objects have been moved. Kind of notification can
	 * take a value of {@link IRefactoringChangesListener.ABOUT_TO_CHANGE}
	 * before moving and {@link IRefactoringChangesListener.CHANGED} after that.
	 * 
	 * @param objects
	 *            represent objects before moving
	 * @param destination
	 *            destination of objects moved
	 * @param kind
	 *            kind of notification
	 */
	void fireMoved(ILazyObject[] objects, ILazyObject destination, int kind);

	/**
	 * Notify framework about objects have been copied. Kind of notification can
	 * take a value of {@link IRefactoringChangesListener.ABOUT_TO_CHANGE}
	 * before moving and {@link IRefactoringChangesListener.CHANGED} after that.
	 * 
	 * @param objects
	 *            represent objects before coping
	 * @param destination
	 *            destination of objects copied
	 * @param newNames
	 *            map from object to its new name
	 * @param kind
	 *            kind of notification
	 */
	void fireCopy(ILazyObject[] objects, ILazyObject destination,
			Map<ILazyObject, String> newNames, int kind);

	/**
	 * @return the {@link ProviderManager} instance
	 */
	ProviderManager getProviderManager();

	/**
	 * @return the {@link Searcher} for convinient way for search annotations.
	 */
	Searcher getSearcher();
	
	boolean isAnnotationFile(IFile file);

	ResourceSet getResourceSet();
	
	/**
	 * Disables notifications till transaction not finished
	 */
	void silentMode();
}
