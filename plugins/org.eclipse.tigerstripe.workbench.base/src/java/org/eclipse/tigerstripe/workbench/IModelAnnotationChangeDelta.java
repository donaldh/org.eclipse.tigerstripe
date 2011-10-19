/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.Annotation;

/**
 * 	
 * @author erdillon
 *
 */
public interface IModelAnnotationChangeDelta {

	public final int CHANGED = 0;
	public final int ADD = 1;
	public final int REMOVE = 2;
	public final int LOADED = 3;
	
	public final int UNKNOWN = -1;


	/**
	 * Returns the type of the model change
	 * 
	 * @return one of {@value #SET}, {@value #ADD}, {@value #REMOVE},
	 *         {@value #UNKNOWN}
	 */
	public int getType();

	/**
	 * The Model component URI on which the delta has occured
	 * 
	 * @author erdillon
	 * 
	 */
	public URI getAffectedModelComponentURI();

	public Annotation getAnnotation();
}
