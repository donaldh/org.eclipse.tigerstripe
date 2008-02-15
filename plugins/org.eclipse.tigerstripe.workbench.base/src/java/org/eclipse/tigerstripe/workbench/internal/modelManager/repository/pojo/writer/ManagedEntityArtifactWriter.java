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
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.writer;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;

public class ManagedEntityArtifactWriter extends AbstractArtifactWriter {

	private final static String TEMPLATE = "org/eclipse/tigerstripe/workbench/internal/modelManager/repository/pojo/writer/resources/managedEntity.vm";

	public ManagedEntityArtifactWriter() {
		this(null);
	}

	public ManagedEntityArtifactWriter(IAbstractArtifact artifact) {
		super(artifact);
		setTemplate(TEMPLATE);
	}

}
