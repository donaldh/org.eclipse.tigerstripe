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
package org.eclipse.tigerstripe.espace.core.tree.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Yuri Strot
 *
 */
public class DefaultContentViewer implements IContentViewer {
	
	private EStructuralFeature feature;
	
	public DefaultContentViewer(EStructuralFeature feature) {
		this.feature = feature;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.core.tree.util.IContentViewer#getText(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public String getText(EObject object) {
		Object value = object.eGet(feature);
		if (value == null)
			return "null";
		return value.toString();
	}

}
