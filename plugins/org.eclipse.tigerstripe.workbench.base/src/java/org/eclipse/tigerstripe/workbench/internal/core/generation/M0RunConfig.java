/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * All the configuration details to drive a M0 run
 * 
 * @author erdillon
 * 
 */
public class M0RunConfig extends RunConfig {

	/**
	 * Use M0Generator.defaultRunConfig(ITigerstripeModelProject project) as a
	 * factory to get all the defaults from a given project
	 */
	/* package */M0RunConfig() {
		super(null); // making the compiler happy
	}

	/* package */M0RunConfig(ITigerstripeModelProject project) {
		super(project);
	}

	// FIXME: once the metamodel is ready this will be a proper EMF object
	private Object instanceMap;

	public Object getInstanceMap() {
		return instanceMap;
	}

	public void setInstanceMap(Object instanceMap) {
		this.instanceMap = instanceMap;
	}

	@Override
	protected void initializeFromProject() throws TigerstripeException {
		super.initializeFromProject();
		setPluginConfigs(M0GenerationUtils.m0PluginConfigs(getTargetProject(),
				false, true));
	}
}
