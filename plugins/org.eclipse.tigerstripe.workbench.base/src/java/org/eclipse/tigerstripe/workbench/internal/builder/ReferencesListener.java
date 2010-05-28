package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.classpath.IReferencesConstants;
import org.eclipse.tigerstripe.workbench.internal.core.classpath.ReferencesClasspathContainer;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ReferencesListener {

	public void changed(Collection<IResource> removedResources,
			Collection<IResource> addedResources,
			Collection<IResource> changedResources) {
		Set<ProjectInfo> changedProjects = new HashSet<ProjectInfo>();
		Set<String> changedReferences = new HashSet<String>();
		if (checkDeletedProjects(removedResources, changedReferences)) {
			// We doesn't know which project deleted, so need to
			// update all references
			updateAllReferences();
			return;
		}
		if (checkChangedProjects(changedResources, changedProjects,
				changedReferences)) {
			updateAllReferences();
			return;
		}
		checkAddedProjects(addedResources, changedReferences);
		checkReferencesProjects(changedProjects, changedReferences);
		updateChangedProjects(changedProjects);
	}

	/**
	 * Check specified removed resources to provide model identifiers which
	 * should be updated. This methods may return null to notify that all
	 * projects should be updated
	 * 
	 * @param removedResources
	 *            removed resources
	 * @param changedReferences
	 *            collection of references
	 * @return true if all projects should be updated
	 */
	private boolean checkDeletedProjects(
			Collection<IResource> removedResources,
			Collection<String> changedReferences) {
		for (IResource res : removedResources) {
			IProject iProject = getProject(res);
			if (iProject != null) {
				ProjectInfo details = nameToDetails.get(iProject);
				if (details == null) {
					return true;
				}
				if (details.getModelId() != null) {
					changedReferences.add(details.getModelId());
				}
			}
		}
		return false;
	}

	private IProject getProject(IResource res) {
		if (res instanceof IProject)
			return (IProject) res;
		if (res instanceof IFile && "tigerstripe.xml".equals(res.getName())) {
			return res.getProject();
		}
		return null;
	}

	private boolean checkChangedProjects(
			Collection<IResource> changedResources,
			Collection<ProjectInfo> changedProjects,
			Collection<String> changedReferences) {
		Set<IProject> projectsToCheck = new HashSet<IProject>();
		for (IResource res : changedResources) {
			if (ITigerstripeConstants.PROJECT_DESCRIPTOR.equals(res.getName())) {
				projectsToCheck.add(res.getProject());
			}
		}
		for (IProject iProject : projectsToCheck) {
			ProjectInfo details = nameToDetails.get(iProject);
			if (details == null) {
				return true;
			}
			if (details.getProject() != null) {
				ProjectInfo newDetails = new ProjectInfo(iProject);
				if (!equals(newDetails.getModelId(), details.getModelId())) {
					changedReferences.add(newDetails.getModelId());
					changedReferences.add(details.getModelId());
				}
				if (!Arrays.equals(details.getReferences(), newDetails
						.getReferences())) {
					changedProjects.add(newDetails);
				}
				nameToDetails.put(iProject, newDetails);
			}
		}
		return false;
	}

	private void checkReferencesProjects(
			Collection<ProjectInfo> changedProjects,
			Collection<String> changedReferences) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			ProjectInfo details = nameToDetails.get(project);
			if (details == null) {
				details = new ProjectInfo(project);
				nameToDetails.put(project, details);
			}
			if (!changedProjects.contains(details)) {
				boolean addProject = false;
				String id = details.getModelId();
				if (id != null && changedReferences.contains(id)) {
					addProject = true;
				} else {
					String[] references = details.getReferences();
					if (references != null) {
						for (String ref : references) {
							if (changedReferences.contains(ref)) {
								addProject = true;
								break;
							}
						}
					}
				}
				if (!addProject && details.needReferenceContainer()) {
					addProject = true;
				}
				if (addProject) {
					changedProjects.add(details);
				}
			}
		}
	}

	private boolean equals(String s1, String s2) {
		if (s1 == null) {
			return s2 == null;
		}
		return s1.equals(s2);
	}

	private void checkAddedProjects(Collection<IResource> addedResources,
			Collection<String> changedReferences) {
		for (IResource res : addedResources) {
			IProject iProject = getProject(res);
			if (iProject != null) {
				ProjectInfo details = new ProjectInfo(iProject);
				nameToDetails.put(iProject, details);
				if (details.getModelId() != null) {
					changedReferences.add(details.getModelId());
				}
			}
		}
	}

	private void updateAllReferences() {
		nameToDetails = new HashMap<IProject, ProjectInfo>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		List<ProjectInfo> changedProjects = new ArrayList<ProjectInfo>(root
				.getProjects().length);
		for (IProject project : root.getProjects()) {
			ProjectInfo details = new ProjectInfo(project);
			nameToDetails.put(project, details);
			changedProjects.add(details);
		}
		updateChangedProjects(changedProjects);
	}

	private void updateChangedProjects(Collection<ProjectInfo> changedProjects) {
		updateClasspathReferences(changedProjects);
		for (ProjectInfo details : changedProjects) {
			ITigerstripeModelProject tsProject = details.getProject();
			if (tsProject != null) {
				try {
					for (ModelReference ref : tsProject.getModelReferences()) {
						ref.resolveModel();
					}
				} catch (TigerstripeException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void updateClasspathReferences(
			Collection<ProjectInfo> changedProjects) {
		Map<IJavaProject, ReferencesClasspathContainer> map = new HashMap<IJavaProject, ReferencesClasspathContainer>();
		for (ProjectInfo details : changedProjects) {
			IProject project = details.getIProject();
			try {
				if (project.exists() && project.isOpen()
						&& project.hasNature(JavaCore.NATURE_ID)) {
					ITigerstripeModelProject tsProject = details.getProject();
					if (tsProject != null) {
						map.put(JavaCore.create(project),
								new ReferencesClasspathContainer(tsProject));
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		if (map.size() > 0) {
			try {
				// update classpath for all affected workspace plug-ins in one
				// operation
				IJavaProject[] jProjects = (IJavaProject[]) map.keySet()
						.toArray(new IJavaProject[map.size()]);

				updateReferenceContainer(jProjects);

				IClasspathContainer[] containers = (IClasspathContainer[]) map
						.values().toArray(new IClasspathContainer[map.size()]);
				JavaCore.setClasspathContainer(
						IReferencesConstants.REFERENCES_CONTAINER_PATH,
						jProjects, containers, null);
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateReferenceContainer(IJavaProject[] projectsToCheck) {
		final List<IJavaProject> toUpdate = new ArrayList<IJavaProject>();
		for (IJavaProject project : projectsToCheck) {
			if (needReferenceContainer(project)) {
				toUpdate.add(project);
			}
		}
		if (toUpdate.size() > 0) {
			Job job = new UpdateContainerJob(toUpdate);
			job.schedule();
		}
	}

	public ProjectInfo getProjectDetails(IProject project) {
		return nameToDetails.get(project);
	}

	static boolean needReferenceContainer(IJavaProject project) {
		try {
			return !haveReferenceContainer(project.getRawClasspath());
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean haveReferenceContainer(IClasspathEntry[] entries) {
		for (IClasspathEntry entry : entries) {
			if (IReferencesConstants.REFERENCES_CONTAINER_PATH.equals(entry
					.getPath())) {
				return true;
			}
		}
		return false;
	}

	private Map<IProject, ProjectInfo> nameToDetails = new HashMap<IProject, ProjectInfo>();

	private class UpdateContainerJob extends Job {

		private List<IJavaProject> jProjects;

		public UpdateContainerJob(List<IJavaProject> jProjects) {
			super("Update model references...");
			this.jProjects = jProjects;
			setRule(ResourcesPlugin.getWorkspace().getRoot());
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			for (IJavaProject project : jProjects) {
				try {
					IClasspathEntry[] entries = project.getRawClasspath();
					if (!haveReferenceContainer(entries)) {
						IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
						System.arraycopy(entries, 0, newEntries, 0,
								entries.length);
						newEntries[entries.length] = JavaCore
								.newContainerEntry(IReferencesConstants.REFERENCES_CONTAINER_PATH);
						project.setRawClasspath(newEntries,
								new NullProgressMonitor());
					}
				} catch (Exception e) {
					BasePlugin.log(e);
				}
			}
			return Status.OK_STATUS;
		}

	}

}
