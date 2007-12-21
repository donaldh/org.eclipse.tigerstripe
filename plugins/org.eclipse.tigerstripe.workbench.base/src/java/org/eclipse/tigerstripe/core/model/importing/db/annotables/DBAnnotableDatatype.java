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
package org.eclipse.tigerstripe.core.model.importing.db.annotables;

import org.eclipse.tigerstripe.core.model.importing.AnnotableDatatype;

public class DBAnnotableDatatype extends DBAnnotableBase implements
		AnnotableDatatype {

	public DBAnnotableDatatype(String name) {
		super(name);
	}

	public DBAnnotableDatatype() {
		super("unknown");
	}
}
