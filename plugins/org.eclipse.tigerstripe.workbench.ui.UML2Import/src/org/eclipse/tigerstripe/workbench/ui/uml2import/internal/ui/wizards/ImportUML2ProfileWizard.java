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
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import java.io.File;
import java.io.FileWriter;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.MessageListDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.uml2import.Activator;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.profile.ProfileImporter;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;


public class ImportUML2ProfileWizard extends Wizard implements INewWizard {


	private ImportUML2ProfileWizardPage firstPage;
	private IStructuredSelection fSelection;
	private MessageList messages;
	private IDialogSettings wizardSettings;
	
	private ImageDescriptor image;

	private boolean replace = true;
	private boolean createUnknown = false;
	private List<String> ignoreList = Arrays.asList("TypeDefinition", "Exception",
			"EnumValue");

	public IStructuredSelection getSelection() {
		return this.fSelection;
	}

	/**
	 * Constructor for ImportProfileWizard.
	 */
	public ImportUML2ProfileWizard() {
		super();
		setNeedsProgressMonitor(true);
		image = Images.getDescriptor(Images.WIZARD_IMPORT_LOGO);
		setDefaultPageImageDescriptor(image);

		
		IDialogSettings uml2ImportSettings = Activator.getDefault().getDialogSettings();
		this.wizardSettings = uml2ImportSettings.getSection("UML2ProfileImportWizard");
		if (wizardSettings == null){
			wizardSettings = uml2ImportSettings.addNewSection("UML2ProfileImportWizard");
		}
		setDialogSettings(uml2ImportSettings);	
		
		setWindowTitle("Import UML2 Profile...");
		messages = new MessageList();
		this.wizardSettings = wizardSettings;
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		super.addPages();

		this.firstPage = new ImportUML2ProfileWizardPage(wizardSettings);

		addPage(this.firstPage);
		
		this.firstPage.init(getSelection());
		
		
	}


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
					extractProfiles(monitor);
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
		} finally {
			wizardSettings.put("TSProfileFile",firstPage.getTSProfileFile());
			wizardSettings.put("ProfilesDir",firstPage.getProfileDir());
			wizardSettings.put("OverwriteExisting", firstPage.getReplace());
			wizardSettings.put("CreateUnknown", firstPage.getCreateUnknown());
		}

		// FIXME perform a refresh on created profile

		return true;
	}

	private boolean extractProfiles(IProgressMonitor monitor)
			throws CoreException {
		replace = this.firstPage.getReplace();
		createUnknown = this.firstPage.getCreateUnknown();
		
		try {
			File pro = new File(this.firstPage.getProfileFilename());
			WorkbenchProfile handle = (WorkbenchProfile) TigerstripeCore.getWorkbenchProfileSession()
					.getWorkbenchProfileFor(pro.getAbsolutePath());
			ProfileImporter importer = new ProfileImporter(messages);
			importer.loadProfile(handle, new File(this.firstPage.getFilename()), 
					ignoreList, replace, createUnknown, monitor);

			FileWriter writer = new FileWriter(pro.getAbsolutePath());
			try {
				writer.write(handle.asText());
				writer.flush();
			} finally {
				if (writer != null) {
					writer.close();
				}
			}

		} catch (Exception e) {
			String msgText = "Parse error loading profiles : ";
			msgText = msgText+e.getMessage();
			addMessage(msgText, 0);
		}

		monitor.done();
		if (!this.messages.isEmpty()) {
			MessageListDialog msgDialog = new MessageListDialog(
					this.getShell(), this.messages);
			msgDialog.open();
		}
		return true;
	}


	

	public void addMessage(String msgText, int severity) {
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(severity);
			this.messages.addMessage(newMsg);

	}

}