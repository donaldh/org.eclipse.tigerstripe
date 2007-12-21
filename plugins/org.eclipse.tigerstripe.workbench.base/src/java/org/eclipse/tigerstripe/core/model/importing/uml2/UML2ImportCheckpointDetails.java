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
package org.eclipse.tigerstripe.core.model.importing.uml2;

import org.eclipse.tigerstripe.core.model.importing.AbstractImportCheckpointDetails;

public class UML2ImportCheckpointDetails extends
		AbstractImportCheckpointDetails {

	public UML2ImportCheckpointDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getType() {
		return UML2ImportCheckpoint.TYPE;
	}

	@Override
	public String getVersion() {
		return UML2ImportCheckpoint.VERSION;
	}

}
