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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.generate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.GenerationCanceledException;
import org.eclipse.tigerstripe.workbench.internal.core.generation.GenerationException;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M0Generator;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M0RunConfig;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.GenerateResultDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.NewTSElementWizard;

/**
 * This wizard start Tigerstripe.
 * 
 * @author Eric Dillon
 */
public class NewTigerstripeM0RunWizard extends NewTSElementWizard {

	// The main page
	private NewTigerstripeM0RunWizardPage fPage;

	private PluginRunStatus[] result;

	private List<Object> instanceList;

	public NewTigerstripeM0RunWizard() {
		super();
		setDefaultPageImageDescriptor(Images.getDescriptor(Images.TS_LOGO));
		setWindowTitle("Instance-Driven Generation (M0-Level)");
	}

	/*
	 * @see Wizard#addPages
	 */
	@Override
	public void addPages() {
		super.addPages();
		fPage = new NewTigerstripeM0RunWizardPage();
		addPage(fPage);
		fPage.init(getSelection());
	}

	public void setInstanceContext(List<Object> instanceList) {
		this.instanceList = instanceList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse
	 * .core.runtime.IProgressMonitor)
	 */
	@Override
	protected void finishPage(IProgressMonitor monitor)
			throws InterruptedException, CoreException {

		try {
			M0RunConfig config = fPage.getM0RunConfig();
			config.setInstanceMap(this.instanceList);
			M0Generator generator = new M0Generator(config);
			result = generator.run(monitor);

		} catch (GenerationCanceledException e) {
			throw new InterruptedException();
		} catch (GenerationException e) {
			Status status = new Status(
					IStatus.ERROR,
					EclipsePlugin.getPluginId(),
					222,
					"An error was detected while generating a Tigerstripe project. Generation may be incomplete.",
					e);
			EclipsePlugin.logErrorStatus(
					"Tigerstripe Generation Error Detected.", status);
			PluginRunStatus runStatus = new PluginRunStatus(e.getMessage());
			runStatus.add(status);
			result = new PluginRunStatus[] { runStatus };
		} catch (TigerstripeException e) {
			Status status = new Status(
					IStatus.ERROR,
					EclipsePlugin.getPluginId(),
					222,
					"An error was detected while generating a Tigerstripe project. Generation may be incomplete.",
					e);
			EclipsePlugin.logErrorStatus(
					"Tigerstripe Generation Error Detected.", status);
			PluginRunStatus runStatus = new PluginRunStatus(e.getMessage());
			runStatus.add(status);
			result = new PluginRunStatus[] { runStatus };
		}
		// IStatus[] stats = fPage.runTigerstripe(monitor, segments); // use the
		// full

		// refresh project so Eclipse picks up the generated files
		// Fix for Bug #185 to refresh in both cases - success and failure
		monitor.beginTask("Refreshing...", 2);
		monitor.subTask(""); //$NON-NLS-1$
		List<IJavaElement> javaElements = new ArrayList<IJavaElement>(5);
		IResource resource = fPage.getIProject();
		if (resource.getType() == IResource.PROJECT) {
			checkLocationDeleted((IProject) resource);
		} else if (resource.getType() == IResource.ROOT) {
			IProject[] projects = ((IWorkspaceRoot) resource).getProjects();
			for (int p = 0; p < projects.length; p++) {
				checkLocationDeleted(projects[p]);
			}
		}
		resource.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(
				monitor, 1));
		IJavaElement jElement = JavaCore.create(resource);
		if (jElement != null && jElement.exists())
			javaElements.add(jElement);

		IJavaModel model = JavaCore.create(ResourcesPlugin.getWorkspace()
				.getRoot());
		model.refreshExternalArchives((IJavaElement[]) javaElements
				.toArray(new IJavaElement[javaElements.size()]),
				new SubProgressMonitor(monitor, 1));

		// // progress monitor
		// if (stats.length != 0) {
		// CoreException core = new CoreException(stats[0]);
		// throw core;
		// }
	}

	private void checkLocationDeleted(IProject project) throws CoreException {
		if (!project.exists())
			return;
		File location = project.getLocation().toFile();
		if (!location.exists()) {
			final String message = NLS
					.bind("The location for project {0} ({1}) has been deleted.\n Delete {0} from the workspace?",
							project.getName(), location.getAbsolutePath());
			final boolean[] result = new boolean[1];
			// Must prompt user in UI thread (we're in the operation thread
			// here).
			getShell().getDisplay().syncExec(new Runnable() {
				public void run() {
					result[0] = MessageDialog.openQuestion(getShell(),
							"Project location has been deleted", message);
				}
			});
			if (result[0]) {
				project.delete(true, true, null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		fPage.setPageComplete(false);
		boolean res = super.performFinish();

		GenerateResultDialog dialog = new GenerateResultDialog(getShell(),
				result);
		dialog.open();

		return res;
	}

}