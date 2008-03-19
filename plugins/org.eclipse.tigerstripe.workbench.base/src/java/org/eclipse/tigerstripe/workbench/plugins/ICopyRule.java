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
 * Base interface for Pluggable Plugin Copy Rule
 * 
 * A copy rule is a global rule that is capabable of copying files/directories
 * from either the containing plugin or the current project to a specified
 * directory location.
 * 
 * @author erdillon
 * @since 2.2.2.1
 */
public interface ICopyRule extends IRule {

	public final static int FROM_PLUGIN = 0;
	public final static int FROM_PROJECT = 1;

	/**
	 * Sets the directory to copy to, relative to target generation directory
	 * 
	 * @param toDirectory
	 */
	public void setToDirectory(String toDirectory);

	/**
	 * Gets the directory that this rule is going to copy to. The directory is
	 * relative to the target generation directory
	 * 
	 * @return
	 */
	public String getToDirectory();

	/**
	 * Sets the copy from. Valid values are {@link #FROM_PLUGIN} or
	 * {@link #FROM_PROJECT}
	 * 
	 * @param from
	 */
	public void setCopyFrom(int from);

	/**
	 * Gets the value of the copyFrom
	 * 
	 * @return
	 */
	public int getCopyFrom();

	/**
	 * Set the fileset match. Expects standard use of "*".
	 * 
	 * @param filesetMatch
	 */
	public void setFilesetMatch(String filesetMatch);

	/**
	 * Returns the fileset match String for this copy rule.
	 * 
	 * @return
	 */
	public String getFilesetMatch();
}
