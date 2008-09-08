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
package org.eclipse.tigerstripe.espace.resources.format;

import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.SAXWrapper;
import org.eclipse.emf.ecore.xmi.impl.XMILoadImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Yuri Strot
 *
 */
@SuppressWarnings("deprecation")
public class AnnotationXMILoad extends XMILoadImpl {

	/**
	 * @param helper
	 */
	public AnnotationXMILoad(XMLHelper helper) {
		super(helper);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMILoadImpl#makeDefaultHandler()
	 */
	@Override
	protected DefaultHandler makeDefaultHandler() {
		return new SAXWrapper(new AnnotationXMLHandler(resource, helper, options));
	}

}
