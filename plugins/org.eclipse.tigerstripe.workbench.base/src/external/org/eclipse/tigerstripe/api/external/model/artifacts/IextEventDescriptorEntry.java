/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.api.external.model.artifacts;

/**
 * This class represents a Event Descriptor Entry. An Event Descriptor is used
 * to identify fields of primitive type from within the (potentially complex
 * type) attributes of an event. This allows for the defintion of filterable
 * properties for example.
 * 
 */
public interface IextEventDescriptorEntry {

	/**
	 * Returns the label for the Entry.
	 * 
	 * @return String - the Label of the Entry
	 */
	public String getLabel();

	/**
	 * Returns a String of the name of the primitive type of the Entry.
	 * 
	 * 
	 * @return String - the name of teh primitive type
	 */
	public String getPrimitiveType();

}
