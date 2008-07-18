/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.generation;

public interface IM1RunConfig {
	public abstract boolean useUseCaseXSL();

	public abstract void setUseUseCaseXSL(boolean useUseCaseXSL);

	public abstract String getUseCaseXSL();

	public abstract void setUseCaseXSL(String useCaseXsl);

	public abstract String getProcessedUseCaseExtension();

	public abstract void setProcessedUseCaseExtension(
			String processUseCaseExtension);

	public abstract void setAbsoluteOutputDir(String absoluteOutputDir);

	public abstract String getAbsoluteOutputDir();

	public abstract void setMergeFacets(boolean mergeFacets);

	public abstract boolean isMergeFacets();

	public abstract void setIgnoreFacets(boolean isIgnoreFacet);

	public abstract boolean isProcessUseCases();

	public abstract void setProcessUseCases(boolean processUseCases);

	public abstract void setGenerateModules(boolean generateModules);

	public abstract boolean isUseCurrentFacet();

	public abstract void setUseCurrentFacet(boolean useCurrentFacet);

	public abstract boolean isGenerateModules();

	public abstract boolean isIgnoreFacets();

	public abstract boolean isGenerateRefProjects();

	public abstract void setGenerateRefProjects(boolean generateRefProjects);
	
	public abstract boolean isOverrideSubprojectSettings();

	public abstract void setOverrideSubprojectSettings(boolean overrideSubprojectSettings);
	

}
