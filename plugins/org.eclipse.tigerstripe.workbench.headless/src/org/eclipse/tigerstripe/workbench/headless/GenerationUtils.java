package org.eclipse.tigerstripe.workbench.headless;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class GenerationUtils {

	public static void generate(IProject project) throws Exception {
		System.out.println("Validating " + project.getName());
		validateProject(project);
		System.out.println("Running generation on " + project.getName());
		generateTigerstripeOutput(project);
	}
	
	public static void validateProject(final IProject project) throws Exception {

		final StringBuffer errorMsg = new StringBuffer();
		IWorkspaceRunnable checkForErrorsRunnable = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {

				IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
				for (int i = 0; i < markers.length; i++) {
					if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
						String message = (String) markers[i].getAttribute(IMarker.MESSAGE);
						if (message.contains("Plugin execution not covered by lifecycle configuration")) {
							continue;
						}
						errorMsg.append("\n - ").append(markers[i].getResource().getProjectRelativePath().toString())
								.append(": ").append(message);
					}
				}
			}
		};

		ResourcesPlugin.getWorkspace().run(checkForErrorsRunnable, new NullProgressMonitor());

		if (errorMsg.length() > 0) {
			throw new TigerstripeException("Unable to perform generation. Project [" + project.getName()
					+ "] contains errors: " + errorMsg.toString());
		}
	}
	
	public static void generateTigerstripeOutput(IProject project) throws TigerstripeException {

		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) project
                .getAdapter(ITigerstripeModelProject.class);
		IM1RunConfig config = (IM1RunConfig) RunConfig.newGenerationConfig(tsProject, RunConfig.M1);
		PluginRunStatus[] statuses = tsProject.generate(config, null);
		StringBuffer failedGenerators = new StringBuffer();
		if (statuses.length != 0) {
			for (PluginRunStatus pluginRunStatus : statuses) {
				System.out.println(pluginRunStatus);
				if (pluginRunStatus.getSeverity() == IStatus.ERROR) {
					if (failedGenerators.length() > 0) {
						failedGenerators.append(", ");
					}
					failedGenerators.append(pluginRunStatus.getPlugin());
				}
			}
		}
		if (failedGenerators.length() > 0) {
			throw new TigerstripeException("Generation failed. Check the following generators for errors: ["
					+ failedGenerators.toString() + "]");
		}
	}

}
