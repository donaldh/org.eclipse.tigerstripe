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
package org.eclipse.tigerstripe.workbench.ui.viewers;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

public interface ITigerstripeLabelDecorator extends ILabelDecorator {

	public Image decorateImage(Image image, IModelComponent component);

	public String decorateText(String text, IModelComponent component);
}
