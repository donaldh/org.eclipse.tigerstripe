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
package org.eclipse.tigerstripe.workbench.plugins;

/**
 * Top-level interface for file generating rules
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IFileGeneratingRule extends IRule {

	public boolean isSuppressEmptyFiles();

	public void setSuppressEmptyFiles(boolean suppress);

	public boolean isOverwriteFiles();

	public void setOverwriteFiles(boolean overwriteFiles);

}
