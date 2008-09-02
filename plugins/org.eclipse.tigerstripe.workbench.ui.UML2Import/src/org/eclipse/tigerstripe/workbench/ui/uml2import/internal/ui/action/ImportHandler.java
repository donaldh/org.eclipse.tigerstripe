/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.action;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.commands.ExecutionException;

public class ImportHandler extends AbstractHandler implements IHandler {

	
	private final String MODEL    = "org.eclipse.tigerstripe.workbench.ui.UML2Import.importModel";
	private final String PROFILE  = "org.eclipse.tigerstripe.workbench.ui.UML2Import.importProfile";
	
	public Object execute(ExecutionEvent event)
			throws org.eclipse.core.commands.ExecutionException {
		Command command = event.getCommand();
		String id = command.getId();
		
		if (id.equals(MODEL)){
			OpenUML2ImportModelWizardAction importModelWizardAction = new OpenUML2ImportModelWizardAction();
			importModelWizardAction.run();
		} else if (id.equals(PROFILE)){
			OpenUML2ImportProfileWizardAction importProfileWizardAction = new OpenUML2ImportProfileWizardAction();
			importProfileWizardAction.run();
		}

		return null;
	}

	

}
