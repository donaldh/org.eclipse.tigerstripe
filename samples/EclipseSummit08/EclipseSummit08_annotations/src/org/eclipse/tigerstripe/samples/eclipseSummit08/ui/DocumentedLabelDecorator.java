/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.samples.eclipseSummit08.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.samples.eclipseSummit08.Activator;
import org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.Note;
import org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.viewers.BaseLabelDecorator;
import org.eclipse.tigerstripe.workbench.ui.viewers.ITigerstripeLabelDecorator;

public class DocumentedLabelDecorator extends BaseLabelDecorator implements
		ITigerstripeLabelDecorator {

	protected URL baseURL = Activator.getDefault().getBundle().getEntry(
			"icons/"); //$NON-NLS-1$

	private static ImageRegistry images;

	protected static ImageRegistry getImages() {
		if (images == null) {
			images = new ImageRegistry();
		}
		return images;
	}

	protected static Image addImage(String key, Image image) {
		getImages().put(key, image);
		return image;
	}

	protected static Image addImage(String key, ImageDescriptor descriptor) {
		getImages().put(key, descriptor);
		return getImage(key);
	}

	protected static Image getImage(String key) {
		return getImages().get(key);
	}

	public Image decorateImage(Image image, IModelComponent component) {
		if (component == null)
			return image;
		if (component instanceof IAbstractArtifact) {
			Note ann = (Note) component.getAnnotation("Note");
			if (ann != null) {
				String key = component.getClass().getName() + "_doc";
				Image newImage = overlayImage(image, key, "pencil.png");
				if (newImage != null)
					image = newImage;
			}
		}
		return image;
	}

	/**
	 * Finds the overlaid image to match the supplied component - either gets it
	 * from the <code>ImageRegistry</code> or creates it from the supplied image
	 * and file-name of the overlay image
	 * 
	 * @param image
	 *            the input image to be overlaid
	 * @param key
	 *            the key that will locate the overlaid image in the
	 *            <code>ImageRegistry</code>
	 * @param overlayName
	 *            the name of the file containing the overlay image
	 * @return the new image with the required overlay
	 */
	protected Image overlayImage(Image image, String key, String overlayName) {
		Image newImage = getImage(key);
		if (newImage == null) {
			try {
				URL iconURL = new URL(baseURL, overlayName);
				ImageDescriptor descriptor = ImageDescriptor
						.createFromURL(iconURL);
				descriptor = new DecorationOverlayIcon(image, descriptor,
						IDecoration.TOP_RIGHT);
				newImage = addImage(key, descriptor);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return newImage;
	}

	public String decorateText(String text, IModelComponent component) {
		return text;
	}
}
