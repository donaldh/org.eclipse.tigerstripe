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
package org.eclipse.tigerstripe.api.versioning;

import java.io.Reader;
import java.net.URI;

import org.eclipse.tigerstripe.api.TigerstripeException;

/**
 * Base element for Tigerstripe. Has a name, and a description. It can be saved
 * to a specified URI and parsed from a Reader.
 * 
 * @author Eric Dillon
 * 
 */
public interface IBaseElement {

	public final static String DEFAULT_NAME = "NO_NAME";
	public final static String DEFAULT_DESCRIPTION = "NO_DESCRIPTION";

	/**
	 * Returns the user visible name of this Element
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Sets the user visible name of this Element
	 * 
	 * @param name
	 */
	public void setName(String name);

	/**
	 * Returns the user description of this Element
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Sets the description name of this Element
	 * 
	 * @param name
	 */
	public void setDescription(String description);

	public void setURI(URI uri) throws TigerstripeException;

	public URI getURI();

	public void doSave() throws TigerstripeException;

	public void doSaveAs(URI uri) throws TigerstripeException;

	public void parse(Reader reader) throws TigerstripeException;

	public String asText() throws TigerstripeException;

}
