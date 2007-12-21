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
package org.eclipse.tigerstripe.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.rendering.IDiagramRenderer;
import org.eclipse.tigerstripe.api.rendering.IDiagramRenderingSession;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;

public class DiagramRenderingSession implements IDiagramRenderingSession {

	private Map<String, IDiagramRenderer> map = new HashMap<String, IDiagramRenderer>();

	public DiagramRenderingSession() {
	}

	public IDiagramRenderer getRendererByName(String rendererName)
			throws TigerstripeException {
		return map.get(rendererName);
	}

	public String[] getSupportedRenderers() {
		return map.keySet().toArray(new String[map.keySet().size()]);
	}

	public void registerRenderer(IDiagramRenderer renderer) {
		TigerstripeRuntime.logInfoMessage(" Registering: "
				+ renderer.getClass().getName());
		map.put(renderer.getClass().getName(), renderer);
	}

	public void unRegisterRenderer(IDiagramRenderer renderer) {
		map.remove(renderer.getClass().getName());
	}

}
