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
package org.eclipse.tigerstripe.workbench.internal.core.project;

import static org.eclipse.tigerstripe.workbench.internal.core.util.CheckUtils.notNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleRef;
import org.eclipse.tigerstripe.workbench.internal.core.util.CheckUtils;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class CyclesValidator {

	private final Set<INode> seen = new HashSet<INode>();
	private final Deque<INode> path = new ArrayDeque<INode>();

	private final Collection<List<INode>> cycles = new ArrayList<List<INode>>();

	public void findCycle(INode node, IProgressMonitor monitor)
			throws TigerstripeException {
		cycles.clear();
		seen.clear();
		findCycleInternal(node, monitor);
	}

	private void findCycleInternal(INode node, IProgressMonitor monitor)
			throws TigerstripeException {

		if (monitor.isCanceled()) {
			return;
		}

		if (node.equals(path.peekLast())) {
			cycles.add(new ArrayList<INode>(path));	
		}

		if (!seen.add(node)) {
			return;
		}
		path.push(node);
		try {
			for (INode child : node.getNodes()) {
				findCycleInternal(child, monitor);
			}
		} finally {
			path.pop();
		}
	}

	public static interface INode {
		Collection<INode> getNodes();
	}

	public static class DependencyNode implements INode {

		private final IDependency dependency;

		public DependencyNode(IDependency dependency) {
			this.dependency = notNull(dependency, "dependency");
		}

		public Collection<INode> getNodes() {

			Dependency dep = (Dependency) dependency;

			ModuleRef moduleRef = dep.getModuleRef();
			if (moduleRef == null) {
				return Collections.emptySet();
			}
			TigerstripeProject proj = moduleRef.getEmbeddedProject();
			if (proj == null) {
				return Collections.emptySet();
			}

			IDependency[] deps = proj.getDependencies();
			ModelReference[] refs = proj.getModelReferences();

			List<INode> nodes = new ArrayList<INode>(deps.length + refs.length);
			addDependencies(deps, nodes);
			addReferences(refs, nodes);
			return nodes;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof DependencyNode)) {
				return false;
			}
			DependencyNode other = (DependencyNode) obj;

			if (other.dependency == null) {
				return false;
			}

			if (dependency == null) {
				return false;
			}
			URI uri = dependency.getURI();
			if (uri == null) {
				return false;
			}
			return uri.equals(other.dependency.getURI());
		}

		@Override
		public int hashCode() {
			if (dependency == null) {
				return super.hashCode();
			}
			URI uri = dependency.getURI();

			if (uri == null) {
				return super.hashCode();
			}
			return uri.hashCode();
		}

		public IDependency getDependency() {
			return dependency;
		}

	}

	private static final ModelReference[] EMPTY_REFERENCES = new ModelReference[0];
	private static final IDependency[] EMPTY_DEPENDENCIES = new IDependency[0];

	public static class ReferenceNode extends ModelNode {
		private final ModelReference reference;

		public ReferenceNode(ModelReference reference) {
			this.reference = CheckUtils.notNull(reference, "reference");
		}

		@Override
		public String getModelId() {
			return reference.getToModelId();
		}

		@Override
		public IDependency[] getDependencies() {
			try {
				ITigerstripeModelProject model;
				return (model = reference.getResolvedModel()) == null ? null
						: model.getDependencies();
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
				return EMPTY_DEPENDENCIES;
			}
		}

		@Override
		public ModelReference[] getModelReferences() {
			try {
				ITigerstripeModelProject model;
				return (model = reference.getResolvedModel()) == null ? null
						: model.getModelReferences();
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
				return EMPTY_REFERENCES;
			}
		}
	}

	public static class ProjectNode extends ModelNode {

		private final ITigerstripeModelProject project;

		public ProjectNode(ITigerstripeModelProject project) {
			this.project = CheckUtils.notNull(project, "project");
		}

		@Override
		public String getModelId() {
			try {
				return project.getModelId();
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
				return null;
			}
		}

		@Override
		public IDependency[] getDependencies() {
			try {
				return project.getDependencies();
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
				return EMPTY_DEPENDENCIES;
			}
		}

		@Override
		public ModelReference[] getModelReferences() {
			try {
				return project.getModelReferences();
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
				return EMPTY_REFERENCES;
			}
		}

	}

	public static abstract class ModelNode implements INode {

		public abstract String getModelId();

		public abstract IDependency[] getDependencies();

		public abstract ModelReference[] getModelReferences();

		public Collection<INode> getNodes() {
			List<INode> nodes = new ArrayList<INode>();
			addDependencies(getDependencies(), nodes);
			addReferences(getModelReferences(), nodes);
			return nodes;
		}

		@Override
		public boolean equals(Object obj) {

			if (!(obj instanceof ModelNode)) {
				return false;
			}

			ModelNode other = (ModelNode) obj;

			String otherModelId = other.getModelId();
			String modelId = getModelId();

			if (modelId == null || otherModelId == null) {
				return false;
			}

			return modelId.equals(otherModelId);
		}

		@Override
		public int hashCode() {
			String modelId = getModelId();
			if (modelId == null) {
				return super.hashCode();
			}
			return modelId.hashCode();
		}
	}

	private static void addDependencies(IDependency[] deps,
			Collection<INode> nodes) {
		if (deps!=null) {
			for (IDependency d : deps) {
				nodes.add(new DependencyNode(d));
			}
		}
	}

	private static void addReferences(ModelReference[] refs,
			Collection<INode> nodes) {
		if (refs!=null) {
			for (ModelReference r : refs) {
				nodes.add(new ReferenceNode(r));
			}
		}
	}

	public Collection<List<INode>> getCycles() {
		return cycles;
	}
}
