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
package org.eclipse.tigerstripe.api.plugins;

import org.eclipse.tigerstripe.api.IPluginReference;


/**
 * An extension of IArtifactModel that allows for passing of the PluginRef.
 * 
 * This interface shouod be implemented if the model needs to be made aware of
 * plugin properties.
 * 
 * 
 */
public interface IPluginAwareArtifactModel extends IArtifactModel {
	/**
	 * Initializes the plugin ref for this model.
	 * 
	 * This method is called once right after creation of this object. The
	 * argument corresponds to the current plugin reference.
	 * 
	 * @param pluginRef -
	 *            The current plugin Reference
	 */
	public void setPluginRef(IPluginReference pluginRef);

}
