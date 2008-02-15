/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
/**
 * 
 */
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;

public class PojoResourceFactoryImpl extends ResourceFactoryImpl implements
		Factory {

	public PojoResourceFactoryImpl() {
		super();
	}

	@Override
	public Resource createResource(URI uri) {
		PojoResourceImpl impl = new PojoResourceImpl();
		impl.setURI(uri);
		return impl;
	}

}