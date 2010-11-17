/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyChangeListener;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyAction;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyDiagramHandler;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyType;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IExternalContext;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.editor.DependencyDiagramEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

public class TigerstripeDependenciesDiagramEditor extends
		DependencyDiagramEditor {

	private IExternalContext context;

	private final Map<String, Subject> dependencies = new HashMap<String, Subject>();
	private final Map<String, Subject> references = new HashMap<String, Subject>();
	private final Set<ITigerstripeModelProject> managedProjects = new HashSet<ITigerstripeModelProject>();

	private IProjectDependencyChangeListener projectListener = new IProjectDependencyChangeListener() {

		public void projectDependenciesChanged(IProjectDependencyDelta delta) {
			try {

				ITigerstripeModelProject parentProject = delta.getProject();
				IDependencyDiagramHandler handler = getHandler();
				switch (delta.getKind()) {
				case IProjectDependencyDelta.PROJECT_DEPENDENCY_ADDED: {
					IDependency dependency = findDependency(
							parentProject.getDependencies(), delta.getPath()
									.toString());
					Subject from = references.get(parentProject.getName());
					if (dependency != null && from != null) {
						Subject to = wrapDependency(dependency);
						from.getDependencies().add(to);
						handler.addDependency(from, to);
						handler.layout(Collections
								.<IDependencySubject> singletonList(to));
					}
					break;
				}
				case IProjectDependencyDelta.PROJECT_DEPENDENCY_REMOVED: {
					Subject from = references.get(parentProject.getName());
					Subject to = dependencies.get(delta.getPath().toString());
					if (from != null && to != null) {
						if (from.getDependencies().remove(to)) {
							handler.removeDependency(from, to);
						}
					}
					break;
				}
				case IProjectDependencyDelta.PROJECT_REFERENCE_ADDED: {
					ITigerstripeModelProject proj = findProject(delta);
					Subject from = references.get(parentProject.getName());
					if (proj != null && from != null) {
						IDependencySubject to = wrapProject(proj);
						from.getDependencies().add(to);
						handler.addDependency(from, to);
						handler.layout(Collections
								.<IDependencySubject> singletonList(to));
					}
					break;
				}
				case IProjectDependencyDelta.PROJECT_REFERENCE_REMOVED: {
					Subject from = references.get(parentProject.getName());
					Subject to = references.get(getProjectName(delta));
					if (from != null && to != null) {
						if (from.getDependencies().remove(to)) {
							handler.removeDependency(from, to);
						}
					}
					break;
				}
				}

			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

	};

	private ITigerstripeModelProject tsPorject;

	private String getProjectName(IProjectDependencyDelta delta) {
		IPath path = delta.getPath();
		if (path == null) {
			return null;
		}
		return path.segment(0);
	}

	private IDependency findDependency(IDependency[] dependencies, String path) {
		for (IDependency dep : dependencies) {
			if (path.equals(dep.getPath())) {
				return dep;
			}
		}
		return null;
	}

	private Subject wrapDependency(IDependency dependency) {

		Subject subject = dependencies.get(dependency.getPath());

		if (subject != null) {
			return subject;
		}

		subject = new Subject(dependency.getPath(), DependencyTypes.DEPENDENCY);
		dependencies.put(dependency.getPath(), subject);
		subject.getProperties().put("URI", dependency.getURI().toString());
		prepare(dependency.getIProjectDetails(), subject);
		return subject;
	}

	private ITigerstripeModelProject findProject(IProjectDependencyDelta delta) {
		String projectName = getProjectName(delta);

		if (projectName == null) {
			return null;
		}

		IProject proj = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);

		return (ITigerstripeModelProject) proj
				.getAdapter(ITigerstripeModelProject.class);
	}

	@Override
	protected void setInput(IEditorInput input) {
		final ITigerstripeModelProject project = getTSProject(input);

		removeProjectListeners();
		references.clear();
		dependencies.clear();
		managedProjects.clear();

		final IDependencySubject model;
		try {
			model = wrapProject(project);
		} catch (TigerstripeException e) {
			throw new RuntimeException(e);
		}

		addProjectListeners();

		context = new IExternalContext() {

			public void save(EObject data) {
				try {
					project.setDependenciesVisualState(data);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}

			public <T extends EObject> T load(Class<T> forClass) {
				try {
					return project.getDependenciesVisualState(forClass);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
					return null;
				}
			}

			public IDependencySubject getRootExternalModel() {
				return model;
			}

			public String getName() {
				return "Tigerstripe Modules Dependencies Diagram";
			}

			public Set<IDependencyType> getAllTypes() {
				return linkedHashSet;
			}
		};
		super.setInput(input);
	}

	private ITigerstripeModelProject getTSProject(IEditorInput input) {
		if (tsPorject == null) {
			return (ITigerstripeModelProject) ((IFileEditorInput) input)
					.getFile().getAdapter(ITigerstripeModelProject.class);
		} else {
			return tsPorject;
		}
	}

	private void addProjectListeners() {
		for (ITigerstripeModelProject p : managedProjects) {
			p.addProjectDependencyChangeListener(projectListener);
		}
	}

	private void removeProjectListeners() {
		for (ITigerstripeModelProject p : managedProjects) {
			p.removeProjectDependencyChangeListener(projectListener);
		}
	}

	private IDependencySubject wrapProject(ITigerstripeModelProject project)
			throws TigerstripeException {

		String projectName = project.getName();
		Subject subject = references.get(projectName);

		if (subject != null) {
			return subject;
		}

		IDependencyType type;
		if (project instanceof ITigerstripeModuleProject) {
			type = DependencyTypes.MODULE_PROJECT;
		} else if (project instanceof IPhantomTigerstripeProject) {
			type = DependencyTypes.PHANTOM_PROJECT;
		} else {
			type = DependencyTypes.PROJECT;
		}

		subject = new Subject(projectName, type);
		references.put(projectName, subject);
		managedProjects.add(project);
		prepare(project.getProjectDetails(), subject);
		for (ITigerstripeModelProject refProject : project
				.getReferencedProjects()) {
			try {
				subject.getDependencies().add(wrapProject(refProject));
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}

		}

		for (IDependency dep : project.getDependencies()) {
			subject.getDependencies().add(wrapDependency(dep));
		}

		return subject;
	}

	private void prepare(IProjectDetails details, Subject depSubject) {
		Map<String, String> props = depSubject.getProperties();
		props.put("modelId", details.getModelId());
		props.put("version", details.getVersion());
		depSubject.setDescription(details.getDescription());
	}

	@Override
	public IExternalContext getExternalContext() {
		Assert.isNotNull(context);
		return context;
	}

	@Override
	public void dispose() {
		removeProjectListeners();
		super.dispose();
	}

	static Set<IDependencyType> linkedHashSet = new LinkedHashSet<IDependencyType>(
			Arrays.asList(DependencyTypes.values()));

	static enum DependencyTypes implements IDependencyType {

		PROJECT(new RGB(255, 243, 243)), MODULE_PROJECT(new RGB(243, 255, 244)), PHANTOM_PROJECT(
				new RGB(255, 254, 243)), DEPENDENCY(new RGB(243, 245, 255));

		private Color bgColor;
		private Color fgColor;

		private DependencyTypes(RGB color) {
			bgColor = new Color(null, color);
			fgColor = new Color(null, sate(color, -30));
		}

		public String getId() {
			return name();
		}

		public String getName() {
			return name();
		}

		public Color getDefaultBackgroundColor() {
			return bgColor;
		}

		public Color getDefaultForegroundColor() {
			return fgColor;
		}

		public Image getImage() {
			return Images.get(Images.TSPROJECT_FOLDER);
		}

		public List<IDependencyAction> getActions() {
			return Collections.emptyList();
		}

	}

	static class Subject implements IDependencySubject {

		private final String modelId;
		private final Set<IDependencySubject> dependencies;
		private SortedMap<String, String> properties;
		private IDependencyType type;
		private String description;

		public Subject(String modelId, IDependencyType type) {
			this.modelId = modelId;
			this.type = type;
			dependencies = new HashSet<IDependencySubject>();
			properties = new TreeMap<String, String>();
		}

		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
			return null;
		}

		public String getId() {
			return modelId;
		}

		public String getName() {
			return modelId;
		}

		public Set<IDependencySubject> getDependencies() {
			return dependencies;
		}

		public SortedMap<String, String> getProperties() {
			return properties;
		}

		public String getDescription() {
			return description;
		}

		public IDependencyType getType() {
			return type;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Image getIcon() {
			return null;
		}
	}

	private static RGB sate(RGB color, int value) {
		int r = ensureColor(color.red + value);
		int g = ensureColor(color.green + value);
		int b = ensureColor(color.blue + value);
		return new RGB(r, g, b);
	}

	private static int ensureColor(int value) {
		value = value > 255 ? 255 : value;
		return value < 0 ? 0 : value;
	}

	public void setTsPorject(ITigerstripeModelProject tsPorject) {
		this.tsPorject = tsPorject;
	}
}
