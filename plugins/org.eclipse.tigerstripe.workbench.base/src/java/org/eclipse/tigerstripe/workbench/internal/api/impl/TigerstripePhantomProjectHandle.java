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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.module.packaging.ModulePackager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProjectMgr;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.ZipFilePackager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;

public class TigerstripePhantomProjectHandle extends TigerstripeProjectHandle
		implements IPhantomTigerstripeProject {

	/**
	 * 
	 * @param projectURI -
	 *            URI of the project directory
	 */
	public TigerstripePhantomProjectHandle() throws TigerstripeException {
		super(getPhantomURI());
	}

	public Collection<IPrimitiveTypeArtifact> getReservedPrimitiveTypes()
			throws TigerstripeException {
		return getArtifactManagerSession().getReservedPrimitiveTypes();
	}

	@Override
	public TigerstripeProject getTSProject() throws TigerstripeException {
		return getPhantomProject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject#
	 * getReferencingModels()
	 */
	public ModelReference[] getReferencingModels(int level) throws TigerstripeException {
		//TODO should return all projects?
		return new ModelReference[0];
	}

	public PhantomTigerstripeProject getPhantomProject()
			throws TigerstripeException {
		// there's only one phantom project it's maintained exclusively by
		// the PhantomTigerstripeProjectMgr
		return BasePlugin.getDefault().getPhantomTigerstripeProjectMgr()
				.getPhantomProject();
	}

	public IModulePackager getPackager() {
		return new ModulePackager(this);
	}

	public static URI getPhantomURI() {
		return BasePlugin.getDefault().getPhantomTigerstripeProjectMgr()
				.getPhantomURI();
	}

	@Override
	protected WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		throw new TigerstripeException("Operation not supported.");
	}

	@Override
	public boolean isDirty() {
		return false;
	}
	
	public void init() throws TigerstripeException {
		boolean needACompile = populatePrimitiveTypes(new NullProgressMonitor()) != 0;
		createJarForEclipsePath(needACompile);
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
	private void createJarForEclipsePath(boolean needACompile) throws TigerstripeException {
		if (needACompile) // only trigger a compile if there's something to compile
			compileArtifacts();
		zipUpCompiledArtifacts();
	}

	/**
	 * Creates PrimitiveType artifacts in the phantom project corresponding to
	 * all definitions in the profile.
	 * 
	 * @return returns the number of PrimitiveType artifacts created
	 * @throws TigerstripeException
	 */
	private int populatePrimitiveTypes(IProgressMonitor monitor) {

		int numberCreated = 0;
		ArtifactManagerSessionImpl artifactMgrSession = null;
		try {
			artifactMgrSession = (ArtifactManagerSessionImpl) getArtifactManagerSession();

			// Bug 536
			artifactMgrSession.getArtifactManager().reset(monitor);

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
			BasePlugin.log(e);
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

	private void compileArtifacts() throws TigerstripeException {

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
				Class<?> mainClass = Class
						.forName("com.sun.tools.javac.main.Main");
				Constructor<?> mainClassConstructor = mainClass
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
			@SuppressWarnings("unused")
			StringWriter strWriter = new StringWriter();
			try {
				Class<?> mainClass = Class
						.forName("org.eclipse.jdt.internal.compiler.batch.Main");
				Constructor<?> mainClassConstructor = mainClass
						.getConstructor(new Class[] { PrintWriter.class,
								PrintWriter.class, boolean.class });
				PrintWriter out = new PrintWriter(System.out);
				PrintWriter err = new PrintWriter(System.err);
				Object compiler = mainClassConstructor
						.newInstance(new Object[] { out, err, false });

				Method compileMethod = mainClass.getMethod("compile",
						new Class[] { String[].class });
				@SuppressWarnings("unused")
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

			// make sure there's at least one file in the zip or else it's not valid
			File empty = new File(getPhantomURI().getPath() + "/empty.txt");
			empty.createNewFile();
			zipper.write(empty, "empty.txt");
			
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
}
