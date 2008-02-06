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
package org.eclipse.tigerstripe.workbench.internal.core.profile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipException;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IActiveWorkbenchProfileChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.internal.core.util.ZipFilePackager;
import org.eclipse.tigerstripe.workbench.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;

/**
 * Singleton class that provide access to the PhantomTigerstripeProject in an
 * install.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PhantomTigerstripeProjectMgr implements
		IActiveWorkbenchProfileChangeListener {

	private static PhantomTigerstripeProjectMgr instance;

	private PhantomTigerstripeProject phantomProject;

	private boolean needACompile = false;

	private PhantomTigerstripeProjectMgr() {
		// register self as a listener for Active profile changes so we can
		// update the phantom project accordingly
		TigerstripeCore.getWorkbenchProfileSession().addActiveProfileListener(
				this);
	}

	public static PhantomTigerstripeProjectMgr getInstance() {
		if (instance == null) {
			instance = new PhantomTigerstripeProjectMgr();
		}
		return instance;
	}

	public synchronized PhantomTigerstripeProject getPhantomProject()
			throws TigerstripeException {
		if (phantomProject == null) {
			createPhantomProject();
			populateFromProfile(new TigerstripeNullProgressMonitor());// FIXME
			createJarForEclipsePath();
		}
		return phantomProject;
	}

	public synchronized void profileChanged(IWorkbenchProfile newActiveProfile) {
		try {
			// invalidate current phantom project and rebuild
			// NOTE: the project itself doesn't change, it's content does.
			createPhantomProject();
			populateFromProfile(new TigerstripeNullProgressMonitor()); // FIXME
			createJarForEclipsePath();
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	/**
	 * Creates and populate the phantom project based on the current active
	 * profile
	 * 
	 */
	protected void createPhantomProject() {

		URI phantomURI = getPhantomURI();
		// first remove any existing stuff
		File phantomDir = new File(phantomURI);
		if (phantomDir.exists()) {
			Util.deleteDir(phantomDir);
		}
		phantomDir.mkdirs();

		phantomProject = new PhantomTigerstripeProject(phantomDir);
		phantomProject.createEmpty(); // creates an empty Project Structure on
		// disk
	}

	/**
	 * Populates the PhantomProject from the current active profile.
	 * 
	 */
	protected void populateFromProfile(ITigerstripeProgressMonitor monitor) {
		if (phantomProject == null)
			return;
		needACompile = populatePrimitiveTypes(monitor) != 0;
	}

	/**
	 * Creates PrimitiveType artifacts in the phantom project corresponding to
	 * all definitions in the profile.
	 * 
	 * @return returns the number of PrimitiveType artifacts created
	 * @throws TigerstripeException
	 */
	private int populatePrimitiveTypes(ITigerstripeProgressMonitor monitor) {

		int numberCreated = 0;
		ArtifactManagerSessionImpl artifactMgrSession = null;
		try {
			artifactMgrSession = (ArtifactManagerSessionImpl) TigerstripeProjectFactory.INSTANCE
					.getPhantomProject().getArtifactManagerSession();

			// Bug 536
			(artifactMgrSession.getArtifactManager()).reset(monitor);

			(artifactMgrSession).setLockForGeneration(true);

			IWorkbenchProfile profile = TigerstripeCore
					.getWorkbenchProfileSession().getActiveProfile();
			Collection<IPrimitiveTypeDef> defs = profile
					.getPrimitiveTypeDefs(false);
			for (IPrimitiveTypeDef def : defs) {
				IPrimitiveTypeArtifact artifact = (IPrimitiveTypeArtifact) artifactMgrSession
						.makeArtifact(IPrimitiveTypeArtifact.class.getName());
				artifact.setName(def.getName());
				artifact.setComment(def.getDescription());
				artifact.setFullyQualifiedName(def.getPackageName() + "."
						+ def.getName());

				try {
					artifactMgrSession.addArtifact(artifact);
					artifact.doSilentSave(monitor);
					TigerstripeRuntime.logInfoMessage("Added " + def.getName());
					numberCreated++;
				} catch (TigerstripeException e) {
					// this was an invalid artifact, ignore here.
				}
			}
		} catch (TigerstripeException e) {
			// Error occured, ignoring now until proper logging.
			// FIXME logging
		} finally {
			if (artifactMgrSession != null) {
				(artifactMgrSession).setLockForGeneration(false);
				try {
					artifactMgrSession.refresh(true, monitor);
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
		}
		return numberCreated;
	}

	/**
	 * Returns the URI corresponding to the Phantom Project dir. If this dir
	 * doesn't exist (first run after install) it is created on the fly
	 * 
	 * @return
	 */
	public URI getPhantomURI() {

		String installationRoot = TigerstripeRuntime
				.getTigerstripeRuntimeRoot();
		String phantomPath = installationRoot + File.separator + "phantom";

		File phantomDir = new File(phantomPath);
		return phantomDir.toURI();
	}

	/**
	 * Because each Tigerstripe project is a Java Project, we need to make sure
	 * all the artifact from the Phantom project are actually in the classpath
	 * of the Tigerstripe projects.
	 * 
	 * This is done by compiling all the artifacts in the Phantom project and
	 * putting them in a .zip file that's going to be added as a library to eacy
	 * project.
	 * 
	 * This method creates this jar file (it is added into the classpath by the
	 * org.eclipse.tigerstripe.ui.eclipse.builder.ClasspathUpdater
	 * 
	 */
	private void createJarForEclipsePath() throws TigerstripeException {
		compileArtifacts();
		zipUpCompiledArtifacts();
	}

	private void zipUpCompiledArtifacts() throws TigerstripeException {
		ZipFilePackager zipper = null;
		File[] binFiles = null;
		try {

			File libDir = new File(getPhantomURI().getPath() + "/lib/");
			if (!libDir.exists()) {
				libDir.mkdirs();
			}

			zipper = new ZipFilePackager(getPhantomURI().getPath()
					+ "/lib/phantom.zip", true);

			// the dir with all compiled artifact
			String binDir = getPhantomURI().getPath() + File.separator
					+ "classes";
			File binDirFile = new File(binDir);
			binFiles = binDirFile.listFiles();

			if (binFiles != null && binFiles.length != 0)
				zipper.write(binFiles, "");

			zipper.finished();

		} catch (IOException e) {
			if (e instanceof ZipException
					&& (binFiles == null || binFiles.length == 0)) {
				// Ignore, Zip complains when creating a zip file with zero
				// entry
			} else
				throw new TigerstripeException(
						"Error while packaging up Phantom Project:"
								+ e.getMessage(), e);
		} finally {
			if (zipper != null) {
				try {
					zipper.finished();
				} catch (IOException e) {
					// just ignore it
				}
			}
		}

	}

	private void compileArtifacts() throws TigerstripeException {

		if (!needACompile) // only trigger a compile if there's something to
			// compile
			return;

		String projectDir = getPhantomURI().getPath();

		List<String> compilerArgs = new ArrayList<String>();

		// First clean up
		File classesDir = new File(projectDir, "classes");
		if (classesDir.exists()) {
			classesDir.delete();
		}

		classesDir.mkdirs();

		// Assemble proper command line for compile
		compilerArgs.add("-d");
		String outputDir = projectDir + File.separator + "classes";
		compilerArgs.add(outputDir);

		// Add all artifacts from the src dir
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir(projectDir);

		String[] includes = { "src/**/*.java" };
		String[] excludes = {};
		scanner.setIncludes(includes);
		scanner.setExcludes(excludes);
		scanner.scan();
		String[] files = scanner.getIncludedFiles();
		for (int j = 0; j < files.length; j++) {
			if (!"".equals(files[j])) {
				compilerArgs.add(projectDir + File.separator
						+ scanner.getResource(files[j]).getName());
			}
		}

		// Try and compile
		// Bug 916: when running from cli, ant, this should revert to using the
		// compiler
		// from the JDK
		if (TigerstripeRuntime.getRuntype() == TigerstripeRuntime.CLI_RUN
				|| TigerstripeRuntime.getRuntype() == TigerstripeRuntime.ANT_RUN) {
			try {
				Class mainClass = Class
						.forName("com.sun.tools.javac.main.Main");
				Constructor mainClassConstructor = mainClass
						.getConstructor(new Class[] { String.class,
								PrintWriter.class });
				String arg = "";
				StringWriter sWriter = new StringWriter();
				PrintWriter writer = new PrintWriter(sWriter);
				Object compiler = mainClassConstructor
						.newInstance(new Object[] { arg, writer });

				Method compileMethod = mainClass.getMethod("compile",
						new Class[] { String[].class });
				Integer result = (Integer) compileMethod.invoke(compiler,
						new Object[] { compilerArgs
								.toArray(new String[compilerArgs.size()]) });

				if (result != 0)
					throw new TigerstripeException(
							"Couldn't compile Phantom Project Content: "
									+ sWriter.toString());

			} catch (InstantiationException e) {
				TigerstripeRuntime
						.logErrorMessage(
								"Couldn't compile Phantom Project content : "
										+ e.getMessage()
										+ "\nAre you a JDK or JRE. JDK is required when running headless",
								e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nAre you a JDK or JRE. JDK is required when running headless",
						e);
			} catch (InvocationTargetException e) {
				TigerstripeRuntime
						.logErrorMessage(
								"Couldn't compile Phantom Project content : "
										+ e.getMessage()
										+ "\nAre you a JDK or JRE. JDK is required when running headless",
								e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nAre you a JDK or JRE. JDK is required when running headless",
						e);
			} catch (IllegalAccessException e) {
				TigerstripeRuntime
						.logErrorMessage(
								"Couldn't compile Phantom Project content : "
										+ e.getMessage()
										+ "\nAre you a JDK or JRE. JDK is required when running headless",
								e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nAre you a JDK or JRE. JDK is required when running headless",
						e);
			} catch (NoSuchMethodException e) {
				TigerstripeRuntime
						.logErrorMessage(
								"Couldn't compile Phantom Project content : "
										+ e.getMessage()
										+ "\nAre you a JDK or JRE. JDK is required when running headless",
								e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nAre you a JDK or JRE. JDK is required when running headless",
						e);
			} catch (ClassNotFoundException e) {
				TigerstripeRuntime
						.logErrorMessage(
								"Couldn't compile Phantom Project content : "
										+ e.getMessage()
										+ "\nAre you a JDK or JRE. JDK is required when running headless",
								e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nAre you a JDK or JRE. JDK is required when running headless",
						e);
			}

		} else {
			// during Eclipse runs, simply use the built-in compiler
			StringWriter strWriter = new StringWriter();
			try {
				Class mainClass = Class
						.forName("org.eclipse.jdt.internal.compiler.batch.Main");
				Constructor mainClassConstructor = mainClass
						.getConstructor(new Class[] { PrintWriter.class,
								PrintWriter.class, boolean.class });
				PrintWriter out = new PrintWriter(System.out);
				PrintWriter err = new PrintWriter(System.err);
				Object compiler = mainClassConstructor
						.newInstance(new Object[] { out, err, false });

				Method compileMethod = mainClass.getMethod("compile",
						new Class[] { String[].class });
				Boolean result = (Boolean) compileMethod.invoke(compiler,
						new Object[] { compilerArgs
								.toArray(new String[compilerArgs.size()]) });

				// if (result != 0) {
				// throw new TigerstripeException(
				// "Couldn't compile Phantom Project Content: "
				// + sWriter.toString());
				// }

			} catch (InstantiationException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
			} catch (InvocationTargetException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
			} catch (IllegalAccessException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
			} catch (NoSuchMethodException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
			} catch (ClassNotFoundException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
				throw new TigerstripeException(
						"Couldn't compile Phantom Project content : "
								+ e.getMessage()
								+ "\nProblem built-in Java Compiler", e);
			}
			// Main compiler = new Main(new PrintWriter(System.out),
			// new PrintWriter(System.err), false);
			// compiler.compile(compilerArgs.toArray(new String[compilerArgs
			// .size()]));
			// int status = compiler.globalErrorsCount;
			//
			// if (status != 0) {
			// throw new TigerstripeException(
			// "Error while compiling Phatom Project content: "
			// + strWriter.toString());
			// }
		}
	}
}
