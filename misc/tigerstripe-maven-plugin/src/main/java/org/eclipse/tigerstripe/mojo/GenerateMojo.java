package org.eclipse.tigerstripe.mojo;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * @goal generate
 * @phase generate-sources
 */
public class GenerateMojo extends AbstractMojo {

	private static final String DELIMITER = "=";
	
	private static final String PROFILE_KEY = "profile";
	
	private static final String PROJECT_KEY = "project";
	
	/**
	 * @parameter expression="${eclipse.install}"
	 * @required
	 */
	public String eclipseInstall;
	
	/**
	 * @parameter expression="${eclipse.workspace}"
	 * @required
	 */
	public String eclipseWorkspace;
	
	/**
	 * @parameter expression="${tigerstripe.profile}"
	 * @required
	 */
	public String tigerstripeProfile;
	
	/**
	 * @parameter
	 * @required
	 */
	public String tigerstripeProject;
	
	public void execute() throws MojoExecutionException {
		
		Commandline cl = new Commandline();
		cl.setExecutable(eclipseInstall + File.separator + "eclipsec.exe");
		cl.createArg(true).setValue("-nosplash");
		cl.createArg().setValue("-data");
		cl.createArg().setValue(eclipseWorkspace);
		cl.createArg().setValue("-application");
		cl.createArg().setValue("org.eclipse.tigerstripe.workbench.headless.tigerstripe");
		
		// plug-in parameters (as key/value pairs)
		cl.createArg().setValue(PROFILE_KEY + DELIMITER + tigerstripeProfile);
		cl.createArg().setValue(PROJECT_KEY + DELIMITER + tigerstripeProject);
		
		
		StreamConsumer consumer = new StreamConsumer() {
			public void consumeLine(String line) {
				getLog().info(line);
			}
		};

		try {
			int result = CommandLineUtils.executeCommandLine(cl, consumer, consumer);
			if(result != 0) {
				throw new MojoFailureException("Tigerstripe generation failed. See logs for more information.");
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Command execution failed.", e);
		}
	}
}
