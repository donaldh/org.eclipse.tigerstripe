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
package org.eclipse.tigerstripe.workbench.internal.core.module.packaging;

import java.io.File;
import java.io.FileWriter;
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.module.InvalidModuleException;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleDescriptorModel;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.core.util.FileUtils;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

/**
 * A module packager is responsible for assembling all the components of a
 * module and wrapping them into a jar file that's ready to be referenced in a
 * TS project
 * 
 * @author Eric Dillon
 * @since 0.4
 */
public class ModulePackager implements IModulePackager {

	// The project to package up
	private ITigerstripeModelProject tsProject;

	public IModuleHeader makeHeader() {
		return new ModuleHeader();
	}

	/**
	 * 
	 * @param tsProject
	 */
	public ModulePackager(ITigerstripeModelProject tsProject) {
		setTSProject(tsProject);
	}

	// ===================================================
	public ITigerstripeModelProject getTSProject() {
		return this.tsProject;
	}

	private void setTSProject(ITigerstripeModelProject tsProject) {
		this.tsProject = tsProject;
	}

	/**
	 * Package up the project into a TS module. The resulting jar file is
	 * identified by the given URI.
	 * 
	 */
	public void packageUp(URI jarURI, File classesDir, IModuleHeader header,
			boolean includeDiagrams, boolean includeAnnotations,
			IProgressMonitor monitor) throws TigerstripeException,
			InvalidModuleException {

		TigerstripeRuntime.logTraceMessage("Packaging "
				+ jarURI.toASCIIString() + " from " + classesDir.toString()
				+ " - " + header.getModuleID());
		// validate header

		// Validate URI

		// Validate Project

		// Populate the module
		File tmpJarDir = createTmpJarDirectory();

		/**
		 * @since 2.2.1 When running headless from Ant we need to compile the
		 *        artifacts. Otherwise at this point the project has been built
		 *        within eclipse, which means the POJOs have been compiled by
		 *        the Eclipse compiler.
		 */
		if (TigerstripeRuntime.getRuntype() != TigerstripeRuntime.ECLIPSE_GUI_RUN)
			compileArtifacts(classesDir);

		try {
			copyCompiledArtifacts(classesDir, tmpJarDir);
			copyDescriptor(tmpJarDir);

			if (includeAnnotations) {
				copyAnnotations(tmpJarDir);
			}

			createModuleDescriptor(tmpJarDir, (ModuleHeader) header, monitor);
			createJarFile(tmpJarDir, jarURI);
			removeTmpJarDirectory(tmpJarDir);
		} catch (IOException e) {
			throw new TigerstripeException("Error while packaging Module: "
					+ e.getMessage(), e);
		}
		// // read in the newly created module into a ModuleRef
		// ModuleRefFactory factory = ModuleRefFactory.getInstance();
		// ModuleRef result = factory.parseModule(jarURI, monitor);
		//
		// TigerstripeRuntime.logTraceMessage("Packaging "
		// + jarURI.toASCIIString() + " from " + classesDir.toString()
		// + " - " + header.getModuleID() + ". Done.");
		//
		// return result;
	}

