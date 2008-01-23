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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.ossjxml;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginRef;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.ossj.OssjUtil;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class XmlType {

	private String name;

	private String keyName;

	private String keyResultName;

	private String dimensions;

	private String fullyQualifiedName;

	private Type type;

	private String namespace;

	private ArtifactManager artifactManager;

	private PluginRef pluginRef;

	public XmlType(String fullyQualifiedName, String dimensions,
			ArtifactManager artifactManager, PluginRef pluginRef)
			throws TigerstripeException {
		this.fullyQualifiedName = fullyQualifiedName;
		this.dimensions = dimensions;
		this.artifactManager = artifactManager;
		this.namespace = "ts";
		this.pluginRef = pluginRef;

		this.type = new Type(fullyQualifiedName, dimensions,
				this.artifactManager);
		build();
	}

	public XmlType(String namespace, String fullyQualifiedName,
			String dimensions, ArtifactManager artifactManager,
			PluginRef pluginRef) throws TigerstripeException {
		this.fullyQualifiedName = fullyQualifiedName;
		this.dimensions = dimensions;
		this.artifactManager = artifactManager;
		this.namespace = namespace;
		this.pluginRef = pluginRef;

		this.type = new Type(fullyQualifiedName, dimensions,
				this.artifactManager);
		build();
	}

	private void build() throws TigerstripeException {
		// This looks All wrong...
		String commonNamespace = this.pluginRef.getProject()
				.getProjectDetails().getProperty("ossj.common.namespacePrefix",
						"");
		// String commonNamespace = "co-v1-3";

		if ("[]".equals(this.dimensions)) {
			this.name = "ArrayOf";
			this.keyName = "ArrayOf";
			this.keyResultName = "ArrayOf";
		} else {
			this.name = "";
			this.keyName = "";
			this.keyResultName = "";
		}
		OssjUtil util = new OssjUtil(this.artifactManager, this.pluginRef);

		if (this.type.isPrimitive()) {
			if (this.name.equals("ArrayOf")) {
				this.name = commonNamespace + ":" + this.name
						+ capitalize(this.type.getFullyQualifiedName());
			} else {
				this.name = this.name + this.type.getFullyQualifiedName();
			}
			this.keyName = "";
			this.keyResultName = "";
		} else if (this.type.isEnum()) {
			this.name = namespace + ":" + this.name + this.type.getName();
			this.keyName = "";
			this.keyResultName = "";
		} else if (this.type.isEntityType() && this.name.equals("ArrayOf")) {
			this.name = namespace
					+ ":"
					+ this.name
					+ capitalize(util.interfaceOf(
							this.type.getFullyQualifiedName()).getName());
			this.keyName = namespace
					+ ":"
					+ this.keyName
					+ capitalize(util.keyInterfaceOf(
							this.type.getFullyQualifiedName()).getName());
			this.keyResultName = namespace
					+ ":"
					+ this.keyResultName
					+ capitalize(util.keyResultInterfaceOf(
							this.type.getFullyQualifiedName()).getName());
		} else if (this.type.isEntityType()) {
			this.name = namespace
					+ ":"
					+ this.name
					+ util.interfaceOf(this.type.getFullyQualifiedName())
							.getName();
			this.keyName = namespace
					+ ":"
					+ this.keyName
					+ util.keyInterfaceOf(this.type.getFullyQualifiedName())
							.getName();
			this.keyResultName = namespace
					+ ":"
					+ this.keyResultName
					+ util.keyResultInterfaceOf(
							this.type.getFullyQualifiedName()).getName();
		} else if (this.type.isArtifact()) {
			this.name = namespace
					+ ":"
					+ this.name
					+ util.interfaceOf(this.type.getFullyQualifiedName())
							.getName();
			this.keyName = "";
			this.keyResultName = "";
		} else if (this.type.isArtifact() && this.name.equals("ArrayOf")) {
			this.name = namespace
					+ ":"
					+ this.name
					+ capitalize(util.interfaceOf(
							this.type.getFullyQualifiedName()).getName());
			this.keyName = "";
			this.keyResultName = "";
		} else {

			// See if the name is mapped...
			// If it is mapped, then we're in the common schema for sure.
			// If it's not we *might* be
			// Really need to look up in the schema to be sure.

			String mappedName = xsdTypeMapping(Util
					.nameOf(this.fullyQualifiedName));

			if (this.name.startsWith("ArrayOf")) {
				// if (Util.nameOf(this.fullyQualifiedName).equals("Calendar")){
				// special case for Calendar...
				// this.name = commonNamespace +":" + this.name +
				// Util.nameOf(this.fullyQualifiedName);

				// } else
				if (!mappedName.equals(Util.nameOf(this.fullyQualifiedName))) {
					this.name = commonNamespace + ":" + this.name
							+ capitalize(mappedName);
				} else {
					this.name = namespace + ":" + this.name + mappedName;
				}
			} else {
				this.name = this.name + mappedName;
			}
			this.keyName = "";
			this.keyResultName = "";
		}

	}

	private String capitalize(String str) {
		return str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}

	public String getPrefix() {
		String prefix = "";
		if (this.name.contains(":")) {
			prefix = this.name.substring(0, this.name.indexOf(":"));
		} else {
			prefix = this.name;
		}
		return prefix;
	}

	public String getName() {
		return this.name;
	}

	public String getNameNoNS() {
		String nameNoNS = "";
		if (this.name.contains(":")) {
			nameNoNS = this.name.substring(this.name.indexOf(":") + 1);
		} else {
			nameNoNS = this.name;
		}
		return nameNoNS;
	}

	public String getKeyName() {
		return this.keyName;
	}

	public String getKeyNameNoNS() {
		String keyNameNoNS = "";
		if (this.keyName.contains(":")) {
			keyNameNoNS = this.keyName.substring(this.keyName.indexOf(":") + 1);
		} else {
			keyNameNoNS = this.keyName;
		}
		return keyNameNoNS;
	}

	public String getKeyResultName() {
		return this.keyResultName;
	}

	public String getKeyResultNameNoNS() {
		String keyResultNameNoNS = "";
		if (this.keyResultName.contains(":")) {
			keyResultNameNoNS = this.keyResultName.substring(this.keyResultName
					.indexOf(":") + 1);
		} else {
			keyResultNameNoNS = this.keyResultName;
		}
		return keyResultNameNoNS;
	}

	public static String xsdTypeMapping(String javaType)
			throws TigerstripeException {

		String baseType = null;

		if ("String".equals(javaType)) {
			baseType = "string";
		} else if ("Char".equals(javaType)) {
			baseType = "string";
			// should also set the length to 1
		} else if ("Date".equals(javaType)) {
			baseType = "dateTime";
		} else if ("Calendar".equals(javaType)) {
			baseType = "dateTime";
		} else if ("BigDecimal".equals(javaType)) {
			baseType = "decimal";
		} else if ("Object".equals(javaType)) {
			baseType = "anyType";
		} else if ("TimeZone".equals(javaType) || "URL".equals(javaType)) {
			baseType = "string";
		} else {
			// TODO: this is necessary for now to have the default
			// primary key to be of string type. Not sure what impact
			// this may have in the future.

			// might it be better to default to String if we don't do anythig
			// special with it ?
			// but things like Name are in there..
			// ideally we could check if this is in the declared namespaces.
			baseType = javaType;
		}

		return baseType;
	}
}