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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface Annotation extends EObject {
	
	/**
	 * @model
	 */
	public URI getUri();

    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.core.Annotation#getUri <em>Uri</em>}' attribute.
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
	public EObject getContent();

    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.core.Annotation#getContent <em>Content</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Content</em>' reference.
     * @see #getContent()
     * @generated
     */
    void setContent(EObject value);

}
