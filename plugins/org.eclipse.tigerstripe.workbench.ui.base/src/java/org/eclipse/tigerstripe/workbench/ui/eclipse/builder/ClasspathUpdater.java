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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.utils.IProjectLocator;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.TigerstripeProgressMonitor;

/**
 * This class updates the Eclipse Project build path whenever a dependency is
 * added to a Tigerstripe project, so that all classes resolve correctly.
 * 
 * @author Eric Dillon
 * 
 */
public class ClasspathUpdater {

	public static void alignDependenciesWithEclipseClasspath(
			ITigerstripeProject tsProject, IProject eclipseProject,
			IProgressMonitor monitor) {

		boolean needUpdate = false;
		boolean phantomLibFound = false;
		boolean legacyCoreArtifactsLib = false;
		ArrayList<IClasspathEntry> newEntryList = new ArrayList<IClasspathEntry>();

		try {
			IJavaProject jProject = JavaCore.create(eclipseProject);
			IClasspathEntry[] entries = jProject.getRawClasspath();

			// Go thru the list an figure out which one is a TS Module or a TS
			// Project
			for (int i = 0; i < entries.length; i++) {
				int matching = entries[i].getPath().matchingFirstSegments(
						eclipseProject.getFullPath());
				String relPath = entries[i].getPath().removeFirstSegments(
						matching).toString();
				if (entries[i].getPath().getDevice() != null)
					relPath = relPath.replaceFirst(entries[i].getPath()
							.getDevice(), "");

				if (entries[i].getEntryKind() == IClasspathEntry.CPE_PROJECT) {
					// TigerstripeRuntime.logInfoMessage(ClasspathUpdater.class,
					// " Handling CPE_PROJECT="
					// + entries[i].getPath());
					ITigerstripeProject prjRef = null;

					// We need to build a list of project references that are
					// in the list of reference projects in Eclipse
					try {
						// IPath path = entries[i].getPath();
						// String uriStr =
						// eclipseProject.getWorkspace().getRoot().getLocation().toString()
						// + path.toFile().getPath();
						// TigerstripeRuntime.logInfoMessage(ClasspathUpdater.class,
						// "URIStr=" + uriStr );
						// URI cpeURI = (new File(uriStr)).toURI();
						// prjRef = API.getDefaultProjectSession()
						// .makeTigerstripeProject(cpeURI, null);

						IProjectLocator loc = (IProjectLocator) InternalTigerstripeCore
								.getFacility(InternalTigerstripeCore.PROJECT_LOCATOR_FACILITY);

						URI uri = loc.locate(tsProject, entries[i].getPath()
								.toString());
						prjRef = (ITigerstripeProject) TigerstripeCore
								.getDefaultProjectSession()
								.makeTigerstripeProject(uri, null);

					} catch (TigerstripeException e) {
						// ignore here
					}
					if (prjRef == null) {
						// This is not a TS project but it's been put in the
						// path,
						// we keep it.
						// TigerstripeRuntime.logInfoMessage(ClasspathUpdater.class,
						// "Not a TS project, keeping it");
						newEntryList.add(entries[i]);
					} else {
						try {
							if (tsProject.hasReference(prjRef)) {
								// It is in the list of refs of the project
								// descriptor, so we keep it.
								// TigerstripeRuntime.logInfoMessage(" A TS
								// project in the descriptor, keeping it");
								newEntryList.add(entries[i]);
							} else {
								// This is not listed as a ref in the
								// project descriptor, so should not be in the
								// classpath.
								// This also means we'll need to update the
								// classpath.
								// TigerstripeRuntime.logInfoMessage(" a TS prj,
								// not in the descriptor, removing it");
								needUpdate = true;
							}
						} catch (TigerstripeException e) {
							// ignore here
							TigerstripeRuntime.logErrorMessage(
									"TigerstripeException detected", e);
							;
						}
					}
				} else if (entries[i].getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
					Dependency dep = null;
					try {
						// First let's make sure this is not an orphan dep
						if (!eclipseProject.getParent().exists(
								entries[i].getPath())) {
							// don't add it. But that means we need an update
							needUpdate = true;
						} else {
							dep = new Dependency(
									((TigerstripeProjectHandle) tsProject)
											.getTSProject(), relPath);
							if (!dep.isValid(new TigerstripeProgressMonitor(
									monitor))) {
								// This is not a TS module, so it has been put
								// in
								// the
								// classpath
								// by the user on purpose. Keep it.
								newEntryList.add(entries[i]);
							} else {
								// This is a TS Module.
								if (tsProject.hasDependency(dep)) {
									// It is in the list of deps of the project
									// descriptor, so
									// we keep it.
									newEntryList.add(entries[i]);
								} else {
									// This is not listed as a dependency in the
									// project
									// descriptor, so should not be in the
									// classpath.
									// This also means we'll need to update the
									// classpath.
									needUpdate = true;
								}
							}
						}
					} catch (TigerstripeException e) {
						// this means the project is not valid for any reason.
						EclipsePlugin.log(e);
					}
				} else if (entries[i].getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
					// Check that the Tigerstripe Phantom Lib is in there and in
					// any case, just add it
					if (entries[i].getPath().equals(
							new Path(ITigerstripeConstants.PHANTOMLIB))) {
						phantomLibFound = true;
						needUpdate = true; // there seems to be problems if the
						// reference is
						// created before the zip file exists, so a refresh is
						// necessary so a "clean
						// project action" works
					} else if (entries[i].getPath().equals(
							new Path(ITigerstripeConstants.LEGACYCOREOSSJ_LIB))) {
						legacyCoreArtifactsLib = true;
						needUpdate = true;
					}
					newEntryList.add(entries[i]);
				} else {
					// if it's not a Jar lib add it anyway
					newEntryList.add(entries[i]);
				}
			}

			// At this point, we've made sure that all the jars in the classpath
			// are either pure jar files that should remain in the classpath, or
			// TS modules that are mentioned in the TS descriptor.

			// We still need to add the TS modules mentioned in the descriptor
			// that are not yet in the classpath.
			try {
				IDependency[] dependencies = tsProject.getDependencies();
				for (int i = 0; i < dependencies.length; i++) {

					// TODO: remove for #299
					if (dependencies[i]
							.getPath()
							.equals(
									TigerstripeRuntime
											.getProperty(TigerstripeRuntime.CORE_OSSJ_ARCHIVE))) {

					} else {
						IPath localPath = eclipseProject.getFile(
								dependencies[i].getPath()).getFullPath();
						IClasspathEntry newEntry = newLibraryEntry(localPath);

						if (!newEntryList.contains(newEntry)) {
							newEntryList.add(newEntry);
							needUpdate = true;
						}
					}
				}
			} catch (TigerstripeException e) {
				// invalid project
			}

			// We need to add all the project references mentioned in the
			// descriptor
			// that are not yet in the classpath
			try {
				ITigerstripeProject[] projects = tsProject
						.getReferencedProjects();
				for (ITigerstripeProject project : projects) {
					IClasspathEntry newEntry = newProjectEntry(project);

					if (!contains(newEntryList, newEntry)) {
						newEntryList.add(newEntry);
						needUpdate = true;
					}
				}

			} catch (TigerstripeException e) {

			}

			if (!phantomLibFound) {
				IClasspathEntry entry = JavaCore.newVariableEntry(new Path(
						ITigerstripeConstants.PHANTOMLIB), null, null);
				newEntryList.add(entry);
				needUpdate = true;
			}

			// TODO: remove this @see #299
			// This is temporary needed until a profile can contain artifacts.
			// This ensures compatibility
			// with previous versions of tigerstripe workbench for OSS/J
			// projects.
			if (!legacyCoreArtifactsLib) {
				IClasspathEntry entry = JavaCore.newVariableEntry(new Path(
						ITigerstripeConstants.LEGACYCOREOSSJ_LIB), null, null);
				newEntryList.add(entry);
				needUpdate = true;
			}

			// Now if the classpath needs an update... let's do it!
			if (needUpdate) {
				final IClasspathEntry[] newEntries = newEntryList
						.toArray(new IClasspathEntry[newEntryList.size()]);

				jProject.setRawClasspath(newEntries, monitor);
			}
		} catch (JavaModelException e) {
			EclipsePlugin.log(e);
		}
	}

