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
package org.eclipse.tigerstripe.espace.resources.core;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 *
 */
public interface IIndexer {
	
	public void addToIndex(EObject object);
	
	public void removeFromIndex(EObject object);
	
	public void resolve();
	
	public void applyChanges();
	
	public void save();
	
	public void clear();

}
