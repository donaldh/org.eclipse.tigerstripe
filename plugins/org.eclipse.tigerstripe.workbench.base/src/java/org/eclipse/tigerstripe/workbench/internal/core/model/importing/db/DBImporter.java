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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModel;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModelImporter;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportResult;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImporterListener;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.database.AbstractDatabaseModel;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.mapper.DBMapper;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.DatabaseSchema;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class DBImporter implements AnnotableModelImporter {

	// FIXME homogenize all importer properly!
	public ModelImportResult importFromURI(String uri, MessageList list,
			ModelImporterListener listener, IModelImportConfiguration config,
			ITigerstripeModelProject targetProject) {
		// TODO Auto-generated method stub
		return null;
	}

	public ModelImportResult importFromDB(MessageList list,
			ModelImporterListener listener, IModelImportConfiguration config,
			ITigerstripeModelProject targetProject) {

		ModelImportResult result = new ModelImportResult();
		DBModelImportConfiguration dbConfig = (DBModelImportConfiguration) config;
		AbstractDatabaseModel dbModel = dbConfig.getDatabaseModel();

		try {
			DatabaseSchema[] schemas = dbModel.extractSchemas(list,
					targetProject, config, listener);

			listener.importBeginTask("Creating annotable model", 5);
			DBMapper mapper = new DBMapper(list, targetProject, config);
			listener.worked(1);
			AnnotableModel model = mapper.mapToModel(schemas);
			listener.importDone();
			result.setModel(model);
		} catch (TigerstripeException e) {
			result.setImportException(new ModelImportException(
					"Error during schema mapping", e));
		}
		return result;
	}
}
