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

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.export.model.facet;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.facets.FacetExporter;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.facets.FacetExporterInput;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLog;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class FacetExportWizard extends Wizard implements IExportWizard {

	private FacetExporterInput inputManager;

	public FacetExportWizard() {

		setNeedsProgressMonitor(true);
		inputManager = new FacetExporterInput();
	}

	public FacetExporterInput getInputManager() {
		return inputManager;
	}

	@Override
	public void addPages() {

		addPage(new FacetExportWizardMainPage());
		addPage(new FacetExportWizardPreviewPage());
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

		inputManager.setInitialSelection(selection);
	}

	@Override
	public boolean performFinish() {

		try {
			getContainer().run(true, false, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					try {
						FacetExporter.export(inputManager, monitor);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					} catch (CoreException e) {
						EclipsePlugin.log(e);
					}
				}
			});
		} catch (InvocationTargetException e) {
			TigerstripeLog.logError(e);
			return false;
		} catch (InterruptedException e) {
			TigerstripeLog.logError(e);
			return false;
		}
		return true;
	}

}
