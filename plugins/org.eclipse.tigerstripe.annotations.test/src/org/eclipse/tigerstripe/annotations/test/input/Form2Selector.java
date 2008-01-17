/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial API and Implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.test.input;

import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ISelector;

public class Form2Selector implements ISelector {

	public boolean isEnabled(IAnnotationSpecification spec, String URI)
			throws AnnotationCoreException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean select(String URI) {
		return URI.equals(IInputConstants.URI2);
	}

	public void setContext(Object context) {
		// TODO Auto-generated method stub

	}

}
