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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

public class ConditionalCreateCommand extends CreateElementCommand {

	public ConditionalCreateCommand(CreateElementRequest req) {
		super(req);
	}

	@Override
	public boolean canExecute() {
		EObject container = ((CreateElementRequest) getRequest())
				.getContainer();
		if (((QualifiedNamedElement) container).isIsReadonly())
			return false;
		// TODO Auto-generated method stub
		return super.canExecute();
	}

}
