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
package org.eclipse.tigerstripe.core.model.importing.db.database;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * A factory of all known database models
 * 
 * @author Eric Dillon
 * 
 */
public class DatabaseModelFactory {

	private static DatabaseModelFactory instance;

	private List<AbstractDatabaseModel> knownModels = new ArrayList<AbstractDatabaseModel>();

	public static DatabaseModelFactory getInstance() {
		if (instance == null) {
			instance = new DatabaseModelFactory();
			instance.registerKnownModels();
		}
		return instance;
	}

	private void registerKnownModels() {
		knownModels.add(new Oracle10gModel());
	}

	public String[] getKnownDatabaseModels() {
		List<String> result = new ArrayList<String>();
		for (AbstractDatabaseModel model : knownModels) {
			result.add(model.getLabel());
		}
		String[] arr = new String[result.size()];
		return result.toArray(arr);
	}

	public AbstractDatabaseModel make(String databaseModelLabel)
			throws TigerstripeException {
		for (AbstractDatabaseModel model : knownModels) {
			if (model.getLabel().equals(databaseModelLabel))
				return model.make();
		}
		throw new TigerstripeException("Unknown database ("
				+ databaseModelLabel + ")");
	}
}
