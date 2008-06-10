/******************************************************************************* 
 * 
 * Copyright (c) 2008 Cisco Systems, Inc. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *    Cisco Systems, Inc. - dkeysell
********************************************************************************/
package org.eclipse.tigerstripe.generators.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;

import org.eclipse.tigerstripe.generators.models.AbstractClassModel;
import org.eclipse.tigerstripe.generators.models.ModelFactory;


public class CastUtils {

	private LegacyFilter legacyFilter = new LegacyFilter();

	private IPluginConfig pluginRef = null;


	public Collection toModel(Collection<IAbstractArtifact> artifacts){
		ArrayList<AbstractClassModel> outCollection = new ArrayList<AbstractClassModel>();
		for (IAbstractArtifact artifact : artifacts){
			if (legacyFilter.select(artifact)){
				AbstractClassModel model = ModelFactory.getInstance().getModel(artifact);
				if (model != null){
					model.setPluginRef(pluginRef);
					outCollection.add( model);
				}
			}
		}
		return outCollection;
	}	

	public void setPluginRef(IPluginConfig pluginRef) {
		this.pluginRef = pluginRef;
	}

}
