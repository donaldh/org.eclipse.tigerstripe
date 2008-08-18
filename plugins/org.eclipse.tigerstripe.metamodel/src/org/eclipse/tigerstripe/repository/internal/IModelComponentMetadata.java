/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.internal;

import java.net.URL;

import org.eclipse.tigerstripe.repository.metamodel.providers.IModelComponentIconProvider;

public interface IModelComponentMetadata {

	public URL getIconURL(Object context);

	public URL getGreyedoutIconURL(Object context);

	public URL getNewIconURL(Object context);

	public String getLabel(Object context);

	public void setIconProvider(IModelComponentIconProvider provider);

	public void setNewIconURL(URL url);

	public void setGreyedoutIconURL(URL url);

	public void setIconURL(URL url);
}
