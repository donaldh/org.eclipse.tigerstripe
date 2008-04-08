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
package org.eclipse.tigerstripe.workbench.ui.visualeditor;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 * 
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAttribute()
 * @model
 * @generated
 */
public interface Attribute extends TypedElement {
	
	public void setField(IField field);
	
	public IField getField();
	
} // Attribute
