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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.PluginLogger;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.MultiFacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.UnknownPluginException;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.ReportModel;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.ReportRunner;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog.LogLevel;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * This class holds the generation logic for Tigerstripe projects, including all
 * details about - facet - module generation
 * 
 * @author Eric Dillon
 * 
 */
public class M1Generator {
	
	// number of ticks for each unit of work.
	static private int WORK_UNIT = 30;

	private ITigerstripeModelProject project = null;

	private M1RunConfig config = null;

	private UseCaseProcessor processor = null;

	public M1Generator(ITigerstripeModelProject project, M1RunConfig config) {
		this.project = project;
		this.config = config;
		this.processor = new UseCaseProcessor(project, config);
	}

	public PluginRunStatus[] run() throws TigerstripeException,
			GenerationException {
		return run(new NullProgressMonitor());
	}

	private void initializeConfig() throws TigerstripeException {
		config = new M1RunConfig();
		IProjectDetails details = project.getProjectDetails();
		config
				.setClearDirectoryBeforeGenerate("true"
						.equalsIgnoreCase(details
								.getProperty(
										IProjectDetails.CLEAR_DIRECTORY_BEFORE_GENERATE,
										IProjectDetails.CLEAR_DIRECTORY_BEFORE_GENERATE_DEFAULT)));
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
		config.setOverrideSubprojectSettings("true".equalsIgnoreCase(details
				.getProperty(IProjectDetails.OVERRIDE_SUBPROJECT_SETTINGS,
						IProjectDetails.OVERRIDE_SUBPROJECT_SETTINGS)));
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

	public void deleteDirContents(File dir) throws TigerstripeException {
		// Delete the contents - including subDiirs
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				if (!f.delete()) {
					throw new TigerstripeException("Could not delete file " + f);
				}
			} else if (f.isDirectory()) {
				deleteDirContents(f);
				if (!f.delete()) {
					throw new TigerstripeException(
							"Could not delete directory " + f);
				}
			}
		}
	}

	public PluginRunStatus[] run(IProgressMonitor monitor)
			throws TigerstripeException, GenerationException {

		List<PluginRunStatus> overallResult = new ArrayList<PluginRunStatus>();

		try {
			// work out progress count .. (Bugzilla 241405)
			int workCount = 
				// for setup...
				WORK_UNIT+
				// allocate 30 ticks for each plugin run against this project.
				config.getPluginConfigs().length*WORK_UNIT + 
				// allocate 40 ticks for each dependent project.
				project.getDependencies().length*WORK_UNIT +
				// for pre generation work
				WORK_UNIT +
				// allocate 40 ticks for each referenced project.
				project.getReferencedProjects().length*WORK_UNIT +
				// and another for stuff in internal run
				WORK_UNIT+
				// for post generation work
				WORK_UNIT;
			

			
			monitor.beginTask("Generating project", workCount);
			monitor.subTask("Setup");
//			long before = System.currentTimeMillis();
			refreshAndSetupForGeneration();
//			long after = System.currentTimeMillis();
//			System.out.println("Refresh of "+project.getProjectLabel()+" :"+(after-before));
			if (project == null)
				throw new TigerstripeException("Invalid project");

			if (config == null) {
				initializeConfig();
			}


			// Attempt to clear the directory if requested
			if (config.isClearDirectoryBeforeGenerate()) {
				String outputPath = "";
				String outputDir = project.getProjectDetails()
						.getOutputDirectory();
				String projectDir = project.getLocation().toOSString();

				outputPath = projectDir + File.separator + outputDir;

				// First check if the directory exists - this may be the first
				// time, or the user may have deleted it!
				File outDir = new File(outputPath);
				if (outDir.exists()) {
					// See if it is actually a dir
					if (!outDir.isDirectory()) {
						throw new TigerstripeException(
								"Target directory is not a directory!");
					}
					try {
						deleteDirContents(outDir);
					} catch (TigerstripeException t) {
						throw new TigerstripeException(
								"Unable to clear target directory (" + outDir
										+ ")");
					}
				}
			}
			monitor.worked(WORK_UNIT);
			
			// First look at the modules to be generated.
			if (config.isGenerateModules()) {
				monitor.subTask("Modules");
				PluginRunStatus[] subResult = generateModules(monitor);
				overallResult.addAll(Arrays.asList(subResult));
			}

			if (config.isGenerateRefProjects()) {
				monitor.subTask("Referenced Projects");
				PluginRunStatus[] subResult = generateRefProjects(monitor);
				overallResult.addAll(Arrays.asList(subResult));
			}

			SubProgressMonitor preWork = new SubProgressMonitor(monitor,WORK_UNIT);
			SubProgressMonitor postWork = new SubProgressMonitor(monitor,WORK_UNIT);
			// Iterate over all facets unless specified
			monitor.subTask("Running Generators");
			if (config.isIgnoreFacets()) {
				preWork.beginTask("Preparing for generation", 1);
				IFacetReference currentFacet = project.getActiveFacet();
				if (currentFacet != null) {
					
					preWork.subTask("Resetting facets");
					project.resetActiveFacet();
				}
				preWork.done();
				PluginRunStatus[] subResult = internalRun(monitor, config);
				overallResult.addAll(Arrays.asList(subResult));
				
				postWork.beginTask("Finishing generation", 10);
				// Use case processing
				if (config.isProcessUseCases()) {
					overallResult.addAll(Arrays.asList(processor.run()));
				}
				postWork.worked(1);
				if (currentFacet != null) {
					postWork.subTask("Reverting to active facet ("
							+ currentFacet.resolve().getName() + ")");
					project.setActiveFacet(currentFacet, postWork);
				}
				postWork.done();
			} else if (config.isUseCurrentFacet()) {
				preWork.beginTask("Preparing for generation", 10);

				IFacetReference facetRef = project.getActiveFacet();

				// Bug 217825
				// In this case we need to reset the active
				if (facetRef.needsToBeEvaluated()) {
					project.resetActiveFacet();
					facetRef.computeFacetPredicate(preWork);
					project.setActiveFacet(facetRef, preWork);
				}

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
				preWork.done();
				
				PluginRunStatus[] subResult = internalRun(monitor, config);
				postWork.beginTask("Preparing for generation", 10);
				overallResult.addAll(Arrays.asList(subResult));

				// Use case processing
				if (config.isProcessUseCases()) {
					overallResult.addAll(Arrays.asList(processor.run()));
				}
				postWork.done();
			} else if (config.isUseProjectFacets()) {
				if (project.getFacetReferences().length == 0) {
					preWork.done();
					// no Facet defined simply run plugins.
					PluginRunStatus[] subResult = internalRun(monitor, config);
					overallResult.addAll(Arrays.asList(subResult));

					// Use case processing
					if (config.isProcessUseCases()) {
						overallResult.addAll(Arrays.asList(processor.run()));
					}
					postWork.done();
				} else {

					IFacetReference currentFacet = project.getActiveFacet();
					if (config.isMergeFacets()) {
						preWork.beginTask("Preparing for generation", 10);
						PluginRunStatus[] facetResult = new PluginRunStatus[0];
						IFacetReference mergedFacet = new MultiFacetReference(
								project.getFacetReferences(), project);
						project.setActiveFacet(mergedFacet, preWork);
						preWork.done();
						facetResult = internalRun(monitor, config);
						overallResult.addAll(Arrays.asList(facetResult));

						// Use case processing
						if (config.isProcessUseCases()) {
							overallResult
									.addAll(Arrays.asList(processor.run()));
						}
					} else {
						preWork.beginTask("Preparing for generation", project.getFacetReferences().length*WORK_UNIT);
						// Now iterate over facets if any
						for (IFacetReference facetRef : project
								.getFacetReferences()) {

							PluginRunStatus[] facetResult = new PluginRunStatus[0];
							if (facetRef.canResolve()) {
								SubProgressMonitor fProgress = new SubProgressMonitor(preWork,WORK_UNIT);
								fProgress.beginTask("Setting active facet to: "
										+ facetRef.resolve().getName(),
										IProgressMonitor.UNKNOWN);
								project.setActiveFacet(facetRef, fProgress);
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
								fProgress.done();
							}

							facetResult = internalRun(monitor, config);
							overallResult.addAll(Arrays.asList(facetResult));

							// Use case processing
							if (config.isProcessUseCases()) {
								overallResult.addAll(Arrays.asList(processor
										.run()));
							}
						}
					}
					if (currentFacet != null) {
						postWork.beginTask("Reverting to active facet ("
								+ currentFacet.resolve().getName() + ")",
								IProgressMonitor.UNKNOWN);
						project.setActiveFacet(currentFacet, postWork);
						postWork.done();
					} else {
						postWork.beginTask("Restoring initial state",
								IProgressMonitor.UNKNOWN);
						project.resetActiveFacet();
						postWork.done();
					}
				}
			} else if (config.isUsePluginConfigFacets()) {
				preWork.beginTask("Preparing for generation", WORK_UNIT);
				IFacetReference currentFacet = project.getActiveFacet();

				if (currentFacet != null) {
					project.resetActiveFacet();
				}
				preWork.done();
				PluginRunStatus[] subResult = internalRun(monitor, config);
				overallResult.addAll(Arrays.asList(subResult));

				// Use case processing
				if (config.isProcessUseCases()) {
					overallResult.addAll(Arrays.asList(processor.run()));
				}
				postWork.beginTask("Finishing generation", IProgressMonitor.UNKNOWN);
				if (currentFacet != null) {
					project.setActiveFacet(currentFacet, postWork);
				}
				postWork.done();

			} else {
				preWork.done();
				// this is the case where there is no Facet whatsoever
				PluginRunStatus[] subResult = internalRun(monitor, config);
				overallResult.addAll(Arrays.asList(subResult));
				postWork.done();
			}

			return overallResult.toArray(new PluginRunStatus[overallResult
					.size()]);
		} finally {
			resetAfterGeneration();
			IPath output = config.getOutputPath();
			IProject iProj = (IProject) project.getAdapter(IProject.class);
			if (iProj != null) {
				IResource res = iProj.findMember(output);
				try {
					if (res != null)
						res.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					else {
						iProj.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					}
				} catch (CoreException e) {
					BasePlugin.log(e);
				}
			}
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
	private PluginRunStatus[] internalRun(IProgressMonitor monitor,
			M1RunConfig config) throws TigerstripeException,
			GenerationException, GenerationCanceledException {

		boolean logMessages = false;
		// Bug 219762
		// if ("true"
		// .equalsIgnoreCase(project
		// .getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_LogMessages)))
		// {
		// logMessages = true;
		// }
		// variables used in process of hijacking stdout/stderr...
		PrintStream stdErrStreamRef = null;
		FileAppender stderrAppender = null;
		PrintStream stdOutStreamRef = null;
		FileAppender stdoutAppender = null;
		boolean changedStdOutStdErr = false;
		// allocate List that is used to hold the results from running
		// each of the plugins
		List<PluginRunStatus> result = new ArrayList<PluginRunStatus>();
		IFacetReference facetToRestore = null;
		boolean shouldRestoreFacet = false;
		SubProgressMonitor irProgress = new SubProgressMonitor(monitor,WORK_UNIT);

		try {
			Collection<PluginReport> reports = new ArrayList<PluginReport>();

			// This is built based on the set of housings and PluginsCofigs (in the case of OSGi Numbering) 
			IPluginConfig[] plugins = config.getPluginConfigs();
			boolean isFirstRef = true;
			boolean validationFailed = false;

			// First run all validation plugins if any
			for (IPluginConfig iRef : plugins) {
				SubProgressMonitor pProgress = new SubProgressMonitor(monitor,WORK_UNIT);
				pProgress.beginTask("Generating "+iRef.getPluginId(), IProgressMonitor.UNKNOWN);

				PluginConfig ref = (PluginConfig) iRef;
				
				// TODO - Do we still need this?
				try {
					ref.resolve();
				} catch (UnknownPluginException e) {
					continue;
				}
				// we're using clones of the actual pluginConfigs, so we need to
				// make
				// sure the handle is set before we attempt generation
				iRef.setProjectHandle(project);

				if (shouldRestoreFacet) {
					if (facetToRestore != null)
						project.setActiveFacet(facetToRestore, pProgress);
					else
						project.resetActiveFacet();
					shouldRestoreFacet = false;
				}

				if (isFirstRef) {
					isFirstRef = false;
					changedStdOutStdErr = hijackOutput(ref, logMessages,
							stdErrStreamRef, stderrAppender, stdOutStreamRef,
							stdoutAppender);
				}

				if (ref.getPluginNature() == EPluggablePluginNature.Validation) {

					// Check for PluginConfig level facet
					if (ref.getFacetReference() != null
							&& !config.isIgnoreFacets()
							&& !config.isUseCurrentFacet()
							&& !config.isUseProjectFacets()) {
						IFacetReference facetRef = ref.getFacetReference();
						if (facetRef.canResolve()) {
							facetToRestore = project.getActiveFacet();
							project.setActiveFacet(facetRef, pProgress);
							shouldRestoreFacet = true;
						} else {
							PluginRunStatus res = new PluginRunStatus(ref,
									project, config, project.getActiveFacet());
							IStatus error = new Status(IStatus.ERROR,
									BasePlugin.getPluginId(), "Invalid facet '"
											+ facetRef.getProjectRelativePath()
											+ "' for plugin '"
											+ iRef.getPluginId() + "'.");
							res.add(error);
							result.add(res);
							continue;
						}
					}

					ref.resetFailState();
					internalPluginLoop(ref, result, reports, pProgress);
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
				pProgress.done();
			}

			irProgress.beginTask("Cleanup", IProgressMonitor.UNKNOWN);

			if (!validationFailed) {
				for (IPluginConfig iRef : plugins) {

					PluginConfig ref = (PluginConfig) iRef;
					try {
						ref.resolve();
					} catch (UnknownPluginException e) {
						// Bug 219954
						// this means the tigerstripe.xml descriptor is
						// referencing
						// a plugin that is not deployed.
						// We've already raised a warning during the validation
						// loop.
						// Simply ignore here.
						continue;
					}

					if (shouldRestoreFacet) {
						if (facetToRestore != null)
							project.setActiveFacet(facetToRestore, irProgress);
						else
							project.resetActiveFacet();
						shouldRestoreFacet = false;
					}

					if (isFirstRef) {
						isFirstRef = false;
						changedStdOutStdErr = hijackOutput(ref, logMessages,
								stdErrStreamRef, stderrAppender,
								stdOutStreamRef, stdoutAppender);
					}

					if (ref.getPluginNature() == EPluggablePluginNature.Generic) {
						// Check for PluginConfig level facet
						if (ref.getFacetReference() != null
								&& !config.isIgnoreFacets()
								&& !config.isUseCurrentFacet()
								&& !config.isUseProjectFacets()) {
							IFacetReference facetRef = ref.getFacetReference();
							if (facetRef.canResolve()) {
								facetToRestore = project.getActiveFacet();
								project.setActiveFacet(facetRef, irProgress);
								shouldRestoreFacet = true;
							} else {
								PluginRunStatus res = new PluginRunStatus(ref,
										project, config, project
												.getActiveFacet());
								IStatus error = new Status(
										IStatus.ERROR,
										BasePlugin.getPluginId(),
										"Invalid facet '"
												+ facetRef
														.getProjectRelativePath()
												+ "' for plugin '"
												+ iRef.getPluginId() + "'.");
								res.add(error);
								result.add(res);
								continue;
							}
						}
						internalPluginLoop(ref, result, reports, irProgress);
					}
				}
			}

			generateRunReport(reports, irProgress);

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

			if (shouldRestoreFacet) {
				if (facetToRestore != null)
					project.setActiveFacet(facetToRestore, irProgress);
				else
					project.resetActiveFacet();
			}
		}
		irProgress.done();

		return result.toArray(new PluginRunStatus[result.size()]);
	}

	private void internalPluginLoop(PluginConfig ref,
			List<PluginRunStatus> result, Collection<PluginReport> reports,
			IProgressMonitor monitor) throws TigerstripeException {
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
				String failureMessage = "An error was detected while triggering '"
						+ ref.getLabel()
						+ "' plugin. Generation may be incomplete.";
				if (!"".equals(e.getMessage())) {
					failureMessage = e.getMessage()
							+ ". Generation may be incomplete.";
				}

				IStatus error = new Status(IStatus.ERROR, BasePlugin
						.getPluginId(), failureMessage, e);
				pluginResult.add(error);
				result.add(pluginResult);
				if (e.getException() != null) {
					PluginLogger.log(LogLevel.ERROR, failureMessage, e
							.getException());
				} else {
					PluginLogger.log(LogLevel.ERROR, failureMessage, e);
				}
			} catch (Exception e) {
				String failureMessage = "An error was detected while triggering '"
						+ ref.getLabel()
						+ "' plugin. Generation may be incomplete.";
				IStatus error = new Status(IStatus.ERROR, BasePlugin
						.getPluginId(), failureMessage, e);
				pluginResult.add(error);
				result.add(pluginResult);
				PluginLogger.log(LogLevel.ERROR, failureMessage, e);
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
				String projectDir = ref.getProjectHandle().getLocation()
						.toOSString();

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
								+ " Generation may be incomplete.", e);
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
			IProgressMonitor monitor) throws TigerstripeException {
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
					"An error occured while running generation report.", e);
		}
	}

	private void refreshAndSetupForGeneration() throws TigerstripeException {
		project.getArtifactManagerSession().refreshAll(false,
				new NullProgressMonitor());
		project.getArtifactManagerSession().generationStart();
	}

	private void resetAfterGeneration() throws TigerstripeException {
		project.getArtifactManagerSession().generationComplete();
	}

	private PluginRunStatus[] generateRefProjects(IProgressMonitor monitor)
			throws TigerstripeException {
		List<PluginRunStatus> overallResult = new ArrayList<PluginRunStatus>();

		ITigerstripeModelProject[] refProjects = project
				.getReferencedProjects();

		SubProgressMonitor subMonitor = new SubProgressMonitor(monitor,refProjects.length*WORK_UNIT);
		subMonitor.beginTask("Generating Referenced Projects", refProjects.length);

		for (ITigerstripeModelProject refProject : refProjects) {

			M1RunConfig refConfig = new M1RunConfig(refProject);
			if (config.isOverrideSubprojectSettings()) {
				IPluginConfig[] theConfigs = config.getPluginConfigs();
				List<IPluginConfig> newConfigs = new ArrayList<IPluginConfig>();
				for (IPluginConfig pc : theConfigs){
					IPluginConfig pcCopy = pc.clone();
					pcCopy.setProjectHandle(refProject);
					newConfigs.add(pcCopy);
				}
				refConfig.setPluginConfigs(newConfigs.toArray(theConfigs));
				refConfig.setGenerateModules(false);
				refConfig.setGenerateRefProjects(false);
			}
//			String absDir = project.getLocation().toOSString() + File.separator
//					+ project.getProjectDetails().getOutputDirectory()
//					+ File.separator + refProject.getProjectLabel();
//			refConfig.setAbsoluteOutputDir(absDir);
			M1Generator gen = new M1Generator(refProject, refConfig);
			PluginRunStatus[] subResult = gen.run();
			for (PluginRunStatus res : subResult) {
				res.setContext("Referenced Project");
			}
			overallResult.addAll(Arrays.asList(subResult));
			subMonitor.worked(1);
			if (subMonitor.isCanceled()){
				subMonitor.done();
				return overallResult.toArray(new PluginRunStatus[overallResult
					                             					.size()]);
			}

		}

		subMonitor.done();
		return overallResult.toArray(new PluginRunStatus[overallResult
		                             					.size()]);
	}

	private PluginRunStatus[] generateModules(IProgressMonitor monitor)
			throws TigerstripeException {
		String corePath = TigerstripeRuntime
				.getProperty(TigerstripeRuntime.CORE_OSSJ_ARCHIVE);
		PluginRunStatus[] result = new PluginRunStatus[0];
		IDependency[] dependencies = project.getDependencies();

		SubProgressMonitor subMonitor = new SubProgressMonitor(monitor,dependencies.length*WORK_UNIT);
		subMonitor.beginTask("Generating Dependencies",dependencies.length);

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
			M1RunConfig myConfig = new M1RunConfig();
			if (config.isOverrideSubprojectSettings()) {
				IPluginConfig[] theConfigs = config.getPluginConfigs();
				List<IPluginConfig> newConfigs = new ArrayList<IPluginConfig>();
				for (IPluginConfig pc : theConfigs){
					IPluginConfig pcCopy = pc.clone();
					pcCopy.setProjectHandle(modProj);
					newConfigs.add(pcCopy);
				}
				myConfig.setPluginConfigs(newConfigs.toArray(theConfigs));

			}
			myConfig.setGenerateModules(false);
			myConfig.setUseCurrentFacet(false);
			myConfig.setIgnoreFacets(true);
			M1Generator gen = new M1Generator(modProj, myConfig);
			result = gen.run(monitor);

			for (PluginRunStatus res : result) {
				res.setContext("dependency");
			}
			modProj.clearTemporaryDependencies(monitor); // necessary step so
			// module
			// is clean
			index++;
			subMonitor.worked(1);
			if (subMonitor.isCanceled()){
				subMonitor.done();
				return result;
			}
		}
		subMonitor.done();
		return result;
	}
}
