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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.gmf.synchronization.DiagramHandle;

/**
 * Interface to be implemented by Diagram Auditors
 * 
 * @author Eric Dillon
 * 
 */
public interface IDiagramAuditor {

	/**
	 * 
	 * @param
	 */
	public IStatus auditDiagram(Diagram diagram, IProgressMonitor monitor)
			throws TigerstripeException;

	/**
	 * 
	 * @param
	 */
	public IStatus auditDiagram(DiagramHandle handle, IProgressMonitor monitor)
			throws TigerstripeException;

	/**
	 * Returns the file extension of the diagram file that this auditor can
	 * audit.
	 * 
	 * @return
	 */
	public String getSupportedFileExtension();

}
