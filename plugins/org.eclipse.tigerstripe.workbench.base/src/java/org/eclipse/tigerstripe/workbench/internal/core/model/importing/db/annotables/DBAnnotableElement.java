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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.annotables;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementAttribute;

public class DBAnnotableElement extends DBAnnotableBase implements
		AnnotableElement {

	public DBAnnotableElement(String name) {
		super(name);
	}

	public DBAnnotableElementAttribute getAnnotableElementAttributeByName(
			String name) {
		for (AnnotableElementAttribute att : getAnnotableElementAttributes()) {
			if (att.getName().equals(name))
				return (DBAnnotableElementAttribute) att;
		}
		return null;
	}
}
