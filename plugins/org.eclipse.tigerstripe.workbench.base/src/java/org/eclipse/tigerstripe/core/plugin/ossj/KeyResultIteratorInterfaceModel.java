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
package org.eclipse.tigerstripe.core.plugin.ossj;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.plugin.PluginRef;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class KeyResultIteratorInterfaceModel extends KeyInterfaceModel {

	private final static String TEMPLATE = "keyResultIteratorInterface.vm";

	protected KeyResultIteratorInterfaceModel(AbstractArtifact artifact,
			PluginRef pluginRef) throws TigerstripeException {
		super(artifact, pluginRef);
		setTemplate(TEMPLATE);
	}

	@Override
	protected String getDefaultName() {
		String valueName = super.getDefaultName();
		return valueName + "ResultIterator";
	}

	/**
	 * Populates this based on the context.
	 * 
	 * @param manager
	 * @param context
	 * @throws TigerstripeException
	 */
	@Override
	protected void build(ArtifactManager manager) throws TigerstripeException {
		super.build(manager);
	}

}