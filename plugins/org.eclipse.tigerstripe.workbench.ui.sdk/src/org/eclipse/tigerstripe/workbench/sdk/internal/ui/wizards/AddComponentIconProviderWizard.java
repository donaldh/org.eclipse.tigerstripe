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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.SDKConstants;
import org.eclipse.tigerstripe.workbench.sdk.internal.ModelUpdater;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class AddComponentIconProviderWizard extends Wizard implements
		INewWizard {

	public AddComponentIconProviderWizard(ISDKProvider provider) {
		super();
		this.provider = provider;
	}

	private ISDKProvider provider;
	private IStructuredSelection fSelection;
	private AddComponentIconProviderWizardPage firstPage;

	public void addPages() {
		super.addPages();
		setWindowTitle("Add A New Component Icon Provider Class");
		this.firstPage = new AddComponentIconProviderWizardPage("", getShell(),
				provider);
		addPage(this.firstPage);
		// this.firstPage.init(getSelection());
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

		IPluginModelBase cont = firstPage.getContributerSelection();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("artifactType", firstPage.getArtifactType());
		attributes.put("provider", firstPage.getProviderClass());

		try {
			IResource res = (IResource) cont.getAdapter(IResource.class);
			IProject contProject = (IProject) res.getProject();

			ModelUpdater mu = new ModelUpdater();
			if (contProject != null) {
				mu.addSimpleExtension(contProject,
						SDKConstants.METADATA_EXT_PT,
						SDKConstants.METADATA_MODELICON_PART, attributes);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
