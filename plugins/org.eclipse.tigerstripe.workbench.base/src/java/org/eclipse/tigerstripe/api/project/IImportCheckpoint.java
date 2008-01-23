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
package org.eclipse.tigerstripe.api.project;

import java.util.List;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;

/**
 * An Import checkpoint contains import information about a project It is stored
 * as a CHECKPOINT_FILE (see below) file at the root of the project.
 * 
 * It contains the set of AnnotableElements as they were upon import, including
 * which ones were imported (at the attribute/constant/operation level) and
 * where they were imported from.
 * 
 * Upon following import, this file is used to check whether the model to import
 * has changed or not (i.e. whether an attribute was added or changed somewhere)
 * so that the user can look at imports incrementally, focusing only on the
 * delta.
 * 
 * The main method to use is the listDelta(...) method that compares the
 * checkpoint with a given set of AnnotableElements (those that we're about to
 * import). The result is the delta between the 2, that the user should address.
 * 
 * @author Eric Dillon
 * 
 */
public interface IImportCheckpoint {

	public final static String CHECKPOINT_FILE = ".importCheckpoint";

	/**
	 * Returns details about this checkpoint.
	 * 
	 * The details are specific to the type of checkpoint (import of XMI vs. of
	 * DB e.g.)
	 * 
	 * @return
	 */
	public IImportCheckpointDetails getDetails();

	/**
	 * For each of the given annotables, will populate the Delta flag. This may
	 * potentially add AnnotableElements to the list if they were in the
	 * checkpoint but are not in the given list.
	 * 
	 * @param annotables
	 * @return
	 */
	public void computeDelta(List<AnnotableElement> newAnnotables)
			throws TigerstripeException;

	public String getHumanReadableType();

}
