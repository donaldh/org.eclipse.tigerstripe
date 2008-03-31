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
package org.eclipse.tigerstripe.workbench.plugins;

/**
 * An ArtifactBased Template Run rule in a Pluggable Plugin
 * 
 * This rule will be run once per instance (with possibility of filtering) of a
 * designated Artifact Type.
 * 
 * For each instance of that Artifact Type a "Model Object" will be instantiated
 * and placed in the velocity context.
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface IArtifactBasedTemplateRule extends ITemplateBasedRule {

	/**
	 * Returns the Fully Qualified Name of the Model to instantiate
	 * 
	 * @return
	 */
	public String getModelClass();

	/**
	 * Sets the classname of the model object to instantiate
	 * 
	 * @param classname
	 */
	public void setModelClass(String classname);

	/**
	 * Get the name to be used in the templates for objects of this type This
	 * will typically be "model" or "class"
	 * 
	 * @return
	 */
	public String getModelClassName();

	/**
	 * Set the name to be used in the templates for objects of this type This
	 * will typically be "model" or "class"
	 * 
	 * @param classname
	 */
	public void setModelClassName(String classname);

	public String getArtifactFilterClass();

	public void setArtifactFilterClass(String classname);

	public String getArtifactType();

	public void setArtifactType(String artifactType);

}
