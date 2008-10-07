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

/**
 * This interface allows to provide a rendering service for diagrams to be used
 * during generation.
 * 
 * This allows to decouple the plugins that implement/use this service.
 * 
 * @author Eric Dillon
 * 
 */
public interface IDiagramRenderer {

	public final static String JPEG = "jpeg";
	public final static String GIF = "gif";

	public void renderDiagram(String projectLabel, String diagRelPath,
			String imageType, String imagePath) throws TigerstripeException;

	public void renderDiagram(String projectLabel, String diagRelPath,
			String imageType, String outputProjectLabel, String imagePath) throws TigerstripeException;
}
