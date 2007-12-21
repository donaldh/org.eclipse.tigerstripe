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
package org.eclipse.tigerstripe.contract;

import java.net.URI;

import org.dom4j.Element;
import org.eclipse.tigerstripe.api.contract.IContract;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.versioning.VersionAwareElement;

/**
 * 
 * @author Eric Dillon
 * 
 */
public class Contract extends VersionAwareElement implements IContract {

	private final static String ELEMENT_TAG = "contract";

	public Contract(URI uri) {
		super(uri);
	}

	@Override
	protected void appendBody(Element rootElement) throws TigerstripeException {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getElementTag() {
		return ELEMENT_TAG;
	}

	@Override
	protected void parseBody(Element rootElement) throws TigerstripeException {
		// TODO Auto-generated method stub

	}

}
