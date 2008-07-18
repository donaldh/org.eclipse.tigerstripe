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
package org.eclipse.tigerstripe.workbench.internal.core.plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactModel;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactWrapper;
import org.eclipse.tigerstripe.workbench.plugins.IExpander;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * 
 * 
 */
public class Expander implements IExpander {

	/** The tag that is used to identify a pluginProperty */
	private final String PROP_TAG = "ppProp";

	private String wrapperName = "model";

	private IPluginConfig pluginConfig;
	private IAbstractArtifact currentArtifact;
	private IArtifactWrapper currentWrapper;

	public Expander(PluginConfig pluginConfig) {
		this.pluginConfig = pluginConfig;
	}

	public Expander() {
		this.pluginConfig = null;
	}

	public void setPluginConfig(IPluginConfig pluginConfig) {
		this.pluginConfig = pluginConfig;
	}

	/**
	 * Allows to set a "current artifact" for this expander. When a current
	 * artifact is set, additional variable may expanded. This is used in the
	 * context of Artifact-based plugin rules so that the user can get access to
	 * details about the current artifact
	 * 
	 * @param currentArtifact
	 * @since 1.2
	 */
	public void setCurrentArtifact(IAbstractArtifact currentArtifact) {
		this.currentArtifact = currentArtifact;
	}

	/**
	 * As for current Artifact, can set a "Current Model" for this expander This
	 * can be used to extract things like the TargetPackage from the model -
	 * especially useful for making file names!
	 * 
	 * @deprecated
	 * @param currentModel
	 */
	public void setCurrentModel(IArtifactModel currentModel) {
		setCurrentWrapper(currentModel);
	}

	/**
	 * This variant is necessary in case the model has been named something
	 * other than "model"
	 * 
	 * @deprecated
	 * @param currentModel
	 * @param modelName
	 */
	public void setCurrentModel(IArtifactModel currentModel, String modelName) {
		setCurrentWrapper(currentModel, wrapperName);
	}

	/**
	 * As for current Artifact, can set a "Current Model" for this expander This
	 * can be used to extract things like the TargetPackage from the model -
	 * especially useful for making file names!
	 * 
	 * @param currentModel
	 */
	public void setCurrentWrapper(IArtifactWrapper wrapper) {
		this.currentWrapper = wrapper;
	}

	/**
	 * This variant is necessary in case the model has been named something
	 * other than "model"
	 * 
	 * @param currentModel
	 * @param wrapperName
	 */
	public void setCurrentWrapper(IArtifactWrapper wrapper, String wrapperName) {
		this.currentWrapper = wrapper;
		setWrapperName(wrapperName);
	}
	
	/*
	 * This is a bit "hairy"
	 */
	public String expandVar(String inString, ITigerstripeModelProject project) {
		try {
			if (project instanceof TigerstripeProjectHandle) {
				TigerstripeProjectHandle tsProj = (TigerstripeProjectHandle) project;
				return expandVar(inString, tsProj.getTSProject());
			} else
				return inString;

		} catch (TigerstripeException t) {
			return inString;
		}
	}

	public String expandVar(String inString) {
		if (this.pluginConfig == null)
			return inString;
		else {
			TigerstripeProject project = ((PluginConfig) this.pluginConfig)
					.getProject();
			return expandVar(inString, project);
		}
	}

	public String expandVar(String inString, TigerstripeProject project) {

		String outString = inString;

		outString = matchProjectProperty(outString, project);
		outString = matchPluginProperty(outString);

		if (currentArtifact != null)
			outString = matchCurrentArtifact(outString);
		if (currentWrapper != null)
			outString = matchCurrentWrapper(outString);

		// Handle "nested" examples of expander - especially important for model
		// filenames that might have project references in
		// loop until the input & output are the same

		// TigerstripeRuntime.logInfoMessage("out "+outString);

		if (!outString.equals(inString)) {

			outString = expandVar(outString);
		}

		return outString;
	}

