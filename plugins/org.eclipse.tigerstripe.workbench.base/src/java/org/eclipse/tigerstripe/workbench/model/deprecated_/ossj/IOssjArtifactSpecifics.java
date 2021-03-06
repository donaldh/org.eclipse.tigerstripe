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
package org.eclipse.tigerstripe.workbench.model.deprecated_.ossj;

import java.util.Properties;

public interface IOssjArtifactSpecifics extends IStandardSpecifics {

	public void setInterfaceProperties(Properties interfaceProperties);

	public void mergeInterfaceProperties(Properties interfaceProperties);

	public void applyDefaults();

	/**
	 * Returns the interface properties for an OSSJ Artifact
	 * 
	 * @return
	 */
	public Properties getInterfaceProperties();
}
