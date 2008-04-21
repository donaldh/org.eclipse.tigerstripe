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
package org.eclipse.tigerstripe.annotation.example.person.validators;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.tigerstripe.annotation.example.person.Person;

/**
 * @author Yuri Strot
 *
 */
public class PersonValidator extends AbstractModelConstraint {

    public IStatus validate(IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		EMFEventType eType = ctx.getEventType();
		
		// In the case of batch mode.
		int age = 0;
		if (eType == EMFEventType.NULL) {
			if (eObj instanceof Person) {
				age = ((Person)eObj).getAge();
			}
			
		// In the case of live mode.
		} else {
			Object newValue = ctx.getFeatureNewValue();
			if (newValue instanceof Integer) {
				age = ((Integer)newValue).intValue();
			}
		}
		
		if (age < 0)
			return ctx.createFailureStatus(new Object[] { "negative" });
		if (age >= 100)
			return ctx.createFailureStatus(new Object[] { "greater than 100 years" });
		
		return ctx.createSuccessStatus();
    }

}
