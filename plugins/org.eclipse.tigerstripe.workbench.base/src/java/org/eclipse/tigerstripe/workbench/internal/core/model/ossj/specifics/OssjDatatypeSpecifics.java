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
package org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics;

import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjDatatypeSpecifics;
@Deprecated
public class OssjDatatypeSpecifics extends OssjArtifactSpecifics implements
		IOssjDatatypeSpecifics {

	public OssjDatatypeSpecifics(IAbstractArtifactInternal artifact) {
		super(artifact);
	}

	@Override
	public void build() {
		super.build();
	}

}
