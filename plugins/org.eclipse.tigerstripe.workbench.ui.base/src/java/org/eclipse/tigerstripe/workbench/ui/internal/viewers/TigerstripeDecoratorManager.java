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
package org.eclipse.tigerstripe.workbench.ui.internal.viewers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.viewers.ITigerstripeLabelDecorator;

/**
 * Gathers all the Tigerstripe Label Decorators registered thru ext point.
 * 
 * @author erdillon
 * 
 */
public class TigerstripeDecoratorManager {

	private final static String EXT_PT = "org.eclipse.tigerstripe.workbench.ui.base.labelDecorator";

	private static TigerstripeDecoratorManager instance = null;

	private List<ITigerstripeLabelDecorator> decorators = new ArrayList<ITigerstripeLabelDecorator>();

	private TigerstripeDecoratorManager() {
		addTigerstripeLabelDecorators();
	}

	public static TigerstripeDecoratorManager getDefault() {
		if (instance == null) {
			instance = new TigerstripeDecoratorManager();
		}
		return instance;
	}

	public List<ITigerstripeLabelDecorator> getDecorators() {
		return Collections.unmodifiableList(decorators);
	}

	private void addTigerstripeLabelDecorators() {
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(EXT_PT);
		for (IConfigurationElement element : elements) {
			try {
				ITigerstripeLabelDecorator deco = (ITigerstripeLabelDecorator) element
						.createExecutableExtension("class");
				decorators.add(deco);
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	public Image decorateImage(Image image, IModelComponent component) {
		Image result = image;
		for (ITigerstripeLabelDecorator decorator : decorators) {
			result = decorator.decorateImage(result, component);
		}
		return result;
	}

	public String decorateText(String text, IModelComponent component) {
		String result = text;
		for (ITigerstripeLabelDecorator decorator : decorators) {
			result = decorator.decorateText(result, component);
		}
		return result;
	}
}
