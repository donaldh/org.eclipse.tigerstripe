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
package org.eclipse.tigerstripe.workbench.ui.gmf;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

public class TSWrapLabel extends WrapLabel {

	private Image iconImage = null;

	public TSWrapLabel() {
		super();
	}

	public TSWrapLabel(String arg0) {
		super(arg0);
	}

	public TSWrapLabel(Image arg0) {
		super(arg0);
	}

	public TSWrapLabel(String arg0, Image arg1) {
		super(arg0, arg1);
	}

	private Image imageWithNewBackground(Image arg0, Color bgColor) {
		if (arg0 != null && bgColor != null) {
			ImageData imageData = arg0.getImageData();
			int threshhold = 255;
			RGB backgroundVal = bgColor.getRGB();
			for (int i = 0; i < imageData.width; i++) {
				for (int j = 0; j < imageData.height; j++) {
					RGB rgbVal = imageData.palette.getRGB(imageData.getPixel(i,
							j));
					if (rgbVal.red >= threshhold && rgbVal.green >= threshhold
							&& rgbVal.blue >= threshhold) {
						imageData.setPixel(i, j, imageData.palette
								.getPixel(backgroundVal));
					}
				}
			}
			return new Image(arg0.getDevice(), imageData);
		} else
			return arg0;

	}

	public void refreshIcon() {
		// reset icon, which should change background color of icon
		super.setIcon(iconImage);
	}

	public void setIcon(Image arg0, boolean isNewIcon) {
		if (isNewIcon)
			iconImage = arg0;
		super.setIcon(arg0);
	}

	@Override
	public void setIcon(Image arg0, int arg1) {
		Color bgColor = this.getBackgroundColor();
		super.setIcon(imageWithNewBackground(arg0, bgColor), arg1);
	}

	@Override
	protected Dimension calculateLabelSize(Dimension txtSize) {
		// TODO Auto-generated method stub
		return super.calculateLabelSize(txtSize);
	}

	@Override
	protected Dimension calculateTextSize(int wHint, int hHint) {
		// TODO Auto-generated method stub
		Dimension d = super.calculateTextSize(wHint, hHint);
		// d.width = d.width +100;
		return d;
	}

}
