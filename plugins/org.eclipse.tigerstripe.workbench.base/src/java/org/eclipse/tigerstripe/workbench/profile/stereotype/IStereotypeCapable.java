/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.profile.stereotype;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.workbench.TigerstripeException;


/**
 * Interface to be implemented by all components of the model that can be
 * annotated with Stereotypes.
 * 
 * @author Eric Dillon
 * 
 */
public interface IStereotypeCapable {

	/**
	 * Returns an array of stereotype instances. These are the stereotypes (also
	 * referred to as annotations) that are applied to this model component. If
	 * there are no stereotypes applied to this component, the method returns
	 * an empty array.
	 * 
	 * @return array of IStereotypeInstance.
	 */
	public Collection<IStereotypeInstance> getStereotypeInstances();
	/**
	 * Returns a StereotypeInstance with the name supplied.
	 * If there is no instance with that name, null is returned.
	 * @param name
	 * @return IStereotypeInstance
	 */
	public IStereotypeInstance getStereotypeInstanceByName(String name);
	
	/**
	 * Checks for the presence of the named stereotype.
	 *  
	 * @param name
	 * @return 
	 */
	public boolean hasStereotypeInstance(String name);

	public void addStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstances(Collection<IStereotypeInstance> instances);

	/**
	 * Creates a new annotation and returns the content wrapped in an <code>Annotation</code>
	 * instance
	 * @param schemeID the scheme to which the annotation will be assigned
	 * @param packij the package as a <code>String</code> to which the annotation content
	 * belongs
	 * @param clazz the class-name of annotation content required
	 * @return the annotation content wrapped in an <code>Annotation</code> instance
	 * @throws TigerstripeException
	 * @deprecated
	 */
	public Annotation addAnnotation(String schemeID, String packij, String clazz)
	    throws TigerstripeException;

	/**
	 * Creates a new annotation and returns the content wrapped in an <code>Annotation</code>
	 * instance - the schemeID is assumed to be <em>"tigerstripe"</em>
	 * @param packij the package as a <code>String</code> to which the annotation content
	 * belongs
	 * @param clazz the class-name of annotation content required
	 * @return the annotation content wrapped in an <code>Annotation</code> instance
	 * @throws TigerstripeException
	 * @deprecated
	 */
	public Annotation addAnnotation(String packij, String clazz)
	    throws TigerstripeException;
	
	/**
	 * Creates a new annotation and returns the content wrapped in an <code>Annotation</code>
	 * instance - the schemeID is assumed to be <em>"tigerstripe"</em>
	 * @param clazz the type of the annotation content to be created as a <code>Class</code>
	 * @return the annotation content wrapped in an <code>Annotation</code> instance
	 * @throws TigerstripeException
	 * @deprecated
	 */
	public Annotation addAnnotation(Class<? extends EObject> clazz)
	    throws TigerstripeException;
	
	/**
	 * Saves an instance of an <code>Annotation</code>, that is, ensures that any changes
	 * to the content transmitted to the internal model and are made persistent
	 * @param annotation the <code>Annotation</code> instance to be saved
	 * @deprecated
	 */
	public void saveAnnotation(Annotation annotation);
}
