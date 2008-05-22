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

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl;

public class LazyXMLSaveImpl extends XMLSaveImpl {

	public LazyXMLSaveImpl(XMLHelper helper) {
		super(helper);
	}

	public LazyXMLSaveImpl(Map<?, ?> options, XMLHelper helper, String encoding) {
		super(options, helper, encoding);
	}

	public LazyXMLSaveImpl(Map<?, ?> options, XMLHelper helper,
			String encoding, String xmlVersion) {
		super(options, helper, encoding, xmlVersion);
	}

	@Override
	protected void saveHref(EObject remote, EStructuralFeature f) {
		// TODO Auto-generated method stub
		super.saveHref(remote, f);
	}

	@Override
	protected void saveHRefMany(EObject o, EStructuralFeature f) {
		// TODO Auto-generated method stub
		super.saveHRefMany(o, f);
	}

	@Override
	protected void saveHRefSingle(EObject o, EStructuralFeature f) {
		// TODO Auto-generated method stub
		super.saveHRefSingle(o, f);
	}

}
