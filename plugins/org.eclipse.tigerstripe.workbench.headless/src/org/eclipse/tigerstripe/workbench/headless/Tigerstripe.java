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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.startup.PostInstallActions;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.osgi.framework.Constants;

/**
 * Main class responsible for headless Tigerstripe builds
 * 
 */
public class Tigerstripe implements IApplication {

	private static final String DELIMITER = "=";

	private static final String VALUE_SEPARATOR = ",";

	public static final String IMPORT_PROJECT_ARG = "PROJECT_IMPORT";

	public static final String GENERATION_THREADS_ARG = "GENERATION_THREADS";

	private List<String> projects;

	private int numThreads = 8;

	private ExecutorService threadPool;

	public Object start(IApplicationContext context) throws Exception {

		long start = System.currentTimeMillis();

		System.out.println("Starting Tigerstripe...");
		printTigerstipeVersionInfo();
		setPluginParams(context);

		try {
			PostInstallActions.init();
			printProfile();

			ImportProjectsRunnable op = new ImportProjectsRunnable(projects);
			ResourcesPlugin.getWorkspace().run(op, null);
			List<IProject> importedProjects = op.getImportedProjects();
			
			/////////////////////
			// Multi-threaded generation is not yet supported by Tigerstripe
			// When support is ready, the threading code in here can be 
			// uncommented to begin running multi-threaded generation.
			/////////////////////	
//			if (importedProjects.size() > 1 && numThreads > 1) {
//				int poolSize = 
//						importedProjects.size() < numThreads ? importedProjects.size() : numThreads;
//				System.out.println("Running generation with " + String.valueOf(poolSize) + " threads.");
//				threadPool = Executors.newFixedThreadPool(poolSize);
//			}

//			List<Future<Boolean>> threadedTasks = new ArrayList<Future<Boolean>>();
			int count = 1;
			for (IProject project : importedProjects) {
//				if (threadPool != null) {
//					System.out.println("Creating thread for project: " + project.getName());
//					Future<Boolean> task = threadPool.submit(new ProjectGenerationRunnable(project));
//					threadedTasks.add(task);
//				} else {
					System.out.println("Starting on project " + count++ + "/" + importedProjects.size() + " - " + project.getName());
					GenerationUtils.generate(project);
//				}
			}
//			if (threadPool != null) {
//				for (Future<Boolean> task : threadedTasks) {
//					if (!exc.isEmpty()) {
//						task.cancel(true);
//					} else {
//						try {
//							task.get();
//						} catch (Throwable e) {
//							exc.add(e);
//							e.printStackTrace();
//						}
//					}
//				}
//			}

		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		} finally {
//			if (threadPool != null) {
//				threadPool.shutdown();
//			}
		}

		long finish = System.currentTimeMillis();
		System.out.println("Generation complete. Took " + (finish - start) + " milliseconds.");

		return EXIT_OK;

	}

	public static void printTigerstipeVersionInfo() {
		System.out.println(TigerstripeCore.getRuntimeDetails().getBaseBundleValue(Constants.BUNDLE_NAME) + " (v"
				+ TigerstripeCore.getRuntimeDetails().getBaseBundleValue(Constants.BUNDLE_VERSION) + ")");
	}

	private void setPluginParams(IApplicationContext context) throws TigerstripeException {
		projects = new ArrayList<String>();
		String[] split = new String[2];
		String[] cmdLineArgs = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		for (String arg : cmdLineArgs) {
			split = arg.split(DELIMITER);
			String key = split[0];
			String[] values = split[1].split(VALUE_SEPARATOR);
			for (String value : values) {
				if (key.equals(IMPORT_PROJECT_ARG)) {
					projects.add(value);
				} else if (key.equals(GENERATION_THREADS_ARG)) {
					try {
						numThreads = Integer.valueOf(value);
					} catch (Exception e) {
						// Oh well, we tried
						System.err.println(
								"Could not parse the " + GENERATION_THREADS_ARG + " as a valid integer: " + value);
						e.printStackTrace();
					}
				}
			}
		}
		if (projects.isEmpty()) {
			throw new TigerstripeException("Must have at least one generation project defined.");
		}
	}

	private void printProfile() {
		IWorkbenchProfileSession profileSession = TigerstripeCore.getWorkbenchProfileSession();
		System.out.println("Active Profile: " + profileSession.getActiveProfile().getName() + " "
				+ profileSession.getActiveProfile().getVersion());
	}
	
	public void stop() {
		System.out.println("Stopping");
	}
}
