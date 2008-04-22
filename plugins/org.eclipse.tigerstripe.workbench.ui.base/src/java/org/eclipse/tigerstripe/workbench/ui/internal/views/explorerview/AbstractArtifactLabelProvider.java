/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AbstractArtifactLabelProvider implements ILabelProvider,
		IColorProvider {

	public Image getImage(Object element) {
		return getImage(element, true);
	}

	public Image getImage(Object element, boolean transparencySupported) {

		transparencySupported = true; // transparency support removed because
		// of
		// problems with SWTHandles. This was introduced for support of better
		// looking
		// icons on Linux. Not needed for now. Will need a better solution.

		boolean isInActiveFacet = true;
		if (element instanceof IAbstractArtifact) {
			IAbstractArtifact artifact = (IAbstractArtifact) element;
			try {
				isInActiveFacet = artifact.isInActiveFacet();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		if (element instanceof ManagedEntityArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.ENTITY_ICON);
			else
				return Images.get(Images.ENTITY_ICON_GS);
		} else if (element instanceof DatatypeArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.DATATYPE_ICON);
			else
				return Images.get(Images.DATATYPE_ICON_GS);
		} else if (element instanceof EventArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.NOTIFICATION_ICON);
			else
				return Images.get(Images.NOTIFICATION_ICON_GS);
		} else if (element instanceof EnumArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.ENUM_ICON);
			else
				return Images.get(Images.ENUM_ICON_GS);
		} else if (element instanceof SessionFacadeArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.SESSION_ICON);
			else
				return Images.get(Images.SESSION_ICON_GS);
		} else if (element instanceof QueryArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.QUERY_ICON);
			else
				return Images.get(Images.QUERY_ICON_GS);
		} else if (element instanceof ExceptionArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.EXCEPTION_ICON);
			else
				return Images.get(Images.EXCEPTION_ICON_GS);
		} else if (element instanceof UpdateProcedureArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.UPDATEPROC_ICON);
			else
				return Images.get(Images.UPDATEPROC_ICON_GS);
		} else if (element instanceof AssociationClassArtifact) {
			// because AssociationClassArtifact is-a AssociationArtifact
			// we must check for AssociationClass first... in that order!
			if (isInActiveFacet)
				return Images.get(Images.ASSOCIATIONCLASS_ICON);
			else
				return Images.get(Images.ASSOCIATIONCLASS_ICON_GS);
		} else if (element instanceof AssociationArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.ASSOCIATION_ICON);
			else
				return Images.get(Images.ASSOCIATION_ICON_GS);
		} else if (element instanceof DependencyArtifact) {
			if (isInActiveFacet)
				return Images.get(Images.DEPENDENCY_ICON);
			else
				return Images.get(Images.DEPENDENCY_ICON_GS);
		} else if (element instanceof PrimitiveTypeArtifact)
			// Bug 947: primite type artifacts are always in facet.
			return Images.get(Images.PRIMITIVE_ICON);
		return null;
	}

	public String getText(Object element) {
		AbstractArtifact artifact = (AbstractArtifact) element;
		return artifact.getFullyQualifiedName();
	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		TigerstripeRuntime.logInfoMessage("registering listener=" + listener);

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public Color getBackground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public Color getForeground(Object element) {
		if (element instanceof IModelComponent) {
			IModelComponent component = (IModelComponent) element;
			try {
				if (!component.isInActiveFacet())
					return ColorUtils.LIGHT_GREY;
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		return ColorUtils.BLACK;
	}

}
