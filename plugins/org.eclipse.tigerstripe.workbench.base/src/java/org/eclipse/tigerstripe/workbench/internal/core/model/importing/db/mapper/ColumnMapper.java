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
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.annotables.DBAnnotableDatatype;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.annotables.DBAnnotableElementAttribute;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.TableColumn;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public class ColumnMapper extends DBElementMapper {

	private DBTypeMapper typeMapper;

	public ColumnMapper(MessageList messageList,
			ITigerstripeProject targetProject, IModelImportConfiguration config) {
		super(messageList, targetProject, config);
		try {
			this.typeMapper = new DBTypeMapper(messageList, targetProject,
					config);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	public DBAnnotableElementAttribute mapToAttribute(TableColumn column) {
		DBAnnotableElementAttribute result = new DBAnnotableElementAttribute();
		result.setName(column.getName());

		if (typeMapper != null) {// this is a hack because the exeption is
			// not handled correctly in creator
			result.setType(typeMapper.mapToType(column));
		} else {
			result.setType(new DBAnnotableDatatype());
		}

		return result;
	}
}
