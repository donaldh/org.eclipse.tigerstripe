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
package org.eclipse.tigerstripe.workbench.optional.UML2Export.ui.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.optional.UML2Export.ProfileAnnotations2UML2;
import org.eclipse.tigerstripe.workbench.optional.UML2Export.ProfileTypes2UML2;
import org.eclipse.tigerstripe.workbench.optional.UML2Export.TS2UML;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.MessageListDialog;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Type;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class UML2ExportWizard extends Wizard implements INewWizard {

	private UML2ExportWizardPage firstPage;

	private IStructuredSelection fSelection;

	private PrintWriter out;
	private MessageList messages;

	public IStructuredSelection getSelection() {
		return this.fSelection;
	}

	/**
	 * Constructor for NewProjectWizard.
	 */
	public UML2ExportWizard() {
		super();
		setNeedsProgressMonitor(true);

		setWindowTitle("Export to UML2 ...");
		messages = new MessageList();
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		super.addPages();

		this.firstPage = new UML2ExportWizardPage();

		addPage(this.firstPage);

		this.firstPage.init(getSelection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		fSelection = currentSelection;
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doExport(monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(false, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}
		return true;
	}

	public boolean doExport(IProgressMonitor monitor) throws CoreException {

		try {
			// Restart the list in case a new XML has been created.
			messages = new MessageList();

			File logFile = new File(this.firstPage.getExportDir()
					+ File.separator + "UML2Export.log");
			out = new PrintWriter(new FileOutputStream(logFile, false));

			// Make profile and Modelelements stuff from the current profile
			// info.
			ProfileTypes2UML2 p2uml = new ProfileTypes2UML2();
			Map<String, Type> primitiveTypeMap = p2uml.loadTSProfileTypestoUML(
					this.firstPage.getExportDir(), this.firstPage
							.getTigerstripeName(), out, messages, monitor);
			Model typesModel = p2uml.getModel();

			ProfileAnnotations2UML2 a2uml = new ProfileAnnotations2UML2(out);
			Profile profile = a2uml.loadTSProfileAnnotationstoUML(
					this.firstPage.getExportDir(), this.firstPage
							.getTigerstripeName(),  messages, monitor,
					primitiveTypeMap, typesModel);

			TS2UML ts2uml = new TS2UML(out, messages, monitor);
			ts2uml.loadTigerstripeToUML(this.firstPage.getExportDir(),
					this.firstPage.getTigerstripeName(), this.firstPage
							.getTigerstripeName(), 
					primitiveTypeMap, profile, typesModel);

			out.close();

			monitor.done();

			if (!messages.isEmpty()) {
				MessageListDialog msgDialog = new MessageListDialog(this
						.getShell(), messages, "UML2 Export Log");
				msgDialog.open();
			}

			return true;

		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			e.printStackTrace(out);
			out.close();
			monitor.done();
			return false;
		}
	}

}