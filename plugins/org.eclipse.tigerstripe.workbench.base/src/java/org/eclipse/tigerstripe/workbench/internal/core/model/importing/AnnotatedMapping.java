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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

/**
 * An Annotated Mapping that can be applied to a set of annotable to turn them
 * into Tigerstripe Artifacts
 * 
 * @author Eric Dillon
 * @since 1.0.4
 */
public interface AnnotatedMapping {

	/**
	 * The result of applying an AnnotatedMapping to a set of AnnotableElements
	 * 
	 * @author Eric Dillon
	 * 
	 */
	public interface AnnotatedMappingResult {

	}

	// public IAbstractArtifact setMappingDetails( AnnotableElement anElement,
	// IMappingDetails details ) throw TigerstripeException {
	//		
	// }
}
