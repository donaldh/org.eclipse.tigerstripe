package org.eclipse.tigerstripe.workbench.headless;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
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
				System.out.println("Importing project: " + projectPath);

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

				IProjectDescription description = ResourcesPlugin.getWorkspace()
						.loadProjectDescription(new Path(projectPath + "/.project"));
				IProject project =  workspace.getRoot().getProject(description.getName());
				project.create(description, null);
				project.open(null);
//				ProjectRecord projectRecord = new ProjectRecord(new File(projectPath + "/.project"));

//				IProject project = workspace.getRoot().getProject(projectName);
//				if (!project.exists()) {
//					try {
//						URI locationURI = projectRecord.description.getLocationURI();
//						File importSource = new File(locationURI);
//						List<?> filesToImport = FileSystemStructureProvider.INSTANCE.getChildren(importSource);
//						ImportOperation operation = new ImportOperation(project.getFullPath(), importSource,
//								FileSystemStructureProvider.INSTANCE, this, filesToImport);
//						operation.setOverwriteResources(true);
//						operation.setCreateContainerStructure(false);
//						operation.run(monitor);
//					} catch (InvocationTargetException e) {
//						e.printStackTrace();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					IFile classpath = project.getFile(".classpath");
					if (!classpath.exists()) {
						try {
							String content = IOUtils.toString(Activator.getFile("templates/classpath.xml"));
							FileUtils.writeStringToFile(classpath.getLocation().toFile(), content);
						} catch (Exception e) {
							System.err
									.println("An error occurred trying to create default .classpath file for project: "
											+ project.getName() + ":");
							e.printStackTrace();
						}
					}

					// Remove maven build/nature before running headless,
					// wreaks
					// havoc.
					List<String> natures = new ArrayList<String>();
					for (String nature : project.getDescription().getNatureIds()) {
						if (!nature.equals("org.eclipse.m2e.core.maven2Nature")
								&& !nature.equals("org.maven.ide.eclipse.maven2Nature")) {
							natures.add(nature);
						}
					}
					String[] newNatures = new String[natures.size()];
					project.getDescription().setNatureIds(natures.toArray(newNatures));

					List<ICommand> builders = new ArrayList<ICommand>();
					for (ICommand build : project.getDescription().getBuildSpec()) {
						if (!build.getBuilderName().equals("org.eclipse.m2e.core.maven2Builder")
								&& !build.getBuilderName().equals("org.maven.ide.eclipse.maven2Builder")) {
							builders.add(build);
						}
					}
					ICommand[] build = new ICommand[builders.size()];
					project.getDescription().setBuildSpec(builders.toArray(build));
					// Let Tigerstripe start processing the project sources
					project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
//				}
				importedProjects.add(project);
			} else {
				System.err.print("Project path is not valid: " + projectPath);
			}
		}

		// FIXME Is this really necessary if all projects have already
		// refreshed?
		workspace.getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);

		// Now that all projects have been imported, we can "build" them, to
		// check for Tigerstripe validation errors
		for (final IProject project : importedProjects) {
			Object adapted = null;
			try {
				adapted = ((IAdaptable) project).getAdapter(ITigerstripeModelProject.class);
				if (adapted != null) {
					((ITigerstripeModelProject) adapted).getArtifactManagerSession().refreshAll(false, monitor);
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
