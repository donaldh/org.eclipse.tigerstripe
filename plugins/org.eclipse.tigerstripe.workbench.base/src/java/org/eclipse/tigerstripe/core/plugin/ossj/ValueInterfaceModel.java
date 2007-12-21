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

import java.util.Properties;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.model.Tag;
import org.eclipse.tigerstripe.core.plugin.PluginRef;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ValueInterfaceModel extends OssjInterfaceModel {

	private final static String TEMPLATE = "valueInterface.vm";
	public final static String VALUE_TAG = "tigerstripe.value";

	private KeyInterfaceModel keyIntfModel;

	public ValueInterfaceModel(AbstractArtifact artifact, PluginRef pluginRef)
			throws TigerstripeException {
		super(artifact, pluginRef);
		setTemplate(TEMPLATE);
	}

	@Override
	protected String getDefaultName() {
		return getArtifact().getName() + "Value";
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

		// Read the tigerstripe.interface tag
		Tag intfTag = getArtifact().getFirstTagByName(VALUE_TAG);
		if (intfTag != null) {
			Properties prop = intfTag.getProperties();
			setPackage(prop.getProperty("package", getDefaultPackage()));
			setName(prop.getProperty("name", getDefaultName()));
			setGenerate(prop.getProperty("generate", "true"));
		}

		this.keyIntfModel = new KeyInterfaceModel(getArtifact(), getPluginRef());
	}

	public KeyInterfaceModel getKeyInterface() {
		return this.keyIntfModel;
	}
}