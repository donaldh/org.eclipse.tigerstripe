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
package org.eclipse.tigerstripe.api.artifacts.model;

import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationClassArtifact;

/**
 * Internal Interface for IAssociationArtifact
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IAssociationClassArtifact extends IAssociationArtifact,
		IextAssociationClassArtifact {

	public final static String DEFAULT_LABEL = "AssociationClass";

	/**
	 * Make a new blank artifact field
	 * 
	 * @return
	 */
	public IField makeIField();

	/**
	 * Sets the fields for this Abstract Artifact
	 * 
	 * @param fields
	 * @throws IllegalArgumentException
	 */
	public void setIFields(IField[] fields);

	public void addIField(IField field);

	public void removeIFields(IField[] fields);

}
