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
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.tigerstripe.repository.manager.IllegalEObjectException;
import org.eclipse.tigerstripe.repository.manager.KeyService;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;

/**
 * The purpose of the name Encoder is simply to encode/decode the name of
 * classes in the HRefs and IDs in the XML so that the type of the object, and
 * its name is encoded.
 * 
 * @author erdillon
 * 
 */
public class NameEncoder {

	public final static String SCHEME = "repository";

	private XMLHelper helper = null;
	private LazySAXXMLHandler handler = null;

	public NameEncoder(XMLHelper helper) {
		this.helper = helper;
	}

	public void setHandler(LazySAXXMLHandler handler) {
		// Note, the handler is only set when loading documents
		this.handler = handler;
	}

	public URI getRepositoryURI(EObject obj) {
		try {
			Object key = KeyService.INSTANCE.getKey(obj);
			String type = helper.getQName(obj.eClass());
			URI uri = URI.createGenericURI(SCHEME, type, (String) key);
			return uri;
		} catch (ModelCoreException e) {
			throw new IllegalEObjectException(e.getMessage(), e);
		}
	}

	public EObject createProxyObject(String str, boolean setID)
			throws ModelCoreException {
		URI uri = URI.createURI(str);
		return createProxyObject(uri, setID);
	}

	public EObject createProxyObject(URI uri, boolean setID)
			throws ModelCoreException {

		if (!"repository".equals(uri.scheme())) {
			throw new ModelCoreException("Can't decode: '" + uri.toString()
					+ "'.");
		}

		String name = uri.fragment();
		String qname = uri.opaquePart();
		String typeName = qname;
		String prefix = "";
		if (qname.indexOf(":") != -1) {
			typeName = qname.substring(qname.indexOf(":") + 1);
			prefix = qname.substring(0, qname.indexOf(":"));
		}

		// Decode EClass from encoded
		org.eclipse.emf.ecore.EFactory eFactory = handler
				.getFactoryForPrefix(prefix);
		EClass eClass = (EClass) helper.getType(eFactory, typeName);
		InternalEObject obj = (InternalEObject) eFactory.create(eClass);

		if (setID) {
			EAttribute idAttr = eClass.getEIDAttribute();
			if (idAttr != null) {
				obj.eSet(idAttr, name);
			}
		}
		obj.eSetProxyURI(uri);
		return obj;
	}
}
