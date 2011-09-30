/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.depsdiagram;

import static org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta.PROJECT_DEPENDENCY_ADDED;
import static org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta.PROJECT_DEPENDENCY_REMOVED;
import static org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta.PROJECT_REFERENCE_ADDED;
import static org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta.PROJECT_REFERENCE_REMOVED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.IWorkingCopy;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.Utils;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager.CommitEvent;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager.CommitListener;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ProjectDependencyChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyChangeListener;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyDiagramHandler;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.MultiKey;

public class TigerstripeSubjectsFactory implements CommitListener {

	private final Map<ITigerstripeModelProject, DependencyListnener> listened = new HashMap<ITigerstripeModelProject, DependencyListnener>();
	private final Map<String, TigerstripeModelSubject> projects = new HashMap<String, TigerstripeModelSubject>();
	private final Map<Object, IDependencySubject> dependencies = new HashMap<Object, IDependencySubject>();
	private final IDependencyDiagramHandler handler;
	private ITigerstripeProjectProvider currentProjectProvider;

	public TigerstripeSubjectsFactory(IDependencyDiagramHandler handler) {
		this.handler = handler;
		WorkingCopyManager.addCommitListener(this);
	}

	public IDependencySubject getSubject(IDependency dependency,
			String forModelId) throws TigerstripeException {
		MultiKey id = new MultiKey(dependency.getPath(), forModelId);
		IDependencySubject subject = dependencies.get(id);
		if (subject == null) {
			dependencies.put(id, subject = new TSDependencySubject(dependency));
		}
		return subject;
	}

	public IDependencySubject getSubject(ITigerstripeModelProject project)
			throws TigerstripeException {
		if (project.isWorkingCopy()) {
			project = (ITigerstripeModelProject) project.getOriginal();
		}
		String id = project.getModelId();
		TigerstripeModelSubject subject = projects.get(id);
		if (subject == null) {
			subject = new TigerstripeModelSubject(project, this);
			listen(subject);
			projects.put(id, subject);
		}
		return subject;
	}

	private void listen(TigerstripeModelSubject subject)
			throws TigerstripeException {
		ITigerstripeModelProject project = subject.getTsProject();
		if (!listened.containsKey(project)) {
			if (!project.wasDisposed()) {
				DependencyListnener listener = new DependencyListnener(subject);
				project.addProjectDependencyChangeListener(listener);
				listened.put(project, listener);
			}
		}
	}

	public void dispose() {
		WorkingCopyManager.removeCommitListener(this);
		Set<Entry<ITigerstripeModelProject, DependencyListnener>> entrySet = listened
				.entrySet();
		for (Entry<ITigerstripeModelProject, DependencyListnener> e : entrySet) {
			e.getKey().removeProjectDependencyChangeListener(e.getValue());
		}
	}

	public void setCurrentProjectProvider(
			ITigerstripeProjectProvider currentProjectProvider) {
		this.currentProjectProvider = currentProjectProvider;
	}

	public ITigerstripeProjectProvider getCurrentProjectProvider() {
		return currentProjectProvider;
	}

	class DependencyListnener implements IProjectDependencyChangeListener {

		private final TigerstripeModelSubject from;

		public DependencyListnener(TigerstripeModelSubject from) {
			this.from = from;
		}

		private final Map<ITigerstripeModelProject, Set<ITigerstripeModelProject>> addedRefs = new HashMap<ITigerstripeModelProject, Set<ITigerstripeModelProject>>();
		private final Map<ITigerstripeModelProject, Set<ITigerstripeModelProject>> removedRefs = new HashMap<ITigerstripeModelProject, Set<ITigerstripeModelProject>>();

		private final Map<ITigerstripeModelProject, Set<IDependency>> addedDeps = new HashMap<ITigerstripeModelProject, Set<IDependency>>();
		private final Map<ITigerstripeModelProject, Set<IDependency>> removedDeps = new HashMap<ITigerstripeModelProject, Set<IDependency>>();

