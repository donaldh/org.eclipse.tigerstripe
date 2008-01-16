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
package org.eclipse.tigerstripe.annotations;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public interface IValidator {

	// A default validator for convenience.
	public final static IValidator DEFAULT = new IValidator() {

		public void setContext(Object context) {
		}

		public IStatus validate(String URI) throws AnnotationCoreException {
			return Status.OK_STATUS;
		}
	};

	public void setContext(Object context);

	public IStatus validate(String URI) throws AnnotationCoreException;
}
