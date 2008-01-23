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

import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IOssjQuerySpecifics;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.QueryArtifact;
import org.eclipse.tigerstripe.core.model.Tag;
import org.eclipse.tigerstripe.core.model.Type;

public class OssjQuerySpecifics extends OssjArtifactSpecifics implements
		IOssjQuerySpecifics {

	private IType returnedEntityType;

	public OssjQuerySpecifics(AbstractArtifact artifact) {
		super(artifact);
	}

	@Override
	public void build() {
		super.build();
		Tag tag = getArtifact().getFirstTagByName(QueryArtifact.MARKING_TAG);
		if (tag != null) {
			Properties props = tag.getProperties();

			String strType = props.getProperty("return", "");

			if (strType != null && strType.length() != 0)
				setReturnedEntityIType(new Type(strType, "", getArtifact()
						.getArtifactManager()));
		}
	}

	public IType getReturnedEntityIType() {
		return this.returnedEntityType;
	}

	public void setReturnedEntityIType(IType returnedEntityType) {
		this.returnedEntityType = returnedEntityType;
	}

}
