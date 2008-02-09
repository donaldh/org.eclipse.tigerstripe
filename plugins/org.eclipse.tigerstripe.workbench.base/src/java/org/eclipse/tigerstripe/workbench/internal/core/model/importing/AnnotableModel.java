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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

import java.util.Collection;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * An abstract annotated model.
 * 
 * 
 * @author Eric Dillon
 * 
 */
public interface AnnotableModel extends Annotable {

	/**
	 * Returns a list of annotableElements from this model
	 * 
	 * @return
	 */
	public Collection<AnnotableElement> getAnnotableElements();

	/**
	 * Returns a list of annotableElements from this model
	 * 
	 * @return
	 */
	public Collection getAnnotablePackageElements();

	/**
	 * Returns a list of annotableElements from this model
	 * 
	 * @return
	 */
	public Collection getAnnotableDatatypes();

	/**
	 * Returns a list of annotableEnumerations from this model
	 */
	public Collection getAnnotableEnumerations();

	/**
	 * Applies the setting found in the given reference project to the
	 * annotables of this model.
	 * 
	 */
	public void applyTargetProjectArtifactTypes(
			ITigerstripeModelProject referenceProject);
}
