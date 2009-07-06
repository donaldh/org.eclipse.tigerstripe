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
package org.eclipse.tigerstripe.workbench.sdk.internal.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.sdk.internal.Activator;
import org.eclipse.tigerstripe.workbench.sdk.internal.patterns.PatternFileCreator;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class PatternCreateWizard extends Wizard implements INewWizard {

	public PatternCreateWizard() {
		super();
		IDialogSettings defaultSettings = Activator.getDefault()
				.getDialogSettings();
		this.wizardSettings = defaultSettings.getSection("PatternCreateWizard");
		if (wizardSettings == null) {
			wizardSettings = defaultSettings
					.addNewSection("PatternCreateWizard");
		}
		setDialogSettings(defaultSettings);
	}

	private IStructuredSelection fSelection;
	private PatternCreateWizardPage firstPage;
	private IDialogSettings wizardSettings;

	public void addPages() {
		super.addPages();
		setWindowTitle("Create Artifact Pattern File");
		this.firstPage = new PatternCreateWizardPage("", wizardSettings);
		addPage(this.firstPage);
		this.firstPage.init(getSelection());
	}

	@Override
	public boolean performFinish() {
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(monitor);
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
					// Save all of the wizard settings
					wizardSettings.put("targetDirectory", firstPage
							.getTargetDirectoryText());
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

	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		fSelection = currentSelection;
	}

	public IStructuredSelection getSelection() {
		return this.fSelection;
	}

	public void doFinish(IProgressMonitor monitor) {
		// Actually do the work!
		// Gather info from the page
		PatternFileCreator creator = new PatternFileCreator();
		creator.makeArtifactPattern(firstPage.getSourceArtifact(), firstPage
				.getTargetFile(), firstPage.getPatternNameText(), firstPage
				.getInlcudeEndNames(), firstPage.getUILabelText(), firstPage
				.getIconPathText(), firstPage.getIndexText(), firstPage
				.getDescriptionText());

	}

}
