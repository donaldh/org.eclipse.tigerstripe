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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xmi.XmiAnnotatedMapping;

/**
 * A factory class for AnnotatedMappings
 * 
 * @author Eric Dillon
 * 
 */
public class AnnotatedMappingFactory {

	private static AnnotatedMappingFactory instance;

	private AnnotatedMappingFactory() {

	}

	public static AnnotatedMappingFactory getInstance() {
		if (instance == null) {
			instance = new AnnotatedMappingFactory();
		}
		return instance;
	}

	public AnnotatedMapping makeAnnotatedMapping(String mappingType)
			throws TigerstripeException {
		if (XmiAnnotatedMapping.class.getName().equals(mappingType))
			return new XmiAnnotatedMapping();
		throw new TigerstripeException(mappingType + " not supported");
	}

	/**
	 * Loads the given XML file into an Annotated Mapping
	 * 
	 * @param path
	 * @return
	 * @throws TigerstripeException
	 */
	public AnnotatedMapping loadAnnotatedMapping(String path)
			throws TigerstripeException {

		return null;
	}
}
