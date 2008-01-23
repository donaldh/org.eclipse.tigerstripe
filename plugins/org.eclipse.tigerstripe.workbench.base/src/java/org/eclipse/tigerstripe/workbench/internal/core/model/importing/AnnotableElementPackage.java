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

import java.util.List;

/**
 * A package of AnnotableElements
 * 
 * It may contain AnnotableElements and AnnotableElementPackages
 * 
 * @author Eric Dillon
 * 
 */
public interface AnnotableElementPackage extends Annotable {

	public List getAnnotableElements();

	public List getAnnotableElementPackages();

	public String getFullyQualifiedName();
}
