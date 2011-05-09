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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
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

	private static final String TEMPLATES_DIR = "templates";
	private static final String CLASSES_DIR = "classes";
	private GeneratorProjectDescriptor descriptor;

	public PluggablePluginProjectPackager(GeneratorProjectDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	private static final FileFilter REPOSITORY_FILTER = new FileFilter() {

		public boolean accept(File pathname) {
			return !isRepositoryMetadataDir(pathname);
		}
	};

	public void packageUpProject(IProgressMonitor monitor, IPath path)
			throws TigerstripeException {
		ZipFilePackager zipper = null;
		try {

			// First validate
			monitor.subTask("Compiling plugin code");
			compileProject();
			monitor.worked(2);

			String sPath = path.toOSString();
			zipper = new ZipFilePackager(sPath, true);

			// Package up the descriptor
			monitor.subTask("Creating .zip file");
			File descriptorFile = descriptor.getFullPath();
			zipper.write(descriptorFile, descriptor.getFilename());
			monitor.worked(1);

			// the templates dir
			String projectDir = descriptor.getBaseDir().getAbsolutePath();
			String templatesDir = projectDir + File.separator + TEMPLATES_DIR;
			File templateDirFile = new File(templatesDir);
			File[] templateFiles = templateDirFile.listFiles(REPOSITORY_FILTER);
			zipper.write(templateFiles, TEMPLATES_DIR);
			monitor.worked(2);

			// the bin dir with all compiled classes
			String binDir = projectDir + File.separator + CLASSES_DIR;
			File binDirFile = new File(binDir);
			File[] binFiles = binDirFile.listFiles(REPOSITORY_FILTER);
			zipper.write(binFiles, CLASSES_DIR);
			monitor.worked(2);

			// package up user-jars
			String baseDir = projectDir + File.separator;
			Collection<File> classpathEntryFiles = new ArrayList<File>();
			for (IPluginClasspathEntry entry : descriptor.getClasspathEntries()) {
				String entryPath = baseDir + entry.getRelativePath();
				File entryFile = new File(entryPath);
				classpathEntryFiles.add(entryFile);
				if (entryFile.exists() && !entryFile.isDirectory()) {
					zipper.write(entryFile, entry.getRelativePath());
				}
			}

			// package up specified additionalFiles
			// Avoid duplicate entries
			List<File> additionalFiles = new ArrayList<File>(
					Arrays.asList(computeAdditionalFiles()));
			additionalFiles.removeAll(Arrays.asList(templateFiles));
			additionalFiles.removeAll(Arrays.asList(binFiles));
			additionalFiles.removeAll(classpathEntryFiles);
			additionalFiles.remove(descriptorFile);

			Iterator<File> it = additionalFiles.iterator();

			while (it.hasNext()) {
				File file = it.next();
				if (isRepositoryMetadataFile(file)) {
					it.remove();
				}
			}

			zipper.write(additionalFiles.toArray(new File[0]), "", baseDir);

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

	private static final Set<String> REPOSITORY_METADATA_NAMES = new HashSet<String>(
			Arrays.asList(".svn", ".cvs", ".git"));

	private static boolean isRepositoryMetadataDir(File file) {
		return REPOSITORY_METADATA_NAMES.contains(file.getName())
				&& file.isDirectory();
	}

	private static boolean isRepositoryMetadataFile(File file) {

		if (isRepositoryMetadataDir(file)) {
			return true;
		}

		while (file != null) {
			if (REPOSITORY_METADATA_NAMES.contains(file.getName())) {
				return true;
			}
			file = file.getParentFile();
		}
		return false;
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
		File classesDir = new File(projectDir, CLASSES_DIR);
		if (classesDir.exists()) {
			classesDir.delete();
		}

		classesDir.mkdirs();

		// Assemble proper command line for compile

		// First compile with 1.5 code by default for now
		compilerArgs.add("-1.5");

		// then look at the classpath
		compilerArgs.add("-d");
		String outputDir = projectDir + File.separator + CLASSES_DIR;
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
					classpath += path.toOSString() + File.pathSeparator;
				} else if (entry.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
					IPath path = JavaCore.getResolvedVariablePath(entry
							.getPath());
					classpath += path.toOSString() + File.pathSeparator;
				}
			}

			// All the runtime classpath entries of the project
			for (IPluginClasspathEntry entry : descriptor.getClasspathEntries()) {
				classpath += descriptor.getBaseDir() + File.separator
						+ entry.getRelativePath() + File.pathSeparator;
			}

			// Finally, add the TS-specific jars at the end
			String tsApiJar = JavaCore.getClasspathVariable(
					ITigerstripeConstants.EXTERNALAPI_LIB).toPortableString();
			classpath += tsApiJar + File.pathSeparator;
			String equinoxJar = JavaCore.getClasspathVariable(
					ITigerstripeConstants.EQUINOX_COMMON).toPortableString();
			classpath += equinoxJar + File.pathSeparator;

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
							.getLocation().toPortableString());
				}
			}

		} catch (JavaModelException e) {
			TigerstripeRuntime
					.logErrorMessage("JavaModelException detected", e);
		}

		// Try and compile
		// Compile only if there is something to compile
		if (unitsNumber > 0) {
			StringWriter outWriter = new StringWriter();
			PrintWriter out = new PrintWriter(outWriter);
			StringWriter errWriter = new StringWriter();
			PrintWriter err = new PrintWriter(errWriter);
			Main compiler = new Main(out, err, false);
			boolean success = compiler.compile(compilerArgs
					.toArray(new String[compilerArgs.size()]));
			int status = compiler.globalErrorsCount;

			String outStr = outWriter.toString();
			String errStr = errWriter.toString();

			if (status != 0) {
				throw new TigerstripeException(
						" Couldn't compile plugin code: " + errStr);
			} else if (errStr.length() != 0) {
				IStatus s = new Status(IStatus.WARNING, BasePlugin.PLUGIN_ID,
						errStr + "(" + compilerArgs + ")");
				BasePlugin.log(s);
			}
		}
	}

}
