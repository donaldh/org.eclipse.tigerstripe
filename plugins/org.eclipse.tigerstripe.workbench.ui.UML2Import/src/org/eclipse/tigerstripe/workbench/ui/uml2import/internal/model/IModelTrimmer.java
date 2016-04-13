/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

/**
 * This interface is used by the importer to optionally trim
 * a model prior to the import
 * @author rcraddoc
 *
 */
public interface IModelTrimmer {

	/**
	 * Take the modelToTrim, remove elements, and return the new Model.
	 * 
	 * The returned Model needs to be  a valid model in its own right.
	 * @param modelToTrim
	 * @return
	 */
	public org.eclipse.uml2.uml.Package trimModel(org.eclipse.uml2.uml.Package modelToTrim);	
}
