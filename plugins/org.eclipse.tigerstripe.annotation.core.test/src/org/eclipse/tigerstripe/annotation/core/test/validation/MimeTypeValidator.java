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
package org.eclipse.tigerstripe.annotation.core.test.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;

/**
 * @author Yuri Strot
 *
 */
public class MimeTypeValidator extends AbstractModelConstraint {
	
	public static final String INVALID_TYPE = "text/plain";

	public static final String VALID_TYPE = "text/xml";

    public IStatus validate(IValidationContext ctx) {
    	MimeType type = (MimeType)ctx.getTarget();
		if (type.getMimeType() != null && type.getMimeType().equals(INVALID_TYPE))
			return ctx.createFailureStatus(new Object[0]);
		return ctx.createSuccessStatus();
    }

}
