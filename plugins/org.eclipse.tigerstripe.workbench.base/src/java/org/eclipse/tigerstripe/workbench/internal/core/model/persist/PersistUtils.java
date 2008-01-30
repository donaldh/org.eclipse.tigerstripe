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
package org.eclipse.tigerstripe.workbench.internal.core.model.persist;

import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IType;

public class PersistUtils {

	public String getVisibilityAsString(IModelComponent component) {
		if (component == null)
			return "public";
		return component.getVisibility().getLabel();
	}

	public String getReturnType(IMethod method) {
		if (method.isVoid())
			return "void";
		else {
			if (method.getReturnType() == null)
				return "void";
			String multiplicity = "";
			if (method.getReturnType().getTypeMultiplicity().isArray()) {
				multiplicity = "[]";
			}

			return method.getReturnType().getFullyQualifiedName()
					+ multiplicity;
		}
	}
}
