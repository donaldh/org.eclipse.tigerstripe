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
package org.eclipse.tigerstripe.espace.resources;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface IndexList extends EObject {
	
	/**
	 * @model mapType="org.eclipse.emf.ecore.EIndexKeyToStringMapEntry<org.eclipse.tigerstripe.espace.resources.IndexKey, org.eclipse.emf.ecore.EString>"
	 */
	public EMap<IndexKey, String> getIndexPaths();

}