	public static IClasspathEntry newLibraryEntry(IPath path) {
		return new ClasspathEntry(IPackageFragmentRoot.K_BINARY,
				IClasspathEntry.CPE_LIBRARY, JavaProject
						.canonicalizedPath(path), ClasspathEntry.INCLUDE_ALL, // inclusion
				// patterns
				ClasspathEntry.EXCLUDE_NONE, // exclusion patterns
				null, null, null, // specific output folder
				false, ClasspathEntry.NO_ACCESS_RULES, false, // no access
				// rules to
				// combine
				ClasspathEntry.NO_EXTRA_ATTRIBUTES);
	}

	private static IClasspathEntry newProjectEntry(ITigerstripeProject project) {
		IPath path = new Path(project.getURI().getPath());
		return new ClasspathEntry(IPackageFragmentRoot.K_BINARY,
				IClasspathEntry.CPE_PROJECT, JavaProject
						.canonicalizedPath(new Path("/" + path.lastSegment())),
				ClasspathEntry.INCLUDE_ALL, // inclusion
				// patterns
				ClasspathEntry.EXCLUDE_NONE, // exclusion patterns
				null, null, null, // specific output folder
				false, ClasspathEntry.NO_ACCESS_RULES, false, // no access
				// rules to
				// combine
				ClasspathEntry.NO_EXTRA_ATTRIBUTES);
	}

	private static boolean contains(List<IClasspathEntry> newEntryList,
			IClasspathEntry entry) {
		for (IClasspathEntry ent : newEntryList) {
			if (entry.getPath().equals(ent.getPath()))
				return true;
		}
		return false;
	}
}
