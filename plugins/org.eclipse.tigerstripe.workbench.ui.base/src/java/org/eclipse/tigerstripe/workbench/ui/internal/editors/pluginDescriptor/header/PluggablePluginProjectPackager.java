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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.header;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.internal.core.util.ZipFilePackager;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

/**
 * Whenever a Plugin Project needs to be deployed/packaged up, the operation is
 * delegated to this class
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PluggablePluginProjectPackager {

	private GeneratorProjectDescriptor descriptor;

	public PluggablePluginProjectPackager(GeneratorProjectDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public void packageUpProject(IProgressMonitor monitor, String path)
			throws TigerstripeException {
		ZipFilePackager zipper = null;
		try {

			// First validate
			monitor.subTask("Compiling plugin code");
			compileProject();
			monitor.worked(2);

			zipper = new ZipFilePackager(path, true);

			// Package up the descriptor
			monitor.subTask("Creating .zip file");
			File descriptorFile = descriptor.getFullPath();
			zipper.write(descriptorFile, descriptor.getFilename());
			monitor.worked(1);

			// the templates dir
			String projectDir = descriptor.getBaseDir().getAbsolutePath();
			String templatesDir = projectDir + File.separator + "templates";
			File templateDirFile = new File(templatesDir);
			File[] templateFiles = templateDirFile.listFiles();
			zipper.write(templateFiles, "templates");
			monitor.worked(2);

			// the bin dir with all compiled classes
			String binDir = projectDir + File.separator + "classes";
			File binDirFile = new File(binDir);
			File[] binFiles = binDirFile.listFiles();
			zipper.write(binFiles, "classes");
			monitor.worked(2);

			// package up user-jars
			String baseDir = projectDir + File.separator;
			for (IPluginClasspathEntry entry : descriptor.getClasspathEntries()) {
				String entryPath = baseDir + entry.getRelativePath();
				File entryFile = new File(entryPath);
				if (entryFile.exists()) {
					zipper.write(entryFile, entry.getRelativePath());
				}
			}

			// package up specified additionalFiles
			File[] additionalFiles = computeAdditionalFiles();
			zipper.write(additionalFiles, "", baseDir);

			zipper.finished();

		} catch (IOException e) {
			throw new TigerstripeException("Error while packaging up '"
					+ descriptor.getProjectDetails().getName() + "':"
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

	protected File[] computeAdditionalFiles() {
		List<File> result = new ArrayList<File>();
		String projectDir = descriptor.getBaseDir().getAbsolutePath();
		String baseDir = projectDir + File.separator;

		List<String> includePaths = descriptor
				.getAdditionalFiles(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE);
		List<String> excludePaths = descriptor
				.getAdditionalFiles(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE);
		for (String path : includePaths) {
			String entryPath = baseDir + path;
			File target = new File(entryPath);

			if (target.exists()) {
				if (target.isFile()) {
					result.add(target);
				} else if (target.isDirectory()) {
					result.addAll(getRecursiveContent(target));
				}
			}
		}

		// remove any excluded file
		for (Iterator<File> iter = result.iterator(); iter.hasNext();) {
			File target = iter.next();
			for (String excluded : excludePaths) {
				String bse = (baseDir + excluded).replace(File.separatorChar,
						'/');
				String tse = target.getAbsolutePath().replace(
						File.separatorChar, '/');
				if (bse.equals(tse)) {
					iter.remove();
				}
			}
		}
		return result.toArray(new File[result.size()]);
	}

	private List<File> getRecursiveContent(File folder) {
		List<File> result = new ArrayList<File>();
		if (!folder.isDirectory())
			result.add(folder);
		else {
			File[] paths = folder.listFiles();
			for (File target : paths) {
				if (target.isFile()) {
					result.add(target);
				} else if (target.isDirectory()) {
					result.addAll(getRecursiveContent(target));
				}
			}
		}

		return result;
	}

	protected void compileProject() throws TigerstripeException {
		String projectDir = descriptor.getBaseDir().getAbsolutePath();

		List<String> compilerArgs = new ArrayList<String>();

		// First clean up
		File classesDir = new File(projectDir, "classes");
		if (classesDir.exists()) {
			classesDir.delete();
		}

		classesDir.mkdirs();

		// Assemble proper command line for compile

		// First compile with 1.6 code by default for now
		compilerArgs.add("-1.6");

		// then look at the classpath
		compilerArgs.add("-d");
		String outputDir = projectDir + File.separator + "classes";
		compilerArgs.add(outputDir);
		int unitsNumber = 0;

		IJavaProject jProject = (IJavaProject) descriptor
				.getAdapter(IJavaProject.class);
		try {
			// Add all the libraries on the classpath
			// The user may have added all kinds of JARs onto the classpath,
			// these need to be there.
			String classpath = "";
			IClasspathEntry[] classpathEntries = jProject.getRawClasspath();
			for (IClasspathEntry entry : classpathEntries) {
				if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
					IPath path = entry.getPath();
					classpath += path.toOSString() + ";";
				} else if (entry.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
					// TODO not sure what to do here if the classpath entry is
					// not TS's???
				}
			}

			// All the runtime classpath entries of the project
			for (IPluginClasspathEntry entry : descriptor.getClasspathEntries()) {
				classpath += descriptor.getBaseDir() + File.separator
						+ entry.getRelativePath() + ";";
			}

			// Finally, add the TS-specific jars at the end
			String runtimeRoot = TigerstripeRuntime
					.getProperty(TigerstripeRuntime.EXTERNAL_API_ARCHIVE);
			classpath += runtimeRoot + ";";
			String equinoxJar = JavaCore.getClasspathVariable(
					ITigerstripeConstants.EQUINOX_COMMON).toOSString();
			classpath += equinoxJar + ";";

			if (classpath.length() != 0) {
				compilerArgs.add("-classpath");
				compilerArgs.add(classpath);
			}

			// Then assemble the list of source files to compile
			IPackageFragment[] fragments = jProject.getPackageFragments();
			for (IPackageFragment fragment : fragments) {
				ICompilationUnit[] units = fragment.getCompilationUnits();
				for (ICompilationUnit unit : units) {
					unitsNumber++;
					compilerArgs.add(unit.getCorrespondingResource()
							.getLocation().toOSString());
				}
			}

		} catch (JavaModelException e) {
			TigerstripeRuntime
					.logErrorMessage("JavaModelException detected", e);
		}

		// Try and compile
		// Compile only if there is something to compile
		if (unitsNumber > 0) {
			Main compiler = new Main(new PrintWriter(System.out),
					new PrintWriter(System.err), false);
			compiler.compile(compilerArgs.toArray(new String[compilerArgs
					.size()]));
			int status = compiler.globalErrorsCount;
			// Main javac = new Main();
			StringWriter writer = new StringWriter();
			// PrintWriter printWriter = new PrintWriter(writer);
			// int status = Main.compile(compilerArgs
			// .toArray(new String[compilerArgs.size()]));

			if (status != 0)
				throw new TigerstripeException(
						" Couldn't compile plugin code: " + writer.toString());
		}
	}

}