	/**
	 * Matches properties of the pluginConfig, based on the leading tag PROP_TAG.
	 * 
	 * There should then be a "." followed by a property name that exists
	 * 
	 * eg ${ppProp.target}
	 * 
	 * If defined return the value of the property from the project, else return
	 * the string unchanged - ie with ${ppProp.XXX} still in it
	 * 
	 * Note there could be multiple matches for multiple properties!
	 * 
	 */
	protected String matchPluginProperty(String inString) {
		String outString = inString;
		Pattern propPattern = Pattern.compile("\\$\\{" + PROP_TAG
				+ "\\.[^\\}]*\\}");
		Matcher propPatternMt = propPattern.matcher(inString);

		// This has to be done in two steps in case of multiple replacements
		// being needed
		ArrayList<MatchResult> results = new ArrayList<MatchResult>();
		while (propPatternMt.find()) {
			// We have a property - so get the name
			MatchResult result = propPatternMt.toMatchResult();
			results.add(result);
		}

		for (MatchResult result : results) {
			// Get the actual propname
			int tagLength = ("${" + PROP_TAG + ".").length();
			String propName = result.group().substring(tagLength,
					result.group().length() - 1);
			// see it it's a declared property
			String[] definedProps = this.pluginConfig.getDefinedProperties();
			for (int i = 0; i < definedProps.length; i++) {
				if (definedProps[i].equals(propName)) {
					// found one - get the value
					String value = (String) this.pluginConfig.getProperty(
							propName);
					// replace the first occurence of this in the original
					// string.
					// watch out for horrible characters causing matching
					// failure!
					outString = outString.replaceFirst("\\$\\{" + PROP_TAG
							+ "\\." + propName + "\\}", value);
				}
			}
		}

		return outString;

	}

	/**
	 * Matches variables against the current wrapper
	 * 
	 * Use the syntax ${wrapper.variableName}
	 * 
	 * We don't know what variables exist in the wrapper so have to be careful.
	 * This looks for fields (but they have to be public), or getXxx methods -
	 * first letter must be capital
	 * 
	 * @param outString
	 * @return
	 */
	protected String matchCurrentWrapper(String outString) {
		Pattern modelPattern = Pattern.compile("\\$\\{" + getWrapperName()
				+ "\\.[^\\}]*\\}");
		Matcher modelPatternMt = modelPattern.matcher(outString);

		// This has to be done in two steps in case of multiple replacements
		// being needed
		ArrayList<MatchResult> results = new ArrayList<MatchResult>();
		while (modelPatternMt.find()) {
			// We have a property - so get the name
			MatchResult result = modelPatternMt.toMatchResult();
			results.add(result);
		}
		for (MatchResult result : results) {
			// Get the varname
			int tagLength = ("${" + getWrapperName() + ".").length();
			String varName = result.group().substring(tagLength,
					result.group().length() - 1);

			// look for a Field or a getMethod
			try {
				java.lang.reflect.Field varField = currentWrapper.getClass()
						.getField(varName);
				String value = varField.get(currentWrapper).toString();
				// Deal with $ characters in the output
				String temp_value = Matcher.quoteReplacement(value);
				outString = outString.replaceFirst("\\$\\{" + getWrapperName()
						+ "\\." + varName + "\\}", temp_value);
				// Return the $ in the original original "replacement"
				// We need this in case of nesting
				outString = outString.replaceFirst("\\\\$", outString);
			} catch (NoSuchFieldException n) {

				// no field - try a method
				try {
					String capVarName = varName.substring(0, 1).toUpperCase()
							+ varName.substring(1);
					Method varMethod = currentWrapper.getClass().getMethod(
							"get" + capVarName);
					String value = varMethod.invoke(currentWrapper).toString();

					// Deal with $ characters in the output
					String temp_value = Matcher.quoteReplacement(value);
					outString = outString.replaceFirst("\\$\\{"
							+ getWrapperName() + "\\." + varName + "\\}",
							temp_value);
					// Return the $ in the original original "replacement"
					// We need this in case of nesting
					outString = outString.replaceFirst("\\\\$", outString);

				} catch (NoSuchMethodException m) {
					// TigerstripeRuntime.logInfoMessage("No such Field/Method :
					// "+varName);
				} catch (Exception e) {
					// Don't know - ignore!
					// TigerstripeRuntime.logInfoMessage("No such Field/Method 2
					// : "+varName+" "+e.getMessage());
				}

			} catch (Exception e) {
				// Don't know - ignore!
				// TigerstripeRuntime.logInfoMessage("Replace failed
				// "+e.getMessage());
			}
		}

		return outString;

	}

