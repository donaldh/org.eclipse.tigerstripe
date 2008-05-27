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
package org.eclipse.tigerstripe.annotation.core.test.model;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface Hibernate extends EObject {
	
	/**
	 * @model
	 */
	public boolean isPersistance();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.core.test.model.Hibernate#isPersistance <em>Persistance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Persistance</em>' attribute.
	 * @see #isPersistance()
	 * @generated
	 */
	void setPersistance(boolean value);

}
