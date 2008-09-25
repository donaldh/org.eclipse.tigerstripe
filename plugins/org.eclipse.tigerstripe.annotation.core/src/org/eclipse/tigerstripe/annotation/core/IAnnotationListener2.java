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
package org.eclipse.tigerstripe.annotation.core;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.espace.core.Mode;

/**
 * An annotation listener that gets informed when resources added or removed 
 *
 * @author Yuri Strot
 * @since 0.4
 */
public interface IAnnotationListener2 extends IAnnotationListener {
	
	/**
     * Notifies this listener that a resource was added.
     * 
	 * @param annotation added annotation
	 */
	public void annotationsAdded(Resource resource, Mode option);
	
	/**
     * Notifies this listener that a resource was removed.
     * 
	 * @param annotations removed annotations
	 */
	public void annotationsRemoved(Resource resource);
	
	/**
     * Notifies this listener that a resource was unregistered.
     * 
	 * @param annotations removed annotations
	 */
	public void annotationsUnregistered(Resource resource);

}