		public void projectDependenciesChanged(IProjectDependencyDelta delta) {
			try {
				ITigerstripeModelProject workingCopy = delta.getProject();
				ITigerstripeModelProject reference = toProject(delta);
				IDependency dependency = toDependency(delta);
				switch (delta.getKind()) {
				case PROJECT_REFERENCE_ADDED:
					if (reference != null) {
						Utils.addToSetInMap(addedRefs, workingCopy, reference);
						Utils.removeFromSetInMap(removedRefs, workingCopy,
							reference);
					}
					break;
				case PROJECT_REFERENCE_REMOVED:
					if (reference != null) {
						Utils.addToSetInMap(removedRefs, workingCopy, reference);
						Utils.removeFromSetInMap(addedRefs, workingCopy,
								reference);
					}
					break;
				case PROJECT_DEPENDENCY_ADDED:
					Utils.addToSetInMap(addedDeps, workingCopy, dependency);
					Utils.removeFromSetInMap(removedDeps, workingCopy,
							dependency);
					break;
				case PROJECT_DEPENDENCY_REMOVED:
					Utils.addToSetInMap(removedDeps, workingCopy, dependency);
					Utils.removeFromSetInMap(addedDeps, workingCopy, dependency);
					break;
				}
				if (!workingCopy.isWorkingCopy()) {
					// commit(...);
				}

			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}

		private void commit(ITigerstripeModelProject workingCopy) {

			Set<ITigerstripeModelProject> rAdded = addedRefs.get(workingCopy);
			if (rAdded != null) {
				handler.addDependencies(from, mapAsSubjects(rAdded));
				rAdded.clear();
			}
			Set<ITigerstripeModelProject> rRemoved = removedRefs
					.get(workingCopy);
			if (rRemoved != null) {
				handler.removeDependencies(from, mapAsSubjects(rRemoved));
				rRemoved.clear();
			}
			Set<IDependency> dAdded = addedDeps.get(workingCopy);
			if (dAdded != null) {
				handler.addDependencies(from,
						mapAsDepsSubjects(dAdded, workingCopy));
				dAdded.clear();
			}
			Set<IDependency> dRemoved = removedDeps.get(workingCopy);
			if (dRemoved != null) {
				handler.removeDependencies(from,
						mapAsDepsSubjects(dRemoved, workingCopy));
				dRemoved.clear();
			}
		}

		private List<IDependencySubject> mapAsSubjects(
				Set<ITigerstripeModelProject> projects) {
			List<IDependencySubject> subjects = new ArrayList<IDependencySubject>();
			for (ITigerstripeModelProject p : projects) {
				try {
					subjects.add(getSubject(p));
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
			}
			return subjects;
		}

		private Collection<IDependencySubject> mapAsDepsSubjects(
				Set<IDependency> deps, ITigerstripeModelProject workingCopy) {
			List<IDependencySubject> subjects = new ArrayList<IDependencySubject>();
			for (IDependency d : deps) {
				try {
					subjects.add(getSubject(d, workingCopy.getModelId()));
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
			}
			return subjects;
		}

		private ITigerstripeModelProject toProject(IProjectDependencyDelta delta)
				throws TigerstripeException {
			ModelReference modelReference = ((ProjectDependencyChangeDelta) delta)
					.getModelReference();
			if (modelReference == null) {
				return null;
			}
			return modelReference.getResolvedModel();
		}

		private IDependency toDependency(IProjectDependencyDelta delta)
				throws TigerstripeException {
			return ((ProjectDependencyChangeDelta) delta).getDependency();
		}

	}

	public void onCommit(CommitEvent event) {
		IWorkingCopy workingCopy = event.getWorkingCopy();
		if (!(workingCopy instanceof ITigerstripeModelProject)) {
			return;
		}

		DependencyListnener dependencyListnener = listened.get(event
				.getOriginal());
		if (dependencyListnener != null) {
			dependencyListnener.commit((ITigerstripeModelProject) workingCopy);
		}
	}
}
