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

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactModel;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IExpander {

	/** The tag that is used to identify a pluginProperty */
	public final String PROP_TAG = "ppProp";

	/**
	 * Sets the pluginConfig for the expander. This is needed in all cases unless
	 * you want to just get the project properties.
	 * 
	 * @param pluginConfig
	 */
	public void setPluginConfig(IPluginConfig pluginConfig);

	/**
	 * Allows to set a "current artifact" for this expander. When a current
	 * artifact is set, additional variable may expanded. This is used in the
	 * context of Artifact-based plugin rules so that the user can get access to
	 * details about the current artifact
	 * 
	 * @param currentArtifact
	 * @since 1.2
	 */
	public void setCurrentArtifact(IAbstractArtifact currentArtifact);

	/**
	 * As for current Artifact, can set a "Current Model" for this expander This
	 * can be used to extract things like the TargetPackage from the model -
	 * especially useful for making file names!
	 * 
	 * @param currentModel
	 */
	public void setCurrentModel(IArtifactModel currentModel);

	/**
	 * As for current Artifact, can set a "Current Model" for this expander This
	 * can be used to extract things like the TargetPackage from the model -
	 * especially useful for making file names! This variant is necessary in
	 * case the model has been named something other than "model"
	 * 
	 * @param currentModel
	 */
	public void setCurrentModel(IArtifactModel currentModel, String modelName);

	/**
	 * This does the actual expansion, using the syntax below.
	 * 
	 * Expander Variable Syntax ${project.Version} and ${project.Name}: This
	 * returns will replace your Version and Name properties of your current
	 * project. ${artifact.Name}, ${artifact.Package} and ${artifact.Path}: This
	 * returns a name, package, and a path-like String for your package (eg
	 * com/mycompany/myPackage) of the current Artifact. This variable is only
	 * useful for Artifact based rules. ${ppProp.propertyName}: This returns the
	 * value of the Global Property named propertyName in your plug-in.
	 * ${model.attributeName}: This returns the value of the attribute named
	 * attributeName in your model. Note: The Model should be replaced with the
	 * name of the model set in the rule specification.
	 * 
	 * 
	 * @param inString
	 * @return
	 */
	public String expandVar(String inString);

	/**
	 * As for the simple expandVar case, but passing in teh project to be used.
	 * 
	 * @param inString
	 * @param project
	 * @return
	 */
	public String expandVar(String inString, ITigerstripeModelProject project);

	/**
	 * Set the "name" of the model. This is only necessary if the default has
	 * been change in a rule defintion.
	 * 
	 * @param modelName
	 */
	public void setModelName(String modelName);

	/**
	 * Get the "name" of the model.
	 * 
	 * @return
	 */
	public String getModelName();

}
