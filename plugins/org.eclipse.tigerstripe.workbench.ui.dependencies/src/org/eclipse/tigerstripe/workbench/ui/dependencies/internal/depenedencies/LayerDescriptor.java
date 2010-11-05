/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies;

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer;


public class LayerDescriptor {

	private final Layer layer;
	private final LayerData layerData;

	public LayerDescriptor(Layer layer, LayerData layerData) {
		this.layer = layer;
		this.layerData = layerData;
	}

	public Layer getLayer() {
		return layer;
	}

	public LayerData getLayerData() {
		return layerData;
	}

}
