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
package org.eclipse.tigerstripe.core.model.ossj.specifics;

import java.util.Properties;

import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.api.external.model.IextType;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.EnumArtifact;
import org.eclipse.tigerstripe.core.model.Tag;
import org.eclipse.tigerstripe.core.model.Type;

public class OssjEnumSpecifics extends OssjArtifactSpecifics implements
		IOssjEnumSpecifics {

	private Type baseType;

	public void setBaseType(Type baseType) {
		this.baseType = baseType;
	}

	public Type getBaseType() {
		return this.baseType;
	}

	private boolean extensible;

	public void setExtensible(boolean extensible) {
		this.extensible = extensible;
	}

	public boolean getExtensible() {
		return this.extensible;
	}

	public OssjEnumSpecifics(AbstractArtifact artifact) {
		super(artifact);
	}

	@Override
	public void build() {
		super.build();

		// Extract the base type
		// Extract Extensibility flag
		Tag tag = getArtifact().getFirstTagByName(EnumArtifact.MARKING_TAG);
		if (tag != null) {
			Properties props = tag.getProperties();
			String fullQual = props.getProperty("base-type", "String");
			Type type = (Type) getArtifact().makeIField().makeIType();
			type.setFullyQualifiedName(fullQual);
			type.setMultiplicity(IextType.MULTIPLICITY_SINGLE);
			setBaseType(type);
			String exten = props.getProperty("extensible", "true");
			if (exten.equals("false")) {
				setExtensible(false);
			} else {
				setExtensible(true);
			}

		} else {
			Type type = (Type) getArtifact().makeIField().makeIType();
			type.setFullyQualifiedName("String");
			type.setMultiplicity(IextType.MULTIPLICITY_SINGLE);
			setBaseType(type);
			setExtensible(true);
		}
	}

	public IType getBaseIType() {
		return getBaseType();
	}

	public void setBaseIType(IType type) {
		setBaseType((Type) type);
	}

	@Override
	public void applyDefaults() {
		super.applyDefaults();
		Type type = (Type) getArtifact().makeIField().makeIType();
		type.setFullyQualifiedName("int");
		type.setMultiplicity(IextType.MULTIPLICITY_SINGLE);
		setBaseType(type);
		setExtensible(true);
	}
}
