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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.ui.actions.SelectionListenerAction;

/**
 * For each action 2 jobs are scheduled: - a check job that checks the action
 * can be run - a job that will perform the actual action itself
 * 
 * @author Eric Dillon
 * 
 */
public abstract class AbstractLogicalNodeAction extends SelectionListenerAction {

	/**
	 * The shell in which to show any dialogs.
	 */
	private Shell shell;

	/**
	 * The tooltip text to use for this action
	 * 
	 * @return
	 */
	protected abstract String getTooltipText();

	/**
	 * The check job name for this action
	 * 
	 * @return
	 */
	protected abstract String getCheckJobName();

	/**
	 * The job name for this action
	 * 
	 * @return
	 */
	protected abstract String getJobName();

	public AbstractLogicalNodeAction(String text, Shell shell) {
		super(text);
		if (shell == null)
			throw new IllegalArgumentException();
		this.shell = shell;
	}

	/**
	 * This method is called before the action is scheduled. This is scheduled
	 * in the checkJob
	 * 
	 * @return true - if the action can be run
	 */
	protected abstract boolean checkInternalRun(IProgressMonitor monitor);

	/**
	 * This method is to executed the action itself. This is scheduled in the
	 * job
	 * 
	 */
	protected abstract void internalRun(IProgressMonitor monitor)
			throws TigerstripeException, CoreException;

	/**
	 * Runs the command in a job
	 */
	@Override
	public final void run() {
		Job actionJob = new Job(getCheckJobName()) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
			 */
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				if (!checkInternalRun(monitor))
					return Status.CANCEL_STATUS;

				scheduleJob();
				return Status.OK_STATUS;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
			 */
			@Override
			public boolean belongsTo(Object family) {
				if (getCheckJobName().equals(family))
					return true;
				return super.belongsTo(family);
			}
		};

		actionJob.schedule();
	}

	protected AbstractLogicalExplorerNode[] getSelectedNodes() {
		List<?> orig = super.getSelectedNonResources();
		List<AbstractLogicalExplorerNode> result = new ArrayList<AbstractLogicalExplorerNode>();
		for (Iterator<?> iter = orig.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof AbstractLogicalExplorerNode) {
				result.add((AbstractLogicalExplorerNode) obj);
			}
		}
		return result.toArray(new AbstractLogicalExplorerNode[result.size()]);
	}

	protected void scheduleJob() {
		// use a non-workspace job with a runnable inside so we can avoid
		// periodic updates
		Job internalJob = new Job(getJobName()) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				try {
					ResourcesPlugin.getWorkspace().run(
							new IWorkspaceRunnable() {
								public void run(IProgressMonitor monitor)
										throws CoreException {
									try {
										internalRun(monitor);
									} catch (TigerstripeException ee) {
										Status status = new Status(
												IStatus.ERROR, EclipsePlugin
														.getPluginId(), 222, ee
														.getLocalizedMessage(),
												ee);
										throw new CoreException(status);
									}
								}
							}, null, IWorkspace.AVOID_UPDATE, monitor);
				} catch (CoreException e) {
					return e.getStatus();
				}
				return Status.OK_STATUS;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
			 */
			@Override
			public boolean belongsTo(Object family) {
				if (getJobName().equals(family))
					return true;
				return super.belongsTo(family);
			}

		};
		internalJob.setRule(getSchedulingRule());
		internalJob.setUser(true);
		internalJob.schedule();
	}

	/**
	 * 
	 * @return
	 */
	protected abstract ISchedulingRule getSchedulingRule();

	/**
	 * Ask the user whether the given resource should be deleted despite being
	 * out of sync with the file system.
	 * 
	 * @param resource
	 *            the out of sync resource
	 * @return One of the IDialogConstants constants indicating which of the
	 *         Yes, Yes to All, No, Cancel options has been selected by the
	 *         user.
	 */
	protected int queryDeleteOutOfSync(IResource resource, String messageTitle,
			String outOfSyncQuestion) {
		final MessageDialog dialog = new MessageDialog(shell, messageTitle,
				null, NLS.bind(outOfSyncQuestion, resource.getName()),
				MessageDialog.QUESTION, new String[] {
						IDialogConstants.YES_LABEL,
						IDialogConstants.YES_TO_ALL_LABEL,
						IDialogConstants.NO_LABEL,
						IDialogConstants.CANCEL_LABEL }, 0);
		shell.getDisplay().syncExec(new Runnable() {
			public void run() {
				dialog.open();
			}
		});
		int result = dialog.getReturnCode();
		if (result == 0)
			return IDialogConstants.YES_ID;
		if (result == 1)
			return IDialogConstants.YES_TO_ALL_ID;
		if (result == 2)
			return IDialogConstants.NO_ID;
		return IDialogConstants.CANCEL_ID;
	}

	protected Shell getShell() {
		return this.shell;
	}

}
