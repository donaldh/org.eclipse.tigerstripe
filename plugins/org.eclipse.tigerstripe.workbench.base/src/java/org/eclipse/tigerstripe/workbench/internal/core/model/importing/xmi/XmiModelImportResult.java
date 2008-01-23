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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.xmi;

import java.net.URI;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportResult;

/**
 * The result of an XMI load.
 * 
 * It contains the actually loaded file (after potential XSLs transforms)
 * 
 * If successful load, it will contain an AnnotableModel
 * 
 * 
 * @author Eric Dillon
 * 
 */
public class XmiModelImportResult extends ModelImportResult {

	private URI actuallyLoadedURI;

	public void setActuallyLoadedURI(URI uri) {
		this.actuallyLoadedURI = uri;
	}

	public URI getActuallyLoadedURI() {
		return this.actuallyLoadedURI;
	}
}
