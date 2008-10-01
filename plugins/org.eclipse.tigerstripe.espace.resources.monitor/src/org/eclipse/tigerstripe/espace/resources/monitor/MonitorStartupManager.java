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
package org.eclipse.tigerstripe.espace.resources.monitor;

import org.eclipse.tigerstripe.annotation.core.IAnnotationParticipant;

/**
 * @author Yuri Strot
 *
 */
public class MonitorStartupManager implements IAnnotationParticipant {
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationParticipant#initialize()
	 */
	public void initialize() {
		ResourcesMonitor.getInstance();
	}

}
