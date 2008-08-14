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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.InvalidAnnotationTargetException;

/**
 * A helper class that provides tigerstripe specific mechanisms for manipulating annotations
 * <code>IAnnotationCapable</code> objects 
 * @author jworrell
 *
 */
public class AnnotationHelper
{
	private static AnnotationHelper instance;

//	private IAnnotationManager manager;
	
	static public AnnotationHelper getInstance()
	{
		// TODO: no synchronization, and do I really need singleton anyway?
		if(instance == null)
		{
			instance = new AnnotationHelper();
		}
		return instance;
	}
	
	protected AnnotationHelper()
	{
//		manager = AnnotationPlugin.getManager();
	}
	
	/**
	 * Creates a new annotation and returns the content wrapped in an <code>Annotation</code>
	 * instance
	 * @param schemeID the scheme to which the annotation will be assigned
	 * @param packij the package as a <code>String</code> to which the annotation content
	 * belongs
	 * @param clazz the class-name of annotation content required
	 * @return the annotation content wrapped in an <code>Annotation</code> instance
	 * @throws TigerstripeException
	 */
//	public Annotation addAnnotation(IAnnotationCapable target, String schemeID, String packij, String clazz)
//	    throws TigerstripeException
//	{
//		
//	}

	/**
	 * Creates a new annotation and returns the content wrapped in an <code>Annotation</code>
	 * instance - the schemeID is assumed to be <em>"tigerstripe"</em>
	 * @param packij the package as a <code>String</code> to which the annotation content
	 * belongs
	 * @param clazz the class-name of annotation content required
	 * @return the annotation content wrapped in an <code>Annotation</code> instance
	 * @throws TigerstripeException
	 */
	public Annotation addAnnotation(IAnnotationCapable target, String packij, String clazz)
	    throws TigerstripeException
	{
		AnnotationType type = AnnotationPlugin.getManager().getType(packij, clazz);
		if(type == null)
			throw new InvalidAnnotationTargetException("No such AnnotationType");
		// Questionable stuff: not sure this should be here
		String[] targets = type.getTargets();
		boolean ok = targets.length == 0;
		if(!ok)
		{
			for(int t = 0; t < targets.length; t++)
			{
				try {
					if(Class.forName(targets[t], false, target.getClass().getClassLoader()).isInstance(target))
						ok = true;
				} catch (ClassNotFoundException e) {/* Nothing */}
			}
		}
		if(!ok)
			throw new InvalidAnnotationTargetException("Target not allowed for AnnotationType");
		// END questionable stuff
		EObject content = type.createInstance();
		try {
			return AnnotationPlugin.getManager().addAnnotation(target, content);
		} catch (AnnotationException e) {
			throw new TigerstripeException("Failed to add annotation of type: "+content.getClass().getName(), e);
		}
	}
	
	/**
	 * Creates a new annotation and returns the content wrapped in an <code>Annotation</code>
	 * instance - the schemeID is assumed to be <em>"tigerstripe"</em>
	 * @param clazz the type of the annotation content to be created as a <code>Class</code>
	 * @return the annotation content wrapped in an <code>Annotation</code> instance
	 * @throws TigerstripeException
	 */
	public Annotation addAnnotation(IAnnotationCapable target, Class<? extends EObject> clazz)
	    throws TigerstripeException
	{
		return addAnnotation(target, clazz.getPackage().getName(), clazz.getName().substring(clazz.getName().lastIndexOf('.')+1));
	}
	
	/**
	 * Saves an instance of an <code>Annotation</code>, that is, ensures that any changes
	 * to the content transmitted to the internal model and are made persistent
	 * @param annotation the <code>Annotation</code> instance to be saved
	 */
	public void saveAnnotation(Annotation annotation)
	{
		AnnotationPlugin.getManager().save(annotation);
	}

	public List<Annotation> getAnnotations(IAnnotationCapable target) {
		IAnnotationManager mgr = AnnotationPlugin.getManager();
		List<Annotation> annotations = Arrays.asList(mgr.getAnnotations(target, false));
		return Collections.unmodifiableList(annotations);
	}

	public Annotation getAnnotation(IAnnotationCapable target, String annotationSpecificationID) {
		List<Annotation> all = getAnnotations(target);
		for (Annotation obj : all) {
			if (isAnnotationMatch(annotationSpecificationID, obj))
				return obj;
		}

		return null;
	}

	public Annotation getAnnotation(IAnnotationCapable target, Class<? extends EObject> clazz) throws TigerstripeException
	{
		return getAnnotation(target, clazz.getName());
	}

	/**
	 * @param annotationSpecificationID
	 * @param obj
	 * @return
	 */
	private boolean isAnnotationMatch(String annotationSpecificationID,	Annotation obj) {
		return isAnnotationMatch(annotationSpecificationID, obj.getContent());
	}

	/**
	 * @param annotationSpecificationID
	 * @param obj
	 * @return
	 */
	private boolean isAnnotationMatch(String annotationSpecificationID,	Object obj) {
		Class<?>[] interfaces = obj.getClass().getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i].getName().endsWith(annotationSpecificationID))
				return true;
		}
		return false;
	}

	public List<Annotation> getAnnotations(IAnnotationCapable target, String annotationSpecificationID) {
		List<Annotation> annotations = getAnnotations(target);
		for (Iterator<Annotation> i = annotations.iterator(); i.hasNext();) {
			if (!isAnnotationMatch(annotationSpecificationID, i.next()))
				i.remove();
		}

		return Collections.unmodifiableList(annotations);
	}
}
