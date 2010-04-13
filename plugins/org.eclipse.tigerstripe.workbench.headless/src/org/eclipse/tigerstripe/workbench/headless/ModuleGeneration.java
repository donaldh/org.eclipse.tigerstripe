/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.headless;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeOssjProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.osgi.framework.Constants;

public class ModuleGeneration implements IApplication {

	private static final String DELIMITER = "=";

	private static final String PROJECT_EXPORT_ARG = "MODULE_PROJECT";

	private static final String OUTPUT_ARG = "MODULE_OUTPUT";

	private static final String INCLUDE_DIAGRAMS_ARG = "INCLUDE_DIAGRAMS";

	private static final String INCLUDE_ANNONATIONS_ARG = "INCLUDE_ANNONATIONS";

	private String moduleProject;

	private String moduleOutput;

	private boolean includeDiagrams = true;

	private boolean includeAnnotations = true;

	private boolean asInstallable = true;

	public Object start(IApplicationContext context) throws Exception {

		long start = System.currentTimeMillis();
		printTigerstipeVersionInfo();
		setPluginParams(context);
		try {
			printProfile();
			initializeWorkspace();
			generateModule();
		} catch (TigerstripeException e) {
			e.printStackTrace();
			return new Integer(1);
		}
		long finish = System.currentTimeMillis();
		System.out.println("Generation complete. Took " + (finish - start)
				+ " milliseconds.");
		return EXIT_OK;
	}

	public static void printTigerstipeVersionInfo() {
		System.out.println(TigerstripeCore.getRuntimeDetails()
				.getBaseBundleValue(Constants.BUNDLE_NAME)
				+ " (v"
				+ TigerstripeCore.getRuntimeDetails().getBaseBundleValue(
						Constants.BUNDLE_VERSION) + ")");
	}

	private void setPluginParams(IApplicationContext context)
			throws TigerstripeException {
		String[] split = new String[2];
		String[] cmdLineArgs = (String[]) context.getArguments().get(
				IApplicationContext.APPLICATION_ARGS);
		for (String arg : cmdLineArgs) {
			split = arg.split(DELIMITER);
			if (split[0].equals(PROJECT_EXPORT_ARG)) {
				moduleProject = split[1];
			} else if (split[0].equals(OUTPUT_ARG)) {
				moduleOutput = split[1];
			} else if (split[0].equals(INCLUDE_DIAGRAMS_ARG)) {
				includeDiagrams = Boolean.parseBoolean(split[1]);
			} else if (split[0].equals(INCLUDE_ANNONATIONS_ARG)) {
				includeAnnotations = Boolean.parseBoolean(split[1]);
			}
		}
		if (moduleProject == null) {
			throw new TigerstripeException(
					"Must have the module project defined.");
		}
		if (moduleOutput == null) {
			throw new TigerstripeException(
					"Must have the module output defined.");
		}
	}

	private void initializeWorkspace() throws TigerstripeException {
		IWorkspaceRunnable op = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				final IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IPath path = getIProject(moduleProject).getLocation().append(
						".project");
				IProjectDescription description = ResourcesPlugin
						.getWorkspace().loadProjectDescription(path);
				description.setLocationURI(null);
				final IProject project = workspace.getRoot().getProject(
						moduleProject);
				if (!project.exists()) {
					try {
						project.create(description, null);
						project.open(IResource.BACKGROUND_REFRESH, null);
					} catch (CoreException e) {
						throw e;
					}
				}
				workspace.getRoot()
						.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
		};

		// run the new project creation operation
		try {
			ResourcesPlugin.getWorkspace().run(op, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void printProfile() {
		IWorkbenchProfileSession profileSession = TigerstripeCore
				.getWorkbenchProfileSession();
		System.out.println("Active Profile: "
				+ profileSession.getActiveProfile().getName() + " "
				+ profileSession.getActiveProfile().getVersion());
	}

	private void generateModule() throws TigerstripeException {
		try {
			IProject project = getIProject(moduleProject);
			URI locationURI = project.getLocationURI();
			ITigerstripeModelProject tsProject = (ITigerstripeModelProject) TigerstripeCore
					.findProject(locationURI);

			IProgressMonitor monitor = new NullProgressMonitor();
			project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);

			IModulePackager packager = ((TigerstripeOssjProjectHandle) tsProject)
					.getPackager();
			File file = new File(moduleOutput);

			IModuleHeader header = packager.makeHeader();
			header.setModuleID(tsProject.getModelId());

			String classesDirStr = locationURI.getPath().toString()
					+ IPath.SEPARATOR + "bin";
			File classesDir = new File(classesDirStr);
			packager
					.packageUp(file.toURI(), classesDir, header,
							includeDiagrams, includeAnnotations, asInstallable,
							monitor);

			// Now refresh project
			List<IJavaElement> javaElements = new ArrayList<IJavaElement>();

			project.refreshLocal(IResource.DEPTH_INFINITE,
					new SubProgressMonitor(monitor, 1));
			IJavaElement jElement = JavaCore.create(project);
			if (jElement != null && jElement.exists())
				javaElements.add(jElement);

			IJavaModel model = JavaCore.create(ResourcesPlugin.getWorkspace()
					.getRoot());
			model.refreshExternalArchives((IJavaElement[]) javaElements
					.toArray(new IJavaElement[javaElements.size()]),
					new SubProgressMonitor(monitor, 1));
		} catch (CoreException e) {
			throw new TigerstripeException(e.getMessage(), e);
		}
	}

	public IProject getIProject(String pathStr) {
		if (pathStr != null && pathStr.length() != 0) {
			IPath path = new Path(pathStr);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource res = root.findMember(path);

			if (res != null)
				return res.getProject();
		}
		return null;
	}

	public void stop() {
		System.out.println("Stopping");
	}

}
