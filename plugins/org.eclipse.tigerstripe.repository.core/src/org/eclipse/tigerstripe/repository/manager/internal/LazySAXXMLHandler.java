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
import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.SAXXMLHandler;
import org.eclipse.tigerstripe.repository.MetamodelCorePlugin;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;

public class LazySAXXMLHandler extends SAXXMLHandler {

	private NameEncoder encoder = null;

	public LazySAXXMLHandler(XMLResource xmlResource, XMLHelper helper,
			Map<?, ?> options) {
		super(xmlResource, helper, options);
		encoder = new NameEncoder(helper);
		encoder.setHandler(this);
	}

	@Override
	protected void setValueFromId(EObject object, EReference eReference,
			String ids) {
		StringTokenizer st = new StringTokenizer(ids);

		int position = 0;
		while (st.hasMoreTokens()) {
			String idRaw = st.nextToken();

			// Extracting the class/Name here.
			try {
				EObject proxy = encoder.createProxyObject(idRaw, true);
				setFeatureValue(object, eReference, proxy, position++);
			} catch (ModelCoreException e) {
				MetamodelCorePlugin.log(e);
			}
		}
	}

	@Override
	public EFactory getFactoryForPrefix(String prefix) {
		return super.getFactoryForPrefix(prefix);
	}

}
