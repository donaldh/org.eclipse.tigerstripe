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
package org.eclipse.tigerstripe.core.model.importing.db.mapper;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.core.model.importing.db.annotables.DBAnnotableElementPackage;
import org.eclipse.tigerstripe.core.model.importing.db.schema.DatabaseSchema;
import org.eclipse.tigerstripe.core.util.messages.MessageList;

public class SchemaMapper extends DBElementMapper {

	public SchemaMapper(MessageList messageList,
			ITigerstripeProject targetProject, IModelImportConfiguration config) {
		super(messageList, targetProject, config);
	}

	public DBAnnotableElementPackage mapToPackage(DatabaseSchema schema)
			throws TigerstripeException {

		DBAnnotableElementPackage pack = new DBAnnotableElementPackage(schema
				.getName());
		return pack;
	}
}
