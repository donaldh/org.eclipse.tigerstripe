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
package org.eclipse.tigerstripe.workbench.ui.internal.export;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.actions.ActionMessages;
import org.eclipse.jdt.internal.ui.actions.WorkbenchRunnableAdapter;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeOssjProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * A wizard dedicated to exporting a TS project to a module.
 * 
 * @author Eric Dillon
 * 
 */
public class ModuleExportWizard extends Wizard implements IWorkbenchWizard {

	private IStructuredSelection fSelection;

	private ModuleExportWizardPage fPage;

	public ModuleExportWizard() {
		setNeedsProgressMonitor(true);
		setWindowTitle("Export to Tigerstripe module");
		setDefaultPageImageDescriptor(Images.getDescriptor(Images.TS_LOGO));
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
	}

	/*
	 * @see Wizard#addPages
	 */
	@Override
	public void addPages() {
		super.addPages();
		fPage = new ModuleExportWizardPage();
		addPage(fPage);
		fPage.init(fSelection);
	}

	@Override
	public boolean performFinish() {

		final ITigerstripeModelProject tsProject = fPage
				.getITigerstripeProject();
		final IProject project = fPage.getIProject();
		final String moduleID = fPage.getModuleID();
		final String jarFile = fPage.getJarFile();
		final boolean includeDiagrams = fPage.getIncludeDiagrams();
		final boolean includeAnnotations = fPage.getIncludeAnnotations();
		final boolean exportAsInstallable = fPage
				.getExportAsInstallableModule();

		if (project == null) {
			TigerstripeException e = new TigerstripeException(
					"Invalid project for Export.");
			Status status = new Status(IStatus.ERROR, EclipsePlugin
					.getPluginId(), 222, "Cannot export to Tigerstripe module",
					e);
			EclipsePlugin.log(status);
			return false;
		} else {
			IWorkspaceRunnable op = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException,
						OperationCanceledException {
					try {
						internalPerformFinish(monitor, tsProject, project,
								moduleID, jarFile, includeDiagrams,
								includeAnnotations, exportAsInstallable);
					} catch (InterruptedException e) {
						throw new OperationCanceledException(e.getMessage());
					}
				}
			};
			try {
				ISchedulingRule rule = null;
				Job job = Platform.getJobManager().currentJob();
				if (job != null)
					rule = job.getRule();
				IRunnableWithProgress runnable = null;
				if (rule != null)
					runnable = new WorkbenchRunnableAdapter(op, rule, true);
				else
					runnable = new WorkbenchRunnableAdapter(op,
							getSchedulingRule());
				getContainer().run(canRunForked(), true, runnable);
			} catch (InvocationTargetException e) {
				handleFinishException(getShell(), e);
				return false;
			} catch (InterruptedException e) {
				return false;
			}
			return true;
		}
	}

	/**
	 * As a result of implementing IWorkbenchWizard
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		fSelection = selection;
	}

	private void internalPerformFinish(IProgressMonitor monitor,
			ITigerstripeModelProject tsProject, IProject project,
			String moduleID, String jarFile, boolean includeDiagrams,
			boolean includeAnnotations, boolean asInstallable)
			throws InterruptedException, CoreException {

		try {
			monitor.beginTask("Packaging project", 10);
			// This step here makes sure the .class are built for all the POJOs
			// This means that when building a module outside of Eclipse, all
			// the
			// src directory needs to be compiled (into the bin directory).
			project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
			monitor.worked(1);

			IModulePackager packager = ((TigerstripeOssjProjectHandle) tsProject)
					.getPackager();
			File file = new File(jarFile);

			IModuleHeader header = packager.makeHeader();
			header.setModuleID(moduleID);

			monitor.worked(3);

			String classesDirStr;
			IPath outputPath = getOutputLocation(tsProject,
					fPage.getPackageFragmentRoot());
			if (outputPath != null) {
				classesDirStr = outputPath.toOSString();
			} else {
				classesDirStr = tsProject.getLocation().toFile().toURI()
						.getPath()
						+ File.separator + "/bin";
			}

			File classesDir = new File(classesDirStr);
			packager
					.packageUp(file.toURI(), classesDir, header,
							includeDiagrams, includeAnnotations, asInstallable,
							monitor);

			// Now refresh project
			// Fixed automatic refresh which was missing - bug # 110
			monitor.beginTask(ActionMessages.RefreshAction_progressMessage, 2);
			monitor.subTask(""); //$NON-NLS-1$
			List javaElements = new ArrayList(5);
			IResource resource = fPage.getIProject();

			resource.refreshLocal(IResource.DEPTH_INFINITE,
					new SubProgressMonitor(monitor, 1));
			IJavaElement jElement = JavaCore.create(resource);
			if (jElement != null && jElement.exists())
				javaElements.add(jElement);

			IJavaModel model = JavaCore.create(ResourcesPlugin.getWorkspace()
					.getRoot());
			model.refreshExternalArchives((IJavaElement[]) javaElements
					.toArray(new IJavaElement[javaElements.size()]),
					new SubProgressMonitor(monitor, 1));
			// End of Bug fix

			monitor.done();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private IPath getOutputLocation(ITigerstripeModelProject tsProject,
			IPackageFragmentRoot sourceFolder) {
		IPath result = null;
		if (sourceFolder != null) {
			IJavaProject javaProject = (IJavaProject) tsProject
					.getAdapter(IJavaProject.class);
			if (javaProject != null) {
				try {
					IPath sourceFolderPath = sourceFolder.getPath();
					IClasspathEntry[] entries = javaProject.getRawClasspath();
					for (IClasspathEntry entry : entries) {
						if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE
								&& sourceFolderPath.equals(entry.getPath())) {
							IPath workspaceRelativeOutputPath = entry
									.getOutputLocation();
							if (workspaceRelativeOutputPath == null) {
								workspaceRelativeOutputPath = javaProject
										.getOutputLocation();
							}
							IFolder out = ResourcesPlugin.getWorkspace()
									.getRoot()
									.getFolder(workspaceRelativeOutputPath);
							result = out.getLocation();
							break;
						}
					}
				} catch (JavaModelException e) {
					EclipsePlugin.log(e);
				}
			}
		}
		return result;
	}

	/**
	 * Returns the scheduling rule for creating the element.
	 */
	protected ISchedulingRule getSchedulingRule() {
		return ResourcesPlugin.getWorkspace().getRoot(); // look all by
		// default
	}

	protected boolean canRunForked() {
		return true;
	}

	protected void handleFinishException(Shell shell,
			InvocationTargetException e) {
		String title = NewWizardMessages.NewElementWizard_op_error_title;
		String message = NewWizardMessages.NewElementWizard_op_error_message;
		ExceptionHandler.handle(e, shell, title, message);
	}
}
