/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.manager.internal;

import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl;
import org.xml.sax.helpers.DefaultHandler;

public class LazyXMLLoadImpl extends XMLLoadImpl {

	public LazyXMLLoadImpl(XMLHelper helper) {
		super(helper);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected DefaultHandler makeDefaultHandler() {
	    return new LazySAXXMLHandler(resource, helper, options);
	}


}
