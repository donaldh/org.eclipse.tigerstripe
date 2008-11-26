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

import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;

/**
 * Top-level interface for Template rules in a Pluggable Plugin
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface ITemplateBasedRule extends IFileGeneratingRule {

	public String getTemplate();

	public void setTemplate(String template);

	public String getOutputFile();

	public void setOutputFile(String outputFile);

	public VelocityContextDefinition[] getVelocityContextDefinitions();

	public void addVelocityContextDefinition(VelocityContextDefinition def);

	public void removeVelocityContextDefinition(VelocityContextDefinition def);

	public void addVelocityContextDefinitions(VelocityContextDefinition[] def);

	public void removeVelocityContextDefinitions(VelocityContextDefinition[] def);

	public String[] getMacroLibraries();

	public void addMacroLibrary(String library);

	public void removeMacroLibrary(String library);

	public void addMacroLibraries(String[] library);

	public void removeMacroLibraries(String[] library);

	public boolean hasMacroLibrary();

}
