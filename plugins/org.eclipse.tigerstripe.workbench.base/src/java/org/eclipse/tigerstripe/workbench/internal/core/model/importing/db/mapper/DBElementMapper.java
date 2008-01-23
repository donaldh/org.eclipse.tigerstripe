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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.mapper;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public abstract class DBElementMapper {

	private ITigerstripeProject targetProject;
	private MessageList messageList;
	private IModelImportConfiguration config;

	public DBElementMapper(MessageList messageList,
			ITigerstripeProject targetProject, IModelImportConfiguration config) {
		this.messageList = messageList;

		if (messageList == null) {
			messageList = new MessageList();
		}
		this.targetProject = targetProject;
		this.config = config;
	}

	protected ITigerstripeProject getTargetProject() {
		return this.targetProject;
	}

	protected IModelImportConfiguration getConfig() {
		return this.config;
	}

	protected MessageList getMessageList() {
		return this.messageList;
	}
}
