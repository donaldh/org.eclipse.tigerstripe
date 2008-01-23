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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.xmi;

import java.util.Collection;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotatedMapping;

/**
 * This represents an Annotated Mapping from an XMI loaded model to a set of TS
 * Artifacts.
 * 
 * @author Eric Dillon
 * 
 */
public class XmiAnnotatedMapping implements AnnotatedMapping {

	public class XmiAnnotatedMappingResult implements AnnotatedMappingResult {

	}

	/**
	 * Applies this mapping to the given set of AnnotableElements
	 * 
	 * @param elements
	 * @return
	 * @throws TigerstripeException
	 */
	public AnnotatedMappingResult applyMapping(
			Collection<AnnotableElement> elements) throws TigerstripeException {

		return null;
	}
}
