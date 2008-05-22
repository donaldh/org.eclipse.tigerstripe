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
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl;

public class LazyXMLHelperImpl extends XMLHelperImpl {

	private NameEncoder nameEncoder = null;

	public LazyXMLHelperImpl() {
		nameEncoder = new NameEncoder(this);
	}

	public LazyXMLHelperImpl(XMLResource resource) {
		super(resource);
		nameEncoder = new NameEncoder(this);
	}

	@Override
	public String getHREF(EObject obj) {
		String href = "[" + getQName(obj.eClass()) + "]:" + super.getHREF(obj);
		return href;
	}

	@Override
	protected URI getHREF(Resource otherResource, EObject obj) {
		URI uri = nameEncoder.getRepositoryURI(obj);
		return uri;
	}

	@Override
	public String getID(EObject obj) {
		String id = super.getID(obj);
		return id;
	}

	@Override
	public String getIDREF(EObject obj) {
		return nameEncoder.getRepositoryURI(obj).toString();
	}

}
