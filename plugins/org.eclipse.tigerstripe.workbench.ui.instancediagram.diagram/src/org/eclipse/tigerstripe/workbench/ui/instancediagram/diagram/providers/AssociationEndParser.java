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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.Activator;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl;

public abstract class AssociationEndParser extends
		InstanceStructuralFeatureParser {

	public AssociationEndParser(EStructuralFeature feature) {
		super(feature);
	}

	protected abstract boolean isAssociationEndIsOrdered(
			AssociationInstance association);

	protected abstract IAssociationEnd getEnd(
			IAssociationArtifact associationArtifact);

	protected abstract Integer getEndOrderNumber(AssociationInstance association);

	protected boolean isEndIsOrdered(AssociationInstance association) {
		// Before 230101 bug "is ordered" flag was ignored and flag value was
		// always
		// "false" during associations
		// creation. So to support ordering on diagrams which was created before
		// 230101 we should get
		// "is ordered" flag from direct artifact instance.
		try {
			IAbstractArtifact artifact = association.getArtifact();
			if (artifact != null && artifact instanceof IAssociationArtifact) {
				IAssociationArtifact associationArtifact = (IAssociationArtifact) artifact;
				IAssociationEnd end = getEnd(associationArtifact);
				if (end != null) {
					return end.isOrdered();
				}
			}
		} catch (TigerstripeException e) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, e
							.getLocalizedMessage(), e));
		}
		return isAssociationEndIsOrdered(association);
	}

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {
		setCurrentMap(adapter);
		AssociationInstance association = (AssociationInstance) adapter
				.getAdapter(AssociationInstanceImpl.class);

		StringBuilder result = new StringBuilder();

		if (!hideOrderedQualifiers()) {
			if (isEndIsOrdered(association)) {
				result.append("{order=");
				Integer orderNumber = getEndOrderNumber(association);
				if (orderNumber != null) {
					result.append(orderNumber);
				} else {
					result.append("unknown");
				}
				result.append("} ");
			}
		}
		result.append(super.getPrintString(adapter, flags));
		return result.toString();
	}

	@Override
	public boolean isAffectingEvent(Object event, int flags) {
		if (!hideOrderedQualifiers()) {
			if (event instanceof Notification) {
				Object obj = ((Notification) event).getFeature();
				if (InstancediagramPackage.eINSTANCE
						.getAssociationInstance_AEndIsOrdered().equals(obj)
						|| InstancediagramPackage.eINSTANCE
								.getAssociationInstance_ZEndIsOrdered().equals(
										obj))
					return true;
			}
		}
		return super.isAffectingEvent(event, flags);
	}
}
