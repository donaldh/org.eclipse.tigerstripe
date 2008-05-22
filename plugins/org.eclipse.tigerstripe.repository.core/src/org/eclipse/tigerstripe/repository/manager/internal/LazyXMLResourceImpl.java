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
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

public class LazyXMLResourceImpl extends XMLResourceImpl {

	public LazyXMLResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected XMLHelper createXMLHelper() {
		return new LazyXMLHelperImpl(this);
	}

	@Override
	protected XMLLoad createXMLLoad() {
		return new LazyXMLLoadImpl(createXMLHelper());
	}

	@Override
	protected XMLSave createXMLSave() {
		return new LazyXMLSaveImpl(createXMLHelper());
	}

	@Override
	public EObject getEObject(String uriFragment) {
		EObject result = super.getEObject(uriFragment);

		if (result == null) {
			// couldn't resolve locally
			System.out.println("trac");
		}
		return result;
	}

	@Override
	protected EObject getEObjectByID(String id) {
		// TODO Auto-generated method stub
		return super.getEObjectByID(id);
	}

	@Override
	protected EObject getEObjectForURIFragmentRootSegment(
			String uriFragmentRootSegment) {
		// TODO Auto-generated method stub
		return super.getEObjectForURIFragmentRootSegment(uriFragmentRootSegment);
	}

}