	/**
	 * Matches various variables against the current artifact that's been put
	 * into context - artifact.Name: the name of the artifact -
	 * artifact.Package: the package of the artifact - artifact.Path: the path
	 * of the artifact (ie. the package where '.' is replaced by '/')
	 * 
	 * @param outString
	 * @return
	 * @since 1.2
	 */
	protected String matchCurrentArtifact(String outString) {

		// ${artifact.Name}
		Pattern artifactNamePt = Pattern.compile("\\$\\{artifact\\.Name\\}");
		Matcher artifactNameMt = artifactNamePt.matcher(outString);
		outString = artifactNameMt.replaceAll(currentArtifact.getName());

		// ${artifact.Package}
		Pattern artifactPackagePt = Pattern
				.compile("\\$\\{artifact\\.Package\\}");
		Matcher artifactPackageMt = artifactPackagePt.matcher(outString);
		outString = artifactPackageMt.replaceAll(currentArtifact.getPackage());

		// ${artifact.Package}
		artifactPackagePt = Pattern.compile("\\$\\{artifact\\.Path\\}");
		artifactPackageMt = artifactPackagePt.matcher(outString);
		outString = artifactPackageMt.replaceAll(currentArtifact.getPackage()
				.replace('.', '/'));

		return outString;
	}

	protected String matchProjectProperty(String outString,
			TigerstripeProject project) {
		// Look for variable in the inString - constrained by ${}
		// If the pattern isn't there - just return as is.

		// Need to check for spaces in the properties being used
		// and replace them with "_"

		Pattern ver = Pattern.compile("\\$\\{ver\\}");
		Matcher versionMatcher = ver.matcher(outString);
		String projectVersion = project.getProjectDetails().getVersion();
		projectVersion = projectVersion.replaceAll(" ", "_");
		if (versionMatcher.find(0)) {
			if (projectVersion.length() > 0) {
				outString = versionMatcher.replaceAll(projectVersion);
			} else {
				outString = versionMatcher.replaceAll("UNKNOWN_VERSION");
			}
		}

		Pattern name = Pattern.compile("\\$\\{name\\}");
		Matcher nameMatcher = name.matcher(outString);
		String projectName = project.getProjectLabel();
		projectName = projectName.replaceAll(" ", "_");
		if (nameMatcher.find(0)) {
			if (projectName.length() > 0) {
				outString = nameMatcher.replaceAll(projectName);
			} else {
				outString = nameMatcher.replaceAll("UNKNOWN_NAME");
			}
		}

		// Should actually be ${project.Name} and ${project.Version} for
		// compatability
		// So check for them as well

		Pattern projVer = Pattern.compile("\\$\\{project\\.Version\\}");
		Matcher projectVersionMatcher = projVer.matcher(outString);
		projectVersion = project.getProjectDetails().getVersion();
		projectVersion = projectVersion.replaceAll(" ", "_");
		if (projectVersionMatcher.find(0)) {
			if (projectVersion.length() > 0) {
				outString = projectVersionMatcher.replaceAll(projectVersion);
			} else {
				outString = projectVersionMatcher.replaceAll("UNKNOWN_VERSION");
			}
		}

		Pattern projName = Pattern.compile("\\$\\{project\\.Name\\}");
		Matcher projNameMatcher = projName.matcher(outString);
		projectName = project.getProjectLabel();
		projectName = projectName.replaceAll(" ", "_");
		if (projNameMatcher.find(0)) {
			if (projectName.length() > 0) {
				outString = projNameMatcher.replaceAll(projectName);
			} else {
				outString = projNameMatcher.replaceAll("UNKNOWN_NAME");
			}
		}

		// Support of BUG 280 (part)
		// Look for the common NS reference so that we can use that locally
		// and always point to the *local* project CommonNS
		// NOTE THIS USES THE LOCAL PROJECT IN ALL CASES - ie IT IGNORES THE
		// PROJECT THAT IS PASSED IN
		TigerstripeProject localProject = ((PluginConfig) this.pluginConfig)
				.getProject();

		Pattern commonNSName = Pattern.compile("\\$\\{project\\.CommonNS\\}");
		Matcher commonNSNameMatcher = commonNSName.matcher(outString);
		String commonNS = localProject.getProjectDetails().getProperty(
				"ossj.common.targetNamespace", "");
		commonNS = commonNS.replaceAll(" ", "_");
		if (commonNSNameMatcher.find(0)) {
			if (commonNS.length() > 0) {
				outString = commonNSNameMatcher.replaceAll(commonNS);
			} else {
				outString = commonNSNameMatcher.replaceAll("UNKNOWN_COMMON_NS");
			}
		}

		return outString;
	}

	public String getWrapperName() {
		return wrapperName;
	}

	public void setWrapperName(String wrapperName) {
		this.wrapperName = wrapperName;
	}

}
