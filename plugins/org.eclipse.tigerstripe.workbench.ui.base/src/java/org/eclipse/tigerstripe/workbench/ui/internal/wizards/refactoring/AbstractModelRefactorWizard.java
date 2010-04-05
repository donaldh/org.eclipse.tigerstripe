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
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramChangeDelta;
import org.eclipse.tigerstripe.workbench.refactor.IRefactorCommand;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractModelRefactorWizard extends Wizard implements
		IWorkbenchWizard {

	protected List<ModelRefactorRequest> requests;

	protected IStructuredSelection selection;

	private IRefactorCommand[] commands = null;

	public void setRefactorCommands(IRefactorCommand[] commands) {
		this.commands = commands;
	}

	public IRefactorCommand[] getRefactorCommands() {
		return commands;
	}

	public AbstractModelRefactorWizard() {

		super();
		requests = new ArrayList<ModelRefactorRequest>();
		setNeedsProgressMonitor(true);
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

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * Returns the default page title used for pages that don't provide their
	 * own page title.
	 * 
	 * @return the default page title or <code>null</code> if non has been set
	 * 
	 * @see #setDefaultPageTitle(String)
	 */
	public String getDefaultPageTitle() {
		return "Refactor Model Artifact";
	}

	@Override
	public boolean performFinish() {

		try {
			getContainer().run(false, true, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {

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
						setRefactorCommands(cmds
								.toArray(new IRefactorCommand[cmds.size()]));
					}

					for (IRefactorCommand command : getRefactorCommands()) {

						try {

							for (DiagramChangeDelta delta : command
									.getDiagramDeltas()) {

								IFile iFile = (IFile) delta
										.getAffDiagramHandle()
										.getDiagramResource();

								IWorkbenchWindow windows[] = PlatformUI
										.getWorkbench().getWorkbenchWindows();
								for (int i = 0; i < windows.length; i++) {
									IWorkbenchPage pages[] = windows[i]
											.getPages();
									for (int j = 0; j < pages.length; j++) {
										IEditorReference[] refs = pages[j]
												.getEditorReferences();
										for (IEditorReference ref : refs) {
											IEditorPart part = ref
													.getEditor(false);
											if (part != null
													&& part.getTitle().equals(
															iFile.getName())) {
												final IEditorPart fPart = part;
												Display display = Display
														.getDefault();
												display
														.syncExec(new Runnable() {
															public void run() {
																PlatformUI
																		.getWorkbench()
																		.getActiveWorkbenchWindow()
																		.getActivePage()
																		.closeEditor(
																				fPart,
																				true);
															}
														});
											}
										}
									}
								}

							}

							command.execute(monitor);
						} catch (TigerstripeException e) {
							throw new InvocationTargetException(e);
						}
					}

				}

			});
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
		} catch (InterruptedException e) {
			EclipsePlugin.log(e);
		}

		return true;
	}
}