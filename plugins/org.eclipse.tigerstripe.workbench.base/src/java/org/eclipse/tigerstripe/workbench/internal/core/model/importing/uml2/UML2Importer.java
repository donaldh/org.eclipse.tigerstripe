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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModel;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModelImporter;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportResult;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImporterListener;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.mapper.UML2Mapper;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class UML2Importer implements AnnotableModelImporter {

	// FIXME homogenize all importer properly!
	public ModelImportResult importFromURI(String uri, MessageList list,
			ModelImporterListener listener, IModelImportConfiguration config,
			ITigerstripeModelProject targetProject) {
		// TODO Auto-generated method stub
		return null;
	}

	public ModelImportResult importFromUML2(MessageList list,
			ModelImporterListener listener, IModelImportConfiguration config,
			ITigerstripeModelProject targetProject) {
		ModelImportResult result = new ModelImportResult();
		try {
			listener.importBeginTask("Creating annotable model", 5);
			UML2Mapper mapper = new UML2Mapper(list, targetProject, config);
			AnnotableModel model = mapper.mapToModel(listener);
			listener.importDone();
			result.setModel(model);
		} catch (TigerstripeException e) {
			result.setImportException(new ModelImportException(
					"Error during UML2 import", e));
		}
		return result;
	}

}
