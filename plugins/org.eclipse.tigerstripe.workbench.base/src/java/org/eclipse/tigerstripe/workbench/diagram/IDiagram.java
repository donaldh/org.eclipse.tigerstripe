/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - jworrell
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.diagram;

import org.eclipse.core.resources.IFile;

public interface IDiagram {

	public IFile getModelFile();

	public IFile getDiagramFile();

}