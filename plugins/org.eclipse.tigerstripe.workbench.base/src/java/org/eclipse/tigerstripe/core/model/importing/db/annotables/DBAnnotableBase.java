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

import org.eclipse.tigerstripe.core.model.importing.BaseAnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.db.schema.DatabaseElement;

public abstract class DBAnnotableBase extends BaseAnnotableElement {

	public DBAnnotableBase(String name) {
		super(name);
	}

	private DatabaseElement correspondingDBElement;

	public DatabaseElement getCorrespondingDBElement() {
		return correspondingDBElement;
	}

	public void setCorrespondingDBElement(DatabaseElement correspondingDBElement) {
		this.correspondingDBElement = correspondingDBElement;
	}
}
