package org.eclipse.tigerstripe.workbench.headless.test;

import static org.eclipse.equinox.app.IApplicationContext.APPLICATION_ARGS;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.io.IOException;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.headless.Tigerstripe;
import org.eclipse.tigerstripe.workbench.project.GeneratorDeploymentHelper;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;

public class TestApplication extends Tigerstripe {

	public static IProject model;

	@SuppressWarnings("unchecked")
	@Override
	public Object start(IApplicationContext context) throws Exception {
		init();
		String modelPath = model.getLocation().toOSString();
		String[] args = { "GENERATION_PROJECT=" + modelPath };
		context.getArguments().put(APPLICATION_ARGS, args);
		return super.start(context);
	}

	private void init() throws IOException, TigerstripeException {
			
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		for (IProject project : workspace.getRoot().getProjects()) {
			try {
				project.delete(true, null);
			} catch (Exception e) {
			}
		}
		
		IProject generator = ProjectImport.doImport(new ZipFile(Resources.get("generator.zip")));
		model = ProjectImport.doImport(new ZipFile(Resources.get("model.zip")));
		
		workspace.checkpoint(true);
		
		ITigerstripeGeneratorProject tgenerator = adapt(generator, ITigerstripeGeneratorProject.class);
		new GeneratorDeploymentHelper().deploy(tgenerator, new NullProgressMonitor());
	}
}
