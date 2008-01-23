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

		switch (component.getVisibility()) {
		case IModelComponent.VISIBILITY_PRIVATE:
			return "private";
		case IModelComponent.VISIBILITY_PROTECTED:
			return "protected";
		case IModelComponent.VISIBILITY_PUBLIC:
			return "public";
		case IModelComponent.VISIBILITY_PACKAGE:
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
			if (method.getReturnIType().getMultiplicity() == IType.MULTIPLICITY_MULTI) {
				multiplicity = "[]";
			}

			return method.getReturnIType().getFullyQualifiedName()
					+ multiplicity;
		}
	}
}
