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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.IOriginalChangeListener;
import org.eclipse.tigerstripe.workbench.OriginalChangeEvent;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyChangeListener;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyDiagramHandler;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;

public class TigerstripeSubjectsFactory {

	private final Set<ITigerstripeModelProject> listened = new HashSet<ITigerstripeModelProject>();
	private final IDependencyDiagramHandler handler;
	private ITigerstripeProjectProvider currentProjectProvider;

	public TigerstripeSubjectsFactory(IDependencyDiagramHandler handler) {
		this.handler = handler;
	}

	public IDependencySubject getSubject(IDependency dependency) {
		return new TSDependencySubject(dependency);
	}

	public IDependencySubject getSubject(ITigerstripeModelProject project) {
		listen(project);
		return new TigerstripeModelSubject(project, this);
	}

	private void listen(ITigerstripeModelProject project) {
		if (listened.add(project)) {
			if (!project.wasDisposed()) {
				project.addProjectDependencyChangeListener(projectListener);
			}
		}
	}

	public void dispose() {
		for (ITigerstripeModelProject proj : listened) {
			proj.removeProjectDependencyChangeListener(projectListener);
			proj.removeOriginalChangeListener(originalChangeListener);
		}
	}

	public void setCurrentProjectProvider(
			ITigerstripeProjectProvider currentProjectProvider) {
		this.currentProjectProvider = currentProjectProvider;
	}

	public ITigerstripeProjectProvider getCurrentProjectProvider() {
		return currentProjectProvider;
	}

	private IProjectDependencyChangeListener projectListener = new IProjectDependencyChangeListener() {

		public void projectDependenciesChanged(IProjectDependencyDelta delta) {
			// if (currentProjectProvider != null) {
			// if
			// (currentProjectProvider.getProject().equals(delta.getProject()))
			// {
			// handler.update();
			// return;
			// }
			// }
			// handler.update();
			delta.getProject()
					.addOriginalChangeListener(originalChangeListener);
		}
	};

	private IOriginalChangeListener originalChangeListener = new IOriginalChangeListener() {

		public void originalChanged(OriginalChangeEvent changeEvent) {
			handler.update();
		}
	};

}
