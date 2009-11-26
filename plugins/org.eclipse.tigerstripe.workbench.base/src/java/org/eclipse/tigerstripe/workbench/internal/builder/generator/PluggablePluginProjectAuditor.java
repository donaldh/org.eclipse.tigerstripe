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
package org.eclipse.tigerstripe.workbench.internal.builder.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.PluginProjectCreator;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRule;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * This is the incremental auditor for a Pluggable Plugin Project.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PluggablePluginProjectAuditor extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = BuilderConstants.PLUGINPROJECT_BUILDER_ID;

	public PluggablePluginProjectAuditor() {
		super();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {

		if (shouldAudit(kind)) {
			final int internalKind = kind;
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					auditProject(internalKind, monitor);
				}
			}, monitor);
		}
		return null;
	}

	public static void reportError(String msg, IResource srcFile, int violation) {
		reportProblem(msg, srcFile, violation, IMarker.SEVERITY_ERROR);
	}

	public static void reportWarning(String msg, IResource srcFile,
			int violation) {
		reportProblem(msg, srcFile, violation, IMarker.SEVERITY_WARNING);
	}

	public static void reportInfo(String msg, IResource srcFile, int violation) {
		reportProblem(msg, srcFile, violation, IMarker.SEVERITY_INFO);
	}

	private static void reportProblem(String msg, IResource srcFile,
			int violation, int severity) {

		if (srcFile == null)
			return;
		try {
			IMarker marker = srcFile.createMarker(BuilderConstants.MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE, msg);
			marker.setAttribute(IMarker.SEVERITY, severity);
		} catch (CoreException e) {
			BasePlugin.log(e);
			return;
		}
	}

	private static Boolean turnedOffForImport = false;

	private static boolean isTurnedOffForImport() {
		synchronized (turnedOffForImport) {
			return turnedOffForImport;
		}
	}

	public static void setTurnedOffForImport(boolean turn) {
		synchronized (turnedOffForImport) {
			turnedOffForImport = turn;
		}
	}

	private boolean shouldAudit(int kind) {

		if (isTurnedOffForImport())
			return false;

		if (kind == FULL_BUILD)
			return true;

		IResourceDelta delta = getDelta(getProject());
		if (delta == null)
			return false;

		// fow now, just build everything no matter what.
		return true;
	}

	private void auditProject(int kind, IProgressMonitor monitor) {

		monitor.beginTask("Audit Tigerstripe Project", 8);

		ITigerstripeM1GeneratorProject pProject = (ITigerstripeM1GeneratorProject) getProject()
				.getAdapter(ITigerstripeM1GeneratorProject.class);
		IResource projectDescriptor = getProject().findMember(
				ITigerstripeConstants.PLUGIN_DESCRIPTOR);
		if (projectDescriptor == null || !projectDescriptor.exists()) {
			projectDescriptor = getProject();
		}

		try {
			if (!deleteAuditMarkers(projectDescriptor))
				return;

			if (checkCancel(monitor))
				return;

			// audit version/description
			String description = pProject.getProjectDetails().getDescription();
			if (description == null || description.length() == 0) {
				reportInfo("Project has no description", projectDescriptor, 222);
			}
			String version = pProject.getProjectDetails().getVersion();
			

			
			
			if (PluginManager.getManager().isOsgiVersioning()){
				// See if the version String in a valid OSGI one.
				try{
					Version osgiVersion = new Version(version);
				} catch (IllegalArgumentException e){
					reportError("Project Version String does not comply with formatting rules", projectDescriptor, 222);
				}
				
			} else {
				if (version == null || version.length() == 0) {
					reportWarning("Project has no version", projectDescriptor, 222);
				}
			}

			// audit rules
			IRule[] rules = pProject.getGlobalRules();
			GlobalRuleAuditor glRulesAuditor = new GlobalRuleAuditor(pProject,
					getProject());
			for (IRule rule : rules) {
				glRulesAuditor.audit(rule, monitor);
			}

			IArtifactRule[] aRules = pProject.getArtifactRules();
			ArtifactBasedRuleAuditor aRulesAuditor = new ArtifactBasedRuleAuditor(
					pProject, getProject());
			for (IArtifactRule rule : aRules) {
				aRulesAuditor.audit(rule, monitor);
			}

			alignAnnotationPluginDependencies(pProject);

		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}

		monitor.done();
	}

	public static boolean deleteAuditMarkers(IResource project) {
		try {
			project.deleteMarkers(BuilderConstants.MARKER_ID, false,
					IResource.DEPTH_INFINITE);
			return true;
		} catch (CoreException e) {
			BasePlugin.log(e);
			return false;
		}
	}

	private boolean checkCancel(IProgressMonitor monitor) {
		if (monitor.isCanceled())
			// TODO do we need to discard build state?
			throw new OperationCanceledException();

		if (isInterrupted())
			// TODO do we need to discard build state?
			return true;

		return false;
	}

	public static boolean hasPluggablePluginProjectAuditor(IProject project) {

		if (!project.isAccessible())
			// closed
			return true;

		// Get the description
		IProjectDescription description;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			BasePlugin.log(e);
			return false;
		}

		// Look for Builder already associated
		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			if (cmds[j].getBuilderName().equals(BUILDER_ID))
				return true;
		}
		return false;
	}

	public static void addBuilderToProject(IProject project) {

		// Cannot modify closed projects
		if (!project.isOpen())
			return;

		// Get the description
		IProjectDescription description;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			BasePlugin.log(e);
			return;
		}

		// Look for Builder already associated
		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			if (cmds[j].getBuilderName().equals(BUILDER_ID))
				return;
		}

		// Associate Builder with Project
		ICommand newCmd = description.newCommand();
		newCmd.setBuilderName(BUILDER_ID);
		List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.add(newCmd);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));
		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
	}

	public static void removeBuilderFromProject(IProject project) {

		// Cannot modify closed projects
		if (!project.isOpen())
			return;

		// Get the description
		IProjectDescription description = null;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			BasePlugin.log(e);
		}

		// Look for Builder
		int index = -1;
		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			if (cmds[j].getBuilderName().equals(BUILDER_ID)) {
				index = j;
				break;
			}
		}
		if (index == -1)
			return;

		// Remove builder from project
		List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.remove(index);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));

		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
	}

	public static IResource getIResourceForArtifact(IProject project,
			IAbstractArtifact artifact) {
		String fqn = artifact.getFullyQualifiedName();
		String path = "src/" + fqn.replace('.', '/') + ".java";
		return project.findMember(path);
	}

	protected void alignAnnotationPluginDependencies(
			ITigerstripeM1GeneratorProject pProject) {

		IJavaProject jProject = JavaCore.create(getProject());

		ArrayList<IClasspathEntry> newEntryList = new ArrayList<IClasspathEntry>();
		try {
			for (IClasspathEntry entry : jProject.getRawClasspath()) {
				if (entry.getEntryKind() != IClasspathEntry.CPE_LIBRARY
						&& entry.getEntryKind() != IClasspathEntry.CPE_VARIABLE) {
					newEntryList.add(entry);
				}
			}

			// then add the required ones
			for (IClasspathEntry entry : PluginProjectCreator.REQUIRED_ENTRIES) {
				newEntryList.add(entry);
			}

		} catch (JavaModelException e) {
			BasePlugin.log(e);
		}
		IResource projectDescriptor = getProject().findMember(
				ITigerstripeConstants.PLUGIN_DESCRIPTOR);
		if (projectDescriptor == null || !projectDescriptor.exists()) {
			projectDescriptor = getProject();
		}

		try {

			// All the classpath entries
			for (IPluginClasspathEntry entry : pProject.getClasspathEntries()) {
				IPath pPath = getProject().getFullPath().append(
						entry.getRelativePath());
				IClasspathEntry e = JavaCore.newLibraryEntry(pPath, null, null);
				newEntryList.add(e);
			}

			// All the Annotation Plugins
			String[] pluginIds = pProject.getRequiredAnnotationPlugins();
			for (String pluginId : pluginIds) {
				Bundle bundle = Platform.getBundle(pluginId);
				if (bundle == null) {
					reportError("Can't resolve required annotation plugin: '"
							+ pluginId + "'", projectDescriptor, 222);
				} else {
					IPath pPath = new Path(pluginId);
					IClasspathEntry entry = JavaCore.newVariableEntry(pPath,
							null, null);
					if (!newEntryList.contains(entry))
						newEntryList.add(entry);
				}
			}
			jProject.setRawClasspath(newEntryList
					.toArray(new IClasspathEntry[newEntryList.size()]), null);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		} catch (JavaModelException e) {
			BasePlugin.log(e);
		}
	}
}
