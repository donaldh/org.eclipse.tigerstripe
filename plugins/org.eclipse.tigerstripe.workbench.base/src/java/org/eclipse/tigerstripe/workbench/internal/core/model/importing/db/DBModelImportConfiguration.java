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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.db;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.BaseModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.database.AbstractDatabaseModel;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class DBModelImportConfiguration extends BaseModelImportConfiguration {

	public DBModelImportConfiguration(ITigerstripeModelProject referenceProject) {
		setReferenceProject(referenceProject);
	}

	private AbstractDatabaseModel dbModel;

	public void setDatabaseModel(AbstractDatabaseModel model) {
		this.dbModel = model;
	}

	public AbstractDatabaseModel getDatabaseModel() {
		return this.dbModel;
	}

	public IModelImportConfiguration make(ITigerstripeModelProject referenceProject) {
		return new DBModelImportConfiguration(referenceProject);
	}

}
