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
package org.eclipse.tigerstripe.core.model.ossj;

import java.io.Writer;

import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;

public class DependencyArtifactPersister extends AbstractArtifactPersister {

	private final static String TEMPLATE = "org/eclipse/tigerstripe/core/model/ossj/resources/dependency.vm";

	public DependencyArtifactPersister(AbstractArtifact artifact, Writer writer) {
		super(artifact, TEMPLATE, writer);
	}
}
