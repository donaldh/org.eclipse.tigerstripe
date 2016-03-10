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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
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
import org.eclipse.tigerstripe.workbench.model.ModelResourcesChangeListener;
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

	public PluginRunStatus[] run() throws TigerstripeException, GenerationException {
		return run(new NullProgressMonitor());
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
					throw new TigerstripeException("Could not delete directory " + f);
				}
			}
		}
	}

	public PluginRunStatus[] run(IProgressMonitor monitor)
			throws TigerstripeException, GenerationException, OperationCanceledException {

		List<PluginRunStatus> overallResult = new ArrayList<PluginRunStatus>();

		try {
			// work out progress count .. (Bugzilla 241405)
			int workCount = WORK_UNIT +
					// for each plugin run against this project.
					config.getPluginConfigs().length * WORK_UNIT +
					// for each dependent project.
					project.getDependencies().length * WORK_UNIT +
					// for pre generation work
					WORK_UNIT +
					// for each referenced project.
					project.getReferencedProjects().length * WORK_UNIT +
					// for internal run
					WORK_UNIT +
					// for post generation work
					WORK_UNIT;

			monitor.subTask("Setup");
			monitor.beginTask("Generating project", workCount);

			refreshAndSetupForGeneration();

			if (project == null)
				throw new TigerstripeException("Invalid project");

			// Attempt to clear the directory if requested
			if (config.isClearDirectoryBeforeGenerate()) {
				String outputPath = "";
				String outputDir = project.getProjectDetails().getOutputDirectory();
				String projectDir = project.getLocation().toOSString();

				outputPath = projectDir + File.separator + outputDir;

				// First check if the directory exists - this may be the first
				// time, or the user may have deleted it!
				File outDir = new File(outputPath);
				if (outDir.exists()) {
					// See if it is actually a dir
					if (!outDir.isDirectory()) {
						throw new TigerstripeException("Target directory is not a directory!");
					}
					try {
						deleteDirContents(outDir);
					} catch (TigerstripeException t) {
						throw new TigerstripeException("Unable to clear target directory (" + outDir + ")");
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

			monitor.subTask("Running Generators");

			List<IFacetReference> facetsToGenerate = new ArrayList<IFacetReference>();
			IFacetReference origFacet = project.getActiveFacet();

			if (config.isUseCurrentFacet() && origFacet.needsToBeEvaluated()) {
				project.resetActiveFacet();
				origFacet.computeFacetPredicate(monitor);
				facetsToGenerate.add(origFacet);
				reportFacetInconsistencies(origFacet, overallResult);
			} else if (config.isUseProjectFacets() && project.getFacetReferences().length > 0) {
				if (config.isMergeFacets()) {
					IFacetReference mergedFacet = new MultiFacetReference(project.getFacetReferences(), project);
					facetsToGenerate.add(mergedFacet);
				} else {
					for (IFacetReference facetRef : project.getFacetReferences()) {
						if (facetRef.canResolve()) {
							reportFacetInconsistencies(facetRef, overallResult);
							facetsToGenerate.add(facetRef);
						}
					}
				}
			} else if (origFacet != null) {
				project.resetActiveFacet();
			}

			if (facetsToGenerate.isEmpty()) {
				PluginRunStatus[] subResult = internalRun(monitor, config);
				overallResult.addAll(Arrays.asList(subResult));
				if (config.isProcessUseCases()) {
					overallResult.addAll(Arrays.asList(processor.run()));
				}
			} else {
				for (IFacetReference facet : facetsToGenerate) {
					project.setActiveFacet(facet, monitor);
					PluginRunStatus[] subResult = internalRun(monitor, config);
					overallResult.addAll(Arrays.asList(subResult));
					if (config.isProcessUseCases()) {
						overallResult.addAll(Arrays.asList(processor.run()));
					}
				}
			}

			if (origFacet != null) {
				monitor.setTaskName("Reverting to active facet (" + origFacet.resolve().getName() + ")");
				project.setActiveFacet(origFacet, monitor);
			}
		} finally {
			monitor.done();

			// Notify any listeners that the generate is complete
			GenerateCompleteManager manager = GenerateCompleteManager.getInstance();
			PluginRunStatus[] actionResults = manager.notifyListeners(this.project,
					overallResult.toArray(new PluginRunStatus[overallResult.size()]));
			if (actionResults.length > 0) {
				// Add the post generate action results to the end of the plugin
				// generate results
				overallResult.addAll(Arrays.asList(actionResults));
			}

			// Remove any markers on the target folder saying for being out of
			// date
			try {
				IProjectDetails details = project.getProjectDetails();
				if (details != null) {
					String outputDir = details.getOutputDirectory();
					IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(project.getName());
					if (proj != null) {
						final IResource folder = proj.findMember(outputDir);
						if (folder != null) {
							IMarker[] markers = folder
									.findMarkers(ModelResourcesChangeListener.TARGET_OUT_OF_DATE_MARKER, true, 1);

							for (int i = 0; i < markers.length; i++) {
								IMarker marker = markers[i];
								if (marker.exists()) {
									marker.delete();
								}
							}
						}
					}
				}
			} catch (Exception e) {
				// IGNORE, this is just a convenience framework anyway.
			}

			resetAfterGeneration();
			if (project.getLocation() != null) {
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
		return overallResult.toArray(new PluginRunStatus[overallResult.size()]);
	}

	private void reportFacetInconsistencies(IFacetReference facetRef, List<PluginRunStatus> result) {
		if (facetRef.getFacetPredicate() != null && !facetRef.getFacetPredicate().isConsistent()) {
			IStatus facetInconsistencies = facetRef.getFacetPredicate().getInconsistencies();
			FacetActivationResult res = new FacetActivationResult(project, config, facetRef);
			if (facetInconsistencies.isMultiStatus()) {
				for (IStatus error : facetInconsistencies.getChildren()) {
					res.add(error);
				}
			}
			result.add(res);
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
	private PluginRunStatus[] internalRun(IProgressMonitor monitor, M1RunConfig config)
			throws TigerstripeException, GenerationException, GenerationCanceledException {

		// Results from running each generator
		List<PluginRunStatus> result = new ArrayList<PluginRunStatus>();
		IFacetReference facetToRestore = null;
		boolean shouldRestoreFacet = false;
		SubProgressMonitor irProgress = new SubProgressMonitor(monitor, WORK_UNIT);

		try {
			Collection<PluginReport> reports = new ArrayList<PluginReport>();

			// This is built based on the set of housings and PluginsCofigs (in
			// the case of OSGi Numbering)
			IPluginConfig[] plugins = config.getPluginConfigs();
			Set<IFacetReference> validatedFacets = new HashSet<IFacetReference>();
			StringBuffer pluginString = new StringBuffer();
			for (IPluginConfig iRef : plugins) {
				if (pluginString.length() > 0) {
					pluginString.append(", ");
				}
				pluginString.append(iRef.getPluginName()).append("(").append(iRef.isEnabled()).append(")");
			}
			BasePlugin.logInfo(
					"The generator configuration for project " + project.getName() + " is: " + pluginString.toString());

			boolean validationFailed = false;
			// First run all validation plugins if any
			for (IPluginConfig iRef : plugins) {

				if (!iRef.isEnabled()) {
					continue;
				}

				SubProgressMonitor pProgress = new SubProgressMonitor(monitor, WORK_UNIT);
				pProgress.beginTask("Running Validation Plugin: " + iRef.getPluginId(), IProgressMonitor.UNKNOWN);
				if (pProgress.isCanceled()) {
					throw new OperationCanceledException("Operation Cancelled by User.");
				}

				PluginConfig ref = (PluginConfig) iRef;
				try {
					ref.resolve();
				} catch (UnknownPluginException e) {
					BasePlugin.logErrorMessage(
							"Failed to resolve required plugin: " + iRef.getPluginName() + ". Plugin will be skipped.",
							e);
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

				if (ref.getPluginNature() == EPluggablePluginNature.Validation) {

					// Check for PluginConfig level facet
					if (ref.getFacetReference() != null && !config.isIgnoreFacets() && !config.isUseCurrentFacet()
							&& !config.isUseProjectFacets()) {
						IFacetReference facetRef = ref.getFacetReference();
						if (facetRef.canResolve()) {
							if (facetRef != project.getActiveFacet()) {
								facetToRestore = project.getActiveFacet();
								project.setActiveFacet(facetRef, pProgress);
								if (!validatedFacets.contains(facetRef)) {
									reportFacetInconsistencies(facetRef, result);
									validatedFacets.add(facetRef);
								}
								shouldRestoreFacet = true;
							}
						} else {
							PluginRunStatus res = new PluginRunStatus(ref, project, config, project.getActiveFacet());
							IStatus error = new Status(IStatus.ERROR, BasePlugin.getPluginId(),
									"Invalid facet '" + facetRef.getProjectRelativePath() + "' for generator '"
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
						PluginRunStatus res = new PluginRunStatus(ref, project, config, project.getActiveFacet());
						IStatus error = new Status(IStatus.ERROR, BasePlugin.getPluginId(),
								"Validation Failed: " + ref.getValidationFailMessage());
						if (ref.getValidationFailThrowable() instanceof Exception) {
							error = new Status(IStatus.ERROR, BasePlugin.getPluginId(),
									"Validation Failed: " + ref.getValidationFailMessage(),
									(Exception) ref.getValidationFailThrowable());
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

					if (!iRef.isEnabled()) {
						continue;
					}

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

					if (ref.getPluginNature() == EPluggablePluginNature.Generic) {
						// Check for PluginConfig level facet
						if (ref.getFacetReference() != null && !config.isIgnoreFacets() && !config.isUseCurrentFacet()
								&& !config.isUseProjectFacets()) {
							IFacetReference facetRef = ref.getFacetReference();
							if (facetRef.canResolve()) {
								if (facetRef != project.getActiveFacet()) {
									facetToRestore = project.getActiveFacet();
									project.setActiveFacet(facetRef, irProgress);
									if (!validatedFacets.contains(facetRef)) {
										reportFacetInconsistencies(facetRef, result);
										validatedFacets.add(facetRef);
									}
									shouldRestoreFacet = true;
								}
							} else {
								PluginRunStatus res = new PluginRunStatus(ref, project, config,
										project.getActiveFacet());
								IStatus error = new Status(IStatus.ERROR, BasePlugin.getPluginId(),
										"Invalid facet '" + facetRef.getProjectRelativePath() + "' for generator '"
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

		} catch (GenerationCanceledException e) {
			throw e;
		} finally {
			// ((ArtifactManagerSessionImpl)
			// project.getArtifactManagerSession())
			// .setLockForGeneration(false);

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

	private void internalPluginLoop(PluginConfig ref, List<PluginRunStatus> result, Collection<PluginReport> reports,
			IProgressMonitor monitor) throws TigerstripeException {

		// Make sure we only trigger "generation" plugins
		if (ref.getCategory() == IPluginConfig.GENERATE_CATEGORY && ref.isEnabled()) {

			PluginLogger.setUpForRun(ref, config);

			PluginRunStatus pluginResult = new PluginRunStatus(ref, project, config, project.getActiveFacet(),
					ref.getLabel());
			try {
				monitor.worked(1);
				monitor.setTaskName("Running: " + ref.getLabel());
				BasePlugin.logInfo("Running: " + ref.getLabel());
				// Bug #741. Need to resolve the ref in case the underlying body
				// changed.
				ref.resolve();
				config.setMonitor(monitor);

				ref.trigger(config);

				PluginReport rep = ref.getReport();
				if (rep != null) {
					pluginResult.setReport(rep);
					reports.add(rep);
				}

				monitor.worked(1);
			} catch (TigerstripeException e) {
				String failureMessage = "An error was detected while triggering '" + ref.getLabel()
						+ "' plugin. Generation may be incomplete.";
				if (!"".equals(e.getMessage())) {
					failureMessage = e.getMessage() + ". Generation may be incomplete.";
				}
				IStatus error = new Status(IStatus.ERROR, BasePlugin.getPluginId(), failureMessage, e);
				PluginLogger.reportStatus(error);

				if (e.getException() != null) {
					PluginLogger.log(LogLevel.ERROR, failureMessage, e.getException());
				} else {
					PluginLogger.log(LogLevel.ERROR, failureMessage, e);
				}
			} catch (Exception e) {
				String failureMessage = "An error was detected while triggering '" + ref.getLabel()
						+ "' plugin. Generation may be incomplete.";
				IStatus error = new Status(IStatus.ERROR, BasePlugin.getPluginId(), failureMessage, e);
				PluginLogger.reportStatus(error);

				PluginLogger.log(LogLevel.ERROR, failureMessage, e);
			} finally {
				for (IStatus status : PluginLogger.getReportedStatuses()) {
					if (status.getSeverity() == IStatus.ERROR) {
						BasePlugin.logErrorMessage(ref.getLabel() + ": " + status.getMessage());
					} else {
						BasePlugin.logInfo(ref.getLabel() + ": " + status.getMessage());
					}
					pluginResult.add(status);
				}
				result.add(pluginResult);

				PluginLogger.tearDown();
			}
		}

	}

	/**
	 * Creates a generation report for a run
	 * 
	 * @param reports
	 * @param monitor
	 * @throws TigerstripeException
	 */
	private void generateRunReport(Collection<PluginReport> reports, IProgressMonitor monitor)
			throws TigerstripeException {
		try {
			ReportModel model = new ReportModel(((TigerstripeProjectHandle) project).getTSProject());

			if ("true".equalsIgnoreCase(
					project.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_GenerateReport))) {
				ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) project.getArtifactManagerSession();
				ArtifactManager artifactMgr = session.getArtifactManager();
				ReportRunner runner = new ReportRunner();
				monitor.setTaskName("Creating Tigerstripe Report");
				runner.generateReport(model, artifactMgr, reports, config);
			}

		} catch (Exception e) {
			throw new TigerstripeException("An error occured while running generation report.", e);
		}
	}

	private void refreshAndSetupForGeneration() throws TigerstripeException {
		project.getArtifactManagerSession().refreshAll(false, new NullProgressMonitor());
		project.getArtifactManagerSession().generationStart();
	}

	private void resetAfterGeneration() throws TigerstripeException {
		project.getArtifactManagerSession().generationComplete();
	}

	private PluginRunStatus[] generateRefProjects(IProgressMonitor monitor) throws TigerstripeException {
		List<PluginRunStatus> overallResult = new ArrayList<PluginRunStatus>();

		ITigerstripeModelProject[] refProjects = project.getReferencedProjects();

		SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, refProjects.length * WORK_UNIT);
		subMonitor.beginTask("Generating Referenced Projects", refProjects.length);

		for (ITigerstripeModelProject refProject : refProjects) {

			M1RunConfig refConfig = new M1RunConfig(refProject);
			if (config.isOverrideSubprojectSettings()) {
				IPluginConfig[] theConfigs = config.getPluginConfigs();
				List<IPluginConfig> newConfigs = new ArrayList<IPluginConfig>();
				for (IPluginConfig pc : theConfigs) {
					IPluginConfig pcCopy = pc.clone();
					pcCopy.setProjectHandle(refProject);
					newConfigs.add(pcCopy);
				}
				refConfig.setPluginConfigs(newConfigs.toArray(theConfigs));
				refConfig.setGenerateModules(false);
				refConfig.setGenerateRefProjects(false);
			}
			// String absDir = project.getLocation().toOSString() +
			// File.separator
			// + project.getProjectDetails().getOutputDirectory()
			// + File.separator + refProject.getProjectLabel();
			// refConfig.setAbsoluteOutputDir(absDir);

			if (refProject.getLocation() == null) {
				refConfig.setAbsoluteOutputDir(project.getLocation().toOSString());
			}

			M1Generator gen = new M1Generator(refProject, refConfig);
			PluginRunStatus[] subResult = gen.run();
			for (PluginRunStatus res : subResult) {
				res.setContext("Referenced Project");
			}
			overallResult.addAll(Arrays.asList(subResult));
			subMonitor.worked(1);
			if (subMonitor.isCanceled()) {
				subMonitor.done();
				return overallResult.toArray(new PluginRunStatus[overallResult.size()]);
			}

		}

		subMonitor.done();
		return overallResult.toArray(new PluginRunStatus[overallResult.size()]);
	}

	private PluginRunStatus[] generateModules(IProgressMonitor monitor) throws TigerstripeException {
		String corePath = TigerstripeRuntime.getProperty(TigerstripeRuntime.CORE_OSSJ_ARCHIVE);
		PluginRunStatus[] result = new PluginRunStatus[0];

		IDependency[] dependencies = project.getDependencies();
		ITigerstripeModelProject[] refProjects = project.getReferencedProjects();

		SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, dependencies.length * WORK_UNIT);
		subMonitor.beginTask("Generating Dependencies", dependencies.length + refProjects.length);

		// for each dependency we create an ITigerstripeProjectModule to which
		// we had
		// the previous dependencies in the list so that the build path is
		// respected.
		int index = 0;
		List<ITigerstripeModelProject> projects = new ArrayList<ITigerstripeModelProject>();
		for (IDependency dep : dependencies) {

			if (corePath != null && corePath.equals(dep.getPath())) {
				index++;
				continue; // DO NOT GENERATE HIDDEN CORE_OSSJ_ARCHIVE
			}

			ITigerstripeModuleProject modProj = dep.makeModuleProject(project);
			for (int i = 0; i < index; i++) {
				modProj.addTemporaryDependency(dependencies[i]);
			}

			// necessary step so added deps are taken into account
			modProj.updateDependenciesContentCache(monitor);
			projects.add(modProj);
		}

		for (ITigerstripeModelProject refProject : refProjects) {
			projects.add(index, refProject);
		}

		for (ITigerstripeModelProject modProj : projects) {
			M1RunConfig myConfig = new M1RunConfig();
			if (config.isOverrideSubprojectSettings()) {
				IPluginConfig[] theConfigs = config.getPluginConfigs();
				List<IPluginConfig> newConfigs = new ArrayList<IPluginConfig>();
				for (IPluginConfig pc : theConfigs) {
					IPluginConfig pcCopy = pc.clone();
					pcCopy.setProjectHandle(modProj);
					newConfigs.add(pcCopy);
				}
				myConfig.setPluginConfigs(newConfigs.toArray(theConfigs));

			}

			if (modProj.getLocation() == null) {
				myConfig.setAbsoluteOutputDir(project.getLocation().toOSString());
			}

			myConfig.setGenerateModules(false);
			myConfig.setUseCurrentFacet(false);
			myConfig.setIgnoreFacets(true);
			M1Generator gen = new M1Generator(modProj, myConfig);
			result = gen.run(monitor);

			for (PluginRunStatus res : result) {
				res.setContext("dependency");
			}

			// necessary step so module is clean
			if (modProj instanceof ITigerstripeModuleProject) {
				ITigerstripeModuleProject module = (ITigerstripeModuleProject) modProj;
				module.clearTemporaryDependencies(monitor);
			}
			index++;
			subMonitor.worked(1);
			if (subMonitor.isCanceled()) {
				subMonitor.done();
				return result;
			}
		}
		subMonitor.done();
		return result;
	}
}