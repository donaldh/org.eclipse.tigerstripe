package org.eclipse.tigerstripe.workbench.optional.buckminster.internal.commands;

import org.apache.tools.ant.BuildException;
import org.eclipse.buckminster.cmdline.AbstractCommand;
import org.eclipse.buckminster.cmdline.UsageException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.optional.buckminster.TigerstripePlugin;
import org.eclipse.tigerstripe.workbench.project.GeneratorDeploymentHelper;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class InstallGeneratorCommand extends AbstractCommand {
	
	private static final int EXIT_OK = 0;
	private static final int EXIT_ERR = 1;
	
	private String generatorProjectName;

	@Override
	protected int run(IProgressMonitor arg0) throws Exception {
		
		final ITigerstripeM1GeneratorProject project = getM1GeneratorProject();
		final GeneratorDeploymentHelper helper = new GeneratorDeploymentHelper();

		IWorkspaceRunnable op = null;
		op = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {

				try {
					if (helper.deploy(project, new NullProgressMonitor()).toOSString() == null) {
						String message;
						try {
							message = getExceptionInformation(project);
						} catch (CoreException e) {
							throw e;
						}
						throw new CoreException(new Status(IStatus.ERROR, TigerstripePlugin.PLUGIN_ID, message));
					}
					System.out.println(project.getName() + " has been successfully deployed");
				} catch (TigerstripeException e) {
					throw new CoreException(new Status(IStatus.ERROR, TigerstripePlugin.PLUGIN_ID, e.getMessage()));
				} catch (CoreException e) {
					throw e;
				}

			}

			private String getExceptionInformation(final ITigerstripeM1GeneratorProject project) throws CoreException {

				String version;
				try {
					version = project.getProjectDetails().getVersion();
				} catch (TigerstripeException e) {
					throw new CoreException(new Status(IStatus.ERROR, TigerstripePlugin.PLUGIN_ID, e.getMessage()));
				}
				return "Plugin " + project.getName() + "(" + version + ") could not be deployed. See Error log for more details";
			}
			
		};

		try {
			ResourcesPlugin.getWorkspace().run(op, arg0);
		} catch (CoreException e) {
			throw e;
		}
		
		return EXIT_OK;
	}
	
	private ITigerstripeM1GeneratorProject getM1GeneratorProject() {

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(generatorProjectName);
		if (!project.exists()) {
			throw new BuildException("The " + generatorProjectName + " Tigerstripe generator project does not exist in the workspace.");
		}

		IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) project.getAdapter(IAbstractTigerstripeProject.class);
		if (!(tsProject instanceof ITigerstripeM1GeneratorProject)) {
			throw new BuildException("The " + generatorProjectName + " project is not a Tigerstripe generator project.");
		}
		return (ITigerstripeM1GeneratorProject) tsProject;
	}

	@Override
	protected void handleUnparsed(String[] unparsed) throws Exception {
		if (unparsed.length != 1){
			throw new UsageException("Need to specify the project to be generated");
		}
		generatorProjectName = unparsed[0];
	}
}
