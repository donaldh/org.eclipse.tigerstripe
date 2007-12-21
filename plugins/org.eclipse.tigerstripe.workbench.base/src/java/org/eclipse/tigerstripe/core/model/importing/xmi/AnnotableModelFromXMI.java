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
package org.eclipse.tigerstripe.core.model.importing.xmi;

import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.importing.BaseAnnotableModel;
import org.eclipse.tigerstripe.core.model.importing.IModelImportConfiguration;

public class AnnotableModelFromXMI extends BaseAnnotableModel {

	private int delta = DELTA_UNKNOWN;

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	public AnnotableModelFromXMI(ITigerstripeProject targetProject,
			IModelImportConfiguration config) {
		super(targetProject, config);
	}
}
