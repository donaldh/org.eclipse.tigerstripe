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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

public class LogicalNodeDeleteAction extends AbstractLogicalNodeAction {

	private final static String ACTION_ID = "logicalNodeAction.delete";

	private final static String CHECKJOB_NAME = "logicalNodeAction.checkDeleteJob";

	private final static String JOB_NAME = "logicalNodeAction.deleteJob";

	/**
	 * Whether or not to automatically delete out of sync resources
	 */
	private boolean forceOutOfSyncDelete = false;

	public LogicalNodeDeleteAction(Shell shell) {
		super(ACTION_ID, shell);
	}

	@Override
	protected boolean checkInternalRun(IProgressMonitor monitor) {
		return true;
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
		IResourceRuleFactory ruleFactory = ResourcesPlugin.getWorkspace()
				.getRuleFactory();
		List<IResource> res = getActualResources();
		IResource[] resourcesToDelete = res.toArray(new IResource[res.size()]);
		ISchedulingRule combinedRule = null;
		for (int i = 0; i < resourcesToDelete.length; i++) {
			IResource resource = resourcesToDelete[i];
			ISchedulingRule deleteRule = ruleFactory.deleteRule(resource);
			if (combinedRule == null) {
				combinedRule = deleteRule;
			} else {
				combinedRule = MultiRule.combine(combinedRule, deleteRule);
			}
		}
		return combinedRule;
	}

	@Override
	protected String getTooltipText() {
		return "Delete this element.";
	}

	@Override
	protected void internalRun(IProgressMonitor monitor)
			throws TigerstripeException, CoreException {
		List<IResource> res = getActualResources();
		IResource[] resourcesToDelete = res.toArray(new IResource[res.size()]);
		delete(resourcesToDelete, monitor);
	}

	/**
	 * Returns a list of the actual resources to delete on behalf of this
	 * Logical node
	 * 
	 * @return
	 */
	protected List getActualResources() {
		List orig = super.getSelectedNonResources();
		List result = new ArrayList();
		for (Iterator iter = orig.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof AbstractLogicalExplorerNode) {
				AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) obj;
				result.addAll(Arrays.asList(node.getUnderlyingResources()));
			}
		}
		return result;
	}

	/**
	 * Creates and returns a result status appropriate for the given list of
	 * exceptions.
	 * 
	 * @param exceptions
	 *            The list of exceptions that occurred (may be empty)
	 * @return The result status for the deletion
	 */
	private IStatus createResult(List exceptions) {
		if (exceptions.isEmpty())
			return Status.OK_STATUS;
		final int exceptionCount = exceptions.size();
		if (exceptionCount == 1)
			return ((CoreException) exceptions.get(0)).getStatus();
		CoreException[] children = (CoreException[]) exceptions
				.toArray(new CoreException[exceptionCount]);
		boolean outOfSync = false;
		for (int i = 0; i < children.length; i++) {
			if (children[i].getStatus().getCode() == IResourceStatus.OUT_OF_SYNC_LOCAL) {
				outOfSync = true;
				break;
			}
		}
		String title = outOfSync ? "Resource is out of sync with the file system. Refresh and try again."
				: "Multiple problems occurred while deleting resources.";

		final MultiStatus multi = new MultiStatus(
				IDEWorkbenchPlugin.IDE_WORKBENCH, 0, title, null);
		for (int i = 0; i < exceptionCount; i++) {
			CoreException exception = children[i];
			IStatus status = exception.getStatus();
			multi.add(new Status(status.getSeverity(), status.getPlugin(),
					status.getCode(), status.getMessage(), exception));
		}
		return multi;
	}

	/**
	 * Deletes the given resources.
	 */
	private void delete(IResource[] resourcesToDelete, IProgressMonitor monitor)
			throws CoreException {
		final List exceptions = new ArrayList();
		forceOutOfSyncDelete = false;
		monitor.beginTask("", resourcesToDelete.length); //$NON-NLS-1$
		try {
			for (int i = 0; i < resourcesToDelete.length; ++i) {
				if (monitor.isCanceled())
					throw new OperationCanceledException();
				try {
					delete(resourcesToDelete[i], new SubProgressMonitor(
							monitor, 1,
							SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK));
				} catch (CoreException e) {
					exceptions.add(e);
				}
			}
			IStatus result = createResult(exceptions);
			if (!result.isOK())
				throw new CoreException(result);
		} finally {
			monitor.done();
		}
	}

	/**
	 * Deletes the given resource.
	 */
	private void delete(IResource resourceToDelete, IProgressMonitor monitor)
			throws CoreException {
		try {
			resourceToDelete.delete(IResource.KEEP_HISTORY, monitor);
		} catch (CoreException exception) {
			if (resourceToDelete.getType() == IResource.FILE) {
				IStatus[] children = exception.getStatus().getChildren();

				if (children.length == 1
						&& children[0].getCode() == IResourceStatus.OUT_OF_SYNC_LOCAL) {
					if (forceOutOfSyncDelete) {
						resourceToDelete.delete(IResource.KEEP_HISTORY
								| IResource.FORCE, monitor);
					} else {
						int result = queryDeleteOutOfSync(
								resourceToDelete,
								"Delete Element",
								resourceToDelete.getName()
										+ " is out of sync with the filesystem.\nDo you want to delete anyway?");

						if (result == IDialogConstants.YES_ID) {
							resourceToDelete.delete(IResource.KEEP_HISTORY
									| IResource.FORCE, monitor);
						} else if (result == IDialogConstants.YES_TO_ALL_ID) {
							forceOutOfSyncDelete = true;
							resourceToDelete.delete(IResource.KEEP_HISTORY
									| IResource.FORCE, monitor);
						} else if (result == IDialogConstants.CANCEL_ID)
							throw new OperationCanceledException();
					}
				} else
					throw exception;
			} else
				throw exception;
		}
	}

}
