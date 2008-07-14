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

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.repository.internal.IModelComponentMetadata;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;

/**
 * @author Eric Dillon
 * 
 */
public class AbstractArtifactLabelProvider implements ILabelProvider,
		IColorProvider {

	// This is used to avoid creating images over and over for icons
	private ImageRegistry iconRegistry = new ImageRegistry();

	public Image getImage(Object element) {
		return getImage(element, true);
	}

	public Image getImage(Object element, boolean transparencySupported) {

		transparencySupported = true; // transparency support removed because
		// of problems with SWTHandles. This was introduced for support of
		// better looking icons on Linux. Not needed for now. Will need a better
		// solution.

		boolean isInActiveFacet = true;
		if (element instanceof IAbstractArtifact) {
			IAbstractArtifact artifact = (IAbstractArtifact) element;
			try {
				isInActiveFacet = artifact.isInActiveFacet();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
				.getMetadata(element);
		if (metadata != null) {
			URL iconURL;
			if (isInActiveFacet) {
				iconURL = metadata.getIconURL(element);
			} else
				iconURL = metadata.getGreyedoutIconURL(element);

			Image result = iconRegistry.get(iconURL.toString());
			if (result == null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(iconURL);
				result = desc.createImage();
				iconRegistry.put(iconURL.toString(), result);
			}

			return result;
		}

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
