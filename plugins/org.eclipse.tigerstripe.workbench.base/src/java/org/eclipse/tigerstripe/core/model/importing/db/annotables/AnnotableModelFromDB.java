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
package org.eclipse.tigerstripe.core.model.importing.db.annotables;

import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementPackage;
import org.eclipse.tigerstripe.core.model.importing.BaseAnnotableModel;
import org.eclipse.tigerstripe.core.model.importing.IModelImportConfiguration;

public class AnnotableModelFromDB extends BaseAnnotableModel {

	private int delta = DELTA_UNKNOWN;

	public int getDelta() {
		return this.delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	public AnnotableModelFromDB(ITigerstripeProject targetProject,
			IModelImportConfiguration config) {
		super(targetProject, config);
	}

	public void addAnnotablePackage(AnnotableElementPackage pack) {
		getAnnotablePackageElements().add(pack);
	}

	public void addAnnotableElement(AnnotableElement elm) {
		getAnnotableElements().add(elm);
	}
}
