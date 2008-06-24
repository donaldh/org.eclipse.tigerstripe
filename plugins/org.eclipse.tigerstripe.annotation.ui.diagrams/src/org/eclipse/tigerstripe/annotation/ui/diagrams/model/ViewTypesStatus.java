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
package org.eclipse.tigerstripe.annotation.ui.diagrams.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Yuri Strot
 * @model
 */
public interface ViewTypesStatus extends EObject {
	
	/**
	 * @model
	 */
	public View getView();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewTypesStatus#getView <em>View</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>View</em>' reference.
	 * @see #getView()
	 * @generated
	 */
	void setView(View value);

	/**
	 * @model containment="true"
	 */
	public EList<TypeStatus> getStatuses();

}
