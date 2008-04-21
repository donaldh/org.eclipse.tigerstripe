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
package org.eclipse.tigerstripe.annotation.internal.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.ILiveValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;

/**
 * @author Yuri Strot
 *
 */
public class ValidationAdapter extends AdapterImpl {
	
	private ILiveValidator validator;
	
	public ValidationAdapter() {
	}
	
	@Override
	public void notifyChanged(Notification msg) {
		if (validator == null) {
			validator = (ILiveValidator) ModelValidationService
					.getInstance().newValidator(EvaluationMode.LIVE);
		}
		
		IStatus status = validator.validate(msg);
		
		if (!status.isOK()) {
			if (status.isMultiStatus()) {
				status = status.getChildren()[0];
			}
			AnnotationPlugin.getDefault().getLog().log(status);
			
			Object notifier = msg.getNotifier();
			Object feature = msg.getFeature();
			if (notifier instanceof EObject && feature instanceof EStructuralFeature) {
				EObject object = (EObject)notifier;
				EStructuralFeature sFeature = (EStructuralFeature)feature;
				object.eSet(sFeature, msg.getOldValue());
			}
		}
	}

}
