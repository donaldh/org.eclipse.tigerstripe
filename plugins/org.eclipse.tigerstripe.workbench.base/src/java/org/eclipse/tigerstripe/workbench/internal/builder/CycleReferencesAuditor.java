/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.project.CyclesValidator;
import org.eclipse.tigerstripe.workbench.internal.core.project.CyclesValidator.DependencyNode;
import org.eclipse.tigerstripe.workbench.internal.core.project.CyclesValidator.INode;
import org.eclipse.tigerstripe.workbench.internal.core.project.CyclesValidator.ModelNode;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class CycleReferencesAuditor extends IncrementalProjectBuilder {

	private static final String TS_PROJECT_DESCRIPTOR_FILENAME = "tigerstripe.xml";

	private static class DescriptorFinderVisitor implements IResourceVisitor,
			IResourceDeltaVisitor {

		private final Collection<IResource> descriptorResources = new HashSet<IResource>();

		public boolean visit(IResourceDelta delta) throws CoreException {
			return visit(delta.getResource());
		}

		public boolean visit(IResource resource) throws CoreException {
			if (resource.getParent() instanceof IProject
					&& TS_PROJECT_DESCRIPTOR_FILENAME
							.equals(resource.getName())) {
				getDescriptorResources().add(resource);
			}
			return true;
		}

		public Collection<IResource> getDescriptorResources() {
			return descriptorResources;
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {

		// TODO - Commented out as the logic is ncorrect
//		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
//			fullBuild(monitor);
//		} else {
//			IResourceDelta delta = getDelta(getProject());
//			if (delta == null) {
//				fullBuild(monitor);
//			} else {
//				incrementalBuild(delta, monitor);
//			}
//		}
		return null;
	}

	private void validateDescriptor(IProgressMonitor monitor,
			IResource descriptorResource) throws CoreException {
		cleanMarkers(descriptorResource);
		ITigerstripeModelProject proj = (ITigerstripeModelProject) getProject()
				.getAdapter(ITigerstripeModelProject.class);

		if (proj != null) {
			CyclesValidator cyclesValidator = new CyclesValidator();
			try {
				cyclesValidator.findCycle(
						new CyclesValidator.ProjectNode(proj), monitor);
				Collection<List<INode>> cycles = cyclesValidator.getCycles();

				if (!cycles.isEmpty()) {
					for (List<INode> cycle : cycles) {
						IMarker marker = descriptorResource
								.createMarker(BuilderConstants.CYCLES_MARKER_ID);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_ERROR);
						marker.setAttribute(IMarker.MESSAGE, String.format(
								"Model '%s' have cycle references. '%s'",
								proj.getModelId(), toStringCycle(cycle)));
					}
				}
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}
	}

	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor)
			throws CoreException {
		DescriptorFinderVisitor descriptorFinderVisitor = new DescriptorFinderVisitor();
		delta.accept(descriptorFinderVisitor);
		validateFirstDescriptor(monitor, descriptorFinderVisitor);
	}

	private void fullBuild(IProgressMonitor monitor) throws CoreException {
		DescriptorFinderVisitor descriptorFinderVisitor = new DescriptorFinderVisitor();
		getProject().accept(descriptorFinderVisitor);
		validateFirstDescriptor(monitor, descriptorFinderVisitor);
	}

	private void validateFirstDescriptor(IProgressMonitor monitor,
			DescriptorFinderVisitor descriptorFinderVisitor)
			throws CoreException {
		Iterator<IResource> it = descriptorFinderVisitor
				.getDescriptorResources().iterator();
		if (it.hasNext()) {
			validateDescriptor(monitor, it.next());
		}
	}

	private String toStringCycle(List<INode> cycle) {
		Iterator<INode> it = cycle.iterator();
		if (!it.hasNext()) {
			return "";
		}
		StringBuilder str = new StringBuilder(toStringNode(it.next()));
		while (it.hasNext()) {
			str.append(" -> ").append(toStringNode(it.next()));
		}
		return str.toString();
	}

	private String toStringNode(INode next) {
		if (next instanceof DependencyNode) {
			IProjectDetails details = ((DependencyNode) next).getDependency()
					.getIProjectDetails();
			if (details == null) {
				return String.format("Dependency: 'UNKNOWN MODEL'");
			} else {
				return String.format("Dependency: '%s'", details.getModelId());
			}
		} else if (next instanceof ModelNode) {
			return String
					.format("Model: '%s'", ((ModelNode) next).getModelId());
		} else {
			return next.toString();
		}
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		super.clean(monitor);
		DescriptorFinderVisitor visitor = new DescriptorFinderVisitor();
		ResourcesPlugin.getWorkspace().getRoot().accept(visitor);
		for (IResource descriptorResource : visitor.getDescriptorResources()) {
			cleanMarkers(descriptorResource);
		}
	}

	private void cleanMarkers(IResource descriptorResource)
			throws CoreException {
		descriptorResource.deleteMarkers(BuilderConstants.CYCLES_MARKER_ID,
				true, IResource.DEPTH_ZERO);
	}
}
