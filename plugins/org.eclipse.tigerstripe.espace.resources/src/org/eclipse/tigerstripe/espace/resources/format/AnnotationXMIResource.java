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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.tigerstripe.espace.resources.internal.format.PackageManager;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationXMIResource extends XMIResourceImpl {
	
	public AnnotationXMIResource(URI uri) {
		super(uri);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl#createXMLLoad()
	 */
	@Override
	protected XMLLoad createXMLLoad() {
	    return new AnnotationXMILoad(createXMLHelper());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl#createXMLSave()
	 */
	@Override
	protected XMLSave createXMLSave() {
		return new AnnotationXMISave(createXMLHelper(), PackageManager.getInstance().getPackageFinder());
	}

}
