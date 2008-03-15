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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.plugins.IPluginRuleExecutor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class M0GlobalRule extends M0LevelRule {

	public final static String LABEL = "Global Instance Rule";

	@Override
	public void buildBodyFromNode(Node node) {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getBodyAsNode(Document document) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException {

	}

}
