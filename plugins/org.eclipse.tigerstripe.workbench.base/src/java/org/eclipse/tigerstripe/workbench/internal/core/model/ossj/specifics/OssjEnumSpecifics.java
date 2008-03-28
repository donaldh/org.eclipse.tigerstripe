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
package org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics;

import java.util.Properties;

import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEnumSpecifics;

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
			Type type = (Type) getArtifact().makeField().makeType();
			type.setFullyQualifiedName(fullQual);
			type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
			setBaseType(type);
			String exten = props.getProperty("extensible", "true");
			if (exten.equals("false")) {
				setExtensible(false);
			} else {
				setExtensible(true);
			}

		} else {
			Type type = (Type) getArtifact().makeField().makeType();
			type.setFullyQualifiedName("String");
			type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
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
		Type type = (Type) getArtifact().makeField().makeType();
		type.setFullyQualifiedName("int");
		type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		setBaseType(type);
		setExtensible(true);
	}
}
