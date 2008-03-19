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
 * Top level rule interface for Generators
 * 
 * @author erdillon
 * 
 */
public interface IRule {

	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String description);

	public String getLabel();

	public String getType();

	public boolean isEnabled();

	public void setEnabled(boolean enabled);
}
