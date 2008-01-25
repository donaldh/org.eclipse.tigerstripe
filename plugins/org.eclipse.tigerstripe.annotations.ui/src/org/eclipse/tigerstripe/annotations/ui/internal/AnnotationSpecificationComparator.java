/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    J. Strawn (Cisco Systems, Inc.) - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.ui.internal;

import java.util.Comparator;

import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;

public class AnnotationSpecificationComparator implements
		Comparator<IAnnotationSpecification> {

	public int compare(IAnnotationSpecification spec1,
			IAnnotationSpecification spec2) {

		return (spec1.getIndex() - spec2.getIndex());
	}

}
