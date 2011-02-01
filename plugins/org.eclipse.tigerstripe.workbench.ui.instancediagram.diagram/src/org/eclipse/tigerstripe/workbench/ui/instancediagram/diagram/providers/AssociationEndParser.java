/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;

public class AssociationEndParser extends
		InstanceStructuralFeatureParser {

	public AssociationEndParser(EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {
		setCurrentMap(adapter);
		return super.getPrintString(adapter, flags);
	}

	@Override
	public boolean isAffectingEvent(Object event, int flags) {
		if (event instanceof Notification) {
			Object obj = ((Notification) event).getFeature();
			if (InstancediagramPackage.eINSTANCE
					.getAssociationInstance_AEndIsOrdered().equals(obj)
					|| InstancediagramPackage.eINSTANCE
							.getAssociationInstance_ZEndIsOrdered().equals(obj))
				return true;
		}
		return super.isAffectingEvent(event, flags);
	}
}
