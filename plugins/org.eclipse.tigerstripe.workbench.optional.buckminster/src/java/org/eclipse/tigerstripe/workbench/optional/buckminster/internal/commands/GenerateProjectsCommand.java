package org.eclipse.tigerstripe.workbench.optional.buckminster.internal.commands;

import java.io.File;
import java.util.List;

import org.eclipse.buckminster.cmdline.AbstractCommand;
import org.eclipse.buckminster.cmdline.Option;
import org.eclipse.buckminster.cmdline.OptionDescriptor;
import org.eclipse.buckminster.cmdline.OptionValueType;
import org.eclipse.buckminster.cmdline.UsageException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.headless.Tigerstripe;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class GenerateProjectsCommand  extends AbstractCommand {
	
	private static final int EXIT_OK = 0;
	private static final int EXIT_ERR = 1;

	private String genProjectName = "";

	@Override
	protected int run(IProgressMonitor arg0) throws Exception {
		long start = System.currentTimeMillis();
		if (genProjectName.equals("")){
			System.err.println("The project to be generated needs to be specified using -p");
			return EXIT_ERR;
		}
		Tigerstripe.printTigerstipeVersionInfo();
	    System.out.println("Generating Project:"+genProjectName);
		generateTigerstripeOutput(genProjectName);
	    long finish = System.currentTimeMillis();
		System.out.println("Generation complete. Took "+(finish- start)+" milliseconds.");
		return EXIT_OK;
	}

	@Override
	protected void handleUnparsed(String[] unparsed) throws Exception {
		if (unparsed.length != 1){
			throw new UsageException("Need to specify the project to be generated");
		}
		genProjectName = unparsed[0];
	}
	public static void generateTigerstripeOutput(String genProjectName) throws TigerstripeException {

		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.findProject(genProjectName);
		IM1RunConfig config = (IM1RunConfig) RunConfig.newGenerationConfig(project, RunConfig.M1);
		PluginRunStatus[] status = project.generate(config, null);

		if (status.length != 0) {
			for (PluginRunStatus pluginRunStatus : status) {
				System.out.println(pluginRunStatus);
			}
		}
	}
}
