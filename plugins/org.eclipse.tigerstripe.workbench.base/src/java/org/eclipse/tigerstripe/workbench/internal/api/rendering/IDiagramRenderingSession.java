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
package org.eclipse.tigerstripe.workbench.internal.api.rendering;

import org.eclipse.tigerstripe.workbench.TigerstripeException;

public interface IDiagramRenderingSession {

	public String[] getSupportedRenderers();

	public void registerRenderer(IDiagramRenderer renderer);

	public void unRegisterRenderer(IDiagramRenderer renderer);

	public IDiagramRenderer getRendererByName(String rendererName)
			throws TigerstripeException;
}
