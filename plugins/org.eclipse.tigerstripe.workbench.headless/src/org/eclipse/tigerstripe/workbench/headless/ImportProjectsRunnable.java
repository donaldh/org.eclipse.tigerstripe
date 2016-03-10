package org.eclipse.tigerstripe.workbench.headless;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

public class ImportProjectsRunnable implements IWorkspaceRunnable, IOverwriteQuery {

	private List<String> projects = null;
	private List<IProject> importedProjects = new ArrayList<IProject>();

	public ImportProjectsRunnable(List<String> projects) {
		this.projects = projects;
	}

	public String queryOverwrite(String pathString) {
		return IOverwriteQuery.YES;
	}

	public List<IProject> getImportedProjects() {
		return importedProjects;
	}

	public void run(final IProgressMonitor monitor) throws CoreException {

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();

		importedProjects = new ArrayList<IProject>();
		for (String projectPath : projects) {
			if (isStringValid(projectPath)) {
				if (projectPath.contains("\\")) {
					projectPath = projectPath.replaceAll("\\\\", "/");
				}

				if (projectPath.endsWith("/")) {
					projectPath = projectPath.substring(0, projectPath.length() - 1);
				}

				System.out.println("Importing project: " + projectPath + " into workspace: "
						+ workspace.getRoot().getLocation().toString());

				String projectName = projectPath.substring(projectPath.lastIndexOf("/") + 1);

				File projectMetaFile = new File(projectPath + "/.project");
				if (!projectMetaFile.exists()) {
					try {
						System.out.println("Attempting to create default .project file for project: " + projectName);
						String content = IOUtils.toString(Activator.getFile("templates/project.xml"));
						content = content.replace("${project.name}", projectName);
						FileUtils.writeStringToFile(projectMetaFile, content);
					} catch (Exception e) {
						e.printStackTrace();
						throw new CoreException(new Status(IStatus.ERROR, "Tigerstripe", projectMetaFile.toString()
								+ " does not exist, and an error was thrown while trying to create a default. File is required and should be checked-in to your SCM."));
					}
				}

				ProjectRecord projectRecord = new ProjectRecord(projectMetaFile);
				IProject project = workspace.getRoot().getProject(projectName);
				
				String workspaceLocation = workspace.getRoot().getLocation().toString();
				if (workspaceLocation.startsWith(projectPath) || projectPath.contains("target/work/")) {
					System.out.println("Workspace is inside project " + projectName
							+ ", copying project sources into the workspace.");
					if (!project.exists()) {
						try {
							URI locationURI = projectRecord.description.getLocationURI();
							File importSource = new File(locationURI);
							List<?> filesToImport = FileSystemStructureProvider.INSTANCE.getChildren(importSource);
							Iterator<?> iter = filesToImport.iterator();
							while (iter.hasNext()) {
								Object file = iter.next();
								if (file instanceof File && ((File)file).getName().equals("target")) {
									System.out.println("Ignoring 'target' folder...");
									iter.remove();
								}
							}
							ImportOperation operation = new ImportOperation(project.getFullPath(), importSource,
									FileSystemStructureProvider.INSTANCE, this, filesToImport);
							operation.setOverwriteResources(true);
							operation.setCreateContainerStructure(false);
							operation.run(monitor);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} else {
					System.out.println("Workspace is outside the project " + project.getName()
							+ ", generation will run directly on the project sources. [project=" + projectPath + ", workspace=" + workspaceLocation + "]");
					IProjectDescription description = ResourcesPlugin.getWorkspace()
							.loadProjectDescription(new Path(projectPath + "/.project"));
					// Use the actual project folders name as seen on disk, as
					// the .project name does not always match what is in SCM
					description.setName(projectName);
					project.create(description, null);
					project.open(null);
				}
				
				IFile classpath = project.getFile(".classpath");
				if (!classpath.exists()) {
					try {
						String content = IOUtils.toString(Activator.getFile("templates/classpath.xml"));
						FileUtils.writeStringToFile(classpath.getLocation().toFile(), content);
					} catch (Exception e) {
						System.err.println("An error occurred trying to create default .classpath file for project: "
								+ project.getName() + ":");
						e.printStackTrace();
					}
				}

				// Let Tigerstripe start processing the project sources
				project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				importedProjects.add(project);
			} else {
				System.err.print("Project path is not valid: " + projectPath);
			}
		}

		// Now that all projects have been imported, we can "build" them, to
		// check for Tigerstripe validation errors
		for (IProject project : importedProjects) {
			try {
				ITigerstripeModelProject tsProject = (ITigerstripeModelProject) project
		                .getAdapter(ITigerstripeModelProject.class);
				if (tsProject != null) {
					tsProject.getArtifactManagerSession().refreshAll(false, monitor);
				} else {
					System.out.println("Failed to process project " + project.getName()
							+ " as a Tigerstripe Project, validation will not be run.");
				}
			} catch (Exception e) {
				throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						"Failed to adapt imported project as a Tigerstripe Model project.", e));
			}

			project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		}
	}

	private boolean isStringValid(String text) {
		return (text != null && text.trim().length() > 0);
	}

}
