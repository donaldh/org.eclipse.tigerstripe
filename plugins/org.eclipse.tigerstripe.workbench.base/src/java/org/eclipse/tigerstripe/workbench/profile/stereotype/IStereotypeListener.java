/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.profile.stereotype;

/**
 * Interface for listening to stereotype changes.
 * 
 * @author Yuri Strot
 */
public interface IStereotypeListener {

	/**
	 * Notifies this listener that a stereotype instance was added.
	 * 
	 * @param instance
	 *            added stereotype instance
	 */
	public void stereotypeAdded(IStereotypeInstance instance);

	/**
	 * Notifies this listener that a stereotype instance was removed.
	 * 
	 * @param instance
	 *            removed stereotype instance
	 */
	public void stereotypeRemove(IStereotypeInstance instance);
}
