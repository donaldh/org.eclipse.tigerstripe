/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.metamodel.providers;

import java.net.URL;

/**
 * 	This interface is to be implemented by clients to register their custom 
 * @author erdillon
 *
 */
public interface IModelComponentIconProvider {

	public URL getIconURL(Object context);

	public URL getGreyedoutIconURL(Object context);

	public URL getCreationToolIconURL(Object context);

	public String getCreationToolLabel(Object context);

}
