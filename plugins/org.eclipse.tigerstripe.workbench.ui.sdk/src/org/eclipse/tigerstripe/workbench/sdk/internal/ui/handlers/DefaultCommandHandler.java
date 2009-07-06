/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.tigerstripe.workbench.sdk.internal.LocalContributions;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.actions.OpenCreatePatternFileWizardAction;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.SDKEditorInput;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class DefaultCommandHandler extends AbstractHandler implements IHandler {

	private final String CREATE_PATTERN_FILE = "org.eclipse.tigerstripe.sdk.createPatternFile";
	private final String EDITOR_OPEN = "org.eclipse.tigerstripe.sdk.openEditor";

	public final static String CONFIG_EDITOR = "org.eclipse.tigerstripe.sdk.editor";

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Command command = event.getCommand();
		String id = command.getId();

		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		if (id.equals(CREATE_PATTERN_FILE)) {
			OpenCreatePatternFileWizardAction createPatternAction = new OpenCreatePatternFileWizardAction();
			createPatternAction.run(null);

		}

		if (id.equals(EDITOR_OPEN)) {
			LocalContributions locals = new LocalContributions();
			// MessageDialog.openInformation(window.getShell(),
			// "Tigerstripe Configuration Editor",
			// "Loading Extension Point Configurations");
			// TODO - Show a Progrees Monirtor Dialog...
			locals.findAll();
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				SDKEditorInput input = new SDKEditorInput();
				input.setProvider(locals);
				page.openEditor(input, CONFIG_EDITOR);
			} catch (PartInitException e) {
				EclipsePlugin.log(e);
			}
		}

		return null;
	}

}
