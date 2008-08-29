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
package org.eclipse.tigerstripe.annotation.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IStatusHandler;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.ILiveValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.emf.validation.util.FilteredCollection;

/**
 * @author Yuri Strot
 *
 */
public class ValidationAdapter extends AdapterImpl {
	
	private ILiveValidator validator;
	private Set<EObject> objects = new HashSet<EObject>();
	
	public ValidationAdapter() {
	}
	
	protected ILiveValidator getValidator() {
		if (validator == null) {
			validator = (ILiveValidator) ModelValidationService
					.getInstance().newValidator(EvaluationMode.LIVE);
			validator.setNotificationFilter(new FilteredCollection.Filter() {
			
				public boolean accept(Object element) {
					if (element instanceof Notification) {
						Notification notification = (Notification)element;
						return notification.getNotifier() instanceof EObject;
					}
					return false;
				}
			
			});
		}
		return validator;
	}
	
	@Override
	public void notifyChanged(Notification msg) {
		Object notifier = msg.getNotifier();
		Object feature = msg.getFeature();
		if (notifier instanceof EObject && feature instanceof EStructuralFeature) {
			EObject object = (EObject)notifier;
			EStructuralFeature sFeature = (EStructuralFeature)feature;
			
			if (objects.contains(object))
				return;
			
			IStatus status = getValidator().validate(msg);
			if (status.isOK()) {
				Diagnostic diagnostic = Diagnostician.INSTANCE.validate(object);
				if (diagnostic != null) {
					if (diagnostic.getSeverity() != Diagnostic.OK) {
						status = new Status(diagnostic.getSeverity(), ValidationPlugin.PLUGIN_ID,
							1, diagnostic.getMessage(), diagnostic.getException());
					}

				}
			}
			handleStatus(status, object);
			if (!status.isOK()) {
				if (status.isMultiStatus()) {
					status = status.getChildren()[0];
				}
				
				objects.add(object);
				
				switch (msg.getEventType()) {
					case Notification.SET:
						object.eSet(sFeature, msg.getOldValue());
						break;
					default:
						break;
				}
				
				objects.remove(object);
			}
		}
	}
	
	protected void handleStatus(IStatus status, Object source) {
		IStatus customStatus = new Status(status.getSeverity(), ValidationPlugin.PLUGIN_ID,
			1, status.getMessage(), status.getException());
		IStatusHandler handler = DebugPlugin.getDefault().getStatusHandler(customStatus);
		if (handler != null) {
			try {
                handler.handleStatus(status, source);
            }
            catch (CoreException e) {
                e.printStackTrace();
            }
		}
	}

}
