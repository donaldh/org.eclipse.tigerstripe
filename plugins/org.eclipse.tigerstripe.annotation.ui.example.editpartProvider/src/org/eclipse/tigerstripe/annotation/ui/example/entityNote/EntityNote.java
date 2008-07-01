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
package org.eclipse.tigerstripe.annotation.ui.example.entityNote;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface EntityNote extends EObject {
	
	/**
	 * @model
	 */
	public String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNote#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

}
