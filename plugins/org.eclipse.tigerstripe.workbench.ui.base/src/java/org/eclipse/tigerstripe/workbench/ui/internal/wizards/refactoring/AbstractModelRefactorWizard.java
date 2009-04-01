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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
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

	protected ModelRefactorRequest request;

	protected IStructuredSelection selection;

	public AbstractModelRefactorWizard() {

		super();
		request = new ModelRefactorRequest();
		setNeedsProgressMonitor(true);
	}

	public ModelRefactorRequest getRequest() {

		return request;
	}

	public IRefactorCommand getCommand() throws TigerstripeException {

		if (request.isValid().getSeverity() == IStatus.OK) {

			return request.getCommand(new NullProgressMonitor());
		} else {

			throw new TigerstripeException("Invalid refactor request. Status: "
					+ request.isValid().getSeverity());
		}

	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {

		try {
			getContainer().run(false, true, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {

					try {

						for (DiagramChangeDelta delta : getCommand()
								.getDiagramDeltas()) {

							IFile iFile = (IFile) delta.getAffDiagramHandle()
									.getDiagramResource();
							System.out.println();

							IWorkbenchWindow windows[] = PlatformUI
									.getWorkbench().getWorkbenchWindows();
							for (int i = 0; i < windows.length; i++) {
								IWorkbenchPage pages[] = windows[i].getPages();
								for (int j = 0; j < pages.length; j++) {
									IEditorReference[] refs = pages[j]
											.getEditorReferences();
									for (IEditorReference ref : refs) {
										IEditorPart part = ref.getEditor(false);
										if (part != null
												&& part.getTitle().equals(
														iFile.getName())) {
											final IEditorPart fPart = part;
											Display display = Display
													.getDefault();
											display.syncExec(new Runnable() {
												public void run() {
													PlatformUI
															.getWorkbench()
															.getActiveWorkbenchWindow()
															.getActivePage()
															.closeEditor(fPart,
																	true);
												}
											});
										}
									}
								}
							}

						}

						getCommand().execute(monitor);
					} catch (TigerstripeException e) {
						throw new InvocationTargetException(e);
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