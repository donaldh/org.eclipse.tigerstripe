/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    jistrawn (Cisco Systems, Inc.) - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.ui.internal;

import java.util.Comparator;

import org.eclipse.tigerstripe.annotations.IAnnotationSpecificationLiteral;

public class AnnotationSpecificationLiteralComparator implements
		Comparator<IAnnotationSpecificationLiteral> {

	public int compare(IAnnotationSpecificationLiteral literal1,
			IAnnotationSpecificationLiteral literal2) {

		return (literal1.getIndex() - literal2.getIndex());
	}

}
