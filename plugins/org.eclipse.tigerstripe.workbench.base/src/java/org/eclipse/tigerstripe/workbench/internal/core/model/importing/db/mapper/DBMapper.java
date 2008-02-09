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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.mapper;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModel;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.annotables.AnnotableModelFromDB;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.annotables.DBAnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.annotables.DBAnnotableElementPackage;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.DatabaseSchema;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.DatabaseTable;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class DBMapper extends DBElementMapper {

	private SchemaMapper schemaMapper;
	private DatabaseTableMapper tableMapper;

	public DBMapper(MessageList list, ITigerstripeModelProject targetProject,
			IModelImportConfiguration config) {
		super(list, targetProject, config);
		this.schemaMapper = new SchemaMapper(list, targetProject, config);
		this.tableMapper = new DatabaseTableMapper(list, targetProject, config);
	}

	public AnnotableModel mapToModel(DatabaseSchema[] schemas)
			throws TigerstripeException {
		AnnotableModelFromDB result = new AnnotableModelFromDB(
				getTargetProject(), getConfig());

		for (DatabaseSchema schema : schemas) {
			DBAnnotableElementPackage pack = schemaMapper.mapToPackage(schema);
			result.addAnnotablePackage(pack);

			// map tables in there now
			for (DatabaseTable table : schema.getTables()) {
				DBAnnotableElement elm = tableMapper.mapTableToElement(table);
				elm.setAnnotableElementPackage(pack);
				result.addAnnotableElement(elm);
			}
		}

		// Should we apply anything to that?
		if (getConfig().getReferenceProject() != null) {
			result.applyTargetProjectArtifactTypes(getConfig()
					.getReferenceProject());
		}

		return result;
	}
}
