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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.xmi;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AbstractImportCheckpointDetails;

public class XmiImportCheckpointDetails extends AbstractImportCheckpointDetails {

	public XmiImportCheckpointDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getType() {
		return XmiImportCheckpoint.TYPE;
	}

	@Override
	public String getVersion() {
		return XmiImportCheckpoint.VERSION;
	}

}
