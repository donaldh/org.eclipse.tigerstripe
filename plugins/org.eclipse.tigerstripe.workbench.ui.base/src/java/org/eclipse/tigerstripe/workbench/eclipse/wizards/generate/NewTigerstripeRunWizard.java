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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.generate;

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
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.actions.ActionMessages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.NewTSElementWizard;
import org.eclipse.tigerstripe.workbench.internal.core.generation.GenerationCanceledException;
import org.eclipse.tigerstripe.workbench.internal.core.generation.GenerationException;
import org.eclipse.tigerstripe.workbench.internal.core.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.ProjectGenerator;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.GenerateResultDialog;

/**
 * @author Eric Dillon
 * 
 * This wizard start Tigerstripe.
 * 
 */
public class NewTigerstripeRunWizard extends NewTSElementWizard {

	// The main page
	private NewTigerstripeRunWizardPage fPage;

	private PluginRunStatus[] result;

	public NewTigerstripeRunWizard() {
		super();
		setDefaultPageImageDescriptor(TigerstripePluginImages.DESC_TS_LOGO);

		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("Generate Tigerstripe Project");
	}

	/*
	 * @see Wizard#addPages
	 */
	@Override
	public void addPages() {
		super.addPages();
		fPage = new NewTigerstripeRunWizardPage();
		addPage(fPage);
		fPage.init(getSelection());
		//
		// IProject project = fPage.getIProject();
		// List<IContractSegment> segments = getContractSegments(project);
		// if (segments.size() != 0) {
		// csPage = new ContractSegmentSelectionWizardPage(segments);
		// addPage(csPage);
		// }
	}

	// private List<IContractSegment> getContractSegments(IProject project) {
	// List<IContractSegment> result = new ArrayList<IContractSegment>();
	// List<IResource> csResources = TigerstripeProjectAuditor.findAll(
	// project, IContractSegment.FILE_EXTENSION);
	// for (IResource res : csResources) {
	// try {
	// IContractSegment segment = API.getIContractSession()
	// .makeIContractSegment(res.getLocationURI());
	// if ( segment != null && !result.contains(segment)) {
	// result.add( segment );
	// }
	// } catch (TigerstripeException e) {
	// TigerstripeRuntime.logErrorMessage("TigerstripeException detected", e);
	// EclipsePlugin.log(e);
	// }
	// }
	//		
	// return result;
	// }
	//

	private ITigerstripeProject getTSProject() throws TigerstripeException {
		IAbstractTigerstripeProject result = EclipsePlugin
				.getITigerstripeProjectFor(fPage.getIProject());

		if (result instanceof ITigerstripeProject)
			return (ITigerstripeProject) result;

		throw new TigerstripeException("Invalid project");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void finishPage(IProgressMonitor monitor)
			throws InterruptedException, CoreException {

		try {
			RunConfig config = fPage.getRunConfig();
			ProjectGenerator generator = new ProjectGenerator(getTSProject(),
					config);
			result = generator.run(monitor);

		} catch (GenerationCanceledException e) {
			throw new InterruptedException();
		} catch (GenerationException e) {
			Status status = new Status(
					IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID,
					222,
					"An error was detected while generating a Tigerstripe project. Generation maybe incomplete.",
					e);
			EclipsePlugin.logErrorStatus(
					"Tigerstripe Generation Error Detected.", status);
		} catch (TigerstripeException e) {
			Status status = new Status(
					IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID,
					222,
					"An error was detected while generating a Tigerstripe project. Generation maybe incomplete.",
					e);
			EclipsePlugin.logErrorStatus(
					"Tigerstripe Generation Error Detected.", status);
		}
		// IStatus[] stats = fPage.runTigerstripe(monitor, segments); // use the
		// full

		// refresh project so Eclipse picks up the generated files
		// Fix for Bug #185 to refresh in both cases - success and failure
		monitor.beginTask(ActionMessages.RefreshAction_progressMessage, 2);
		monitor.subTask(""); //$NON-NLS-1$
		List javaElements = new ArrayList(5);
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
			final String message = Messages.format(
					ActionMessages.RefreshAction_locationDeleted_message,
					new Object[] { project.getName(),
							location.getAbsolutePath() });
			final boolean[] result = new boolean[1];
			// Must prompt user in UI thread (we're in the operation thread
			// here).
			getShell().getDisplay().syncExec(new Runnable() {
				public void run() {
					result[0] = MessageDialog.openQuestion(getShell(),
							ActionMessages.RefreshAction_locationDeleted_title,
							message);
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