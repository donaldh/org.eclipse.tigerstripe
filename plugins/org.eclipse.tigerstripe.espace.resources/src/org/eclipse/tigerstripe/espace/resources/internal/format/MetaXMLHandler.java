/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.espace.resources.internal.format;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;

/**
 * @author Yuri Strot
 *
 */
public class MetaXMLHandler extends SAXXMIHandler {

	/**
	 * @param xmiResource
	 * @param helper
	 * @param options
	 */
	public MetaXMLHandler(XMLResource xmiResource, XMLHelper helper,
			Map<?, ?> options) {
		super(xmiResource, helper, options);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#handleXSINoNamespaceSchemaLocation(java.lang.String)
	 */
	@Override
	protected void handleXSINoNamespaceSchemaLocation(
			String noNamespaceSchemaLocation) {
		super.handleXSINoNamespaceSchemaLocation(noNamespaceSchemaLocation);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void startElement(String uri, String localName, String name) {
		if (IFormatConstants.ECORE_DEFINITION_TAG.equals(name)) {
			//ignore
		}
		else {
			super.startElement(uri, localName, name);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String name) {
		if (IFormatConstants.ECORE_DEFINITION_TAG.equals(name)) {
			for (EObject object : extent) {
				if (object instanceof EPackage) {
					EPackage pack = (EPackage)object;
					String nsURI = pack.getNsURI();
					if (packageRegistry.getEPackage(nsURI) == null) {
						packageRegistry.put(nsURI, pack);
					}
				}
			}
			documentRoot = null;
			extent.clear();
		}
		else {
			super.endElement(uri, localName, name);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMIHandler#processElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void processElement(String name, String prefix, String localName) {
		super.processElement(name, prefix, localName);
	}

}
