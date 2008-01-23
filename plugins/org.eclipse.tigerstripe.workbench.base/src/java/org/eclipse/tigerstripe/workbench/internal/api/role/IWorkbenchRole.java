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
package org.eclipse.tigerstripe.workbench.internal.api.role;

/**
 * Top-level interface for Tigerstripe workbench roles.
 * 
 * Workbench roles allow to identify what functionalities are available within a
 * Tigerstripe install, ranging from using Tigerstripe workbench, to configuring
 * it, creating new plugins, etc...
 * 
 * Available roles that have been purchased are encoded in the license key. A
 * Workbench site admin can then create restricted licenses where roles are
 * restricted for other users within an organization. By doing so, the admin can
 * control what features of Workbench each individual user can use.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IWorkbenchRole {

}
