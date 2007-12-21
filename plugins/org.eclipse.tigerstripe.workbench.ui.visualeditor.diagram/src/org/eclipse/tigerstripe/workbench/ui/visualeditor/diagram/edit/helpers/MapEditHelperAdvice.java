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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.helpers;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;

public class MapEditHelperAdvice extends AbstractEditHelperAdvice {

	public MapEditHelperAdvice() {
		super();
	}

	@Override
	protected ICommand getBeforeReorientReferenceRelationshipCommand(
			ReorientReferenceRelationshipRequest request) {
		return UnexecutableCommand.INSTANCE;
	}

	protected ICommand getBeforeReorientRelationshipCommand(
			ReorientReferenceRelationshipRequest request) {
		return UnexecutableCommand.INSTANCE;
	}

}
