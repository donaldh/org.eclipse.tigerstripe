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

/**
 * @author Yuri Strot
 *
 */
public class TargetAnnotationType {
	
	private AnnotationType type;
	private IAnnotationTarget[] targets;

	/**
	 * @param type
	 * @param targets
	 */
	public TargetAnnotationType(AnnotationType type, IAnnotationTarget[] targets) {
		this.type = type;
		this.targets = targets;
	}
	
	/**
	 * @return the type
	 */
	public AnnotationType getType() {
		return type;
	}
	
	/**
	 * @return the targets
	 */
	public IAnnotationTarget[] getTargets() {
		return targets;
	}

}
