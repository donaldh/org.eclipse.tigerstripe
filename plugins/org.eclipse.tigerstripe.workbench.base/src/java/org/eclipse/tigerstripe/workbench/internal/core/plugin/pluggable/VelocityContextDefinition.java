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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable;

import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.BaseTemplatePPluginRule;

import com.sun.corba.se.spi.ior.MakeImmutable;

/**
 * A velocity context definition as it appears in a Pluggable Plugin project
 * 
 * @author Eric Dillon
 * 
 */
public class VelocityContextDefinition {

	private String name;
	private String className;
	private BaseTemplatePPluginRule containingRule;

	public void setContainingRule(BaseTemplatePPluginRule rule) {
		this.containingRule = rule;
	}

	private void markDirty() {
		if (containingRule != null)
			containingRule.markProjectDirty();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty();
	}

	public String getClassname() {
		return this.className;
	}

	public void setClassname(String classname) {
		this.className = classname;
		markDirty();
	}
}
