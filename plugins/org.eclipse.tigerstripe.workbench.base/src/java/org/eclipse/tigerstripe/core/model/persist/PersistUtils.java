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
package org.eclipse.tigerstripe.core.model.persist;

import org.eclipse.tigerstripe.api.artifacts.model.IMethod;
import org.eclipse.tigerstripe.api.artifacts.model.IModelComponent;
import org.eclipse.tigerstripe.api.external.model.IextModelComponent;
import org.eclipse.tigerstripe.api.external.model.IextType;

public class PersistUtils {

	public String getVisibilityAsString(IModelComponent component) {
		if (component == null)
			return "public";

		switch (component.getVisibility()) {
		case IextModelComponent.VISIBILITY_PRIVATE:
			return "private";
		case IextModelComponent.VISIBILITY_PROTECTED:
			return "protected";
		case IextModelComponent.VISIBILITY_PUBLIC:
			return "public";
		case IextModelComponent.VISIBILITY_PACKAGE:
			return "";
		default:
			return "public";
		}
	}

	public String getReturnType(IMethod method) {
		if (method.isVoid())
			return "void";
		else {
			if (method.getReturnIType() == null)
				return "void";
			String multiplicity = "";
			if (method.getReturnIType().getMultiplicity() == IextType.MULTIPLICITY_MULTI) {
				multiplicity = "[]";
			}

			return method.getReturnIType().getFullyQualifiedName()
					+ multiplicity;
		}
	}
}
