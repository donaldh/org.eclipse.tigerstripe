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
package org.eclipse.tigerstripe.workbench.model.deprecated_;

public interface IPrimitiveTypeArtifact extends IAbstractArtifact,
		IModelComponent {

	public final static String RESERVED = "<reserved>";

	public final static String[][] reservedPrimitiveTypes = {
			{ "int", "Reserved int primitive Type.", "^[-+]?[0-9]*" },
			{ "char", "Reserved char primitive Type.", ".{1}" },
			{ "boolean", "Reserved boolean primitive Type.", "1|0|true|false" },
			{ "byte", "Reserved byte primitive Type.", "^[-+]?[0-9]*" },
			{ "double", "Reserved double primitive Type.",
					"^[-+]?[0-9]*\\.?[0-9]+$" },
			{ "float", "Reserved float primitive Type.",
					"^[-+]?[0-9]*\\.?[0-9]+$" },
			{ "long", "Reserved long primitive Type.", "^[-+]?[0-9]*" },
			{ "short", "Reserved short primitive Type.", "^[-+]?[0-9]*" },
			{ "void", "Reserved void primitive Type.", null } };

}
