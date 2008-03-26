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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.FolderSelectionDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.dialog.LogicalNodePromptForNameDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class LogicalNodeCopyAction extends AbstractLogicalNodeAction {

	private final static String CHECKJOB_NAME = "logicalNodeAction.checkCopyJob";

	private final static String JOB_NAME = "logicalNodeAction.copyJob";

	private boolean shouldPerformCopy = false;

	private IContainer targetContainer = null;

	private String newName;

	class NodeCopyDetails {
		AbstractLogicalExplorerNode node;
		String targetName;
		IContainer targetContainer;
	}

	public LogicalNodeCopyAction(String text, Shell shell) {
		super(text, shell);
	}

	private boolean isValidTargetProject(IContainer cont, IResource keyResource) {
		IProject targetProject = cont.getProject();
		IProject sourceProject = keyResource.getProject();

		try {
			if (targetProject.equals(sourceProject))
				return true;

			for (IProject ref : targetProject.getReferencedProjects()) {
				if (ref.equals(sourceProject))
					return true;
			}
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
		return false;
	}

	@Override
	protected boolean checkInternalRun(IProgressMonitor monitor) {
		getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				AbstractLogicalExplorerNode[] nodes = getSelectedNodes();
				if (nodes.length != 0) {
					final IResource keyRes = nodes[0].getKeyResource();
					final IProject proj = keyRes.getProject();
					ILabelProvider lp = new WorkbenchLabelProvider();
					ITreeContentProvider cp = new WorkbenchContentProvider();

					FolderSelectionDialog di = new FolderSelectionDialog(
							getShell(), lp, cp);
					di.setTitle("Copy element...");
					di.setAllowMultiple(false);
					di.setDoubleClickSelects(true);
					di.setValidator(new ISelectionStatusValidator() {
						public IStatus validate(Object[] selection) {
							if (selection.length != 0) {
								IContainer cont = (IContainer) selection[0];
								IPath tentativePath = cont
										.getProjectRelativePath().append(
												keyRes.getName());
								if (isValidTargetProject(cont, keyRes)
										&& !tentativePath.equals(keyRes
												.getProjectRelativePath()))
									return new Status(
											IStatus.OK,
											TigerstripePluginConstants.PLUGIN_ID,
											222, "", null);
							}
							IStatus result = new Status(IStatus.ERROR,
									TigerstripePluginConstants.PLUGIN_ID, 222,
									"Invalid location.", null);
							return result;
						}
					});
					di.addFilter(new ViewerFilter() {
						@Override
						public boolean select(Viewer viewer,
								Object parentElement, Object element) {

							IResource res = (IResource) element;
							if (res.getProject() != proj)
								return false;
							if (element instanceof IFolder) {
								IFolder folder = (IFolder) element;
								if ("bin".equals(folder.getName())
										|| ".settings".equals(folder.getName()))
									return false;

								return true;
							} else if (element instanceof IProject)
								return true;
							return false;
						}

					});
					di.setInput(proj.getParent());
					di.open();
					if (di.getReturnCode() == Window.OK) {
						Object[] objs = di.getResult();
						if (objs.length != 0 && objs[0] instanceof IContainer) {
							shouldPerformCopy = true;
							targetContainer = (IContainer) objs[0];
						}
					}
				}
			}
		});
		return shouldPerformCopy;
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
		return "Copy this element.";
	}

	@Override
	protected void internalRun(IProgressMonitor monitor)
			throws TigerstripeException, CoreException {
		AbstractLogicalExplorerNode[] nodes = getSelectedNodes();
		if (nodes.length != 0) {
			nodes[0].performMove(targetContainer, monitor);
		}
	}

	public void runBatch(final AbstractLogicalExplorerNode[] nodes,
			final IContainer targetContainer) {
		// use a non-workspace job with a runnable inside so we can avoid
		// periodic updates
		Job internalBatchJob = new Job(getJobName()) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				try {
					ResourcesPlugin.getWorkspace().run(
							new IWorkspaceRunnable() {
								public void run(IProgressMonitor monitor)
										throws CoreException {
									try {
										internalBatchRun(nodes,
												targetContainer, monitor);
									} catch (TigerstripeException ee) {
										Status status = new Status(
												IStatus.ERROR,
												TigerstripePluginConstants.PLUGIN_ID,
												222, ee.getLocalizedMessage(),
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
		internalBatchJob.setRule(getSchedulingRule());
		internalBatchJob.setUser(true);
		internalBatchJob.schedule();
	}

	private void internalBatchRun(AbstractLogicalExplorerNode[] nodes,
			final IContainer targetContainer, IProgressMonitor monitor)
			throws TigerstripeException {
		for (AbstractLogicalExplorerNode node : nodes) {
			final AbstractLogicalExplorerNode fNode = node;
			newName = null;
			shouldPerformCopy = false;
			IResource keyRes = node.getKeyResource();
			IPath tentativePath = targetContainer.getProjectRelativePath()
					.append(keyRes.getName());
			IResource tentativeResource = targetContainer.findMember(keyRes
					.getName());
			if (tentativePath.equals(keyRes.getProjectRelativePath())
					|| (tentativeResource != null && tentativeResource.exists())) {

				// In this we need to duplicate the diagram, so we need to
				// prompt
				// for a new name
				getShell().getDisplay().syncExec(new Runnable() {
					public void run() {
						LogicalNodePromptForNameDialog dialog = new LogicalNodePromptForNameDialog(
								getShell(), fNode, "Duplicate Element",
								"Enter new name for this element.", targetContainer);
						dialog.setInitialName("CopyOf" + fNode.getText());
						shouldPerformCopy = dialog.open() == Window.OK;
						if (shouldPerformCopy) {
							newName = dialog.getNewName();
						}
					}
				});
			} else {
				shouldPerformCopy = true;
				newName = node.getText();
			}

			if (shouldPerformCopy) {
				try {
					node.performCopy(newName, targetContainer, monitor);
				} catch (CoreException e) {
					new TigerstripeException("while performing copy: "
							+ e.getLocalizedMessage(), e);
				}
			}
		}
	}
}
