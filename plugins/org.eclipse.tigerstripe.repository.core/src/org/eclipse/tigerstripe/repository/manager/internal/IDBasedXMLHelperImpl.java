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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl;

//FIXME: this needs to be removed and is only here for reference
public class IDBasedXMLHelperImpl extends XMLHelperImpl {

//	private XMLModelRepositoryImpl modelRepositoryImpl;

	public IDBasedXMLHelperImpl() {
		super();
	}

//	public IDBasedXMLHelperImpl(XMLModelRepositoryImpl resource) {
//		super(resource);
//		this.modelRepositoryImpl = resource;
//	}
//
	@Override
	public URI resolve(URI relative, URI base) {
		// TODO Auto-generated method stub
		return super.resolve(relative, base);
	}

	@Override
	public String getID(EObject obj) {
		return null;
//		return modelRepositoryImpl.getID(obj);
	}

	@Override
	public String getIDREF(EObject obj) {
		String res = "model:classpath#";
//				+ modelRepositoryImpl.getIDHelper().getIDValue(obj);
		return res;
	}

	@Override
	public URI deresolve(URI uri) {
		// TODO Auto-generated method stub
		return super.deresolve(uri);
	}

	@Override
	public String getHREF(EObject obj) {
		String s = super.getHREF(obj);
		return s;
	}

	@Override
	protected URI getHREF(Resource otherResource, EObject obj) {
		URI uri = super.getHREF(otherResource, obj);
		return uri;
	}

}
