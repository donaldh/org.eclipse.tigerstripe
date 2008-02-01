/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    jistrawn (Cisco Systems, Inc.) - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.internal;

import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecificationLiteral;

public class AnnotationSpecificationLiteral implements IAnnotationSpecificationLiteral {

	private String value;
	
	private int index = IAnnotationSpecification.UNDEF_INDEX;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
