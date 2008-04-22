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
package org.eclipse.tigerstripe.workbench.ui.internal.builder.pluggable;

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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;

/**
 * This is the incremental auditor for a Pluggable Plugin Project.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PluggablePluginProjectAuditor extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = EclipsePlugin.getDefault()
			.getDescriptor().getUniqueIdentifier()
			+ ".pluggablePluginProjectAuditor";

	public PluggablePluginProjectAuditor() {
		super();
	}

	@Override
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
			IMarker marker = srcFile
					.createMarker(TigerstripePluginConstants.MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE, msg);
			marker.setAttribute(IMarker.SEVERITY, severity);
		} catch (CoreException e) {
			EclipsePlugin.log(e);
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

		ITigerstripeM1GeneratorProject pProject = (ITigerstripeM1GeneratorProject) TSExplorerUtils
				.getProjectHandleFor(getProject());

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

			// First audit rules
			IRule[] rules = pProject.getGlobalRules();
			GlobalRuleAuditor glRulesAuditor = new GlobalRuleAuditor(pProject,
					getProject());
			for (IRule rule : rules) {
				glRulesAuditor.audit(rule, monitor);
			}

			ITemplateBasedRule[] aRules = pProject.getArtifactRules();
			ArtifactBasedRuleAuditor aRulesAuditor = new ArtifactBasedRuleAuditor(
					pProject, getProject());
			for (ITemplateBasedRule rule : aRules) {
				aRulesAuditor.audit(rule, monitor);
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		monitor.done();
	}

	public static boolean deleteAuditMarkers(IResource project) {
		try {
			project.deleteMarkers(TigerstripePluginConstants.MARKER_ID, false,
					IResource.DEPTH_INFINITE);
			return true;
		} catch (CoreException e) {
			EclipsePlugin.log(e);
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
			EclipsePlugin.log(e);
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
			EclipsePlugin.log(e);
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
		List newCmds = new ArrayList();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.add(newCmd);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));
		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			EclipsePlugin.log(e);
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
			EclipsePlugin.log(e);
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
		List newCmds = new ArrayList();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.remove(index);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));

		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
	}

	public static IResource getIResourceForArtifact(IProject project,
			IAbstractArtifact artifact) {
		String fqn = artifact.getFullyQualifiedName();
		String path = "src/" + fqn.replace('.', '/') + ".java";
		return project.findMember(path);
	}

}