	/**
	 * Compile the artifacts
	 * 
	 * This relies on the Javac compiler to be in the classpath. So the JDK is
	 * needed to run this piece of code (using the JRE will trigger a
	 * TigerstripeException mentioning this).
	 * 
	 * @param classesDir
	 * @throws TigerstripeException
	 */
	protected void compileArtifacts(File classesDir)
			throws TigerstripeException {
		String projectDir = getTSProject().getLocation().toOSString();

		List<String> compilerArgs = new ArrayList<String>();

		// First clean up
		if (classesDir.exists()) {
			classesDir.delete();
		}

		classesDir.mkdirs();

		// Assemble proper command line for compile

		// then look at the classpath
		compilerArgs.add("-d");
		String outputDir = classesDir.getAbsolutePath();
		compilerArgs.add(outputDir);
		int unitsNumber = 0;

		// Add all the dependencies of this project
		try {
			String classpath = "";
			ITigerstripeModelProject project = getTSProject();
			for (IDependency dep : project.getDependencies()) {
				classpath += getTSProject().getLocation().toFile()
						+ File.separator + dep.getPath() + ";";
			}

			if (classpath.length() != 0) {
				compilerArgs.add("-classpath");
				compilerArgs.add(classpath);
			}

			// Then assemble the list of artifact POJO files to compile
			IQueryAllArtifacts query = (IQueryAllArtifacts) project
					.getArtifactManagerSession().makeQuery(
							IQueryAllArtifacts.class.getName());
			query.setIncludeDependencies(false);
			Collection artifacts = project.getArtifactManagerSession()
					.queryArtifact(query);

			for (Object obj : artifacts) {
				AbstractArtifact artifact = (AbstractArtifact) obj;
				unitsNumber++;
				compilerArgs.add(getTSProject().getLocation().toFile()
						+ File.separator + artifact.getArtifactPath());
			}

		} catch (TigerstripeException e) {
			TigerstripeRuntime
					.logErrorMessage(
							"Error while compiling artifacts for project '"
									+ getTSProject().getName() + "': "
									+ e.getMessage(), e);
		}

		// Try and compile
		// Compile only if there is something to compile
		if (unitsNumber > 0) {
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
							"Couldn't compile artifacts: " + sWriter.toString());

			} catch (InstantiationException e) {
				TigerstripeRuntime.logErrorMessage("Can't package project '"
						+ getTSProject().getName()
						+ "' into a module without JDK. An error occured: "
						+ e.getMessage(), e);
				throw new TigerstripeException("Can't package project '"
						+ getTSProject().getName()
						+ "' into a module without JDK. An error occured: "
						+ e.getMessage(), e);
			} catch (InvocationTargetException e) {
				TigerstripeRuntime.logErrorMessage("Can't package project '"
						+ getTSProject().getName()
						+ "' into a module without JDK. An error occured: "
						+ e.getMessage(), e);
				throw new TigerstripeException("Can't package project '"
						+ getTSProject().getName()
						+ "' into a module without JDK. An error occured: "
						+ e.getMessage(), e);
			} catch (IllegalAccessException e) {
				TigerstripeRuntime.logErrorMessage("Can't package project '"
						+ getTSProject().getName()
						+ "' into a module without JDK. An error occured: "
						+ e.getMessage(), e);
				throw new TigerstripeException("Can't package project '"
						+ getTSProject().getName()
						+ "' into a module without JDK. An error occured: "
						+ e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				TigerstripeRuntime.logErrorMessage("Can't package project '"
						+ getTSProject().getName()
						+ "' into a module without JDK. An error occured: "
						+ e.getMessage(), e);
				throw new TigerstripeException("Can't package project '"
						+ getTSProject().getName()
						+ "' into a module without JDK. An error occured: "
						+ e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				// it seems we're not running the JDK, so without compiler we
				// can't make
				// this work.
				TigerstripeRuntime
						.logErrorMessage(
								"Can't package project '"
										+ getTSProject().getName()
										+ "' into a module without JDK. Please install Java JDK instead of Java JRE.",
								e);
				throw new TigerstripeException(
						"Can't package project '"
								+ getTSProject().getName()
								+ "' into a module without JDK. Please install Java JDK instead of Java JRE.",
						e);
			}
			// Main compiler = new Main(new PrintWriter(System.out),
			// new PrintWriter(System.err), false);
			// compiler.compile(compilerArgs.toArray(new String[compilerArgs
			// .size()]));
			// int status = compiler.globalErrorsCount;
			// // Main javac = new Main();
			// StringWriter writer = new StringWriter();
			// // PrintWriter printWriter = new PrintWriter(writer);
			// // int status = Main.compile(compilerArgs
			// // .toArray(new String[compilerArgs.size()]));
			//
			// if (status != 0) {
			// throw new TigerstripeException(
			// " Couldn't compile plugin code: " + writer.toString());
			// }
		}
	}

