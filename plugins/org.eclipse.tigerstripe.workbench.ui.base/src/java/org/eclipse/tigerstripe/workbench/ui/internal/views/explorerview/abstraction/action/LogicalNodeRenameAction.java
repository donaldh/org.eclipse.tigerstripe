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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.action;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.dialog.LogicalNodePromptForNameDialog;

public class LogicalNodeRenameAction extends AbstractLogicalNodeAction {

	private final static String CHECKJOB_NAME = "logicalNodeAction.checkRenameJob";

	private final static String JOB_NAME = "logicalNodeAction.renameJob";

	private boolean shouldPerformRename = false;

	private String newName = null;

	public LogicalNodeRenameAction(String text, Shell shell) {
		super(text, shell);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean checkInternalRun(IProgressMonitor monitor) {
		getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				AbstractLogicalExplorerNode[] nodes = getSelectedNodes();
				if (nodes.length != 0) {
					LogicalNodePromptForNameDialog dialog = new LogicalNodePromptForNameDialog(
							getShell(), nodes[0], "Rename Element",
							"Enter new name for this element.", nodes[0]
									.getKeyResource().getParent());
					shouldPerformRename = dialog.open() == Window.OK;
					if (shouldPerformRename) {
						newName = dialog.getNewName();
					}
				}
			}
		});
		return shouldPerformRename;
	}

	@Override
	protected String getCheckJobName() {
		return CHECKJOB_NAME;
	}

	@Override
	protected String getJobName() {
		return JOB_NAME;
	}

	@Override
	protected ISchedulingRule getSchedulingRule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTooltipText() {
		return "Rename this element.";
	}

	@Override
	protected void internalRun(IProgressMonitor monitor)
			throws TigerstripeException, CoreException {
		AbstractLogicalExplorerNode[] nodes = getSelectedNodes();
		if (nodes.length != 0) {
			nodes[0].performRename(newName, monitor);
		}
	}

}
