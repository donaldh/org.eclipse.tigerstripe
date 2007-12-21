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
package org.eclipse.tigerstripe.workbench.ui.eclipse.elements;

import org.eclipse.jface.viewers.ILabelProvider;

/**
 * 
 * @author Eric Dillon
 * 
 */
public interface IArtifactLabelProvider extends ILabelProvider {

	public String getName(Object Element);

	public String getPackage(Object Element);

	public Object getProperty(Object Element, String propertyName);

	public void setProperty(Object Element, String propertyName, Object property);
}
