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
package org.eclipse.tigerstripe.workbench.model.artifacts;

import org.eclipse.tigerstripe.workbench.model.IModelComponent;

public interface IPrimitiveTypeArtifact extends IAbstractArtifact,
		IModelComponent {

	public final static String RESERVED = "<reserved>";

	public final static String DEFAULT_LABEL = "Primitive";

	public final static String[][] reservedPrimitiveTypes = {
			{ "int", "Reserved int primitive Type." },
			{ "char", "Reserved char primitive Type." },
			{ "boolean", "Reserved boolean primitive Type." },
			{ "byte", "Reserved byte primitive Type." },
			{ "double", "Reserved double primitive Type." },
			{ "float", "Reserved float primitive Type." },
			{ "long", "Reserved long primitive Type." },
			{ "short", "Reserved short primitive Type." },
			{ "void", "Reserved void primitive Type." } };

}
