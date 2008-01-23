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
package org.eclipse.tigerstripe.core.plugin.ossjxml;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.model.Label;
import org.eclipse.tigerstripe.core.plugin.PluginRef;
import org.eclipse.tigerstripe.core.util.VelocityContextUtil;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OssjXmlUtil {

	private ArtifactManager artifactManager;
	private PluginRef pluginRef;

	public OssjXmlUtil(ArtifactManager artifactManager, PluginRef pluginRef) {
		this.artifactManager = artifactManager;
		this.pluginRef = pluginRef;
	}

	/**
	 * 
	 * @param fullyQualifiedName
	 * @param dimension
	 * @return
	 * @throws TigerstripeException
	 * 
	 * @deprecated Please use typeNSOf( namespace, fullyQualifiedName, dimension )
	 *             instead.
	 */
	@Deprecated
	public XmlType typeOf(String fullyQualifiedName, String dimension)
			throws TigerstripeException {
		return new XmlType(fullyQualifiedName, dimension, this.artifactManager,
				this.pluginRef);
	}

	public XmlType typeNSOf(String namespace, String fullyQualifiedName,
			String dimension) throws TigerstripeException {
		XmlType me = new XmlType(namespace, fullyQualifiedName, dimension,
				this.artifactManager, this.pluginRef);
		return me;
	}

	public String valueOfLabel(Label label) {
		if (label.getType().getName().equals("int"))
			return label.getValue();
		else
			return VelocityContextUtil.stripExternalQuotes(label.getValue());
	}

	public String xMLComment(String inString) {
		StringBuffer inBuff = new StringBuffer(inString);
		inBuff = replaceChar(inBuff, "<", "&lt;");
		inBuff = replaceChar(inBuff, ">", "&gt;");
		inBuff = replaceChar(inBuff, "\"", "&quot;");
		return inBuff.toString();
	}

	public StringBuffer replaceChar(StringBuffer inBuff, String what,
			String with) {
		while (inBuff.indexOf(what) != -1) {
			int pos = inBuff.indexOf(what);
			inBuff.deleteCharAt(pos);
			inBuff.insert(pos, with);
		}
		return inBuff;
	}
}