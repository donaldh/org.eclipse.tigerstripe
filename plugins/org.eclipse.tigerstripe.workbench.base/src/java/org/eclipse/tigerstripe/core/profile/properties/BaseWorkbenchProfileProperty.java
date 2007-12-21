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
package org.eclipse.tigerstripe.core.profile.properties;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * Some convenience methods for Workbench Properties
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public abstract class BaseWorkbenchProfileProperty {

	protected Document documentFromString(String serializedString)
			throws TigerstripeException {
		Document document = null;
		StringReader reader = new StringReader(serializedString);

		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(reader);

		} catch (DocumentException e) {
			throw new TigerstripeException("Couldn't parse content: "
					+ e.getMessage(), e);
		}
		return document;
	}
}
