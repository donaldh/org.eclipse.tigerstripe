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
package org.eclipse.tigerstripe.workbench.internal.api.project;

import java.util.Collection;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * 
 * Tigerstripe Workbench uses a "phantom" project to store any convenient
 * artifact instance that is to be referenced accross an installation. For
 * example, primitive type definitions are all stored in this "phantom" project
 * and put in the path of any project (behind the scenes, i.e. the end users
 * DON't KNOW)
 * 
 * There is only 1 Phantom project per instance, it's content cannot be
 * controled directly, it is populated based on the active profile. Each time
 * the active profile is reloaded, the content of the phantom project is
 * updated.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IPhantomTigerstripeProject extends ITigerstripeModelProject {

	public Collection<IPrimitiveTypeArtifact> getReservedPrimitiveTypes()
			throws TigerstripeException;
}
