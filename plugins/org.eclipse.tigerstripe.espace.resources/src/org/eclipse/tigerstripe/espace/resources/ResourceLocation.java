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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.espace.core.Mode;

/**
 * @author Yuri Strot
 * @model
 */
public interface ResourceLocation extends EObject {
	
	/**
	 * @model
	 */
	public URI getUri();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.espace.resources.ResourceLocation#getUri <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
	void setUri(URI value);

	/**
	 * @model
	 */
	public long getTimeStamp();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.espace.resources.ResourceLocation#getTimeStamp <em>Time Stamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Stamp</em>' attribute.
	 * @see #getTimeStamp()
	 * @generated
	 */
	void setTimeStamp(long value);
	
	/**
	 * @model
	 */
	public Mode getOption();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.espace.resources.ResourceLocation#getOption <em>Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Option</em>' attribute.
	 * @see org.eclipse.tigerstripe.espace.core.Mode
	 * @see #getOption()
	 * @generated
	 */
	void setOption(Mode value);

}
