/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.PluginLogger;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.MultiFacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.ReportModel;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.ReportRunner;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog.LogLevel;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * This class holds the generation logic for Tigerstripe projects, including all
 * details about - facet - module generation
 * 
 * @author Eric Dillon
 * 
 */
public class ProjectGenerator {

	private ITigerstripeProject project = null;

	private RunConfig config = null;

	private UseCaseProcessor processor = null;

	public ProjectGenerator(ITigerstripeProject project) {
		this(project, null);
	}

	public ProjectGenerator(ITigerstripeProject project, RunConfig config) {
		this.project = project;
		this.config = config;
		this.processor = new UseCaseProcessor(project, config);
	}

	public PluginRunStatus[] run() throws TigerstripeException,
			GenerationException {
		return run(new TigerstripeNullProgressMonitor());
	}

	private void initializeConfig() throws TigerstripeException {
		config = new RunConfig();
		IProjectDetails details = project.getProjectDetails();
		config.setIgnoreFacets("true".equalsIgnoreCase(details.getProperty(
				IProjectDetails.IGNORE_FACETS,
				IProjectDetails.IGNORE_FACETS_DEFAULT)));
		config.setGenerateModules("true".equalsIgnoreCase(details.getProperty(
				IProjectDetails.GENERATE_MODULES,
				IProjectDetails.GENERATE_MODULES_DEFAULT)));
		config.setMergeFacets("true".equalsIgnoreCase(details.getProperty(
				IProjectDetails.MERGE_FACETS,
				IProjectDetails.MERGE_FACETS_DEFAULT)));
		config.setGenerateRefProjects("true".equalsIgnoreCase(details
				.getProperty(IProjectDetails.GENERATE_REFPROJECTS,
						IProjectDetails.GENERATE_REFPROJECTS_DEFAULT)));
		config.setProcessUseCases("true".equalsIgnoreCase(details.getProperty(
				IProjectDetails.PROCESS_USECASES,
				IProjectDetails.PROCESS_USECASES_DEFAULT)));
		config.setProcessedUseCaseExtension(details.getProperty(
				IProjectDetails.USECASE_PROC_EXT,
				IProjectDetails.USECASE_PROC_EXT_DEFAULT));
		config.setUseCaseXSL(details.getProperty(
				IProjectDetails.USECASE_USEXSLT,
				IProjectDetails.USECASE_USEXSLT_DEFAULT));
	}

