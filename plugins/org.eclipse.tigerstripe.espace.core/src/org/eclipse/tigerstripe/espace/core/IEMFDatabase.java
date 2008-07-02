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
package org.eclipse.tigerstripe.espace.core;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;

/**
 * This interface represent persistence service which used for storing/retrieving
 * annotation objects (but it's able to persist any EMF object as well). Upon initialization service 
 * install available content-based routers which used to route annotations to resources.
 * Also service provide default routing scenario. Currently it store all objects
 * in the single file in the workspace metadata.
 * 
 * @see EObjectRouter
 * @author Yuri Strot
 */
public interface IEMFDatabase {
	
	/**
	 * Save EMF object to the specific resource
	 * 
	 * @param object EMF object
	 */
	public void write(EObject object);
	
	/**
	 * @param object
	 * @param changes
	 */
	public void update(EObject object, ChangeDescription changes);
	
	/**
	 * Return EMF objects by the specific feature value
	 * 
	 * @param feature
	 * @param value
	 * @return objects by the specific feature value
	 */
	public EObject[] get(EStructuralFeature feature, Object value);
	
	/**
	 * Return EMF objects which value started with the specified feature value
	 * 
	 * @param feature
	 * @param value
	 * @return objects started with the specified feature value
	 */
	public EObject[] getPostfixes(EStructuralFeature feature, Object value);
	
	/**
	 * Return all stored objects of the passed classifier
	 * 
	 * @param classifier classifier of the objects should be found
	 * @return Return all stored objects of the passed classifier
	 */
	public EObject[] query(EClassifier classifier);
	
	/**
	 * Remove EMF object from the specific resource
	 * 
	 * @param object
	 */
	public void remove(EObject object);
	
	/**
	 * Read all previously saved EMF objects
	 * 
	 * @return previously saved EMF objects or empty array if none
	 */
	public EObject[] read();

}