	/**
	 * Sets up a temp directory where the Jar components will be assembled
	 * before they're jar-ed up.
	 * 
	 * @return the newly created directory
	 */
	protected File createTmpJarDirectory() {
		File tmp = new File(getOSSpecificTmpLocation() + File.separator
				+ "tsExport" + System.currentTimeMillis());

		while (tmp.exists()) {
			tmp = new File(getOSSpecificTmpLocation() + File.separator
					+ "tsExport" + System.currentTimeMillis());
		}

		tmp.mkdirs();
		return tmp;
	}

	/**
	 * Removes the given dir
	 * 
	 * @param tmpDir
	 */
	protected void removeTmpJarDirectory(File tmpDir) {
		if (tmpDir != null && tmpDir.exists()) {
			tmpDir.delete();
		}
	}

	/**
	 * Returns the OS Specific tmp dir
	 * 
	 * @return
	 */
	protected String getOSSpecificTmpLocation() {
		String result = System.getProperty("java.io.tmpdir");
		return result;
	}

	/**
	 * Compiles the src artifacts and makes sure the .class files are actually
	 * copied in the tmpDir ready for packaging.
	 * 
	 */
	protected void copyCompiledArtifacts(File tmpDir, File classesDir)
			throws IOException {
		FileUtils.copyDir(tmpDir.getAbsolutePath(), classesDir
				.getAbsolutePath(), true);
	}

	/**
	 * Compiles the src artifacts and makes sure the .class files are actually
	 * copied in the tmpDir ready for packaging.
	 * 
	 */
	protected void copyDescriptor(File tmpDir) throws TigerstripeException {
		File descriptor = new File(getTSProject().getLocation().toFile()
				.toURI().getPath()
				+ File.separator + ITigerstripeConstants.PROJECT_DESCRIPTOR);
		try {
			FileUtils.copy(descriptor.getAbsolutePath(), tmpDir
					.getAbsolutePath(), true);
		} catch (IOException e) {
			throw new TigerstripeException(
					"Error while copying Descriptor to tmp Dir: "
							+ e.getMessage(), e);
		}
	}

	protected void copyAnnotations(File tmpDir) throws TigerstripeException {
		File annDir = new File(getTSProject().getLocation().toFile().toURI()
				.getPath()
				+ File.separator + ITigerstripeConstants.ANNOTATION_DIR);

		if (!annDir.exists()) // no annotations?
			return;

		File tgDir = new File(tmpDir.getAbsolutePath() + File.separator
				+ ITigerstripeConstants.ANNOTATION_DIR);
		if (!tgDir.exists()) {
			tgDir.mkdirs();
		}

		try {
			FileUtils.copyDir(annDir.getAbsolutePath(), tmpDir
					.getAbsolutePath()
					+ File.separator + ITigerstripeConstants.ANNOTATION_DIR,
					true);
		} catch (IOException e) {
			throw new TigerstripeException(
					"Error while copying Descriptor to tmp Dir: "
							+ e.getMessage(), e);
		}
	}

	/**
	 * Creates the module descriptor for this module and stores it in the tmpDir
	 * ready for packaging.
	 * 
	 * @param tmpDir
	 */
	protected void createModuleDescriptor(File tmpDir, ModuleHeader header,
			IProgressMonitor monitor) throws TigerstripeException {
		ModuleDescriptorModel model = new ModuleDescriptorModel(getTSProject());
		model.setModuleHeader(header);

		String filename = tmpDir.getAbsolutePath() + File.separator
				+ ModuleDescriptorModel.DESCRIPTOR;

		try {
			File xmlDescriptor = new File(filename);
			FileWriter writer = new FileWriter(xmlDescriptor);
			model.writeModel(writer, monitor);
		} catch (IOException e) {
			throw new TigerstripeException("Error creating Module Descriptor",
					e);
		}
	}

	/**
	 * Creates the final jar file for this module, based on the given tmpDir
	 * 
	 * @param tmpDir
	 */
	protected void createJarFile(File tmpDir, URI jarURI) throws IOException {
		File f = new File(jarURI);
		FileUtils.createJar(f.getAbsolutePath(), tmpDir.getAbsolutePath());
	}
}