	public PluginRunStatus[] run(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException, GenerationException {

		List<PluginRunStatus> overallResult = new ArrayList<PluginRunStatus>();

		try {
			monitor.beginTask("Refreshing project", IProgressMonitor.UNKNOWN);
			refreshAndSetupForGeneration();

			if (project == null)
				throw new TigerstripeException("Invalid project");

			if (config == null) {
				initializeConfig();
			}

			// First look at the modules to be generated.
			if (config.isGenerateModules()) {
				PluginRunStatus[] subResult = generateModules(monitor);
				overallResult.addAll(Arrays.asList(subResult));
			}

			if (config.isGenerateRefProjects()) {
				PluginRunStatus[] subResult = generateRefProjects(monitor);
				overallResult.addAll(Arrays.asList(subResult));
			}

			// Iterate over all facets unless specified
			if (config.isIgnoreFacets()) {
				IFacetReference currentFacet = project.getActiveFacet();

				if (currentFacet != null) {
					monitor.beginTask("Resetting facets",
							ITigerstripeProgressMonitor.UNKNOWN);
					project.resetActiveFacet();
					monitor.done();
				}
				PluginRunStatus[] subResult = internalRun(monitor);
				overallResult.addAll(Arrays.asList(subResult));

				// Use case processing
				if (config.isProcessUseCases()) {
					overallResult.addAll(Arrays.asList(processor.run()));
				}

				if (currentFacet != null) {
					monitor.beginTask("Reverting to active facet ("
							+ currentFacet.resolve().getName() + ")",
							ITigerstripeProgressMonitor.UNKNOWN);
					project.setActiveFacet(currentFacet, monitor);
					monitor.done();
				}
			} else if (config.isUseCurrentFacet()) {

				IFacetReference facetRef = project.getActiveFacet();
				if (facetRef.getFacetPredicate() != null
						&& !facetRef.getFacetPredicate().isConsistent()) {
					IStatus facetInconsistencies = facetRef.getFacetPredicate()
							.getInconsistencies();
					FacetActivationResult res = new FacetActivationResult(
							project, config, facetRef);
					if (facetInconsistencies.isMultiStatus()) {
						for (IStatus error : facetInconsistencies.getChildren()) {
							res.add(error);
						}
					}
					overallResult.add(res);
				}

				PluginRunStatus[] subResult = internalRun(monitor);
				overallResult.addAll(Arrays.asList(subResult));

				// Use case processing
				if (config.isProcessUseCases()) {
					overallResult.addAll(Arrays.asList(processor.run()));
				}
			} else {

				if (project.getFacetReferences().length == 0) {
					// no Facet defined simply run plugins.
					PluginRunStatus[] subResult = internalRun(monitor);
					overallResult.addAll(Arrays.asList(subResult));

					// Use case processing
					if (config.isProcessUseCases()) {
						overallResult.addAll(Arrays.asList(processor.run()));
					}
				} else {

					IFacetReference currentFacet = project.getActiveFacet();
					if (config.isMergeFacets()) {
						PluginRunStatus[] facetResult = new PluginRunStatus[0];
						IFacetReference mergedFacet = new MultiFacetReference(
								project.getFacetReferences(), project);
						project.setActiveFacet(mergedFacet, monitor);
						facetResult = internalRun(monitor);
						overallResult.addAll(Arrays.asList(facetResult));

						// Use case processing
						if (config.isProcessUseCases()) {
							overallResult
									.addAll(Arrays.asList(processor.run()));
						}

					} else {
						// Now iterate over facets if any
						for (IFacetReference facetRef : project
								.getFacetReferences()) {

							PluginRunStatus[] facetResult = new PluginRunStatus[0];
							if (facetRef.canResolve()) {
								monitor.beginTask("Setting active facet to: "
										+ facetRef.resolve().getName(),
										ITigerstripeProgressMonitor.UNKNOWN);
								project.setActiveFacet(facetRef, monitor);
								if (facetRef.getFacetPredicate() != null
										&& !facetRef.getFacetPredicate()
												.isConsistent()) {
									IStatus facetInconsistencies = facetRef
											.getFacetPredicate()
											.getInconsistencies();
									FacetActivationResult res = new FacetActivationResult(
											project, config, facetRef);
									if (facetInconsistencies.isMultiStatus()) {
										for (IStatus error : facetInconsistencies
												.getChildren()) {
											res.add(error);
										}
									}
									overallResult.add(res);
								}
								monitor.done();
							}

							facetResult = internalRun(monitor);
							overallResult.addAll(Arrays.asList(facetResult));

							// Use case processing
							if (config.isProcessUseCases()) {
								overallResult.addAll(Arrays.asList(processor
										.run()));
							}

						}
					}
					if (currentFacet != null) {
						monitor.beginTask("Reverting to active facet ("
								+ currentFacet.resolve().getName() + ")",
								ITigerstripeProgressMonitor.UNKNOWN);
						project.setActiveFacet(currentFacet, monitor);
						monitor.done();
					} else {
						monitor.beginTask("Restoring initial state",
								ITigerstripeProgressMonitor.UNKNOWN);
						project.resetActiveFacet();
						monitor.done();
					}
				}
			}

			return overallResult.toArray(new PluginRunStatus[overallResult
					.size()]);
		} finally {
			resetAfterGeneration();
		}
	}

	/**
	 * Performs a run of all enabled plugins
	 * 
	 * At this stage it is assumed the facet has been set up properly
	 * 
	 * @return an array of PluginRunResult for details about each plugin run
	 * @throws TigerstripeException
	 * @throws GenerationException
	 */
	private PluginRunStatus[] internalRun(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException, GenerationException,
			GenerationCanceledException {

		boolean logMessages = false;
		if ("true"
				.equalsIgnoreCase(project
						.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_LogMessages))) {
			logMessages = true;
		}
		// variables used in process of hijacking stdout/stderr...
		PrintStream stdErrStreamRef = null;
		FileAppender stderrAppender = null;
		PrintStream stdOutStreamRef = null;
		FileAppender stdoutAppender = null;
		boolean changedStdOutStdErr = false;
		// allocate List that is used to hold the results from running
		// each of the plugins
		List<PluginRunStatus> result = new ArrayList<PluginRunStatus>();

		try {
			Collection<PluginReport> reports = new ArrayList<PluginReport>();

			monitor.worked(3);
			monitor.setTaskName("Refreshing project");
			// project.getArtifactManagerSession().refresh(false);
			// ((ArtifactManagerSessionImpl)
			// project.getArtifactManagerSession())
			// .setLockForGeneration(true);

			IPluginConfig[] plugins = project.getPluginConfigs();
			boolean isFirstRef = true;
			boolean validationFailed = false;

			// First run all validation plugins if any
			for (IPluginConfig iRef : plugins) {
				PluginConfig ref = (PluginConfig) iRef;
				if (isFirstRef) {
					isFirstRef = false;
					changedStdOutStdErr = hijackOutput(ref, logMessages,
							stdErrStreamRef, stderrAppender, stdOutStreamRef,
							stdoutAppender);
				}

				ref.resolve();
				if (ref.getPluginNature() == EPluggablePluginNature.Validation) {
					ref.resetFailState();
					internalPluginLoop(ref, result, reports, monitor);
					if (ref.validationFailed()) {
						validationFailed = true;
						PluginRunStatus res = new PluginRunStatus(ref, project,
								config, project.getActiveFacet());
						IStatus error = new Status(IStatus.ERROR, BasePlugin
								.getPluginId(), "Validation Failed: "
								+ ref.getValidationFailMessage());
						if (ref.getValidationFailThrowable() instanceof Exception) {
							error = new Status(IStatus.ERROR, BasePlugin
									.getPluginId(), "Validation Failed: "
									+ ref.getValidationFailMessage(),
									(Exception) ref
											.getValidationFailThrowable());
						}
						res.add(error);
						result.add(res);
					}
				}
			}

			if (!validationFailed) {
				for (IPluginConfig iRef : plugins) {
					PluginConfig ref = (PluginConfig) iRef;
					if (isFirstRef) {
						isFirstRef = false;
						changedStdOutStdErr = hijackOutput(ref, logMessages,
								stdErrStreamRef, stderrAppender,
								stdOutStreamRef, stdoutAppender);
					}

					if (ref.getPluginNature() == EPluggablePluginNature.Generic) {
						internalPluginLoop(ref, result, reports, monitor);
					}
				}
			}

			generateRunReport(reports, monitor);
		} finally {
			// ((ArtifactManagerSessionImpl)
			// project.getArtifactManagerSession())
			// .setLockForGeneration(false);

			// if System.out/System.err were redirected, need to replace the
			// redirected streams with the originals
			if (changedStdOutStdErr) {
				stdoutAppender.close();
				stderrAppender.close();
				System.setErr(stdErrStreamRef);
				System.setOut(stdOutStreamRef);
			}
		}

		return result.toArray(new PluginRunStatus[result.size()]);
	}

	private void internalPluginLoop(PluginConfig ref,
			List<PluginRunStatus> result, Collection<PluginReport> reports,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		// Make sure we only trigger "generation" plugins (i.e. not
		// the
		// publisher)

		// TODO NOW! check to see if a pluggable Ref actually exists
		// (may have been recently un-deployed - during this
		// session)

		if (ref.getCategory() == IPluginConfig.GENERATE_CATEGORY
				&& ref.isEnabled()) {

			PluginLogger.setUpForRun(ref, config);

			PluginRunStatus pluginResult = new PluginRunStatus(ref, project,
					config, project.getActiveFacet());
			try {
				monitor.worked(1);
				monitor.setTaskName("Running: " + ref.getLabel());
				ref.resolve(); // Bug #741. Need to resolve the ref in
				// case the underlying body changed.
				// TODO Capture the list of generated stuff
				config.setMonitor(monitor);
				ref.trigger(config);
				if (!ref.validationFailed()) {
					result.add(pluginResult);
				}
				PluginReport rep = ref.getReport();
				if (rep != null)
					reports.add(rep);

				monitor.worked(1);
			} catch (TigerstripeException e) {
				IStatus error = new Status(IStatus.ERROR, BasePlugin
						.getPluginId(),
						"An error was detected while triggering '"
								+ ref.getLabel()
								+ "' plugin. Generation maybe incomplete.", e);
				pluginResult.add(error);
				result.add(pluginResult);
				if (e.getException() != null) {
					PluginLogger.log(LogLevel.ERROR,
							"An error was detected while triggering '"
									+ ref.getLabel()
									+ "' plugin. Generation maybe incomplete.",
							e.getException());
				} else {
					PluginLogger.log(LogLevel.ERROR,
							"An error was detected while triggering '"
									+ ref.getLabel()
									+ "' plugin. Generation maybe incomplete.",
							e);
				}
			} catch (Exception e) {
				IStatus error = new Status(IStatus.ERROR, BasePlugin
						.getPluginId(),
						"An error was detected while triggering '"
								+ ref.getLabel()
								+ "' plugin. Generation maybe incomplete.", e);
				pluginResult.add(error);
				result.add(pluginResult);
				PluginLogger.log(LogLevel.ERROR,
						"An error was detected while triggering '"
								+ ref.getLabel()
								+ "' plugin. Generation maybe incomplete.", e);
			} finally {
				PluginLogger.tearDown();
			}
		}

	}

	/**
	 * This method hijacks the StdOut and StdErr during a plugin run if the user
	 * has chosen so so that all messages are captured and redirected to a log.
	 * 
	 * @param ref
	 * @param logMessages
	 * @param stdErrStreamRef
	 * @param stderrAppender
	 * @param stdOutStreamRef
	 * @param stdoutAppender
	 * @return
	 * @throws TigerstripeException
	 */
	private boolean hijackOutput(PluginConfig ref, boolean logMessages,
			PrintStream stdErrStreamRef, FileAppender stderrAppender,
			PrintStream stdOutStreamRef, FileAppender stdoutAppender)
			throws TigerstripeException {
		// this should only occur if a preference to "hijack
		// stdout/stderr"
		// has been set

		boolean changedStdOutStdErr = false;
		if (logMessages) {
			stdErrStreamRef = System.err;
			stdOutStreamRef = System.out;
			changedStdOutStdErr = true;
			// make sure everything sent to System.err is logged
			try {
				// determine the name to use for the logfile (in the
				// target directory)
				String outputFile = "generation.log";
				String outputDir = ref.getProjectHandle().getProjectDetails()
						.getOutputDirectory();
				String projectDir = ref.getProjectHandle().getBaseDir()
						.getCanonicalPath();

				String outputPath = projectDir + File.separator + outputDir
						+ File.separator + outputFile;
				if (config != null && config.getAbsoluteOutputDir() != null) {
					outputPath = config.getAbsoluteOutputDir() + File.separator
							+ outputDir + File.separator + outputFile;
				}
				// now, make sure that everything sent to System.err
				// is logged
				PatternLayout stderrPatternLayout = new PatternLayout();
				String stderrConversionPattern = "System.err [%d{dd-MMM-yyyy HH:mm:ss.SSS}] - %m%n";
				stderrPatternLayout
						.setConversionPattern(stderrConversionPattern);
				stderrAppender = new FileAppender(stderrPatternLayout,
						outputPath);
				Logger errLogger = Logger.getLogger("SystemErr");
				errLogger.removeAllAppenders();
				errLogger.addAppender(stderrAppender);
				errLogger.setAdditivity(false);
				// Temporary disabled until go ahead from Eclipse Legal to use.
				// System.setErr(new PrintStream(new LoggingOutputStream(
				// errLogger, warn), true));
				// and also make sure everything sent to System.out
				// is logged
				PatternLayout stdoutPatternLayout = new PatternLayout();
				String stdoutConversionPattern = "System.out [%d{dd-MMM-yyyy HH:mm:ss.SSS}] - %m%n";
				stdoutPatternLayout
						.setConversionPattern(stdoutConversionPattern);
				stdoutAppender = new FileAppender(stdoutPatternLayout,
						outputPath);
				Logger outLogger = Logger.getLogger("SystemOut");
				outLogger.removeAllAppenders();
				outLogger.addAppender(stdoutAppender);
				Level info = Level.INFO;
				outLogger.setLevel(info);
				outLogger.setAdditivity(false);
				// Temporary disabled until go ahead from Eclipse Legal to use.
				// System.setOut(new PrintStream(new LoggingOutputStream(
				// outLogger, info), true));
			} catch (IOException e) {
				TigerstripeRuntime.logErrorMessage("IOException detected", e);
				PluginRunStatus pluginResult = new PluginRunStatus(ref,
						project, config, project.getActiveFacet());
				IStatus error = new Status(IStatus.ERROR, BasePlugin
						.getPluginId(),
						"An error was detected while redirecting stdout/stderr."
								+ " Generation maybe incomplete.", e);
				pluginResult.add(error);
			}
		}

		return changedStdOutStdErr;
	}

	/**
	 * Creates a generation report for a run
	 * 
	 * @param reports
	 * @param monitor
	 * @throws TigerstripeException
	 */
	private void generateRunReport(Collection<PluginReport> reports,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		try {
			ReportModel model = new ReportModel(
					((TigerstripeProjectHandle) project).getTSProject());

			if ("true"
					.equalsIgnoreCase(project
							.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_GenerateReport))) {
				ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) project
						.getArtifactManagerSession();
				ArtifactManager artifactMgr = session.getArtifactManager();
				ReportRunner runner = new ReportRunner();
				monitor.setTaskName("Creating Tigerstripe Report");
				runner.generateReport(model, artifactMgr, reports, config);
			}

		} catch (Exception e) {
			throw new TigerstripeException(
					"An error occured will running generation report.", e);
		}
	}

	private void refreshAndSetupForGeneration() throws TigerstripeException {
		project.getArtifactManagerSession().refresh(false,
				new TigerstripeNullProgressMonitor());
		project.getArtifactManagerSession().generationStart();
	}

	private void resetAfterGeneration() throws TigerstripeException {
		project.getArtifactManagerSession().generationComplete();
	}

	private PluginRunStatus[] generateRefProjects(
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		PluginRunStatus[] result = new PluginRunStatus[0];
		ITigerstripeProject[] refProjects = project.getReferencedProjects();

		monitor.beginTask("Generating Referenced Projects", refProjects.length);
		for (ITigerstripeProject refProject : refProjects) {

			RunConfig refConfig = new RunConfig(refProject);
			String absDir = project.getBaseDir().getAbsolutePath()
					+ File.separator
					+ project.getProjectDetails().getOutputDirectory()
					+ File.separator + refProject.getProjectDetails().getName();
			refConfig.setAbsoluteOutputDir(absDir);
			ProjectGenerator gen = new ProjectGenerator(refProject, refConfig);
			result = gen.run();
			for (PluginRunStatus res : result) {
				res.setContext("Referenced Project");
			}
			monitor.worked(1);
		}

		monitor.done();
		return result;
	}

	private PluginRunStatus[] generateModules(
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		String corePath = TigerstripeRuntime
				.getProperty(TigerstripeRuntime.CORE_OSSJ_ARCHIVE);
		PluginRunStatus[] result = new PluginRunStatus[0];
		IDependency[] dependencies = project.getDependencies();

		monitor.beginTask("Generating Dependencies", dependencies.length);

		// for each dependency we create an ITigerstripeProjectModule to which
		// we had
		// the previous dependencies in the list so that the build path is
		// respected.
		int index = 0;
		for (IDependency dep : dependencies) {

			if (corePath != null && corePath.equals(dep.getPath())) {
				index++;
				continue; // DO NOT GENERATE HIDDEN CORE_OSSJ_ARCHIVE
			}

			ITigerstripeModuleProject modProj = dep.makeModuleProject(project);

			for (int i = 0; i < index; i++) {
				modProj.addTemporaryDependency(dependencies[i]);
			}

			modProj.updateDependenciesContentCache(monitor); // necessary
			// step so
			// added
			// deps are taken into
			// account
			RunConfig myConfig = new RunConfig();
			myConfig.setGenerateModules(false);
			myConfig.setUseCurrentFacet(false);
			myConfig.setIgnoreFacets(true);
			ProjectGenerator gen = new ProjectGenerator(modProj, myConfig);
			result = gen.run(monitor);

			for (PluginRunStatus res : result) {
				res.setContext("dependency");
			}
			modProj.clearTemporaryDependencies(monitor); // necessary step so
			// module
			// is clean
			index++;
			monitor.worked(1);
		}

		monitor.done();
		return result;
	}
}
