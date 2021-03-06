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
package org.eclipse.tigerstripe.repository.metamodel.pojo.writer;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;

public class EventArtifactWriter extends AbstractArtifactWriter {

	private final static String TEMPLATE = "org/eclipse/tigerstripe/repository/metamodel/pojo/writer/resources/event.vm";

	public EventArtifactWriter() {
		this(null);
	}

	public EventArtifactWriter(IAbstractArtifact artifact) {
		super(artifact);
		setTemplate(TEMPLATE);
	}

}
