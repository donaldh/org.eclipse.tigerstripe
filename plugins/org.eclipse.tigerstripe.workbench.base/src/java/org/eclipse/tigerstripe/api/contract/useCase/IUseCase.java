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
package org.eclipse.tigerstripe.api.contract.useCase;

import org.eclipse.tigerstripe.api.versioning.IVersionAwareElement;

/**
 * 
 * @author Eric Dillon
 * 
 */
public interface IUseCase extends IVersionAwareElement {

	public final static String FILE_EXTENSION = "ucd";

	public final static String DEFAULT_USECASE_FILE = "default" + "."
			+ FILE_EXTENSION;

	public String getBody();

	public void setBody(String body);

	public String getXslRelPath();

	public void setXslRelPath(String xslRelPath);

	public boolean bodyIsXML();

	public void setBodyIsXML(boolean bodyIsXML);

	public String getBodySchemaOrDTD();

	public void setBodySchemaOrDTD(String bodySchemaOrDTD);
}
