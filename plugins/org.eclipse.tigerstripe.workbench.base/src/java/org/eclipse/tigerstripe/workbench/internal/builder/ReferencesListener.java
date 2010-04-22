package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.classpath.IReferencesConstants;
import org.eclipse.tigerstripe.workbench.internal.core.classpath.ReferencesClasspathContainer;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ReferencesListener {

	public void changed(Collection<IResource> removedResources,
			Collection<IResource> addedResources,
			Collection<IResource> changedResources) {
		Set<ProjectDetails> changedProjects = new HashSet<ProjectDetails>();
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
		checkAccess(changedReferences);
		checkReferencesProjects(changedProjects, changedReferences);
		updateReferences(changedProjects);
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
			if (res instanceof IProject) {
				IProject iProject = (IProject) res;
				ProjectDetails details = nameToDetails.get(iProject);
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

	private boolean checkChangedProjects(
			Collection<IResource> changedResources,
			Collection<ProjectDetails> changedProjects,
			Collection<String> changedReferences) {
		Set<IProject> projectsToCheck = new HashSet<IProject>();
		for (IResource res : changedResources) {
			if ("tigerstripe.xml".equals(res.getName())) {
				projectsToCheck.add(res.getProject());
			}
		}
		for (IProject iProject : projectsToCheck) {
			ProjectDetails details = nameToDetails.get(iProject);
			if (details == null) {
				return true;
			}
			if (details.getProject() != null) {
				ProjectDetails newDetails = new ProjectDetails(iProject);
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

	private void checkAccess(Collection<String> changedReferences) {
		for (ProjectDetails details : nameToDetails.values()) {
			IProject iProject = details.getIProject();
			if (iProject.isOpen() == details.isNoAccess()) {
				ProjectDetails newDetails = new ProjectDetails(iProject);
				nameToDetails.put(iProject, newDetails);
				if (details.getModelId() != null) {
					changedReferences.add(details.getModelId());
				}
				if (newDetails.getModelId() != null) {
					changedReferences.add(newDetails.getModelId());
				}
			}
		}
	}

	private void checkReferencesProjects(
			Collection<ProjectDetails> changedProjects,
			Collection<String> changedReferences) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			ProjectDetails details = nameToDetails.get(project);
			if (details == null) {
				details = new ProjectDetails(project);
				nameToDetails.put(project, details);
			}
			if (!changedProjects.contains(details)) {
				String[] references = details.getReferences();
				boolean addProject = false;
				if (references != null) {
					for (String ref : references) {
						if (changedReferences.contains(ref)) {
							addProject = true;
							break;
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
			if (res instanceof IProject) {
				IProject iProject = (IProject) res;
				ProjectDetails details = new ProjectDetails(iProject);
				nameToDetails.put(iProject, details);
				if (details.getModelId() != null) {
					changedReferences.add(details.getModelId());
				}
			}
		}
	}

	private void updateAllReferences() {
		nameToDetails = new HashMap<IProject, ProjectDetails>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		List<ProjectDetails> changedProjects = new ArrayList<ProjectDetails>(
				root.getProjects().length);
		for (IProject project : root.getProjects()) {
			ProjectDetails details = new ProjectDetails(project);
			nameToDetails.put(project, details);
			changedProjects.add(details);
		}
		updateReferences(changedProjects);
	}

	private void updateReferences(Collection<ProjectDetails> changedProjects) {
		Map<IJavaProject, ReferencesClasspathContainer> map = new HashMap<IJavaProject, ReferencesClasspathContainer>();
		for (ProjectDetails details : changedProjects) {
			IProject project = details.getIProject();
			try {
				if (project.hasNature(JavaCore.NATURE_ID)) {
					map.put(JavaCore.create(project),
							new ReferencesClasspathContainer(details
									.getProject()));
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

	private static boolean needReferenceContainer(IJavaProject project) {
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

	private Map<IProject, ProjectDetails> nameToDetails = new HashMap<IProject, ProjectDetails>();

	private class ProjectDetails {

		private String modelId;
		private String[] references;
		private ITigerstripeModelProject project;
		private IProject iProject;
		private boolean noAccess;

		public ProjectDetails(IProject project) {
			this.iProject = project;
			noAccess = !project.isOpen();
			if (noAccess)
				return;
			IAbstractTigerstripeProject aProject = (IAbstractTigerstripeProject) project
					.getAdapter(IAbstractTigerstripeProject.class);
			if (aProject instanceof ITigerstripeModelProject) {
				this.project = (ITigerstripeModelProject) aProject;
				try {
					modelId = this.project.getModelId();
				} catch (Exception e) {
					// ignore any exceptions
				}
				try {
					ModelReference[] modelReferences = this.project
							.getModelReferences();
					references = new String[modelReferences.length];
					for (int i = 0; i < modelReferences.length; i++) {
						references[i] = modelReferences[i].getToModelId();
					}
				} catch (Exception e) {
					// ignore any exceptions
				}
			}
		}

		public boolean needReferenceContainer() {
			if (project == null || iProject == null)
				return false;
			IJavaProject jProject = JavaCore.create(iProject);
			if (jProject == null)
				return false;
			return ReferencesListener.needReferenceContainer(jProject);
		}

		public boolean isNoAccess() {
			return noAccess;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((iProject == null) ? 0 : iProject.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProjectDetails other = (ProjectDetails) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (iProject == null) {
				if (other.iProject != null)
					return false;
			} else if (!iProject.equals(other.iProject))
				return false;
			return true;
		}

		public String getModelId() {
			return modelId;
		}

		public String[] getReferences() {
			return references;
		}

		public IProject getIProject() {
			return iProject;
		}

		public ITigerstripeModelProject getProject() {
			return project;
		}

		private ReferencesListener getOuterType() {
			return ReferencesListener.this;
		}

	}

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
