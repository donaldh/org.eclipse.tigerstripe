package org.eclipse.tigerstripe.workbench.optional.buckminster.internal.actor;

import org.apache.tools.ant.BuildException;
import org.eclipse.buckminster.core.actor.AbstractActor;
import org.eclipse.buckminster.core.actor.IActionContext;
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

public class InstallGeneratorActor extends AbstractActor {

	public static final String ID = "tigerstripe.installGenerator";

	public static final String PROJECT_NAME_PROPERTY = "projectname";

	@Override
	protected IStatus internalPerform(IActionContext ctx, IProgressMonitor monitor) throws CoreException {

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
			ResourcesPlugin.getWorkspace().run(op, monitor);
		} catch (CoreException e) {
			throw e;
		}
		
		return Status.OK_STATUS;
	}

	private ITigerstripeM1GeneratorProject getM1GeneratorProject() {

		String projectname = this.getActorProperty(PROJECT_NAME_PROPERTY);

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectname);
		if (!project.exists()) {
			throw new BuildException("The " + projectname + " Tigerstripe generator project does not exist in the workspace.");
		}

		IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) project.getAdapter(IAbstractTigerstripeProject.class);
		if (!(tsProject instanceof ITigerstripeM1GeneratorProject)) {
			throw new BuildException("The " + projectname + " project is not a Tigerstripe generator project.");
		}
		return (ITigerstripeM1GeneratorProject) tsProject;
	}

}
