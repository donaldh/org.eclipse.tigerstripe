/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Difference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.uml2import.Activator;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.diff.DiffFixer;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;


public class UML2ImportWizard extends Wizard implements INewWizard {

	private ImageDescriptor image;
	private MappingWizardPage secondPage;
	private UML2ImportDetailsWizardPage firstPage;
	private ReviewUpdatesWizardPage thirdPage;
	private MessageList messages;
	private IDialogSettings wizardSettings;
	
	
	public UML2ImportWizard() {
		super();
		setNeedsProgressMonitor(true);
		image = Images.getDescriptor(Images.WIZARD_IMPORT_LOGO);
		setDefaultPageImageDescriptor(image);
		
		IDialogSettings uml2ImportSettings = Activator.getDefault().getDialogSettings();
		this.wizardSettings = uml2ImportSettings.getSection("UML2ImportWizard");
		if (wizardSettings == null){
			wizardSettings = uml2ImportSettings.addNewSection("UML2ImportWizard");
		}
		setDialogSettings(uml2ImportSettings);	
		
		
		
		setWindowTitle("Import/Update from UML2 File ...");
		messages = new MessageList();
		
		this.wizardSettings = wizardSettings;
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		super.addPages();

		this.firstPage = new UML2ImportDetailsWizardPage(wizardSettings);
		this.secondPage = new MappingWizardPage(); 
		this.thirdPage = new ReviewUpdatesWizardPage();
		
		addPage(this.firstPage);
		addPage(this.secondPage);
		addPage(this.thirdPage);
		
		secondPage.init();
	}
	
	@Override
	public boolean performFinish() {
			// make sure no build will happen during import or
			// else if becomes catastrophic performance-wise (try to import SID
		// :-))
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
			throws InvocationTargetException {
				try {
					TigerstripeProjectAuditor.setTurnedOffForImport(true);
					DiffFixer fixer = new DiffFixer();

					ArrayList<Difference> secondPassDiffs = fixer.fixAll(
							getThirdPage().getProjectDiffs(),
							getSecondPage().getImporter().getExtractedArtifacts(),
							getSecondPage().getImporter().getTigerstripeProject().getArtifactManagerSession(),
							getSecondPage().getImporter().getOut(), messages);
					// Bit of a sledgehammer - we'll re-add the artifacts.

					fixer.fixAll(secondPassDiffs, 
							getSecondPage().getImporter().getExtractedArtifacts(),
							getSecondPage().getImporter().getTigerstripeProject().getArtifactManagerSession(),
							getSecondPage().getImporter().getOut(), messages);
					
					// Need a refresh on the imported project
					ITigerstripeModelProject tsProject = getSecondPage().getImporter().getTigerstripeProject();
					IProject p = (IProject) tsProject.getAdapter(IProject.class);
					p.refreshLocal(IResource.DEPTH_INFINITE, monitor);
						

				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					
					// Turn auditor back on come what may!
					TigerstripeProjectAuditor.setTurnedOffForImport(false);
					
					// Save all of the wizard settings
					wizardSettings.put("TSProject",getFirstPage().getTigerstripeName());
					wizardSettings.put("ModelFile",getFirstPage().getModelFilename());
					wizardSettings.put("ProfileDir",getFirstPage().getProfilesFilename());
					wizardSettings.put("IgnoreUnknown",getFirstPage().getIgnoreUnknown());
					wizardSettings.put("UnknownType",getFirstPage().getUnknownType());
					wizardSettings.put("StringType",getFirstPage().getStringType());
					
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
			e.printStackTrace();
				return false;
		}

		// FIXME perform a refresh on created profile
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}

	public UML2ImportDetailsWizardPage getFirstPage() {
		return firstPage;
	}
	
	public MappingWizardPage getSecondPage() {
		return secondPage;
	}

	public ReviewUpdatesWizardPage getThirdPage() {
		return thirdPage;
	}

	public IDialogSettings getWizardSettings() {
		return wizardSettings;
	}

	
	
}