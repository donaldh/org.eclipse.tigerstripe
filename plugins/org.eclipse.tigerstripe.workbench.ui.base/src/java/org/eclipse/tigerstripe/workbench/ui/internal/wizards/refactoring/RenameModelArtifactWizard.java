/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramChangeDelta;
import org.eclipse.tigerstripe.workbench.refactor.IRefactorCommand;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class RenameModelArtifactWizard extends Wizard implements
		IModelRefactorWizard {

	private IStructuredSelection selection;

	private List<ModelRefactorRequest> requests;

	private IRefactorCommand[] commands = null;

	public void setRefactorCommands(IRefactorCommand[] commands) {
		this.commands = commands;
	}

	public IRefactorCommand[] getRefactorCommands() {
		return commands;
	}

	private static class DisableAutoBuildingRunnable implements
			IRunnableWithProgress {

		private final IRunnableWithProgress delegat;

		public DisableAutoBuildingRunnable(IRunnableWithProgress delegat) {
			this.delegat = delegat;
		}

		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {

			boolean autoBuildingEnabled = Util.isAutoBuildingEnabled();
			if (autoBuildingEnabled) {
				Util.setAutoBuilding(false);
			}
			try {
				delegat.run(monitor);
			} finally {
				if (autoBuildingEnabled) {
					Util.setAutoBuilding(true);
				}
			}
		}

	}

	private class RefactorRunnable implements IRunnableWithProgress {

		public void run(IProgressMonitor pm) throws InvocationTargetException,
				InterruptedException {

			try {
				if (getRefactorCommands().length == 0) {
					List<IRefactorCommand> cmds = new ArrayList<IRefactorCommand>();
					List<ModelRefactorRequest> requests = getRequests();
					for (ModelRefactorRequest request : requests) {
						try {
							IRefactorCommand command = request
									.getCommand(new NullProgressMonitor());
							cmds.add(command);
						} catch (Exception e) {
							BasePlugin.log(e);
						}
					}
					setRefactorCommands(cmds.toArray(new IRefactorCommand[cmds
							.size()]));
				}

				final SubMonitor monitor = SubMonitor.convert(pm,
						"Executing commands", getRefactorCommands().length);
				for (final IRefactorCommand command : getRefactorCommands()) {
					try {
						closeEditors(command);
						IWorkspace workspace = ResourcesPlugin.getWorkspace();
						workspace.run(new IWorkspaceRunnable() {
							
							public void run(IProgressMonitor monitor) throws CoreException {
								try {
									command.execute(monitor);
								} catch (TigerstripeException e) {
									throw new CoreException(EclipsePlugin.getStatus(e));
								}
							}
							
						}, workspace.getRoot(), 0, monitor.newChild(1));
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					}
				}
			} finally {
				pm.done();
			}
		}

		private void closeEditors(IRefactorCommand command) {
			for (DiagramChangeDelta delta : command.getDiagramDeltas()) {

				IFile iFile = (IFile) delta.getAffDiagramHandle()
						.getDiagramResource();

				IWorkbenchWindow windows[] = PlatformUI.getWorkbench()
						.getWorkbenchWindows();
				for (int i = 0; i < windows.length; i++) {

					IWorkbenchPage pages[] = windows[i].getPages();
					for (int j = 0; j < pages.length; j++) {

						IEditorReference[] refs = pages[j]
								.getEditorReferences();
						for (IEditorReference ref : refs) {

							IEditorPart part = ref.getEditor(false);
							if (part != null
									&& part.getTitle().equals(iFile.getName())) {

								final IEditorPart fPart = part;
								Display display = Display.getDefault();
								display.syncExec(new Runnable() {
									public void run() {
										PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getActivePage()
												.closeEditor(fPart, true);
									}
								});
							}
						}
					}
				}
			}
		}
	}

	public RenameModelArtifactWizard() {
		super();

		setNeedsProgressMonitor(true);
		requests = new ArrayList<ModelRefactorRequest>();
	}

	public void init(IStructuredSelection selection) {

		this.selection = selection;
	}

	@Override
	public void addPages() {

		setWindowTitle("Refactor Model Artifact");

		RenameModelArtifactWizardPage renamePage = new RenameModelArtifactWizardPage();
		addPage(renamePage);

		RenameModelArtifactPreviewWizardPage previewPage = new RenameModelArtifactPreviewWizardPage();
		addPage(previewPage);

		renamePage.init(selection);

	}

	public void addRequest(ModelRefactorRequest request)
			throws TigerstripeException {

		if (request.isValid().getSeverity() == IStatus.OK) {
			requests.add(request);
		} else {
			throw new TigerstripeException(request.isValid().getMessage());
		}
	}

	public List<ModelRefactorRequest> getRequests() {
		return requests;
	}

	public void clearRequests() {

		requests = new ArrayList<ModelRefactorRequest>();
		commands = new IRefactorCommand[0];
	}

	@Override
	public boolean performFinish() {
		try {
			if (new RefactoringSaveHelper(
					RefactoringSaveHelper.SAVE_ALL_ALWAYS_ASK)
					.saveEditors(getShell())) {

				getContainer()
						.run(true,
								true,
								new DisableAutoBuildingRunnable(
										new RefactorRunnable()));
			}
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
		} catch (InterruptedException e) {
			EclipsePlugin.log(e);
		}
		return true;
	}
}
