/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    jistrawn (Cisco Systems, Inc.) - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations;

/**
 * Interface for the EnumerationAnnotationLiteral Object stored by
 * IEnumerationAnnotationSpecifications
 * 
 * @author jistrawn
 * 
 */
public interface IAnnotationSpecificationLiteral {

	/**
	 * Returns the literal value for this EnumerationAnnotationSpecification.
	 * 
	 * @return
	 */
	public String getValue();

	/**
	 * The index of the EnumerationAnnotationSpecification literal should be
	 * used to specify the order in which all literals should be represented in
	 * the view.
	 * 
	 * @return
	 */
	public int getIndex();
}
