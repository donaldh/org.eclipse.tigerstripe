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
package org.eclipse.tigerstripe.espace.resources;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface ResourceList extends EObject {
	
	/**
	 * @model default="1"
	 */
	public int getCurrentId();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.espace.resources.ResourceList#getCurrentId <em>Current Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Id</em>' attribute.
	 * @see #getCurrentId()
	 * @generated
	 */
	void setCurrentId(int value);

	/**
	 * @model containment="true"
	 */
	EList<ResourceLocation> getLocations();

}
